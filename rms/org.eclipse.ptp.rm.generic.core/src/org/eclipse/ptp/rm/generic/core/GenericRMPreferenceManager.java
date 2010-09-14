/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.generic.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.ptp.rm.core.AbstractToolsPreferenceManager;

public class GenericRMPreferenceManager extends AbstractToolsPreferenceManager {

	public static Preferences getPreferences() {
		return GenericRMCorePlugin.getDefault().getPluginPreferences();
	}

	public static void savePreferences() {
		GenericRMCorePlugin.getDefault().savePluginPreferences();
	}

	public static void initializePreferences() throws CoreException {

		Preferences preferences = GenericRMCorePlugin.getDefault().getPluginPreferences();
		GenericRMDefaults.loadDefaults();
		preferences.setDefault(PREFS_LAUNCH_CMD, GenericRMDefaults.LAUNCH_CMD);
		preferences.setDefault(PREFS_DEBUG_CMD, GenericRMDefaults.DEBUG_CMD);
		preferences.setDefault(PREFS_REMOTE_INSTALL_PATH, GenericRMDefaults.PATH);
	}
}
