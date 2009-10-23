/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.internal.rdt.core.index;

import java.net.URI;
import java.util.Arrays;

import org.eclipse.cdt.core.dom.IPDOMIndexer;
import org.eclipse.cdt.core.dom.IPDOMIndexerTask;
import org.eclipse.cdt.core.model.ICElement;
import org.eclipse.cdt.core.model.ITranslationUnit;
import org.eclipse.cdt.internal.core.pdom.IndexerProgress;
import org.eclipse.cdt.utils.FileSystemUtilityManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ptp.internal.rdt.core.model.Scope;
import org.eclipse.ptp.rdt.core.serviceproviders.IIndexServiceProvider;

/**
 * @author crecoskie
 *
 */
public class RemoteIndexerTask implements IPDOMIndexerTask {

	protected RemoteFastIndexer fIndexer;
	
	protected IIndexServiceProvider fIndexServiceProvider;
	
	protected ITranslationUnit[] fAdded;
	protected ITranslationUnit[] fChanged;
	protected ITranslationUnit[] fRemoved;
	protected boolean fUpdate;
	
	private RemoteIndexerProgress fRemoteProgress = new RemoteIndexerProgress();
	
	public RemoteIndexerTask(RemoteFastIndexer indexer,
			IIndexServiceProvider indexingServiceProvider, ITranslationUnit[] added, ITranslationUnit[] changed, ITranslationUnit[] removed, boolean update) {
		fIndexer = indexer;
		
		fIndexServiceProvider = indexingServiceProvider;
		
		fAdded = added;
		fChanged = changed;
		fRemoved = removed;
		fUpdate = update;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.dom.IPDOMIndexerTask#getIndexer()
	 */
	public IPDOMIndexer getIndexer() {
		return fIndexer;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.dom.IPDOMIndexerTask#getProgressInformation()
	 */
	public IndexerProgress getProgressInformation() {
		
		synchronized (fRemoteProgress) {
			return RemoteIndexerProgress.getIndexerProgress(fRemoteProgress);
		}
	}
	
	public void updateProgressInformation(RemoteIndexerProgress progress){
		synchronized (fRemoteProgress) {
			if (progress != null)
				fRemoteProgress = progress;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.dom.IPDOMIndexerTask#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InterruptedException {
		IIndexLifecycleService service = fIndexServiceProvider.getIndexLifeCycleService();
		IProject project = fIndexer.getProject().getProject();
		String name = project.getName();
		
		FileSystemUtilityManager fsUtilityManager = FileSystemUtilityManager.getDefault();
		
		URI locationURI = project.getLocationURI();
		
		String mappedPath = fsUtilityManager.getMappedPath(locationURI);
		String rootPath = fsUtilityManager.getPathFromURI(locationURI);
		
		URI managedURI = fsUtilityManager.getManagedURI(locationURI); 
		String host = null;
		
		if(managedURI != null)
			host = managedURI.getHost();
		else
			host = locationURI.getHost();
		
		if (fUpdate)
			service.update(new Scope(project.getName(), locationURI.getScheme(), host, rootPath, mappedPath), Arrays.<ICElement>asList(fAdded), Arrays.<ICElement>asList(fChanged), Arrays.<ICElement>asList(fRemoved), monitor, this);
		else
			service.reindex(new Scope(project.getName(), locationURI.getScheme(), host, rootPath, mappedPath), fIndexServiceProvider.getIndexLocation(), Arrays.<ICElement>asList(fAdded), monitor, this);
	}

}
