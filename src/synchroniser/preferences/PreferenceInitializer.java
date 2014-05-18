/*
 * [SQLI] Plugin eclipse to synchronise local env with remote srv
 * Copyright (c) 2014 SQLI
 * All rights reserved.
 * Author: Youssef El Jaoujat
 * 
 */
package synchroniser.preferences;

import impex.editors.ImpexDataDeffinition;

import java.util.regex.Pattern;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.service.prefs.Preferences;

import synchroniser.Activator;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	private static String LINE_SEPARATOR = "@@@@";
	private static String FIELD_SEPARATOR = "####";
	private ImpexDataDeffinition impexDataDeffinition;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_DUMP_PREFIX_NAME_STRING,
				"dbDump");
		store.setDefault(PreferenceConstants.P_MEDIA_PREFIX_NAME_STRING,
				"media");
		store.setDefault(PreferenceConstants.P_DUMP_CURRENT_DAY_BOOLEAN, true);
		
		Activator.getDefault().getPreferenceStore()
		  .addPropertyChangeListener(new IPropertyChangeListener() {
		    @Override
		    public void propertyChange(PropertyChangeEvent event) {
		      if (event.getProperty() == "MySTRING1") {
		        String value = event.getNewValue().toString();
		        // do something with the new value
		      }
		    }
		  }); 
	}

	public static String createList(String[][] commands) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < commands.length; i++) {
			if (i > 0) {
				stringBuilder.append(LINE_SEPARATOR);
			}
			String[] command = commands[i];
			for (int j = 0; j < command.length; j++) {
				if (j > 0) {
					stringBuilder.append(FIELD_SEPARATOR);
				}
				stringBuilder.append(command[j]);
			}
		}
		return stringBuilder.toString();
	}
	
	public static String[][] parseString(String commandsString) {
        if (commandsString != null && commandsString.length() > 0) {
                String[] commands = commandsString.split(Pattern.quote(LINE_SEPARATOR));
                String[][] parsedCommands = new String[commands.length][];
                for (int i = 0; i < commands.length; i++) {
                        String command = commands[i];
                        if (command.indexOf(FIELD_SEPARATOR) == -1) {
                                parsedCommands[i] = new String[] {command, "*", command};
                        } else {
                                String[] fields = command.split(Pattern.quote(FIELD_SEPARATOR));
                                parsedCommands[i] = new String[fields.length];
                                for (int j = 0; j < fields.length; j++) {
                                        parsedCommands[i][j] = fields[j];
                                }
                        }
                }
                return parsedCommands;
        }
        return new String[0][0];
}
	
	
}
