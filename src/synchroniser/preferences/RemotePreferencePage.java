package synchroniser.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
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

public class RemotePreferencePage
	extends FieldEditorPreferencePage
	implements IWorkbenchPreferencePage {

	public RemotePreferencePage() {
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
			new StringFieldEditor(PreferenceConstants.P_HOST_USER_NAME_STRING, "Usern Name :", getFieldEditorParent()));
		
		addField(
				new StringFieldEditor(PreferenceConstants.P_HOST_PWD_STRING, "Password", getFieldEditorParent()));
		
		addField(
				new StringFieldEditor(PreferenceConstants.P_REMOTE_PATH, "Backup Data Directory :", getFieldEditorParent()));
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}
	
}