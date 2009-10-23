package org.eclipse.ptp.internal.rdt.core.model;

import java.net.URI;

public interface IHasManagedLocation {

	public abstract void setManagedLocation(URI managedLocation);

	public abstract URI getManagedLocation();

}