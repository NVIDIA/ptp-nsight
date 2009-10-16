/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.rm.pbs.ui;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.ptp.core.PTPCorePlugin;
import org.eclipse.ptp.core.elementcontrols.IPUniverseControl;
import org.eclipse.ptp.core.elementcontrols.IResourceManagerControl;
import org.eclipse.ptp.rm.core.rmsystem.AbstractRemoteResourceManagerServiceProvider;
import org.eclipse.ptp.rm.pbs.core.PBSPreferenceManager;
import org.eclipse.ptp.rm.pbs.core.rmsystem.IPBSResourceManagerConfiguration;
import org.eclipse.ptp.rm.pbs.core.rmsystem.PBSResourceManager;
import org.eclipse.ptp.services.core.IServiceProvider;
import org.eclipse.ptp.services.core.IServiceProviderWorkingCopy;


/**
 * Service provider for IBM Parallel Environment
 */
public class PBSServiceProvider extends AbstractRemoteResourceManagerServiceProvider implements IPBSResourceManagerConfiguration{
	private static final String TAG_PBSD_PATH = "pbsdPath"; //$NON-NLS-1$
	private static final String TAG_PBSD_ARGS = "pbsdArgs"; //$NON-NLS-1$
	private static final String TAG_PBSD_DEFAULTS = "pbsdDefaults"; //$NON-NLS-1$
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$
	
	private Preferences preferences = PBSPreferenceManager.getPreferences();

	public PBSServiceProvider() {
		super();
		setDescription("PBS Resource Manager"); //$NON-NLS-1$
	}

	/**
	 * Constructor for creating a working copy of the service provider
	 * 
	 * @param provider provider we are making a copy from
	 */
	public PBSServiceProvider(IServiceProvider provider) {
		super(provider);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.services.core.ServiceProvider#copy()
	 */
	@Override
	public IServiceProviderWorkingCopy copy() {
		return new PBSServiceProvider(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rmsystem.AbstractResourceManagerServiceProvider#createResourceManager()
	 */
	@Override
	public IResourceManagerControl createResourceManager() {
		IPUniverseControl universe = (IPUniverseControl) PTPCorePlugin.getDefault().getUniverse();
		return new PBSResourceManager(Integer.valueOf(universe.getNextResourceManagerId()), universe, this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.pbs.core.rmsystem.IPBSResourceManagerConfiguration#getPBSdArgs()
	 */
	public String getPBSdArgs() {
		return getString(TAG_PBSD_ARGS, preferences.getString(EMPTY_STRING));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.pbs.core.rmsystem.IPBSResourceManagerConfiguration#getPBSdPath()
	 */
	public String getPBSdPath() {
		return getString(TAG_PBSD_PATH, preferences.getString(EMPTY_STRING));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rmsystem.AbstractResourceManagerServiceProvider#getResourceManagerId()
	 */
	@Override
	public String getResourceManagerId() {
		return getId();
	}
	
    /* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.pbs.core.rmsystem.IPBSResourceManagerConfiguration#getUseDefaults()
	 */
	public boolean getUseDefaults() {
		return getBoolean(TAG_PBSD_DEFAULTS, true);
	}
    
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.services.core.IServiceProvider#isConfigured()
	 */
	public boolean isConfigured() {
		return true;
	}
	
	/*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ptp.rmsystem.IResourceManagerConfiguration#setDefaultNameAndDesc()
     */
    public void setDefaultNameAndDesc()
    {
		String name = "PBS"; //$NON-NLS-1$
		String conn = getConnectionName();
		if (conn != null && !conn.equals("")) { //$NON-NLS-1$
		    name += "@" + conn; //$NON-NLS-1$
		}
		setName(name);
		setDescription("PBS Resource Manager"); //$NON-NLS-1$
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.pbs.core.rmsystem.IPBSResourceManagerConfiguration#setPBSdArgs(java.lang.String)
	 */
	public void setPBSdArgs(String pbsdArgs) {
		putString(TAG_PBSD_ARGS, pbsdArgs);
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.pbs.core.rmsystem.IPBSResourceManagerConfiguration#setPBSdPath(java.lang.String)
	 */
	public void setPBSdPath(String pbsdPath) {
		putString(TAG_PBSD_PATH, pbsdPath);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.pbs.core.rmsystem.IPBSResourceManagerConfiguration#setUseDefaults(boolean)
	 */
	public void setUseDefaults(boolean useDefaults) {
		putBoolean(TAG_PBSD_DEFAULTS, useDefaults);
	}
}
