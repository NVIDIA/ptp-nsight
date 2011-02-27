package org.eclipse.ptp.rm.jaxb.core.runnable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ptp.remote.core.IRemoteFileManager;
import org.eclipse.ptp.rm.jaxb.core.IJAXBNonNLSConstants;
import org.eclipse.ptp.rm.jaxb.core.JAXBCorePlugin;
import org.eclipse.ptp.rm.jaxb.core.data.ManagedFile;
import org.eclipse.ptp.rm.jaxb.core.data.ManagedFiles;
import org.eclipse.ptp.rm.jaxb.core.messages.Messages;
import org.eclipse.ptp.rm.jaxb.core.rm.JAXBResourceManager;
import org.eclipse.ptp.rm.jaxb.core.variables.RMVariableMap;

public class ManagedFilesJob extends Job implements IJAXBNonNLSConstants {

	private final JAXBResourceManager rm;
	private final String sourceDir;
	private final String stagingDir;
	private final List<ManagedFile> files;

	public ManagedFilesJob(ManagedFiles files, JAXBResourceManager rm) throws CoreException {
		super(Messages.ManagedFilesJob);
		this.rm = rm;
		sourceDir = RMVariableMap.getInstance().getString(files.getFileSourceLocation());
		stagingDir = RMVariableMap.getInstance().getString(files.getFileStagingLocation());
		this.files = files.getManagedFile();
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		SubMonitor progress = SubMonitor.convert(monitor, files.size() * 10);
		/*
		 * for now we handle the files serially
		 */
		for (ManagedFile file : files) {
			try {
				File localFile = maybeWriteFile(file);
				progress.worked(5);
				/**
				 * no support for Windows as target ...
				 */
				String target = stagingDir + REMOTE_PATH_SEP + RMVariableMap.getInstance().getString(file.getName());
				copyFileToRemoteHost(localFile.getAbsolutePath(), target, progress);
				if (file.isDeleteAfterUse()) {
					localFile.delete();
				}
			} catch (Throwable t) {
				t.printStackTrace();
				progress.done();
				return new Status(Status.ERROR, JAXBCorePlugin.getUniqueIdentifier(), Messages.ManagedFilesJobError, t);
			}
			progress.worked(5);
		}
		progress.done();
		return Status.OK_STATUS;
	}

	/*
	 * Copy local data from a path (can be a file or directory) from the local
	 * host to the remote host.
	 * 
	 * @param localPath
	 * 
	 * @param remotePath
	 * 
	 * @param configuration
	 * 
	 * @throws CoreException
	 */
	private void copyFileToRemoteHost(String localPath, String remotePath, IProgressMonitor monitor) throws CoreException {
		SubMonitor progress = SubMonitor.convert(monitor, 15);
		try {
			IRemoteFileManager localFileManager = rm.getLocalFileManager();
			IRemoteFileManager remoteFileManager = rm.getRemoteFileManager();
			progress.newChild(5);
			if (progress.isCanceled()) {
				throw new CoreException(new Status(IStatus.ERROR, JAXBCorePlugin.getUniqueIdentifier(),
						Messages.Copy_Operation_cancelled_by_user, null));
			}
			if (remoteFileManager == null) {
				throw new CoreException(new Status(IStatus.ERROR, JAXBCorePlugin.getUniqueIdentifier(),
						Messages.Copy_Operation_Null_FileManager));
			}

			IFileStore lres = localFileManager.getResource(localPath);
			if (!lres.fetchInfo(EFS.NONE, progress.newChild(5)).exists()) {
				// Local file not found!
				throw new CoreException(new Status(IStatus.ERROR, JAXBCorePlugin.getUniqueIdentifier(),
						Messages.Copy_Operation_Local_resource_does_not_exist));
			}
			IFileStore rres = remoteFileManager.getResource(remotePath);

			// Copy file
			lres.copy(rres, EFS.OVERWRITE, progress.newChild(5));
		} finally {
			progress.done();
		}
	}

	private File maybeWriteFile(ManagedFile file) throws IOException, CoreException {
		String name = RMVariableMap.getInstance().getString(file.getName());
		File localFile = new File(sourceDir, name);
		String contents = file.getContents();
		FileWriter fw = null;
		try {
			if (contents == null) {
				if (!localFile.exists() || !localFile.isFile()) {
					throw new FileNotFoundException(localFile.getAbsolutePath());
				}
			} else {
				if (file.isUniqueIdPrefix()) {
					localFile = new File(sourceDir, UUID.randomUUID() + name);
				}
				contents = RMVariableMap.getInstance().getString(contents);
				fw = new FileWriter(localFile, false);
				fw.write(contents);
				fw.flush();
			}
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException t) {
				t.printStackTrace();
			}
		}
		return localFile;
	}
}