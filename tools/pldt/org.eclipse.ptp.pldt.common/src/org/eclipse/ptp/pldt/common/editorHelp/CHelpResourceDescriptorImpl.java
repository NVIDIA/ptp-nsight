/**********************************************************************
 * Copyright (c) 2005,2010IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.ptp.pldt.common.editorHelp;

import java.net.URL;

import org.eclipse.cdt.ui.ICHelpBook;
import org.eclipse.cdt.ui.ICHelpResourceDescriptor;
import org.eclipse.cdt.ui.IFunctionSummary;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.help.IHelpResource;
import org.osgi.framework.Bundle;

public class CHelpResourceDescriptorImpl implements ICHelpResourceDescriptor
{
    ICHelpBook  book;
    String        name;
    String        label;
    String        href;
    IHelpResource [] resources;

    /**
     * This is where the location of the html help file (to be displayed for an API) is located
     * @param helpBook
     * @param functionSummary
     * @param pluginId
     */
    public CHelpResourceDescriptorImpl(ICHelpBook helpBook, IFunctionSummary functionSummary, String pluginId)
    {
        book = helpBook;
        name = functionSummary.getName();
        //href = "/"+pluginId + "/html/" + name + ".html";
        StringBuffer buf=new StringBuffer();
        
		// Find where the html dir is located - could vary e.g. if a fragment
		// provides "extra" info
        String htmlLocn=findHTMLdir(pluginId);
		buf.append("/").append(pluginId).append("/").append(htmlLocn).append("/").append(name).append(".html"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		href = buf.toString();
        //System.out.println("looking for help file: "+href);
        // href="file://c:/temp/foo.html";      
        label = functionSummary.getPrototype().getPrototypeString(false);
        resources = new IHelpResource[1];
        resources[0] = new IHelpResource() {
            public String getHref()
            {
                return href;
            }

            public String getLabel()
            {
                return label;
            }
        };
    }

    public ICHelpBook getCHelpBook()
    {
        return book;
    }

    public IHelpResource[] getHelpResources()
    {
        return resources;
    }
    public String toString() {
    	return name+" -> "+href; //$NON-NLS-1$
    }

	/**
	 * Workaraound to be able to recognize alternate html dir e.g. in a fragment
	 * "add-on" plugin, to replace the html help files. 5.0 version will use an
	 * extension point.
	 * 
	 * @param pluginId
	 * @return
	 */
	private String findHTMLdir(String pluginId) {
		String HTML_ALT_DIR = "html2";
		// System.out.println("Looking or HTML dir for "+pluginId);
    	Bundle bundle = Platform.getBundle(pluginId);
		URL url = FileLocator.find(bundle, new Path(HTML_ALT_DIR), null);
		if (url != null) {
			return HTML_ALT_DIR;
		} else {
			return "html";
		}

    }
}
