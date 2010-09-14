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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.osgi.framework.Bundle;

public class GenericRMDefaults {

	public static String LAUNCH_CMD = null;
	public static String DEBUG_CMD = null;
	public static String PATH = null;

	private static String defaultsResourcePath = "/data/defaults.properties"; //$NON-NLS-1$

	public static void loadDefaults() throws CoreException {
		Path defaultsPropertiesPath = new Path(defaultsResourcePath);
		Bundle bundle = GenericRMCorePlugin.getDefault().getBundle();
		Properties properties = read(defaultsPropertiesPath, bundle);

		LAUNCH_CMD = getString(bundle, properties, "LAUNCH_CMD"); //$NON-NLS-1$
		DEBUG_CMD = getString(bundle, properties, "DEBUG_CMD"); //$NON-NLS-1$
		PATH = getString(bundle, properties, "PATH"); //$NON-NLS-1$

		assert LAUNCH_CMD != null;
		assert DEBUG_CMD != null;
		assert PATH != null;
	}

	public static Properties read(Path defaultsPropertiesPath, Bundle bundle) throws CoreException {
		InputStream inStream;
		Properties properties = new Properties();
		try {
			inStream = FileLocator.openStream(bundle, defaultsPropertiesPath, false);
			properties.load(inStream);

		} catch (IOException e) {
			throw GenericRMCorePlugin.coreErrorException("Failed to read properties file with default preferences", e); //$NON-NLS-1$
		}
		return properties;
	}

	public static String getString(Bundle bundle, Properties properties, String key) throws CoreException {
		String value = properties.getProperty(key);
		if (value == null) {
			throw new CoreException(new Status(IStatus.ERROR, bundle.getSymbolicName(), NLS.bind(
					"Missing default value for {0}", key))); //$NON-NLS-1$
		}
		return value;
	}

	public static int getInteger(Bundle bundle, Properties properties, String key) throws CoreException {
		String value = getString(bundle, properties, key);
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new CoreException(new Status(IStatus.ERROR, bundle.getSymbolicName(), NLS.bind(
					"Failed to parse integer default value for {0}", key))); //$NON-NLS-1$
		}
	}

	public static boolean getBoolean(Bundle bundle, Properties properties, String key) throws CoreException {
		String value = getString(bundle, properties, key);
		return Boolean.parseBoolean(value);
	}
}
