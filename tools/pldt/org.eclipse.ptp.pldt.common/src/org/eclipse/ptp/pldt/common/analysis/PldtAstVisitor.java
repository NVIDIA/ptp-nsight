/**********************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ptp.pldt.common.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTExpression;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTIdExpression;
import org.eclipse.cdt.core.dom.ast.IASTLiteralExpression;
import org.eclipse.cdt.core.dom.ast.IASTMacroExpansion;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTNodeLocation;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorMacroDefinition;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.dom.ast.c.CASTVisitor;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ptp.pldt.common.Artifact;
import org.eclipse.ptp.pldt.common.CommonPlugin;
import org.eclipse.ptp.pldt.common.ScanReturn;
import org.eclipse.ptp.pldt.common.util.SourceInfo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

/**
 * This dom-walker helper collects interesting constructs (currently
 * function calls and constants), and adds markers to the source file for
 * C/C++ code. <br>
 * This base class encapsulates the common behaviors for both C and C++
 * code.
 * 
 * @author Beth Tibbitts
 * 
 */
public class PldtAstVisitor extends CASTVisitor {


	protected static String ARTIFACT_CALL = "Artifact Call";
	protected static String ARTIFACT_CONSTANT = "Artifact Constant";
	protected static String ARTIFACT_NAME = "Artifact Name";
	protected static String PREFIX = "";
	private static /*final*/ boolean traceOn=false;
	
	private static boolean dontAskToModifyIncludePathAgain=false;

	/**
	 * List of include paths that we'll probably want to consider in the work that this visitor does.
	 * For example, only paths found in this list (specified in PLDT preferences) would be considered
	 * to be a path from which definitions of "Artifacts" would be found.
	 */
	private /*final*/ List<String> includes_;
	private final String fileName;
	private final ScanReturn scanReturn;
	//private static final String STRING_QUOTE = "\"";

	/**
	 * 
	 * @param includes
	 *            list of include paths that we'll probably want to consider in
	 *            the work that this visitor does
	 * @param fileName
	 *            the name of the file that this visitor is visiting(?)
	 * @param scanReturn
	 *            the ScanReturn object to which the artifacts that we find will
	 *            be appended.
	 */
	public PldtAstVisitor(List<String> includes, String fileName, ScanReturn scanReturn) {
		this.includes_ = includes;
		this.fileName = fileName;
		this.scanReturn = scanReturn;
		dontAskToModifyIncludePathAgain=false;
		if(!traceOn) traceOn=CommonPlugin.getTraceOn();
		if(traceOn)System.out.println("PldtAstVisitor, traceOn="+traceOn);
	}

	/**
	 * Skip statements that are included.
	 */
	public int visit(IASTStatement statement) { 
		if (preprocessorIncluded(statement)) {
			return ASTVisitor.PROCESS_SKIP;
		}
		return ASTVisitor.PROCESS_CONTINUE; 
	}

	/**
	 * Skip decls that are included.
	 * 
	 * @param declaration
	 * @return
	 */
	public int visit(IASTDeclaration declaration) {//called; both paths get taken
		if (preprocessorIncluded(declaration)) {
			return ASTVisitor.PROCESS_SKIP;
		}
		return ASTVisitor.PROCESS_CONTINUE;
	}

	/**
	 * Process a function name from an expression and determine if it should be
	 * marked as an Artifact. If so, append it to the scanReturn object that
	 * this visitor is populating.
	 * 
	 * An artifact is a function name that was found in the MPI include path,
	 * as defined in the MPI preferences.
	 * 
	 * @param astExpr
	 * @param funcName
	 */
	public void processFuncName(IASTName funcName, IASTExpression astExpr) {
		//IASTTranslationUnit tu = funcName.getTranslationUnit();

		if (isArtifact(funcName)) {
			SourceInfo sourceInfo = getSourceInfo(astExpr, Artifact.FUNCTION_CALL);
			if (sourceInfo != null) {
				if(traceOn) System.out.println("found artifact: " + funcName.toString());
				// FIXME we're determining the artifact name twice. (also in chooseName())
				String artName=funcName.toString();
				String rawName=funcName.getRawSignature();
				//String bName=funcName.getBinding().getName();
				if(!artName.equals(rawName)) {
					if(rawName.length()==0)rawName="  ";
					artName=artName+"  ("+rawName+")"; // indicate orig pre-pre-processor value in parens
					// note: currently rawName seems to always be empty.
				}
				scanReturn.addArtifact(new Artifact(fileName, sourceInfo.getStartingLine(), 1,
						artName, ARTIFACT_CALL, sourceInfo));

			}
		}

	}

	/**
	 * 
	 * @param astExpr
	 */
	public void processExprWithConstant(IASTExpression astExpr) {
		IASTName funcName = ((IASTIdExpression) astExpr).getName();
		//IASTTranslationUnit tu = funcName.getTranslationUnit();

		if (isArtifact(funcName)) {
			SourceInfo sourceInfo = getSourceInfo(astExpr, Artifact.FUNCTION_CALL);
			if (sourceInfo != null) {
				System.out.println("found MPI artifact: " + funcName.toString());

				scanReturn.addArtifact(new Artifact(fileName, sourceInfo.getStartingLine(), 1,
						funcName.toString(), ARTIFACT_CALL, sourceInfo));

			}
		}

	}

	/**
	 * Determines if the funcName is an instance of the type of artifact in
	 * which we are interested. <br>
	 * An artifact is a function name that was found in the  include path (e.g. MPI or OpenMP),
	 * as defined in the PLDT preferences.
	 * 
	 * @param funcName
	 */
	protected boolean isArtifact(IASTName funcName) {
		//String funcNameString=funcName.toString();
		IBinding binding = funcName.resolveBinding();
		String name=binding.getName(); 
		String rawSig=funcName.getRawSignature();
		name = chooseName(name, rawSig);


		IASTTranslationUnit tu = funcName.getTranslationUnit();
		
		IASTName[] decls=tu.getDeclarationsInAST(binding);
		if (traceOn) {
			if (decls.length == 0) { // BRT decls=null detection
				IASTName[] na = tu.getDeclarationsInAST(binding);
				//IASTDeclaration[] astDecls = tu.getDeclarations();
				for (int i = 0; i < na.length; i++) {
					System.out.println("   IASTName: "+na[i]);
				}
			}
		}
		if(decls.length==0){
			System.out.println("PldtAstVisitor.isArtifact(): decls empty: there are no declarations for "+
					funcName+ " in translation unit for "+tu.getContainingFilename());
		}		
		
		for (int i = 0; i < decls.length; ++i) {
			// IASTFileLocation is file and range of lineNos
			IASTFileLocation loc = decls[i].getFileLocation();
			String filename = loc.getFileName();
			if(traceOn)System.out.println("PldtAstVisitor found filename " + filename);
			IPath path = new Path(filename);
			// is this path valid?
			if(traceOn)System.out.println("PldtAstVisitor found path " + path);

			if (isInIncludePath(path)){
				if(traceOn)System.out.println("   path match! "+name+" is an artifact.");
				return true;
			}
			else {
				if (traceOn) {
          System.out.println(name + " was found in " + path
              + " but  PLDT preferences have been set to only include: " + includes_.toString());
        }
				// add them here?
				if(allowIncludePathAdd()) {
					boolean addit = addIncludePath(path, name, dontAskToModifyIncludePathAgain);
					if(addit)return true;
				}
				
			}
		}
		return false;
	}


	/**
	 * Choose how to distinguish between binding name, and raw signature.<br>
	 * Could be overridden by subclasses if, for example, a name with a prefix e.g. "MPI::foo" should be preferred over "foo".<br>
	 * Here, the default case is that we always choose the regular/binding name, unless it's empty, in which case we choose the rawSignature.
	 * @param bindingName
	 * @param rawSignature
	 * @return
	 */
	protected String chooseName(String bindingName, String rawSignature){
		String name=bindingName;
		if(bindingName.length()==0) {
			name=rawSignature;
		}
		return name;
	}
 
	public void processMacroLiteral(IASTLiteralExpression expression) {
		IASTNodeLocation[] locations = expression.getNodeLocations();
		if ((locations.length == 1) && (locations[0] instanceof IASTMacroExpansion)) {//path taken &not
			// found a macro, does it come from the include path required to be "one of ours"?
			IASTMacroExpansion astMacroExpansion = (IASTMacroExpansion) locations[0];
			IASTPreprocessorMacroDefinition preprocessorMacroDefinition = astMacroExpansion
					.getMacroDefinition();
			// String shortName =
			// preprocessorMacroDefinition.getName().toString()+'='+literal;
			String shortName = preprocessorMacroDefinition.getName().toString();
			IASTNodeLocation[] preprocessorLocations = preprocessorMacroDefinition
					.getNodeLocations();
			while ((preprocessorLocations.length == 1)
					&& (preprocessorLocations[0] instanceof IASTMacroExpansion)) {
				preprocessorLocations = ((IASTMacroExpansion) preprocessorLocations[0])
						.getMacroDefinition().getNodeLocations();
			}

			if ((preprocessorLocations.length == 1)
					&& isInIncludePath(new Path(preprocessorLocations[0].asFileLocation()
							.getFileName()))) {
				SourceInfo sourceInfo = getSourceInfo(astMacroExpansion);
				if (sourceInfo != null) {
					scanReturn.addArtifact(new Artifact(fileName, sourceInfo.getStartingLine(), 1, // column:
							shortName, ARTIFACT_CONSTANT, sourceInfo));
				}

			}
		}
	}

	/**
	 * Is this path found in the include path in which we are interested?
	 * E.g. is it in the  include path specified in PLDT preferences,
	 * which would identify it as an  artifact of interest?
	 * 
	 * @param includeFilePath under consideration
	 * @return true if this is found in the include path from PLDT preferences
	 */
	private boolean isInIncludePath(IPath includeFilePath) {
		if (includeFilePath == null)
			return false;
		// java5
		for (String includeDir : includes_) {
			IPath includePath = new Path(includeDir);
			if(traceOn)System.out.println("PldtAstVisitor: is "+includeFilePath+" found in "+includeDir+"?");
			if (includePath.isPrefixOf(includeFilePath))
				return true;
		}
		return false;
	}

	/**
	 * Get exact source location info for a function call
	 * 
	 * @param astExpr
	 * @param constructType
	 * @return
	 */
	private SourceInfo getSourceInfo(IASTExpression astExpr, int constructType) {
		SourceInfo sourceInfo = null;
		IASTNodeLocation[] locations = astExpr.getNodeLocations();
		if (locations.length == 1) {
			IASTFileLocation astFileLocation = null;
			if (locations[0] instanceof IASTFileLocation) {
				astFileLocation = (IASTFileLocation) locations[0];
			}
			// handle the case e.g. #define foo MPI_fn - recognize foo() as MPI_fn()
			else if (locations[0] instanceof IASTMacroExpansion) {
				IASTMacroExpansion me=(IASTMacroExpansion)locations[0];
				astFileLocation=me.asFileLocation();
			}
			if(astFileLocation!=null) {
				//String tmp=astFileLocation.toString();
				sourceInfo = new SourceInfo();
				sourceInfo.setStartingLine(astFileLocation.getStartingLineNumber());
				sourceInfo.setStart(astFileLocation.getNodeOffset());
				sourceInfo.setEnd(astFileLocation.getNodeOffset() + astFileLocation.getNodeLength());
				sourceInfo.setConstructType(constructType);			
			}
		}
		return sourceInfo;
	}

	/**
	 * Get exact source location info for a constant originated from Macro
	 * expansion(s)
	 * 
	 * @param iASTMacroExpansion
	 *            represents the original macro
	 * @return
	 */
	private SourceInfo getSourceInfo(IASTMacroExpansion iASTMacroExpansion) {
		SourceInfo sourceInfo = null;
		IASTFileLocation iASTFileLocation = iASTMacroExpansion.asFileLocation();
		sourceInfo = new SourceInfo();
		sourceInfo.setStartingLine(iASTFileLocation.getStartingLineNumber());
		sourceInfo.setStart(iASTFileLocation.getNodeOffset());
		sourceInfo.setEnd(iASTFileLocation.getNodeOffset() + iASTFileLocation.getNodeLength());
		sourceInfo.setConstructType(Artifact.CONSTANT);

		return sourceInfo;
	}
	
	private boolean preprocessorIncluded(IASTNode astNode) {
		if (astNode.getFileLocation() == null)
			return false;

		String location = astNode.getFileLocation().getFileName();
		String tuFilePath = astNode.getTranslationUnit().getFilePath();
		// System.err.println("node="+astNode.getRawSignature());
		// System.err.println("location="+location);
		// System.err.println("tu="+tuFilePath);
		return !location.equals(tuFilePath);
	}

	public void processIdExprAsLiteral(IASTIdExpression expression) {
		IASTName name = expression.getName();

		if (isArtifact(name)) {
			SourceInfo sourceInfo = getSourceInfo(expression, Artifact.CONSTANT);
			if (sourceInfo != null) {
				scanReturn.addArtifact(new Artifact(fileName, sourceInfo.getStartingLine(), 1, // column:

						name.toString(), ARTIFACT_CONSTANT, sourceInfo));
			}
		}
	}

	/**
	 * allow dynamic adding to include path?  Can be overridden by derived classes.
	 * @return
	 */
	public boolean allowIncludePathAdd() {
		return !dontAskToModifyIncludePathAgain;
	}
	/**
	 * Replace the includes list in this visitor so the change will be recognized.
	 * @param includes
	 */
	protected void replaceIncludes(String includes) {
		includes_=convertToList(includes);
	}
    @SuppressWarnings({ "unchecked"})
	public List convertToList(String stringList)
    {
        StringTokenizer st = new StringTokenizer(stringList, File.pathSeparator + "\n\r");//$NON-NLS-1$
        List dirs = new ArrayList();
        while (st.hasMoreElements()) {
            dirs.add(st.nextToken());
        }
        return dirs;
    }
	/** 
	 * Add an include path to the prefs - probably found dynamically during analysis
	 * and requested to be added by the user
	 * <br>
	 * Note that the path will be to the actual file in which the name was found;
	 * the path that will be added to the prefs is the parent directory of that file.
	 * @param path
	 * @param name the name (function etc) that was found in the path
	 * @param dontAskAgain  initial value of toggle "don't ask again"
	 * 
	 * @returns whether the user chose to add the path or not
	 */
	public boolean addIncludePath( IPath path,  String name/*, IPreferenceStore store, String id*/, boolean dontAskAgain) {

		IPreferenceStore store=getPreferenceStore();
		String id=getIncludesPrefID();
		String type=getTypeName();
		boolean doitThisTime=false;
		
		if(store==null || id == null) {
			CommonPlugin.log(IStatus.ERROR, "PLDT: Visitor subclass does not implement getPreferenceStore() or "+
					"getIncludesPrefID() to return non-null values.");
			return false;
		}
		
		try {
		//final IPreferenceStore store = MpiPlugin.getDefault().getPreferenceStore();
		//final String id =MpiIDs.MPI_INCLUDES;
		 String value = store.getString(id);
		System.out.println("value: "+value);
		String newValue;
		
		if(!dontAskAgain) {

				// probably inefficient string construction, but rarely called.
				String msg = "The following API: \n  " + name
						+ "\nwas found in the following path: \n  "
						+ path.toString()
						+ "\nwhich is not in the "+type+" preferences path list of: \n  "
						+ value + ".\nDo you want to add it?  If so, further APIs found here will be automatically recognized.";
				String title = "Add "+type+" include path?";
				boolean[] twoAnswers=  askUI(title, msg, dontAskToModifyIncludePathAgain);
        doitThisTime=twoAnswers[0];
				dontAskAgain=twoAnswers[1];
				dontAskToModifyIncludePathAgain= dontAskAgain;

			}
		
		if(doitThisTime) {
			String s = java.io.File.pathSeparator;
			String parent = path.toFile().getParent();
			// add path separator (: or ; ?) if necessary
			if(!value.endsWith(s)) {
			 value+=s;
			}
			// add this new include path location to the value stored in preferences, and add it to the value within this class as well.
			value+=parent+s;
			System.out.println("new path: "+value);
			store.putValue(id, value);
			replaceIncludes(value);
			//includes_=convertToList(value);
		}
		int stophere=0;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return doitThisTime;
	}

	/**
	 * needs to be overrridden for derived classes that need to dynamically update the pref store 
	 * e.g. for the includes path.  This type name is used for messages, etc.
	 * @return artifact type name such as "MPI", "OpenMP" etc.
	 */	
	protected String getTypeName() {
		return "";
	}

	/**
	 * needs to be overrridden for derived classes that need to dynamically update the pref store 
	 * e.g. for the includes path
	 * @return
	 */
	protected String getIncludesPrefID() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * needs to be overrridden for derived classes that need to dynamically update the pref store 
	 * e.g. for the includes path
	 * @return
	 */
	protected IPreferenceStore getPreferenceStore() {
		// TODO Auto-generated method stub
		return null;
	}

/**
 * Dialog to ask a question in the UI thread
 * @author beth
 *
 */
	class RunGetAnswer implements Runnable {
		boolean answer, dontAskAgain;
		String title, message;

		RunGetAnswer(String title, String message, boolean initialToggleState) {
			this.title = title;
			this.message = message;
			this.dontAskAgain=initialToggleState;
		}

		public void run() {
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
			Shell shell = w.getShell();
			if (shell == null) {
				Display display = CommonPlugin.getStandardDisplay();
				shell = display.getActiveShell();
			}
			//answer = MessageDialog.openQuestion(shell, title, message);
			
			// see also: openYesNoCancelQuestion
			String toggleMessage="Don't ask again";
			IPreferenceStore store = null;
			String key = null;
			MessageDialogWithToggle md;
			md=MessageDialogWithToggle.openYesNoQuestion(shell, title, message, toggleMessage, dontAskAgain, store, key);
			int retCode=md.getReturnCode();  // yes=2
			answer = (retCode==2);
			dontAskAgain= md.getToggleState();  
		}

		public boolean getAnswer() {
			return answer;
		}
		public boolean getDontAskAgain() {
		  return dontAskAgain;
		}
	}
		
	public boolean[] askUI(final String title, final String message, boolean dontAskAgain) {
	  boolean[] twoAnswers = new boolean[2];
		RunGetAnswer runner = new RunGetAnswer(title,message,dontAskAgain);
		
		Display.getDefault().syncExec(runner);
		boolean answer=runner.getAnswer();
		dontAskAgain=runner.getDontAskAgain();
		twoAnswers[0]=answer;
		twoAnswers[1]=dontAskAgain;
		return twoAnswers;
	}
	/**
	 * Show a message dialog; force to UI thread
	 * This has NOT been tried.
	 */
	public void messageUI_HIDE(final String title, final String message) {

		UIJob job = new UIJob("PLDTAstVisitor query") {
			@Override
			public IStatus runInUIThread(IProgressMonitor monitor) {
				IWorkbench wb = PlatformUI.getWorkbench();

				IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
				//IWorkbenchPage page = w.getActivePage();
				Shell shell = w.getShell();
				if (shell == null) {
					Display display = CommonPlugin.getStandardDisplay();
					shell = display.getActiveShell();
				}
				boolean answer = MessageDialog.openQuestion(w.getShell(), title, message);

				IStatus result = new Status(IStatus.OK, CommonPlugin.PLUGIN_ID,
						IStatus.OK, "ok", null);
				return result;
			}

		};
		job.schedule();
		
		
		
	}
/*	
	public void temp() {
		Display.getDefault().syncExec(new Runnable(){  // syncExec always blocks until done
			 public void run(){
				 IWorkbench wb = PlatformUI.getWorkbench();
				 IWorkbenchWindow w = wb.getActiveWorkbenchWindow();
				 Shell shell =w.getShell();
				// probably inefficient string construction, but rarely called.
					String msg="The following API: "+name+" was found in the following path: "+path.toString()+
					" which is not in the MPI preferences path list of: "+value+".  Do you want to add it?";
					boolean doit=MessageDialog.openQuestion(shell, "Add MPI include path?", msg);
				 //MessageDialog.openError(w.getShell(), title, message);
					if(doit) {
						String s = java.io.File.pathSeparator;
						//char s = new String(Path.DEVICE_SEPARATOR); // colon or semicolon
//						System.out.println("before: "+path.toPortableString());
//						path.removeLastSegments(1); // remove the file name from the path
//						System.out.println("after:  "+path.toPortableString());
						String parent = path.toFile().getParent();
						System.out.println("parent: "+parent);
						// add path separator (: or ; ?) if necessary
						if(!value.endsWith(s)) {
						 value+=s;
						}
						// add this new include path location to the value stored in preferences, and add it to the value within this class as well.
						value+=parent+s;
						System.out.println("new path: "+value);
						store.putValue(id, value);
						replaceIncludes(value);
						//includes_=convertToList(value);
						newValue = value;
						result=value;
					}
			 }
			 public String result;
			 public String getResult() {return result;}
		 });
	}
	*/




}