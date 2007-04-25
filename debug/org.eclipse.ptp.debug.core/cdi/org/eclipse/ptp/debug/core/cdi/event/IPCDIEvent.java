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
 *******************************************************************************/
package org.eclipse.ptp.debug.core.cdi.event;

import org.eclipse.ptp.core.elements.IPJob;
import org.eclipse.ptp.core.util.BitList;
import org.eclipse.ptp.debug.core.cdi.model.IPCDIObject;
import org.eclipse.ptp.debug.core.cdi.model.IPCDITarget;

/**
 * @author Clement chu
 *
 */
public interface IPCDIEvent {
	/** Get all processes including registered or unregistered
	 * @return
	 */
	public BitList getAllProcesses();
	/** Get unregistered processes only
	 * @return
	 */
	public BitList getAllUnregisteredProcesses();
	/** Get registered processes only
	 * @return
	 */
	public BitList getAllRegisteredProcesses();
	/** Get debug job
	 * @return
	 */
	public IPJob getDebugJob();
	/** Check given task id whether it contains in this event 
	 * @param task_id
	 * @return true if contains given task
	 */
	public boolean containTask(int task_id);
	/** Get pcdi object by given task id
	 * @param task_id
	 * @return
	 */
	public IPCDITarget getTarget(int task_id);
	/** Get pcdi object
	 * @return
	 */
	IPCDIObject getSource();
	
	/** Get source
	 * @param task_id
	 * @return
	 */
	IPCDIObject getSource(int task_id);
}

