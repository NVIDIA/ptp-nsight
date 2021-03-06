/*******************************************************************************
 * Copyright (c) 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.rm.jaxb.control.core;

import java.net.URL;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ptp.core.jobs.IJobControl;
import org.eclipse.ptp.rm.jaxb.core.IVariableMap;
import org.eclipse.ptp.rm.jaxb.core.data.CommandType;
import org.eclipse.ptp.rm.jaxb.core.data.ResourceManagerData;

/**
 * Launch Control interface.
 * 
 * @since 2.1
 * 
 */
public interface ILaunchController extends IJobControl {
	/**
	 * Safely dispose of this controller.
	 */
	public void dispose();

	/**
	 * Get the control configuration
	 * 
	 * @return configuration
	 */
	public ResourceManagerData getConfiguration();

	/**
	 * Get the connection used to control the job
	 * 
	 * @return remote connection name
	 * @since 6.0
	 */
	public String getConnectionName();

	/**
	 * @return variable resolver environment
	 */
	public IVariableMap getEnvironment();

	/**
	 * Get the remote services provider used to control the job
	 * 
	 * @return remote services provider
	 * @since 6.0
	 */
	public String getRemoteServicesId();

	/**
	 * Initialize the controller. The controller is ready to be used after initialization, however it must be started before any
	 * control operations can be performed.
	 * 
	 * @throws CoreException
	 */
	public void initialize() throws CoreException;

	/**
	 * Check if controller is initialized.
	 * 
	 * @return true if controller is initialized
	 */
	public boolean isInitialized();

	/**
	 * Check if the controller has running jobs
	 * 
	 * @return true if controller has running jobs
	 */
	public boolean hasRunningJobs();

	/**
	 * Run a command. The command will be executed on the target system specified in the launch
	 * controller. The any side effects of the command will be applied to the supplied attribute map rather than the launch
	 * controller attribute map.
	 * 
	 * @param command
	 *            Command to execute. Only interactive commands are supported.
	 * @param attributes
	 *            Attribute map containing attributes that will be side effected by the command
	 * @throws CoreException
	 *             This exception is thrown if the command fails to execute.
	 */
	public void runCommand(CommandType command, IVariableMap attributes) throws CoreException;

	/**
	 * Run a command. The command will be executed on the target system specified in the launch
	 * controller.
	 * 
	 * @param name
	 *            Name of the command to execute. The name should refer to a command or action specified in the target
	 *            configuration. Only commands defined using the <code>start-up</code>, <code>shut-down</code>, or
	 *            <code>button-action</code> elements can be run.
	 * @param resetValue
	 *            Name of an attribute to cleared prior to command execution
	 * @param configuration
	 *            Launch configuration from which to initialize attributes. The configuration can be null, in which case the current
	 *            attributes will be used.
	 * @throws CoreException
	 *             This exception is thrown if the command fails to execute.
	 */
	public void runCommand(String name, String resetValue, ILaunchConfiguration configuration) throws CoreException;

	/**
	 * Set the connection name for this control
	 * 
	 * @param connName
	 */
	public void setConnectionName(String connName);

	/**
	 * Set the remote services ID for this control
	 * 
	 * @param id
	 */
	public void setRemoteServicesId(String id);

	/**
	 * @param url
	 *            of the JAXB configuration for this controller.
	 * 
	 * @since 5.0
	 */
	public void setRMConfigurationURL(URL url);

	/**
	 * Start the launch control. This must be called before any other operations.
	 * 
	 * @param monitor
	 *            progress monitor
	 * @throws CoreException
	 *             this exception is thrown if the start command fails
	 */
	public void start(IProgressMonitor monitor) throws CoreException;

	/**
	 * Stop the the launch control. Normally only used when the plugin shuts down or the connection is closed.
	 * 
	 * @throws CoreException
	 *             this exception is thrown if the stop command fails
	 * @since 5.0
	 */
	public void stop() throws CoreException;
}
