/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.remote.core;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ptp.internal.remote.core.RemoteServicesImpl;
import org.eclipse.ptp.internal.remote.core.RemoteServicesProxy;
import org.eclipse.ptp.internal.remote.core.services.local.LocalServices;

/**
 * Main entry point for accessing remote services.
 * 
 * @since 7.0
 */
public class RemoteServices {
	// Local services
	private static IRemoteServices fLocalServices;

	/**
	 * Retrieve the local services provider. Guaranteed to exist and be initialized.
	 * 
	 * @return local services provider
	 */
	public static IRemoteServices getLocalServices() {
		if (fLocalServices == null) {
			fLocalServices = getRemoteServices(LocalServices.LocalServicesId);
		}
		return fLocalServices;
	}

	/**
	 * Get the remote service implementation identified by id and ensure that it is initialized.
	 * 
	 * @param id
	 *            id of the remote service
	 * @return remote service or null if the service cannot be found or failed to initialized
	 */
	public static IRemoteServices getRemoteServices(String id) {
		return getRemoteServices(id, null);
	}

	/**
	 * Get the remote service implementation identified by id and ensure that it is initialized. This method will present the user
	 * with a dialog box that can be canceled.
	 * 
	 * @param id
	 *            id of remote service to retrieve
	 * @param monitor
	 *            progress monitor to allow user to cancel operation
	 * @return remote service, or null if the service cannot be found or failed to initialized
	 * @since 5.0
	 */
	public static IRemoteServices getRemoteServices(String id, IProgressMonitor monitor) {
		RemoteServicesProxy proxy = RemoteServicesImpl.getRemoteServiceProxyById(id);
		if (proxy != null) {
			IRemoteServices service = proxy.getServices();
			if (service.initialize(monitor)) {
				return service;
			}
		}
		return null;
	}

	/**
	 * Get the remote services identified by a URI.
	 * 
	 * @param uri
	 *            URI of remote services to retrieve
	 * @return remote service, or null if the service cannot be found or failed to initialized
	 */
	public static IRemoteServices getRemoteServices(URI uri) {
		return getRemoteServices(uri, null);
	}

	/**
	 * Get the remote services implementation identified by URI. This method will present the user
	 * with a dialog box that can be canceled.
	 * 
	 * @param uri
	 *            URI of remote services to retrieve
	 * @param monitor
	 *            progress monitor to allow user to cancel operation
	 * @return remote service, or null if the service cannot be found or failed to initialized
	 * @since 5.0
	 */
	public static IRemoteServices getRemoteServices(URI uri, IProgressMonitor monitor) {
		RemoteServicesProxy proxy = RemoteServicesImpl.getRemoteServiceProxyByURI(uri);
		if (proxy != null) {
			IRemoteServices service = proxy.getServices();
			if (service.initialize(monitor)) {
				return service;
			}
		}
		return null;
	}
}
