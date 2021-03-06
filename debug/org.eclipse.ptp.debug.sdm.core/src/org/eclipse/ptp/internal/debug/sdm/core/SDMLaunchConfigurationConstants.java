/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.internal.debug.sdm.core;

import org.eclipse.ptp.core.IPTPLaunchConfigurationConstants;

/**
 * @since 5.0
 * 
 */
public interface SDMLaunchConfigurationConstants {
	/**
	 * Launch configuration attribute key to specify the SDM debugger backend
	 */
	public static final String ATTR_DEBUGGER_SDM_BACKEND = IPTPLaunchConfigurationConstants.PTP_LAUNCH_ID + ".DEBUGGER_SDM_BACKEND"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key to specify the SDM debugger backend path
	 * 
	 * @since 6.0
	 */
	public static final String ATTR_DEBUGGER_SDM_BACKEND_PATH = IPTPLaunchConfigurationConstants.PTP_LAUNCH_ID
			+ ".DEBUGGER_SDM_BACKEND_PATH"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key to specify that the built-in sdm should be used if available
	 */
	public static final String ATTR_DEBUGGER_USE_BUILTIN_SDM = IPTPLaunchConfigurationConstants.PTP_LAUNCH_ID
			+ ".DEBUGGER_USE_BUILTIN_SDM"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key to specify the path to the sdm executable. This is the fallback path used if the builtin
	 * sdm can't be used, or if the builtin option is disabled.
	 */
	public static final String ATTR_DEBUGGER_SDM_EXECUTABLE = IPTPLaunchConfigurationConstants.PTP_LAUNCH_ID
			+ ".DEBUGGER_SDM_EXECUTABLE"; //$NON-NLS-1$

}
