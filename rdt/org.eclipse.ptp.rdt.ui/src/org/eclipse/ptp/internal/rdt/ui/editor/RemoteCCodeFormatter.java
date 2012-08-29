/*******************************************************************************
 * Copyright (c) 2000, 2012 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     QNX Software Systems - Initial API and implementation
 *     Sergey Prigogin, Google
 *     Anton Leherbauer (Wind River Systems)
 *     IBM Corporation
 *******************************************************************************/
package org.eclipse.ptp.internal.rdt.ui.editor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.formatter.CodeFormatter;
import org.eclipse.cdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ParserUtil;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.internal.formatter.AbortFormatting;
import org.eclipse.cdt.internal.formatter.CodeFormatterVisitor;
import org.eclipse.cdt.internal.formatter.DefaultCodeFormatterOptions;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ptp.internal.rdt.core.formatter.RemoteDefaultCodeFormatterOptions;
import org.eclipse.ptp.rdt.core.services.IRDTServiceConstants;
import org.eclipse.ptp.rdt.ui.UIPlugin;
import org.eclipse.ptp.rdt.ui.serviceproviders.IIndexServiceProvider2;
import org.eclipse.ptp.services.core.IService;
import org.eclipse.ptp.services.core.IServiceConfiguration;
import org.eclipse.ptp.services.core.IServiceModelManager;
import org.eclipse.ptp.services.core.IServiceProvider;
import org.eclipse.ptp.services.core.ServiceModelManager;
import org.eclipse.text.edits.TextEdit;

/**
 * @author Vivian Kong
 * see CCodeFormatter
 */
public class RemoteCCodeFormatter extends CodeFormatter {
	private RemoteDefaultCodeFormatterOptions preferences;
	private Map<String, ?> options;

	public RemoteCCodeFormatter() {
		this(DefaultCodeFormatterOptions.getDefaultSettings());
	}

	public RemoteCCodeFormatter(DefaultCodeFormatterOptions preferences) {
		this(preferences, null);
	}

	public RemoteCCodeFormatter(DefaultCodeFormatterOptions defaultCodeFormatterOptions, Map<String, ?> options) {
		setOptions(options);
		if (defaultCodeFormatterOptions != null) {
			preferences.set(defaultCodeFormatterOptions.getMap());
		}
	}

	public RemoteCCodeFormatter(Map<String, ?> options) {
		this(null, options);
	}

	@Override
	public void setOptions(Map<String, ?> options) {
		if (options != null) {
			this.options= options;
			Map<String, String> formatterPrefs= new HashMap<String, String>(options.size());
			for (String key : options.keySet()) {
				Object value= options.get(key);
				if (value instanceof String) {
					formatterPrefs.put(key, (String) value);
				}
			}
			preferences= new RemoteDefaultCodeFormatterOptions(formatterPrefs);
		} else {
			this.options= CCorePlugin.getOptions();
			preferences= RemoteDefaultCodeFormatterOptions.getDefaultSettings();
		}
	}

	/*
	 * @see org.eclipse.cdt.core.formatter.CodeFormatter#format(int, java.lang.String, int, int, int, java.lang.String)
	 */
	@Override
	public TextEdit format(int kind, String source, int offset, int length, int indentationLevel, String lineSeparator) {
		TextEdit edit= null;
		ITranslationUnit tu= (ITranslationUnit)options.get(DefaultCodeFormatterConstants.FORMATTER_TRANSLATION_UNIT);
		if (tu == null) {
			IFile file= (IFile)options.get(DefaultCodeFormatterConstants.FORMATTER_CURRENT_FILE);
			if (file != null) {
				tu= (ITranslationUnit)CoreModel.getDefault().create(file);
			}
		}
		if (lineSeparator != null) {
			this.preferences.line_separator = lineSeparator;
		} else {
			//for remote projects
			this.preferences.line_separator = "\n"; //$NON-NLS-1$
		}
		this.preferences.initial_indentation_level = indentationLevel;
		
		
		
		if (tu != null) {
			IRemoteCodeFormattingService codeFormattingService = getCodeFormattingService(tu.getCProject().getProject());
			try {
				return codeFormattingService.computeCodeFormatting(tu, source, this.preferences, offset, length, new NullProgressMonitor());
			} catch (CoreException e) {
				UIPlugin.log(e);
			}
		}
		return edit;
	}
	
	private IRemoteCodeFormattingService getCodeFormattingService(IProject project) {
		IServiceModelManager smm = ServiceModelManager.getInstance();
		IServiceConfiguration serviceConfig = smm.getActiveConfiguration(project);
		IService indexingService = smm.getService(IRDTServiceConstants.SERVICE_C_INDEX);
		IServiceProvider serviceProvider = serviceConfig.getServiceProvider(indexingService);
		if (!(serviceProvider instanceof IIndexServiceProvider2)) {
			return null;
		}
		IRemoteCodeFormattingService service = ((IIndexServiceProvider2) serviceProvider).getRemoteCodeFormattingService();
		return service;
	}
}
