/*******************************************************************************
 * Copyright (c) 2006, 2011 QNX Software Systems and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * QNX - Initial API and implementation
 * Markus Schorn (Wind River Systems)
 * IBM Corporation
 *******************************************************************************/

/* -- ST-Origin --
 * Source folder: org.eclipse.cdt.ui/src
 * Class: org.eclipse.cdt.internal.ui.search.PDOMSearchElementQuery
 * Version: 1.14
 */

package org.eclipse.ptp.internal.rdt.core.search;


import org.eclipse.cdt.core.dom.ast.IBinding;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.index.IIndexLocationConverter;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ISourceReference;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ptp.internal.rdt.core.index.IndexQueries;

public class RemoteSearchElementQuery extends RemoteSearchQuery {
	private static final long serialVersionUID = 1L;

	private ISourceReference fElement;

	private String fPath;
	
	public RemoteSearchElementQuery(ICElement[] scope, ISourceReference element, String path, int flags) {
		super(scope, flags | IIndex.SEARCH_ACROSS_LANGUAGE_BOUNDARIES);
		fElement = element;
		fPath = path;
	}
	
	public ISourceReference getSourceReference() {
		return fElement;
	}


	public void runWithIndex(IIndex parseIndex,  IIndex searchScopeindex, IIndexLocationConverter converter, IProgressMonitor monitor) throws OperationCanceledException, CoreException, InterruptedException {
		fConverter = converter;
		
		if (fElement instanceof ICElement) {
			IBinding binding = null;
			parseIndex.acquireReadLock();
			try{
				binding= IndexQueries.elementToBinding(parseIndex, (ICElement) fElement, fPath);
				cElement = CElementForBinding(parseIndex, binding);
			}finally{
				parseIndex.releaseReadLock();
			}
			if (binding != null) {
				searchScopeindex.acquireReadLock();
				try{
					createMatches(searchScopeindex, binding);
				}finally{
					searchScopeindex.releaseReadLock();
				}
			}
		}
	
	}
	
}
