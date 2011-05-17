/*******************************************************************************
 * Copyright (c) 2011 University of Illinois All rights reserved. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html 
 * 	
 * Contributors: 
 * 	Albert L. Rossi - design and implementation
 ******************************************************************************/
package org.eclipse.ptp.rm.jaxb.control.internal.runnable;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ptp.rm.jaxb.control.JAXBControlConstants;
import org.eclipse.ptp.rm.jaxb.control.internal.ICommandJobStatus;
import org.eclipse.ptp.rm.jaxb.control.internal.ICommandJobStatusMap;
import org.eclipse.ptp.rmsystem.IJobStatus;
import org.eclipse.ptp.rmsystem.IResourceManagerControl;
import org.eclipse.ptp.rmsystem.IResourceManagerMonitor;

/**
 * Class for handling status of submitted jobs.
 * 
 * @author arossi
 * 
 */
public class JobStatusMap extends Thread implements ICommandJobStatusMap {

	private final IResourceManagerControl control;
	private final IResourceManagerMonitor monitor;
	private final Map<String, ICommandJobStatus> map;
	private Thread t;
	private boolean running = false;

	public JobStatusMap(IResourceManagerControl control, IResourceManagerMonitor monitor) {
		this.control = control;
		this.monitor = monitor;
		map = new HashMap<String, ICommandJobStatus>();
	}

	/*
	 * Also unpins the id if it is pinned. (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.ICommandJobStatusMap#addJobStatus(java.lang
	 * .String, org.eclipse.ptp.rm.jaxb.core.ICommandJobStatus)
	 */
	public void addJobStatus(String jobId, ICommandJobStatus status) {
		boolean notifyAdd = false;
		synchronized (map) {
			if (!map.containsKey(jobId)) {
				notifyAdd = true;
			}
			map.put(jobId, status);
		}
		if (notifyAdd && monitor != null) {
			monitor.addJob(jobId, status);
		}
	}

	/*
	 * Synchronized cancel. External calls are premature and thus should not
	 * block waiting for the remote files if any.(non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.ICommandJobStatusMap#cancel(java.lang.String
	 * )
	 */
	public ICommandJobStatus cancel(String jobId) {
		ICommandJobStatus status = null;
		synchronized (map) {
			status = map.get(jobId);
			if (status != null) {
				status.cancel();
				status.setState(IJobStatus.CANCELED);
			}
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.ICommandJobStatusMap#getStatus(java.lang
	 * .String)
	 */
	public ICommandJobStatus getStatus(String jobId) {
		ICommandJobStatus status = null;
		synchronized (map) {
			status = map.get(jobId);
		}
		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.jaxb.core.ICommandJobStatusMap#halt()
	 */
	public void halt() {
		synchronized (map) {
			running = false;
			t.interrupt();
			map.notifyAll();
		}
	}

	/**
	 * Thread daemon for cleanup on the map. Eliminates stray completed state
	 * information, and also starts the stream proxies on jobs which have been
	 * submitted to a scheduler and have become active.
	 */
	@Override
	public void run() {
		t = Thread.currentThread();
		Map<String, String> toPrune = new HashMap<String, String>();

		synchronized (map) {
			running = true;
		}

		while (isRunning()) {
			synchronized (map) {
				try {
					map.wait(2 * JAXBControlConstants.MINUTE_IN_MS);
				} catch (InterruptedException ignored) {
				}

				for (String jobId : map.keySet()) {
					IJobStatus status = control.getJobStatus(jobId);
					String state = status.getState();
					if (IJobStatus.COMPLETED.equals(state)) {
						toPrune.put(jobId, jobId);
					}
				}

				for (String jobId : toPrune.keySet()) {
					remove(jobId, true);
				}
				toPrune.clear();
			}
		}

		synchronized (map) {
			for (String jobId : map.keySet()) {
				doTerminated(jobId, false);
			}
			map.clear();
		}
	}

	/*
	 * Synchronized terminate. (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.rm.jaxb.core.ICommandJobStatusMap#terminated(java.lang
	 * .String)
	 */
	public ICommandJobStatus terminated(String jobId) {
		ICommandJobStatus status = null;
		synchronized (map) {
			status = doTerminated(jobId, true);
		}
		return status;
	}

	/**
	 * Must be called under synchronization.
	 * 
	 * @param jobId
	 *            either internal UUID or scheduler id for the job.
	 * @param block
	 *            wait for the remote files
	 */
	private ICommandJobStatus doTerminated(String jobId, boolean block) {
		ICommandJobStatus status = map.get(jobId);
		if (status != null) {
			String d = status.getStateDetail();
			block = block && !IJobStatus.CANCELED.equals(d) && !IJobStatus.FAILED.equals(d);
			if (!status.isInteractive()) {
				if (block) {
					status.maybeWaitForHandlerFiles(JAXBControlConstants.READY_FILE_BLOCK);
				} else {
					status.maybeWaitForHandlerFiles(0);
				}
			}
			status.cancel();
		}
		return status;
	}

	/**
	 * @return whether the daemon is running
	 */
	private boolean isRunning() {
		boolean b = false;
		synchronized (map) {
			b = running;
		}
		return b;
	}

	/**
	 * Must be called under synchronization. Also unpins the id.
	 * 
	 * @param jobId
	 *            either internal UUID or scheduler id for the job.
	 * @param block
	 *            wait for the remote files
	 */
	private ICommandJobStatus remove(String jobId, boolean block) {
		ICommandJobStatus status = doTerminated(jobId, block);
		map.remove(jobId);
		return status;
	}
}