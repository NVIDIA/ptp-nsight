/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    John Liu (IBM)
 *******************************************************************************/ 

package org.eclipse.ptp.internal.rdt.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.core.model.CoreModelUtil;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.ptp.rdt.core.RDTLog;


public class RemoteProjectResourcesUtil {
	

	
	
	public static List<ICElement> getCElements(IProject project){
		final List<ICElement> elements = new ArrayList<ICElement>();
		
		// TODO replace with ICElementVisitor
		IResourceVisitor resourceVisitor = new IResourceVisitor() {
			public boolean visit(IResource resource) throws CoreException {
				if (!(resource instanceof IFile)){
				    if(IRemoteIndexerInfoProvider.EXCLUDED_DIRECTORIES.contains(resource.getName())){
				    	return false;
				    }else{
				    	return true;
				    }
				}
				if(!IRemoteIndexerInfoProvider.EXCLUDED_FILES.contains(resource.getName())){
										
					ITranslationUnit tu = CoreModelUtil.findTranslationUnit((IFile) resource);
					if(tu != null)
						elements.add(tu);
				}
				return true;
			}
		};
	
		
		try {
			project.accept(resourceVisitor);
		} catch (CoreException e) {
			RDTLog.logError(e);
		}
		return elements;
	}
	
	public static List<ICElement> filterElements(List<ICElement> elements){
		List<ICElement> filteredElements = new ArrayList<ICElement>(elements.size());
		for(ICElement element: elements){
			IPath path = element.getPath();
			if(path !=null){
				if(!IRemoteIndexerInfoProvider.EXCLUDED_FILES.contains(path.lastSegment())){
					filteredElements.add(element);
				}
			}
		}
		return filteredElements;
		
	}
	
	
	
	

}
