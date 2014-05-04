/*
 * [SQLI] Plugin eclipse to synchronise local env with remote srv
 * Copyright (c) 2014 SQLI
 * All rights reserved.
 * Author: Youssef El Jaoujat
 * 
 */
package synchroniser.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import synchroniser.Activator;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class GeneralPreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public GeneralPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("A demonstration of a preference page implementation");
	}
	
	/**
	 * Creates the field editors. Field editors are abstractions of
	 * the common GUI blocks needed to manipulate various types
	 * of preferences. Each field editor knows how to save and
	 * restore itself.
	 */
	public void createFieldEditors() {
		
		addField(
				new StringFieldEditor(PreferenceConstants.P_HOST_NAME_STRING, "Host Name :", getFieldEditorParent()));		
		
		addField(
			new BooleanFieldEditor(
				PreferenceConstants.P_DUMP_CURRENT_DAY_BOOLEAN,
				"Restaurer la base du jours par defaut, oui ",
				getFieldEditorParent()));

		addField(
				new StringFieldEditor(PreferenceConstants.P_MEDIA_PREFIX_NAME_STRING, "Prefix du fichier fichier media zip", getFieldEditorParent()));
		
		addField(
				new StringFieldEditor(PreferenceConstants.P_DUMP_PREFIX_NAME_STRING, "Prefix du fichier dump de la base", getFieldEditorParent()));
		addField(
				new StringFieldEditor(PreferenceConstants.P_DUMP_DAY, "date du dump", getFieldEditorParent()));
	
		
	}

	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}