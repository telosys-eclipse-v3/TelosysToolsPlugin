package org.telosys.tools.eclipse.plugin.generator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.telosys.tools.generator.task.ITaskMonitor;

public class TaskMonitor implements ITaskMonitor {

	private final IProgressMonitor progressMonitor ;
	
	public TaskMonitor(IProgressMonitor progressMonitor) {
		super();
		this.progressMonitor = progressMonitor;
	}

	@Override
	public void beginTask(String arg0, int arg1) {
		progressMonitor.beginTask(arg0, arg1);		
	}

	@Override
	public void done() {
		progressMonitor.done();		
	}

	@Override
	public boolean isCanceled() {
		return progressMonitor.isCanceled();
	}

	@Override
	public void setCanceled(boolean arg0) {
		progressMonitor.setCanceled(arg0);
	}

	@Override
	public void setTaskName(String arg0) {
		progressMonitor.setTaskName(arg0);
	}

	@Override
	public void subTask(String arg0) {
		progressMonitor.subTask(arg0);
	}

	@Override
	public void worked(int arg0) {
		progressMonitor.worked(arg0);		
	}

}
