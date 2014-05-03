package synchroniser.hanlders.scp;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.apache.log4j.Logger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.ssh.Scp;
import org.eclipse.jface.preference.IPreferenceStore;

import synchroniser.Activator;
import synchroniser.preferences.PreferenceConstants;

public class ScpHandler {

	private String remotePath;
	private String  localPath;
	private File tempDir ;
	private String hostName;
	private String hostUserName;
	private String hostPwd;
	private String sshHostUri ;
	private int port = Integer.parseInt(System.getProperty("scp.port", "22"));
	private String knownHosts = System.getProperty("scp.known.hosts");
	private Logger LOG=Logger.getLogger(ScpHandler.class);

	public ScpHandler() {
		
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		localPath = store.getString(PreferenceConstants.P_LOCAL_PATH_STRING);
		tempDir = new File(localPath);
		
		
		hostName=store.getString(PreferenceConstants.P_HOST_NAME_STRING);
		hostUserName=store.getString(PreferenceConstants.P_HOST_USER_NAME_STRING);
		hostPwd=store.getString(PreferenceConstants.P_HOST_PWD_STRING);
		remotePath=store.getString(PreferenceConstants.P_REMOTE_PATH);
		sshHostUri = buildHostUri();
		port = Integer.parseInt(System.getProperty("scp.port", "22"));
		knownHosts = System.getProperty("scp.known.hosts");
	}

	public boolean downloadFile(String fileName) {
		Scp scpTask = createTask();
		LOG.info("Remote File Location : "+sshHostUri+"/"+fileName);
		scpTask.setFile(sshHostUri+"/"+fileName);
		scpTask.setTodir(localPath+"/"+fileName);
		scpTask.setTrust(true);
		try {
			scpTask.execute();
		} catch (BuildException be) {
			System.out.println("Faild to Download file " + fileName);
			return false;

		}

		return true;

	}
	
	
	
	
	
	public String buildHostUri(){
		
		return  hostUserName+":"+hostPwd+"@"+hostName+":"+remotePath;
		
	}
	private Scp createTask() {
		Scp scp = new Scp();
		Project p = new Project();
		p.init();
		scp.setProject(p);
		if (knownHosts != null) {
			scp.setKnownhosts(knownHosts);
		} else {
			scp.setTrust(true);
		}
		scp.setPort(port);
		return scp;
	}
	

	
}
