/*******************************************************************************
 * Copyright (c) 2006 The Regents of the University of California. 
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
/**
 * 
 */
package org.eclipse.ptp.rmsystem;

import org.eclipse.ptp.core.elementcontrols.IResourceManagerControl;



/**
 * @author rsqrd
 * 
 */
/**
 * @author rsqrd
 *
 */
public abstract class AbstractResourceManagerFactory implements
		IResourceManagerFactory {

	private String name;
	private String id;

	/**
	 * @param name
	 */
	public AbstractResourceManagerFactory(final String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rm.IResourceManagerFactory#create(org.eclipse.ptp.rm.IRMConfiguration)
	 */
	public abstract IResourceManagerControl create(IResourceManagerConfiguration configuration);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.internal.rm.IResourceManagerFactory#getName()
	 */
	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ptp.rmsystem.IResourceManagerFactory#getId()
	 */
	public String getId() {
		return this.id;
	}
}
