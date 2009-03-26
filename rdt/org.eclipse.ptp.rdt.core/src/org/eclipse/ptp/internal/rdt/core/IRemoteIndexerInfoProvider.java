/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *    Mike Kucera (IBM)
 *******************************************************************************/ 
package org.eclipse.ptp.internal.rdt.core;

import org.eclipse.cdt.internal.core.indexer.IStandaloneScannerInfoProvider;

/**
 * Provides necessary information to the remote indexer.
 */
public interface IRemoteIndexerInfoProvider extends IStandaloneScannerInfoProvider {

	/**
	 * Returns the language ID of the file that corresponds to the given path
	 * or null if the path is not recognized.
	 */
	String getLanguageID(String path);
	
	/**
	 * Returns true if the file that corresponds to the given path
	 * is a header file.
	 */
	boolean isHeaderUnit(String path);
}
