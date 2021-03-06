/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.rdt.sync.ui;


/**
 * Must be extended by extensions to the syncProperties extension point.
 * 
 */
public abstract class AbstractSynchronizeProperties implements ISynchronizeProperties {
	public ISynchronizePropertiesDescriptor fDescriptor;

	public AbstractSynchronizeProperties(ISynchronizePropertiesDescriptor descriptor) {
		fDescriptor = descriptor;
	}

	@Override
	public String getNature() {
		return fDescriptor.getNature();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.rdt.sync.ui.ISynchronizePropertiesDescriptor#getProperties()
	 */
	@Override
	public ISynchronizeProperties getProperties() {
		return this;
	}
}
