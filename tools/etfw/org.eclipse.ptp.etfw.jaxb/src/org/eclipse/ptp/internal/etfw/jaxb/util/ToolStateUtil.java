/*******************************************************************************
 * Copyright (c) 2009, 2013 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.internal.etfw.jaxb.util;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.ptp.internal.etfw.jaxb.data.ToolStateType;

public class ToolStateUtil {
	
	public static boolean evaluate(boolean globalState, ILaunchConfiguration configuration, ToolStateType toolStateType) {
		
		
		if (configuration == null || toolStateType == null) {
			return globalState;
		}
		
		if (toolStateType.getExecuteIf() != null) {
			boolean result = ToolStateRuleUtil.evaluate(configuration, toolStateType.getExecuteIf());
			return result;
		}
		
		if (toolStateType.getDoNotExecuteIf() != null) {
			boolean result = !ToolStateRuleUtil.evaluate(configuration, toolStateType.getDoNotExecuteIf());
			return result;
		}
		
		return globalState;
		
	}

}
