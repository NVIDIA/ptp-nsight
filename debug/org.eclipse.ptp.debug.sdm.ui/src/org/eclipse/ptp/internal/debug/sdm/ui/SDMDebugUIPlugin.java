package org.eclipse.ptp.internal.debug.sdm.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @since 4.1
 */
public class SDMDebugUIPlugin extends AbstractUIPlugin {
	public static final String PLUGIN_ID = "org.eclipse.ptp.debug.sdm.ui"; //$NON-NLS-1$

	// The shared instance.
	private static SDMDebugUIPlugin plugin;

	/**
	 * Returns the shared instance.
	 */
	public static SDMDebugUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Get a unique identifier for this plugin
	 * 
	 * @return
	 */
	public static String getUniqueIdentifier() {
		return PLUGIN_ID;
	}

	/**
	 * The constructor.
	 */
	public SDMDebugUIPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}
}
