/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - Initial API and implementation
 *******************************************************************************/ 
package org.eclipse.ptp.internal.rdt.ui.scannerinfo;

import org.eclipse.cdt.core.settings.model.CIncludePathEntry;
import org.eclipse.cdt.core.settings.model.ICLanguageSettingEntry;
import org.eclipse.cdt.ui.newui.IncludeTab;
import org.eclipse.swt.widgets.Composite;


/**
 * Reuse the standard CDT IncludeTab but override the functionality
 * of the Add and Edit buttons to pop up our custom remote dialog.
 * 
 * @author Mike Kucera
 */
public class RemoteIncludeTab extends IncludeTab {
	
	@Override
	public ICLanguageSettingEntry doAdd() {
		
		RemoteIncludeDialog dlg = new RemoteIncludeDialog(usercomp.getShell(), Messages.RemoteIncludeTab_title, false);
		
		if(dlg.open() && dlg.getDirectory().trim().length() > 0 ) {
			toAllCfgs = dlg.isAllConfigurations();
			toAllLang = dlg.isAllLanguages();
			int flags = 0;
			return new CIncludePathEntry(dlg.getDirectory(), flags);
		}
		
		return null;
	}
	
	@Override
	public ICLanguageSettingEntry doEdit(ICLanguageSettingEntry ent) {
		
		RemoteIncludeDialog dlg = new RemoteIncludeDialog(usercomp.getShell(), Messages.RemoteIncludeTab_title, true);
		dlg.setPathText(ent.getValue());
		
		if(dlg.open()) {
			int flags = 0;
			return new CIncludePathEntry(dlg.getDirectory(), flags);
		}
		
		return null;
	}

	@Override
	public void createControls(final Composite parent) {
		super.createControls(parent);
		ImportExportWizardButtons.createControls(usercomp, page.getElement());
	}
	
}
