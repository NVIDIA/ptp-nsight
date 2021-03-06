package org.eclipse.ptp.core.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ptp.remote.core.IRemoteConnection;
import org.eclipse.ptp.remote.core.IRemoteConnectionManager;
import org.eclipse.ptp.remote.core.IRemoteProcess;
import org.eclipse.ptp.remote.core.IRemoteProcessBuilder;
import org.eclipse.ptp.remote.core.IRemoteServices;
import org.eclipse.ptp.remote.core.RemoteServices;
import org.eclipse.ptp.remote.core.exception.RemoteConnectionException;

public class RemoteProcessTests extends TestCase {
	private static final String USERNAME = "greg"; //$NON-NLS-1$
	private static final String PASSWORD = ""; //$NON-NLS-1$
	private static final String HOST = "localhost"; //$NON-NLS-1$
	private static int NUM_THREADS = 5;

	private IRemoteServices fRemoteServices;
	private IRemoteConnection fRemoteConnection;

	public void testConcurrentProcess() {
		Thread[] threads = new Thread[NUM_THREADS];
		final Set<String> results = Collections.synchronizedSet(new HashSet<String>());

		for (int t = 0; t < NUM_THREADS; t++) {
			Thread thread = new Thread("test thread " + t) {
				@Override
				public void run() {
					IRemoteProcessBuilder builder = fRemoteServices.getProcessBuilder(fRemoteConnection, "perl", "-V:version"); //$NON-NLS-1$
					builder.redirectErrorStream(true);
					for (int i = 0; i < 10; i++) {
						try {
							IRemoteProcess proc = builder.start();
							BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
							String line;
							while ((line = stdout.readLine()) != null) {
								results.add(line);
							}
							try {
								proc.waitFor();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} catch (IOException e) {
							e.printStackTrace();
							fail(e.getLocalizedMessage());
						}
					}
				}

			};
			thread.start();
			threads[t] = thread;
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
			}
		}
		assertTrue(results.size() == 1);
	}

	public void testEnv() {
		IRemoteProcessBuilder builder = fRemoteServices.getProcessBuilder(fRemoteConnection, "printenv"); //$NON-NLS-1$
		builder.redirectErrorStream(true);
		String path = builder.environment().get("PATH");
		builder.environment().clear();
		builder.environment().put("PATH", path);
		try {
			IRemoteProcess proc = builder.start();
			BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line;
			String result = null;
			while ((line = stdout.readLine()) != null) {
				assertTrue(result == null);
				result = line;
			}
			assertEquals(result, "PATH=" + path);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		fRemoteServices = RemoteServices.getRemoteServices("org.eclipse.ptp.remote.RemoteTools"); //$NON-NLS-1$
		assertNotNull(fRemoteServices);

		IRemoteConnectionManager connMgr = fRemoteServices.getConnectionManager();
		assertNotNull(connMgr);

		try {
			fRemoteConnection = connMgr.newConnection("test_connection"); //$NON-NLS-1$
		} catch (RemoteConnectionException e) {
			fail(e.getLocalizedMessage());
		}
		assertNotNull(fRemoteConnection);
		fRemoteConnection.setAddress(HOST);
		fRemoteConnection.setUsername(USERNAME);
		fRemoteConnection.setPassword(PASSWORD);

		try {
			fRemoteConnection.open(new NullProgressMonitor());
		} catch (RemoteConnectionException e) {
			fail(e.getLocalizedMessage());
		}
		assertTrue(fRemoteConnection.isOpen());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		fRemoteConnection.close();
		IRemoteConnectionManager connMgr = fRemoteServices.getConnectionManager();
		assertNotNull(connMgr);
		connMgr.removeConnection(fRemoteConnection);
	}

}
