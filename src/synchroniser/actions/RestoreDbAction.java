package synchroniser.actions;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import synchroniser.Activator;
import synchroniser.hanlders.db.RestoreDbHandler;
import synchroniser.hanlders.scp.ScpHandler;
import synchroniser.preferences.PreferenceConstants;

/**
 * Our sample action implements workbench action delegate.
 * The action proxy will be created by the workbench and
 * shown in the UI. When the user tries to use the action,
 * this delegate will be created and execution will be 
 * delegated to it.
 * @see IWorkbenchWindowActionDelegate
 */
public class RestoreDbAction implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;
	private ScpHandler scpHandler;
	private RestoreDbHandler restoreDbHandler;
	/**
	 * The constructor.
	 */
	public RestoreDbAction() {
	}

	/**
	 * The action has been activated. The argument of the
	 * method represents the 'real' action sitting
	 * in the workbench UI.
	 * @see IWorkbenchWindowActionDelegate#run
	 */
	public void run(IAction action) {
		
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		String prefixDumpName = store.getString(PreferenceConstants.P_DUMP_PREFIX_NAME_STRING);
		boolean dumpCurrenDay=store.getDefaultBoolean(PreferenceConstants.P_DUMP_CURRENT_DAY_BOOLEAN);
		final String dumpDay = store.getString(PreferenceConstants.P_DUMP_DAY);
		final String fileName=prefixDumpName+"_"+dumpDay+".sql";
		Job restoreJob=new Job("Synchronisation la base de donnés  du "+dumpDay) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
		        monitor.beginTask("", 2);
		        
		        monitor.subTask("Telecharger la base de donnés  du "+dumpDay);
		        scpHandler=new ScpHandler();
				boolean isDownloadOk=scpHandler.downloadFile(fileName);
				if(isDownloadOk){
					monitor.worked(1);
				}
				
		        monitor.subTask("Restaurer la base de donnés  du "+dumpDay);
		        restoreDbHandler=new RestoreDbHandler();
		        restoreDbHandler.restoredbfromsql();
		        
		        monitor.worked(1);
		        return Status.OK_STATUS;
			}
		};
		restoreJob.setUser(true);
		restoreJob.schedule();
	}

	
	/**
	 * Selection in the workbench has been changed. We 
	 * can change the state of the 'real' action here
	 * if we want, but this can only happen after 
	 * the delegate has been created.
	 * @see IWorkbenchWindowActionDelegate#selectionChanged
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/**
	 * We can use this method to dispose of any system
	 * resources we previously allocated.
	 * @see IWorkbenchWindowActionDelegate#dispose
	 */
	public void dispose() {
	}

	/**
	 * We will cache window object in order to
	 * be able to provide parent shell for the message dialog.
	 * @see IWorkbenchWindowActionDelegate#init
	 */
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

//	@Override
//	public void run(IProgressMonitor monitor)
//			throws InvocationTargetException, InterruptedException {
//	
//        monitor.beginTask("", 1);
//        monitor.subTask("restoreDb");
//        restoreDbHandler=new ScpHandler();
//        restoreDbHandler.restoredbfromsql();
//        monitor.worked(1);
//        progressBar.setSelection(progressBar.getSelection() + 1);
//       
//	}
	

}