/*******************************************************************************
 * Copyright (c) 2005 The Regents of the University of California. 
 * This material was produced under U.S. Government contract W-7405-ENG-36 
 * for Los Alamos National Laboratory, which is operated by the University 
 * of California for the U.S. Department of Energy. The U.S. Government has 
 * rights to use, reproduce, and distribute this software. NEITHER THE 
 * GOVERNMENT NOR THE UNIVERSITY MAKES ANY WARRANTY, EXPRESS OR IMPLIED, OR 
 * ASSUMES ANY LIABILITY FOR THE USE OF THIS SOFTWARE. If software is modified 
 * to produce derivative works, such modified software should be clearly marked, 
 * so as not to confuse it with the version available from LANL.
 * 
 * Additionally, this program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * LA-CC 04-115
 *  
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.ptp.rm.ibm.ll.core;

import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.ptp.core.PTPCorePlugin;
import org.eclipse.ptp.remote.core.IRemoteProxyOptions;
import org.eclipse.ptp.rm.core.RMPreferenceConstants;

public class IBMLLPreferenceManager {
	private static final String PROXY_EXECUTABLE_NAME = "ptp_ibmll_proxy"; //$NON-NLS-1$
	private static final String PROXY_EXECUTABLE_PATH = null; // use local fragment directory
	private static final boolean LAUNCH_MANUALLY = false; // use local fragment directory
	private static final int OPTIONS = IRemoteProxyOptions.PORT_FORWARDING;
	
	public static int getDefaultOptions() {
		return OPTIONS;
	}
	
	public static boolean getDefaultLaunchManualFlag() {
		return LAUNCH_MANUALLY;
	}

	public static String getDefaultProxyExecutablePath() {
		return PROXY_EXECUTABLE_PATH;
	}

	public static Preferences getPreferences() {
		return Activator.getDefault().getPluginPreferences();
	}
	
	public static void savePreferences() {
		Activator.getDefault().savePluginPreferences();
	}
	
	public static void initializePreferences() {
		Preferences preferences = Activator.getDefault().getPluginPreferences();
		
		String server = ""; //$NON-NLS-1$
			
		if (PROXY_EXECUTABLE_PATH != null) {
			server = new Path(PROXY_EXECUTABLE_PATH).append(PROXY_EXECUTABLE_NAME).toOSString();
		} else {
			server = PTPCorePlugin.getDefault().locateFragmentFile("org.eclipse.ptp", PROXY_EXECUTABLE_NAME); //$NON-NLS-1$
			if (server == null) {
				server = ""; //$NON-NLS-1$
			}
       }
		
		preferences.setDefault(RMPreferenceConstants.PROXY_PATH, server);
		preferences.setDefault(RMPreferenceConstants.OPTIONS, OPTIONS); 
	}
}
