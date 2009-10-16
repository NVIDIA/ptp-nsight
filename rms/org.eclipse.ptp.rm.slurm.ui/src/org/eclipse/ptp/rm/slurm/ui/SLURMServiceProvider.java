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
package org.eclipse.ptp.rm.slurm.ui;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.ptp.core.PTPCorePlugin;
import org.eclipse.ptp.core.elementcontrols.IPUniverseControl;
import org.eclipse.ptp.core.elementcontrols.IResourceManagerControl;
import org.eclipse.ptp.rm.core.rmsystem.AbstractRemoteResourceManagerServiceProvider;
import org.eclipse.ptp.rm.slurm.core.SLURMPreferenceManager;
import org.eclipse.ptp.rm.slurm.core.rmsystem.ISLURMResourceManagerConfiguration;
import org.eclipse.ptp.rm.slurm.core.rmsystem.SLURMResourceManager;
import org.eclipse.ptp.services.core.IServiceProvider;
import org.eclipse.ptp.services.core.IServiceProviderWorkingCopy;


/**
 * Service provider for IBM Parallel Environment
 */
public class SLURMServiceProvider extends AbstractRemoteResourceManagerServiceProvider implements ISLURMResourceManagerConfiguration{
	private static final String TAG_SLURMD_PATH = "slurmdPath"; //$NON-NLS-1$
	private static final String TAG_SLURMD_ARGS = "slurmdArgs"; //$NON-NLS-1$
	private static final String TAG_SLURMD_DEFAULTS = "slurmdDefaults"; //$NON-NLS-1$
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$
	
	private Preferences preferences = SLURMPreferenceManager.getPreferences();
	
	public SLURMServiceProvider() {
		super();
		setDescription("SLURM Resource Manager"); //$NON-NLS-1$
	}

	/**
	 * Constructor for creating a working copy of the service provider
	 * 
	 * @param provider provider we are making a copy from
	 */
	public SLURMServiceProvider(IServiceProvider provider) {
		super(provider);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.services.core.ServiceProvider#copy()
	 */
	@Override
	public IServiceProviderWorkingCopy copy() {
		return new SLURMServiceProvider(this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rmsystem.AbstractResourceManagerServiceProvider#createResourceManager()
	 */
	@Override
	public IResourceManagerControl createResourceManager() {
		IPUniverseControl universe = (IPUniverseControl) PTPCorePlugin.getDefault().getUniverse();
		return new SLURMResourceManager(Integer.valueOf(universe.getNextResourceManagerId()), universe, this);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rmsystem.AbstractResourceManagerServiceProvider#getResourceManagerId()
	 */
	@Override
	public String getResourceManagerId() {
		return getId();
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.slurm.core.rmsystem.ISLURMResourceManagerConfiguration#getSlurmdArgs()
	 */
	public String getSlurmdArgs() {
		return getString(TAG_SLURMD_ARGS, preferences.getString(EMPTY_STRING));
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.slurm.core.rmsystem.ISLURMResourceManagerConfiguration#getSlurmdPath()
	 */
	public String getSlurmdPath() {
		return getString(TAG_SLURMD_PATH, preferences.getString(EMPTY_STRING));
	}
	
    /* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.slurm.core.rmsystem.ISLURMResourceManagerConfiguration#getUseDefaults()
	 */
	public boolean getUseDefaults() {
		return getBoolean(TAG_SLURMD_DEFAULTS, true);
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
		String name = "SLURM"; //$NON-NLS-1$
		String conn = getConnectionName();
		if (conn != null && !conn.equals("")) { //$NON-NLS-1$
		    name += "@" + conn; //$NON-NLS-1$
		}
		setName(name);
		setDescription("SLURM Resource Manager"); //$NON-NLS-1$
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.slurm.core.rmsystem.ISLURMResourceManagerConfiguration#setSlurmdArgs(java.lang.String)
	 */
	public void setSlurmdArgs(String slurmdArgs) {
		putString(TAG_SLURMD_ARGS, slurmdArgs);
	}	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.slurm.core.rmsystem.ISLURMResourceManagerConfiguration#setSlurmdPath(java.lang.String)
	 */
	public void setSlurmdPath(String slurmdPath) {
		putString(TAG_SLURMD_PATH, slurmdPath);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rm.slurm.core.rmsystem.ISLURMResourceManagerConfiguration#setUseDefaults(boolean)
	 */
	public void setUseDefaults(boolean useDefaults) {
		putBoolean(TAG_SLURMD_DEFAULTS, useDefaults);
	}
}
