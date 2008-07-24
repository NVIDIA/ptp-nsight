/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.remote.rse.ui;

import org.eclipse.ptp.remote.rse.core.RSEConnectionManager;
import org.eclipse.ptp.remote.ui.IRemoteUIConnectionManager;
import org.eclipse.rse.core.IRSESystemType;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.ui.actions.SystemNewConnectionAction;
import org.eclipse.swt.widgets.Shell;

public class RSEUIConnectionManager implements IRemoteUIConnectionManager {
	private SystemNewConnectionAction action;
	private RSEConnectionManager manager;

	public RSEUIConnectionManager(RSEConnectionManager manager) {
		this.manager = manager;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.remote.IRemoteConnectionManager#newConnection()
	 */
	public void newConnection(Shell shell) {
		if (action == null) {
 			action = new SystemNewConnectionAction(shell, false, false, null);
 	   		IRSESystemType systemType = RSECorePlugin.getTheCoreRegistry().getSystemTypeById(IRSESystemType.SYSTEMTYPE_SSH_ONLY_ID);
			if (systemType != null) {
 	   			action.restrictSystemTypes(new IRSESystemType[] { systemType });
			}
		}
    		
		try 
		{
			action.run();
		} catch (Exception e)
		{
			// Ignore
		}
		
		manager.refreshConnections();
	}
}
