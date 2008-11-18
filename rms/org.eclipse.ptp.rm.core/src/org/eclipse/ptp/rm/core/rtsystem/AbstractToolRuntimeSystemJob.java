/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.core.rtsystem;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ptp.core.PTPCorePlugin;
import org.eclipse.ptp.core.attributes.ArrayAttribute;
import org.eclipse.ptp.core.attributes.AttributeManager;
import org.eclipse.ptp.core.attributes.BooleanAttribute;
import org.eclipse.ptp.core.attributes.EnumeratedAttribute;
import org.eclipse.ptp.core.attributes.IAttribute;
import org.eclipse.ptp.core.attributes.IAttributeDefinition;
import org.eclipse.ptp.core.attributes.IllegalValueException;
import org.eclipse.ptp.core.attributes.StringAttribute;
import org.eclipse.ptp.core.elements.IPJob;
import org.eclipse.ptp.core.elements.attributes.JobAttributes;
import org.eclipse.ptp.remote.core.IRemoteProcess;
import org.eclipse.ptp.remote.core.IRemoteProcessBuilder;
import org.eclipse.ptp.rm.core.ToolsRMPlugin;
import org.eclipse.ptp.rm.core.messages.Messages;
import org.eclipse.ptp.rm.core.rmsystem.AbstractEffectiveTollRMConfiguration;
import org.eclipse.ptp.rm.core.utils.DebugUtil;
import org.eclipse.ptp.utils.core.linux.ArgumentParser;

/**
 * Implements a job that controls the parallel application launched with a command line tool.
 * This class is different from {@link AbstractRemoteCommandJob} because it is not aimed towards parsing output of the
 * tool called by command line, but to prepare a whole launch environment for the command line tool, also supporting
 * semantings of the parallel application launcher.
 *
 * @author Daniel Felix Ferber
 */
public abstract class AbstractToolRuntimeSystemJob extends Job implements IToolRuntimeSystemJob {

	protected String jobID;
	protected String queueID;
	protected IRemoteProcess process = null;
	protected AttributeManager attrMgr;
	protected AbstractToolRuntimeSystem rtSystem;

	protected boolean terminateJobFlag = false;

	public AbstractToolRuntimeSystemJob(String jobID, String queueID, String name, AbstractToolRuntimeSystem rtSystem,
			AttributeManager attrMgr) {
		super(name);
		this.attrMgr = attrMgr;
		this.rtSystem = rtSystem;
		this.jobID = jobID;
		this.queueID = queueID;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IToolRuntimeSystemJob.class) {
			return this;
		}
		return super.getAdapter(adapter);
	}

	public String getQueueID() {
		return queueID;
	}

	public String getJobID() {
		return jobID;
	}

	public AbstractToolRuntimeSystem getRtSystem() {
		return rtSystem;
	}

	public AttributeManager getAttrMgr() {
		return attrMgr;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		changeJobState(JobAttributes.State.STARTED);

		if (DebugUtil.RTS_JOB_TRACING_MORE) {
			System.out.println("Launch attributes:"); //$NON-NLS-1$
			String array[] = getAttrMgr().toStringArray();
			for (int i = 0; i < array.length; i++) {
				System.out.println(array[i]);
			}
		}

		try {
			DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "RTS job #{0}: handle prepare", jobID); //$NON-NLS-1$
			doPrepareExecution();
		} catch (CoreException e) {
			changeJobState(JobAttributes.State.ERROR);
			return new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), Messages.AbstractToolRuntimeSystemJob_Exception_PrepareExecution, e);
		}

		try {
			/*
			 * Calculate command and environment.
			 */
			DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "About to run RTS job #{0}.", jobID); //$NON-NLS-1$
			List<String> command = null;
			Map<String,String> environment = null;
			String directory = null;
			try {
				AttributeManager baseSubstitutionAttributeManager = retrieveBaseSubstitutionAttributes();
				//System.out.println(baseSubstitutionAttributeManager);
				environment = retrieveEnvironment(baseSubstitutionAttributeManager);
				directory = retrieveWorkingDirectory(baseSubstitutionAttributeManager);

				AttributeManager commandSubstitutionAttributeManager = retrieveCommandSubstitutionAttributes(baseSubstitutionAttributeManager, directory, environment);
				//System.out.println(commandSubstitutionAttributeManager);
				BooleanAttribute debugAttr = attrMgr.getAttribute(JobAttributes.getDebugFlagAttributeDefinition());
				if (debugAttr != null && debugAttr.getValue()) {
					command = retrieveCreateDebugCommand(commandSubstitutionAttributeManager);
				} else {
					command = retrieveCreateLaunchCommand(commandSubstitutionAttributeManager);
				}
				if (DebugUtil.RTS_JOB_TRACING) {
					System.out.println("Available macros for environment and work directory:"); //$NON-NLS-1$
					for (IAttribute<?, ?, ?> attr : baseSubstitutionAttributeManager.getAttributes()) {
						System.out.println(MessageFormat.format("  {0}={1}", attr.getDefinition().getId(), attr.getValueAsString())); //$NON-NLS-1$
					}
					System.out.println("Available macros for command:"); //$NON-NLS-1$
					for (IAttribute<?, ?, ?> attr : commandSubstitutionAttributeManager.getAttributes()) {
						System.out.println(MessageFormat.format("  {0}={1}", attr.getDefinition().getId(), attr.getValueAsString())); //$NON-NLS-1$
					}
					System.out.println("Environment variables:"); //$NON-NLS-1$
					for (Entry<String, String> env : environment.entrySet()) {
						System.out.println(MessageFormat.format("  export {0}={1}", env.getKey(), env.getValue())); //$NON-NLS-1$
					}
					System.out.println(MessageFormat.format("Work directory: {0}", directory)); //$NON-NLS-1$
					ArgumentParser argumentParser = new ArgumentParser(command);
					System.out.println(MessageFormat.format("Command: {0}", argumentParser.getCommandLine(false))); //$NON-NLS-1$
				}
			} catch (CoreException e) {
				changeJobState(JobAttributes.State.ERROR);
				return new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), Messages.AbstractToolRuntimeSystemJob_Exception_CreateCommand, e);
			}

			try {
				DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "RTS job #{0}: handle before execution", jobID); //$NON-NLS-1$
				doBeforeExecution();
			} catch (CoreException e) {
				changeJobState(JobAttributes.State.ERROR);
				return new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), Messages.AbstractToolRuntimeSystemJob_Exception_BeforeExecution, e);
			}

			/*
			 * Execute remote command for the job.
			 */
			try {
				IRemoteProcessBuilder processBuilder = rtSystem.createProcessBuilder(command, directory);
				processBuilder.environment().putAll(environment);
				DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "RTS job #{0}: start", jobID); //$NON-NLS-1$
				process = processBuilder.start();
			} catch (IOException e) {
				changeJobState(JobAttributes.State.ERROR);
				return new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), Messages.AbstractToolRuntimeSystemJob_Exception_ExecuteCommand, e);
			}

			try {
				DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "RTS job #{0}: handle start", jobID); //$NON-NLS-1$
				doExecutionStarted();
			} catch (CoreException e) {
				changeJobState(JobAttributes.State.ERROR);
				return new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), Messages.AbstractToolRuntimeSystemJob_Exception_ExecutionStarted, e);
			}

			changeJobState(JobAttributes.State.RUNNING);

			try {
				DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "RTS job #{0}: wait to finish", jobID); //$NON-NLS-1$
				doWaitExecution();
			} catch (CoreException e) {
				changeJobState(JobAttributes.State.ERROR);
				return new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), Messages.AbstractToolRuntimeSystemJob_Exception_WaitExecution, e);
			}

			DebugUtil.trace(DebugUtil.RTS_JOB_TRACING, "RTS job #{0}: exit value {1}", jobID, process.exitValue()); //$NON-NLS-1$


			//			try {
			//				DebugUtil.trace(DebugUtil.COMMAND_TRACING, "RTS job #{0}: wait to finish", jobID); //$NON-NLS-1$
			//				process.waitFor();
			//			} catch (InterruptedException e) {
			//				changeJobState(JobAttributes.State.ERROR);
			//				return new Status(IStatus.ERROR, Activator.getDefault().getBundle().getSymbolicName(), "Failed while terminating the command.", e);
			//			}

			try {
				DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "RTS job #{0}: handle finish", jobID); //$NON-NLS-1$
				doExecutionFinished();
			} catch (CoreException e) {
				changeJobState(JobAttributes.State.ERROR);
				return new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), Messages.AbstractToolRuntimeSystemJob_Exception_ExecutionFinished, e);
			}

			changeJobState(JobAttributes.State.TERMINATED);

			return new Status(IStatus.OK, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), NLS.bind(Messages.AbstractToolRuntimeSystemJob_Success, process.exitValue()));

		} finally {
			DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "RTS job #{0}: cleanup", jobID); //$NON-NLS-1$
			final IPJob ipJob = PTPCorePlugin.getDefault().getUniverse().getResourceManager(rtSystem.getRmID()).getQueueById(getQueueID()).getJobById(getJobID());
			switch (ipJob.getState()) {
			case TERMINATED:
			case ERROR:
				break;
			case PENDING:
			case RUNNING:
			case STARTED:
			case SUSPENDED:
			case UNKNOWN:
				changeJobState(JobAttributes.State.TERMINATED);
				break;
			}
			doExecutionCleanUp();
		}
	}

	abstract protected void doPrepareExecution() throws CoreException;

	abstract protected void doExecutionCleanUp();

	abstract protected void doWaitExecution() throws CoreException;

	abstract protected void doExecutionFinished() throws CoreException;

	abstract protected void doExecutionStarted() throws CoreException;

	abstract protected void doBeforeExecution() throws CoreException;

	abstract protected void doTerminateJob();

	/**
	 * Change the state of the job state.
	 * @param newState
	 */
	protected void changeJobState(JobAttributes.State newState) {
		EnumeratedAttribute<JobAttributes.State> state = JobAttributes.getStateAttributeDefinition().create(newState);
		AttributeManager attrManager = new AttributeManager();
		attrManager.addAttribute(state);
		rtSystem.changeJob(jobID, attrManager);
	}

	/**
	 * Retrieve the working directory for the launch.
	 * @param baseSubstitutionAttributeManager
	 * @return
	 */
	protected String retrieveWorkingDirectory(AttributeManager baseSubstitutionAttributeManager) {
		/*
		 * TODO Return IPath instead of string
		 */
		String workdir = attrMgr.getAttribute(JobAttributes.getWorkingDirectoryAttributeDefinition()).getValue();
		String newWorkdir = replaceVariables(workdir, baseSubstitutionAttributeManager);
		if (! workdir.equals(newWorkdir)) {
			DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "Changed work directory from {0} to {1}", workdir, newWorkdir); //$NON-NLS-1$
			workdir = newWorkdir;
		}
		return workdir;
	}

	/**
	 * Retrieve the environment variables.
	 * @param baseSubstitutionAttributeManager
	 */
	protected Map<String,String> retrieveEnvironment(AttributeManager baseSubstitutionAttributeManager) throws CoreException {
		HashMap<String, String> environmentMap = new HashMap<String, String>();

		/*
		 * First, get environment from the attribute manager.
		 */
		retrieveEnvironmentFromAttrMrg(environmentMap);

		/*
		 * Then, get extra environment variables that are specific for the tool.
		 */
		HashMap<String, String> extraEnvironmentMap = doRetrieveToolEnvironment();
		if (extraEnvironmentMap != null) {
			environmentMap.putAll(extraEnvironmentMap);
		}

		/*
		 * Do substitution on each environment variable.
		 */
		for (Iterator<Entry<String, String>> iterator = environmentMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, String> env = iterator.next();
			String value = env.getValue();
			String newValue = replaceVariables(value, baseSubstitutionAttributeManager);
			if (! value.equals(newValue)) {
				DebugUtil.trace(DebugUtil.RTS_JOB_TRACING_MORE, "Changed environment '{0}={1}' to '{0}={2}", env.getKey(), value, newValue); //$NON-NLS-1$
				env.setValue(newValue);
			}
		}

		return environmentMap;
	}

	/**
	 * Retrieve additional environment variables that are specific for the tool.
	 */
	abstract protected HashMap<String, String> doRetrieveToolEnvironment() throws CoreException;

	private void retrieveEnvironmentFromAttrMrg(
			HashMap<String, String> environmentMap) {
		ArrayAttribute<String> environmentAttribute = getAttrMgr().getAttribute(JobAttributes.getEnvironmentAttributeDefinition());
		if (environmentAttribute != null) {
			List<String> environment = environmentAttribute.getValue();
			for (String entry : environment) {
				int i = entry.indexOf('=');
				String key = entry.substring(0, i);
				String value = entry.substring(i+1);
				environmentMap.put(key, value);
			}
		}
	}

	/**
	 * Retrieve attributes used to expand macros.
	 * @return
	 * @throws CoreException
	 */
	protected AttributeManager retrieveBaseSubstitutionAttributes() throws CoreException {
		AttributeManager newAttributeManager = new AttributeManager(getAttrMgr().getAttributes());

		/*
		 * First, add all default attributes that are default attributes for the launch.
		 * If they are not present in the launch attributes, then use default value.
		 */
		for (IAttributeDefinition<?, ?, ?> attributeDefinition : getDefaultSubstitutionAttributes()) {
			IAttribute<?, ?, ?> attribute = newAttributeManager.getAttribute(attributeDefinition.getId());
			if (attribute == null) {
				// Create one with default value.
				try {
					newAttributeManager.addAttribute(attributeDefinition.create());
				} catch (IllegalValueException e) {
					throw new CoreException(new Status(IStatus.ERROR, ToolsRMPlugin.getDefault().getBundle().getSymbolicName(), NLS.bind(Messages.AbstractToolRuntimeSystemJob_Exception_DefaultAttributeValue, attributeDefinition.getName()), e));
				}
			}
		}

		/*
		 * Then, add attributes that are specific for the tool.
		 */
		IAttribute<?,?,?> extraAttributes[] = doRetrieveToolBaseSubstitutionAttributes();
		if (extraAttributes != null) {
			newAttributeManager.addAttributes(extraAttributes);
		}

		return newAttributeManager;
	}

	/**
	 * Retrieve additional attributes to expand macros that are specific for the tool.
	 */
	abstract protected IAttribute<?, ?, ?>[] doRetrieveToolBaseSubstitutionAttributes() throws CoreException;

	/**
	 * A list of all attributes definitions from the launch configuration that can be used to expand macros.
	 * @return
	 */
	protected IAttributeDefinition<?, ?, ?>[] getDefaultSubstitutionAttributes() {
		return new IAttributeDefinition[]{
				JobAttributes.getEnvironmentAttributeDefinition(),
				JobAttributes.getExecutableNameAttributeDefinition(),
				JobAttributes.getExecutablePathAttributeDefinition(),
				JobAttributes.getJobIdAttributeDefinition(),
				JobAttributes.getNumberOfProcessesAttributeDefinition(),
				JobAttributes.getProgramArgumentsAttributeDefinition(),
				JobAttributes.getQueueIdAttributeDefinition(),
				JobAttributes.getSubIdAttributeDefinition(),
				JobAttributes.getUserIdAttributeDefinition(),
				JobAttributes.getWorkingDirectoryAttributeDefinition()
		};
	}

	final protected AttributeManager retrieveCommandSubstitutionAttributes(
			AttributeManager baseSubstitutionAttributeManager,
			String directory, Map<String, String> environment) {
		AttributeManager newAttributeManager = new AttributeManager(baseSubstitutionAttributeManager.getAttributes());

		/*
		 * Add attributes that are specific for the tool.
		 */
		IAttribute<?,?,?> extraAttributes[] = doRetrieveToolCommandSubstitutionAttributes(baseSubstitutionAttributeManager, directory, environment);
		if (extraAttributes != null) {
			newAttributeManager.addAttributes(extraAttributes);
		}

		return newAttributeManager;
	}


	abstract protected IAttribute<?, ?, ?>[] doRetrieveToolCommandSubstitutionAttributes(
			AttributeManager baseSubstitutionAttributeManager,
			String directory, Map<String, String> environment);

	protected List<String> retrieveCreateLaunchCommand(AttributeManager substitutionAttributeManager) throws CoreException {
		/*
		 * Create launch command. If there is no launch command, simply launch the executable.
		 */
		AbstractEffectiveTollRMConfiguration effectiveConfiguration = getRtSystem().retrieveEffectiveToolRmConfiguration();
		List<String> command = new ArrayList<String>();
		if (! effectiveConfiguration.hasLaunchCmd()) {
			// Fall back to calling the executable.
			StringAttribute execPath = getAttrMgr().getAttribute(JobAttributes.getExecutablePathAttributeDefinition());
			ArrayAttribute<String> arguments = getAttrMgr().getAttribute(JobAttributes.getProgramArgumentsAttributeDefinition());
			command.add(execPath.getValue());
			command.addAll(arguments.getValue());
		} else {
			// Use the tool to launch executable
			String launchCommand = effectiveConfiguration.getLaunchCmd();
			Assert.isNotNull(launchCommand);
			Assert.isTrue(launchCommand.trim().length() > 0);
			launchCommand = replaceVariables(launchCommand, substitutionAttributeManager);
			ArgumentParser argumentParser = new ArgumentParser(launchCommand);
			command = argumentParser.getTokenList();
		}
		return command;
	}

	protected List<String> retrieveCreateDebugCommand(AttributeManager substitutionAttributeManager) throws CoreException {
		/*
		 * Create debug command. If there is no debug command, simply launch the executable.
		 */
		AbstractEffectiveTollRMConfiguration effectiveConfiguration = getRtSystem().retrieveEffectiveToolRmConfiguration();
		List<String> command = new ArrayList<String>();
		if (! effectiveConfiguration.hasDebugCmd()) {
			// Fall back to calling the executable.
			StringAttribute execPath = getAttrMgr().getAttribute(JobAttributes.getExecutablePathAttributeDefinition());
			ArrayAttribute<String> arguments = getAttrMgr().getAttribute(JobAttributes.getProgramArgumentsAttributeDefinition());
			command.add(execPath.getValue());
			command.addAll(arguments.getValue());
		} else {
			// Use the tool to launch executable
			String debugCommand = effectiveConfiguration.getDebugCmd();
			Assert.isNotNull(debugCommand);
			Assert.isTrue(debugCommand.trim().length() > 0);
			debugCommand = replaceVariables(debugCommand, substitutionAttributeManager);
			ArgumentParser argumentParser = new ArgumentParser(debugCommand);
			command = argumentParser.getTokenList();
		}
		return command;
	}

	/*
	 * Pattern to fined variables according these rules:
	 * Starts with "${" and ends with "}"
	 * The content is a name and a set of parameters separated by ":"
	 * In the parameters, "\" may be used to quote following chars: '\', '}' and ':'
	 *
	 * TODO move this patter substitution code into the attribute manager
	 * TODO enable the attribute manager to do substitution -> have this feature available on entire PTP.
	 */
	static final Pattern variablePattern = Pattern.compile(("/$/{(/w+)("+"(?:(?:////)|(?:///})|[^/}])*"+")/}").replace('/','\\')); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	static final Pattern parameterPattern = Pattern.compile(":((?:(?:////)|(?:///:)|(?:///})|[^:])*)".replace('/', '\\')); //$NON-NLS-1$

	/**
	 * Performs substitution of variables using attributes from the attribute manager as variables.
	 * @param input the string with variables.
	 * @param substitutionAttributeManager
	 * @return The string after substitution of variables.
	 */
	protected String replaceVariables(String input, AttributeManager substitutionAttributeManager) {
		StringBuffer output = new StringBuffer();
		Matcher matcher = variablePattern.matcher(input);

		int lastPos = 0;
		while (matcher.find()) {
			int startPos = matcher.start();
			int endPos = matcher.end();
			String name = matcher.group(1);
			String parameterList = matcher.group(2);
			String variable = matcher.group();
			output.append(input.substring(lastPos, startPos));

			/*
			 * Resolve variable.
			 */
			String resolvedValue = null;
			IAttribute<?,?,?> attribute = substitutionAttributeManager.getAttribute(name);
			if (attribute != null) {
				if (attribute instanceof ArrayAttribute<?>) {
					/*
					 * Retrieve parameters or use defaults.
					 */
					String optStartStr = ""; //$NON-NLS-1$
					String optEndStr = ""; //$NON-NLS-1$
					String startStr = ""; //$NON-NLS-1$
					String endStr = ""; //$NON-NLS-1$
					String separatorStr = " "; //$NON-NLS-1$
					Matcher paramMatcher = parameterPattern.matcher(parameterList);
					if (paramMatcher.find()) {
						startStr = paramMatcher.group(1);
						if (paramMatcher.find()) {
							separatorStr = paramMatcher.group(1);
							if (paramMatcher.find()) {
								endStr = paramMatcher.group(1);
								if (paramMatcher.find()) {
									optStartStr = paramMatcher.group(1);
									if (paramMatcher.find()) {
										optEndStr = paramMatcher.group(1);
									}
								}
							}
						}
					}

					/*
					 * Build content.
					 */
					ArrayAttribute<?> array_attr = (ArrayAttribute<?>) attribute;
					StringBuffer buffer = new StringBuffer();
					boolean first = true;
					List<?> array = array_attr.getValue();
					if (array.size() > 0) {
						buffer.append(optStartStr);
					}
					buffer.append(startStr);
					for (Object element : array) {
						if (first) {
							first = false;
						} else {
							buffer.append(separatorStr);
						}
						assert element != null;
						buffer.append(element);
					}
					buffer.append(endStr);
					if (array.size() > 0) {
						buffer.append(optEndStr);
					}
					resolvedValue = buffer.toString();
				} else {
					resolvedValue = attribute.getValueAsString();
				}
			}

			/*
			 * If failed to resolve variable, keep it on the string. Else replace by its value.
			 */
			if (resolvedValue == null) {
				output.append(variable);
			} else {
				// Recursive macro substitution
				resolvedValue = replaceVariables(resolvedValue, substitutionAttributeManager);
				output.append(resolvedValue);
			}
			lastPos = endPos;
		}
		output.append(input.substring(lastPos));
		String result = output.toString();
		return result;
	}

	public void terminate() {
		terminateJobFlag = true;
		if (process != null) {
			process.destroy();
		}
		doTerminateJob();
	}

	@Override
	protected void canceling() {
		terminate();
		super.canceling();
	}
}
