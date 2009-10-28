/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/

package org.eclipse.ptp.rdt.ui.serviceproviders;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ptp.rdt.services.core.IServiceConfiguration;
import org.eclipse.ptp.rdt.services.core.ServiceModelManager;
import org.eclipse.ptp.rdt.services.ui.NewServiceModelWidget;
import org.eclipse.ptp.rdt.ui.UIPlugin;
import org.eclipse.ptp.rdt.ui.help.IRHelpContextIds;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Remote project property page for configuring service providers
 * @author vkong
 *
 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as
 * part of a work in progress. There is no guarantee that this API will work or
 * that it will remain the same. Please do not use this API without consulting
 * with the RDT team.
 * 
 */
public class ServiceModelPropertyPage extends PropertyPage {
	
	private NewServiceModelWidget fModelWidget;

	
	@Override
	protected Control createContents(Composite parent) {
		fModelWidget = new NewServiceModelWidget(parent, SWT.NONE);
		IProject project = getProject();
		
		IServiceConfiguration config = ServiceModelManager.getInstance().getActiveConfiguration(project);
		
		Set<String> natures = Collections.emptySet();
		try {
			natures = new HashSet<String>(Arrays.asList(project.getDescription().getNatureIds()));
		} catch (CoreException e) {
			UIPlugin.log(e);
		}
		
		fModelWidget.setServiceConfiguration(config, natures);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(fModelWidget, IRHelpContextIds.SERVICE_MODEL_PROPERTIES);
		return fModelWidget;
	}

	
	private IProject getProject() {
		Object element = getElement();
		if (element instanceof IProject)
			return (IProject) element;
		else 
			return ((ICProject) element).getProject();
	}
	
	@Override
	public boolean performOk() {  // called when OK or Apply is pressed
		fModelWidget.applyChangesToConfiguration();
		try {
			ServiceModelManager.getInstance().saveModelConfiguration();
		} catch (IOException e) {
			UIPlugin.log(e);
		}
		return true;
	}


}
