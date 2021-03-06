/**********************************************************************
 * Copyright (c) 2009,2011 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ptp.etfw.feedback;

import java.util.List;

import org.eclipse.core.resources.IFile;

/**
 * The interface for the parser that will provide IFeedbackItems for the view.
 * <p>
 * <strong>EXPERIMENTAL</strong>. This class or interface has been added as part of a work in progress. There is no guarantee that
 * this API will work or that it will remain the same. We do not recommending using this API without consulting with the
 * etfw.feedback team.
 * 
 * @author beth tibbitts
 * @since 6.0
 * 
 */
public interface IFeedbackParser {

	/**
	 * Get the items for the feedback view. Note that these should be highest
	 * level items, possible parent nodes, which may have children.
	 * 
	 * @return
	 * @since 5.0
	 */
	public List<IFeedbackItem> getFeedbackItems(IFile file);

	public void createMarkers(List<IFeedbackItem> items, String markerID);

	/**
	 * Marker ID to be used for creating of markers generated by this feedback
	 * parser. For now, they are all the same.
	 * 
	 * @return
	 */
	public String getMarkerID();

	/**
	 * View to be used to show items that this parser provides
	 * 
	 * @return
	 */
	public String getViewID();
}
