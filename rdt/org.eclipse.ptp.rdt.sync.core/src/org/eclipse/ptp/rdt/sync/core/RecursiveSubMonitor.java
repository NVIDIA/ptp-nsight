/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    John Eblen - initial implementation
 *******************************************************************************/
package org.eclipse.ptp.rdt.sync.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IProgressMonitorWithBlocking;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubMonitor;

/**
 * Submonitor capable of recursive task reporting. New subtasks are appended to the parent's subtask.
 * Operates as a thin wrapper around the usual tree of submonitors, which still do most of the work.
 * 
 * @since 3.0
 * 
 */
public class RecursiveSubMonitor implements IProgressMonitorWithBlocking {
	private SubMonitor subMonitor;
	private String subTaskName = ""; //$NON-NLS-1$
	private RecursiveSubMonitor parent = null;

	private RecursiveSubMonitor(SubMonitor subMon) {
		subMonitor = subMon;
	}

	private RecursiveSubMonitor(SubMonitor subMon, RecursiveSubMonitor p) {
		subMonitor = subMon;
		parent = p;
	}

	/**
	 * Get the name of the subtask - trivial but essential for recursive task reporting to work
	 * 
	 * @return name of subtask
	 */
	public String getSubTaskName() {
		return subTaskName;
	}

	/**
	 * Return parent monitor or null if parent does not exist or is not a RecursiveSubMonitor
	 * 
	 * @return parent
	 */
	public RecursiveSubMonitor getParentMonitor() {
		return parent;
	}

	/**
	 * Intercept setting of subtask name to store it and prepend parent's subtask
	 * 
	 * @param name
	 */
	@Override
	public void subTask(String name) {
		if (parent != null) {
			subTaskName = parent.getSubTaskName() + " : " + name; //$NON-NLS-1$
		} else {
			subTaskName = name;
		}
		subMonitor.subTask(subTaskName);
	}

	/**
	 * Intercept creating of child monitors to store the parent in the new monitor
	 * 
	 * @param totalWork
	 * @return new monitor
	 */
	public RecursiveSubMonitor newChild(int totalWork) {
		return new RecursiveSubMonitor(subMonitor.newChild(totalWork), this);
	}

	/**
	 * Intercept creating of child monitors to store the parent in the new monitor
	 * 
	 * @param totalWork
	 * @param suppressFlags
	 * @return new monitor
	 */
	public RecursiveSubMonitor newChild(int totalWork, int suppressFlags) {
		return new RecursiveSubMonitor(subMonitor.newChild(totalWork, suppressFlags), this);
	}

	/**
	 * Convert the underlying submonitor as before but wrap it in a recursive submonitor.
	 * 
	 * @param monitor
	 * @return
	 */
	public static RecursiveSubMonitor convert(IProgressMonitor monitor) {
		// If already a recursive submonitor, just replace the wrapped submonitor. Do not create a new instance.
		if (monitor instanceof RecursiveSubMonitor) {
			((RecursiveSubMonitor) monitor).subMonitor = SubMonitor.convert(((RecursiveSubMonitor) monitor).subMonitor);
			return (RecursiveSubMonitor) monitor;
		}
		return new RecursiveSubMonitor(SubMonitor.convert(monitor));
	}

	/**
	 * Convert the underlying submonitor as before but wrap it in a recursive submonitor.
	 * 
	 * @param monitor
	 * @return
	 */
	public static RecursiveSubMonitor convert(IProgressMonitor monitor, int work) {
		// If already a recursive submonitor, just replace the wrapped submonitor. Do not create a new instance.
		if (monitor instanceof RecursiveSubMonitor) {
			((RecursiveSubMonitor) monitor).subMonitor = SubMonitor.convert(((RecursiveSubMonitor) monitor).subMonitor, work);
			return (RecursiveSubMonitor) monitor;
		}
		return new RecursiveSubMonitor(SubMonitor.convert(monitor, work));
	}

	/**
	 * Convert the underlying submonitor as before but wrap it in a recursive submonitor.
	 * 
	 * @param monitor
	 * @return
	 */
	public static RecursiveSubMonitor convert(IProgressMonitor monitor, String taskName, int work) {
		// If already a recursive submonitor, just replace the wrapped submonitor. Do not create a new instance.
		if (monitor instanceof RecursiveSubMonitor) {
			((RecursiveSubMonitor) monitor).subMonitor = SubMonitor.convert(((RecursiveSubMonitor) monitor).subMonitor, taskName,
					work);
			return (RecursiveSubMonitor) monitor;
		}
		return new RecursiveSubMonitor(SubMonitor.convert(monitor, taskName, work));
	}

	// Boilerplate forwarding functions - let the wrapped submonitor do most of the work
	public RecursiveSubMonitor setWorkRemaining(int workRemaining) {
		subMonitor.setWorkRemaining(workRemaining);
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitor#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		return subMonitor.isCanceled();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitor#setTaskName(java.lang.String)
	 */
	@Override
	public void setTaskName(String name) {
		subMonitor.setTaskName(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitor#beginTask(java.lang.String, int)
	 */
	@Override
	public void beginTask(String name, int totalWork) {
		subMonitor.beginTask(name, totalWork);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitor#done()
	 */
	@Override
	public void done() {
		subMonitor.done();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitor#internalWorked(double)
	 */
	@Override
	public void internalWorked(double work) {
		subMonitor.internalWorked(work);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitor#worked(int)
	 */
	@Override
	public void worked(int work) {
		subMonitor.worked(work);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitor#setCanceled(boolean)
	 */
	@Override
	public void setCanceled(boolean b) {
		subMonitor.setCanceled(b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#clearBlocked()
	 */
	@Override
	public void clearBlocked() {
		subMonitor.clearBlocked();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IProgressMonitorWithBlocking#setBlocked(org.eclipse.core.runtime.IStatus)
	 */
	@Override
	public void setBlocked(IStatus reason) {
		subMonitor.setBlocked(reason);
	}
}
