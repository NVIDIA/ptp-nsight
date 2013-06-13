/*******************************************************************************
 * Copyright (c) 2007, 2013 NVIDIA Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Eugene Ostroukhov (NVIDIA) - Initial API and implementation
 *******************************************************************************/
package org.eclipse.ptp.internal.remote.rse.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.ptp.remote.core.AbstractRemoteProcess;

import com.jcraft.jsch.ChannelExec;

/**
 * Special remote process sitting on top of JSch SSH channel obtained through RSE.
 */
public final class RSESSHProcess extends AbstractRemoteProcess {
    private final ChannelExec channel;
    private final InputStream stderr;
    private final InputStream stdout;
    private final OutputStream stdin;

    public RSESSHProcess(final ChannelExec channel) throws IOException {
        this.channel = channel;
        stdout = channel.getInputStream();
        stderr = channel.getErrStream();
        stdin = channel.getOutputStream();
    }

    @Override
    public void destroy() {
        channel.disconnect();
    }

    @Override
    public int exitValue() {
        return channel.getExitStatus();
    }

    @Override
    public InputStream getErrorStream() {
        return stderr;
    }

    @Override
    public InputStream getInputStream() {
        return stdout;
    }

    @Override
    public OutputStream getOutputStream() {
        return stdin;
    }

    @Override
    public int waitFor() throws InterruptedException {
        while (isCompleted()) {
            Thread.sleep(20);
        }
        return exitValue();
    }

    @Override
    public boolean isCompleted() {
        try {
            return stdout.available() == 0 && stderr.available() == 0 && channel.isConnected();
        } catch (final IOException e) {
            RSEAdapterCorePlugin.log(e);
            return true;
        }
    }
}
