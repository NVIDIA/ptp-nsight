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
package org.eclipse.ptp.remote.remotetools.ui;

import org.eclipse.ptp.remote.core.IRemoteConnection;
import org.eclipse.ptp.remote.core.IRemoteConnectionManager;
import org.eclipse.ptp.remote.core.IRemoteFileManager;
import org.eclipse.ptp.remote.core.IRemoteServices;
import org.eclipse.ptp.remote.ui.IRemoteUIConnectionManager;
import org.eclipse.ptp.remote.ui.IRemoteUIFileManager;
import org.eclipse.ptp.remote.ui.IRemoteUIServicesDelegate;


public class RemoteToolsUIServices implements IRemoteUIServicesDelegate {
	private static RemoteToolsUIServices instance = new RemoteToolsUIServices();
	private static IRemoteServices services;

	/**
	 * Get shared instance of this class
	 * 
	 * @return instance
	 */
	public static RemoteToolsUIServices getInstance(IRemoteServices services) {
		instance.setServices(services);
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.remote.ui.IRemoteUIServicesDelegate#getUIConnectionManager()
	 */
	public IRemoteUIConnectionManager getUIConnectionManager(IRemoteConnectionManager manager) {
		return new RemoteToolsUIConnectionManager(manager);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.remote.ui.IRemoteUIServicesDelegate#getUIFileManager(org.eclipse.ptp.remote.core.IRemoteConnection)
	 */
	public IRemoteUIFileManager getUIFileManager(IRemoteConnection connection) {
		IRemoteFileManager fileMgr = services.getFileManager(connection);
		if (fileMgr == null) {
			return null;
		}
		return new RemoteToolsUIFileManager(fileMgr);
	}

	/**
	 * Set remote services for this provider
	 * 
	 * @param services
	 */
	private void setServices(IRemoteServices services) {
		this.services = services;
	}
}
