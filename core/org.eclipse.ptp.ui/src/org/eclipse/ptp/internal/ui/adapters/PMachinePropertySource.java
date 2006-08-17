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
package org.eclipse.ptp.internal.ui.adapters;

import org.eclipse.ptp.core.IPMachine;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class PMachinePropertySource implements IPropertySource {

	private final IPMachine pmachine;
	private final PropertyDescriptor[] descriptors;

	public PMachinePropertySource(IPMachine pmachine) {
		this.pmachine = pmachine;
		String[] keys = pmachine.getAttributeKeys();
		descriptors = new PropertyDescriptor[keys.length + 2];
		descriptors[0] = new PropertyDescriptor("name", "name");
		descriptors[1] = new PropertyDescriptor("numNodes", "numNodes");
		for (int i = 0; i < keys.length; ++i) {
			descriptors[i+2] = new PropertyDescriptor(keys[i], keys[i]);
		}
	}

	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		return descriptors;
	}

	public Object getPropertyValue(Object id) {
		if ("name".equals(id)) {
			return pmachine.getElementName();
		}
		if ("numNodes".equals(id)) {
			return Integer.toString(pmachine.getNodes().length);
		}

		return pmachine.getAttribute(id.toString());
	}

	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub
		
	}

	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub
		
	}

}
