/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - Initial API and implementation
 *******************************************************************************/ 
package org.eclipse.ptp.internal.rdt.ui.scannerinfo;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ptp.internal.rdt.ui.wizards.settings.ProjectSettingsExportWizard;
import org.eclipse.ptp.internal.rdt.ui.wizards.settings.ProjectSettingsImportWizard;
import org.eclipse.ptp.internal.rdt.ui.wizards.settings.ProjectSettingsWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class ImportExportWizardButtons {

	public static void createControls(final Composite parent, final IAdaptable selection) {
		
		// TODO use image registry
		final Image importIcon = new Image(parent.getDisplay(), ImportExportWizardButtons.class.getResourceAsStream("/icons/etool16/importc_settings.gif")); //$NON-NLS-1$
		final Image exportIcon = new Image(parent.getDisplay(), ImportExportWizardButtons.class.getResourceAsStream("/icons/etool16/exportc_settings.gif")); //$NON-NLS-1$
		parent.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent _) {
				importIcon.dispose();
				exportIcon.dispose();
			}
		});
				
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, true);
		layout.marginHeight = layout.marginWidth = 0;
		comp.setLayout(layout);
		GridData data = new GridData();
		data.horizontalSpan = 2;
		comp.setLayoutData(data);
		
		Button importButton = new Button(comp, SWT.NONE);
		importButton.setText(Messages.RemoteIncludeTab_import);
		importButton.setImage(importIcon);
		importButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				boolean finishedPressed = launchWizard(parent.getShell(), selection, false);
				// There is no way to get the contents of the property page to update
				// other than to close the whole dialog and then reopen it.
				if(finishedPressed)
					parent.getShell().close();
			}
		});
		
		Button exportButton = new Button(comp, SWT.NONE);
		exportButton.setText(Messages.RemoteIncludeTab_export);
		exportButton.setImage(exportIcon);
		exportButton.addSelectionListener(new SelectionAdapter() {
			@Override public void widgetSelected(SelectionEvent e) {
				launchWizard(parent.getShell(), selection, true);
			}
		});
		
	}
	
	
	private static boolean launchWizard(Shell shell, IAdaptable selection, boolean export) {
		ProjectSettingsWizard wizard;
		if(export)
			wizard = new ProjectSettingsExportWizard();
		else
			wizard = new ProjectSettingsImportWizard();
		
		wizard.init(PlatformUI.getWorkbench(), new StructuredSelection(selection));
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		dialog.open();
		
		return wizard.isFinishedPressed();
	}
}
