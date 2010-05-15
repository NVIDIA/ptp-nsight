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
package org.eclipse.ptp.debug.internal.core.pdi.aif;

import org.eclipse.ptp.debug.core.pdi.messages.Messages;
import org.eclipse.ptp.debug.core.pdi.model.aif.AIFFactory;
import org.eclipse.ptp.debug.core.pdi.model.aif.AIFFormatException;
import org.eclipse.ptp.debug.core.pdi.model.aif.IAIFTypeFloat;

public class AIFTypeFloat extends AIFType implements IAIFTypeFloat {
	private int fSize;
	private final boolean fComplex = false;
	private final boolean fImaginary = false;
	private final boolean fIsLong = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.debug.core.pdi.model.aif.IAIFTypeFloat#isComplex()
	 */
	public boolean isComplex() {
		return fComplex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.debug.core.pdi.model.aif.IAIFTypeFloat#isImaginary()
	 */
	public boolean isImaginary() {
		return fImaginary;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ptp.debug.core.pdi.model.aif.IAIFTypeFloat#isLong()
	 */
	public boolean isLong() {
		return fIsLong;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ptp.debug.internal.core.pdi.aif.AIFType#parse(java.lang.String
	 * )
	 */
	@Override
	public String parse(String fmt) throws AIFFormatException {
		int pos = AIFFactory.getFirstNonDigitPos(fmt, 0, false);
		if (pos == -1) {
			throw new AIFFormatException(Messages.AIFTypeFloat_0);
		}
		fSize = Integer.parseInt(fmt.substring(0, pos));
		return fmt.substring(pos);
	}

	public int sizeof() {
		return fSize;
	}

	@Override
	public String toString() {
		return "f" + sizeof(); //$NON-NLS-1$
	}
}
