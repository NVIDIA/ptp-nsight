/*******************************************************************************
 * Copyright (c) 2010 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - implementation
					  replaces earlier ResourceManager wizard page
 ******************************************************************************/
package org.eclipse.ptp.rm.pbs.ui.wizards;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ptp.rm.pbs.ui.managers.PBSBatchScriptTemplateManager;

public class PBSBatchScriptTemplateWizard extends Wizard {

	private final PBSBatchScriptTemplateWizardPage page;
	private boolean added;

	public PBSBatchScriptTemplateWizard(PBSBatchScriptTemplateManager manager) throws Throwable {
		page = new PBSBatchScriptTemplateWizardPage(manager);
		added = false;
	}

	@Override
	public void addPages() {
		if (!added) {
			super.addPage(page);
			added = true;
		}
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
