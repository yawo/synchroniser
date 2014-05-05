/*
 * [SQLI] Plugin eclipse to synchronise local env with remote srv
 * Copyright (c) 2014 SQLI
 * All rights reserved.
 * Author: Youssef El Jaoujat
 * 
 */
package synchroniser.hanlders.db;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.tools.ant.util.StringUtils;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.custom.Bullet;

import synchroniser.Activator;
import synchroniser.preferences.PreferenceConstants;

public class RestoreDbHandler {

	private String dbname;
	private String dbusername;
	private String dbpwd;
	private String localPath;
	private String dumpPrefixName ;
	private Boolean dumpCurrenDay;
	private String dumpDay;
	
	private String mysqlInstallationDir;

	public RestoreDbHandler() {

		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		dbname = store.getString(PreferenceConstants.P_DB_NAME_STRING);
		dbusername = store.getString(PreferenceConstants.P_DB_USERNAME_STRING);
		dbpwd = store.getString(PreferenceConstants.P_DB_PWD_STRING);
		localPath = store.getString(PreferenceConstants.P_LOCAL_PATH_STRING);
		dumpPrefixName = store
				.getString(PreferenceConstants.P_DUMP_PREFIX_NAME_STRING);
		dumpCurrenDay=store.getDefaultBoolean(PreferenceConstants.P_DUMP_CURRENT_DAY_BOOLEAN);
        dumpDay =dumpCurrenDay?new SimpleDateFormat("YYYYMMdd").format(new Date()): store.getString(PreferenceConstants.P_DUMP_DAY);
		mysqlInstallationDir=store.getString(PreferenceConstants.P_MYSQL_PATH);

	}

	public  String  restoredbfromsql() {
		try {
			/*
			 * NOTE: String s is the mysql file name including the .sql in its
			 * name
			 */
			/* NOTE: Getting path to the Jar file being executed */
			/*
			 * NOTE: YourImplementingClass-> replace with the class executing
			 * the code
			 */

			/* NOTE: Creating Path Constraints for restoring */

			/* NOTE: Used to create a cmd command */
			/*
			 * NOTE: Do not create a single large string, this will cause buffer
			 * locking, use string array
			 */

			String[] executeCmd = new String[]{mysqlInstallationDir, dbname, "-u" + dbusername, "-p" + dbpwd, "-e", " source "+buildMysqlSourPath()};

			/*
			 * NOTE: processComplete=0 if correctly executed, will contain other
			 * values if not
			 */
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();

			/*
			 * NOTE: processComplete=0 if correctly executed, will contain other
			 * values if not
			 */
			if (processComplete == 0) {
				return "SUCESS";
			} else {
				return "FAILURE";
			}

		} catch ( InterruptedException | IOException ex) {
			JOptionPane.showMessageDialog(null, "Error at Restoredbfromsql"
					+ ex.getMessage());
		}
		return null;

	}

	public String buildMysqlSourPath(){
		
		StringBuffer sb=new StringBuffer(localPath);
		sb.append(dumpPrefixName);
		sb.append('_');
		sb.append(dumpDay);
		sb.append(".sql");
		return sb.toString();
		
	}
}
