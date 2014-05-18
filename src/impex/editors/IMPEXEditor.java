package impex.editors;

import org.eclipse.ui.editors.text.TextEditor;

public class IMPEXEditor extends TextEditor {

	private ColorManager colorManager;
	private ImpexDataDeffinition impexDataDeffinition;

	public IMPEXEditor() {
		super();
		colorManager = new ColorManager();
		impexDataDeffinition=new ImpexDataDeffinition();
		setSourceViewerConfiguration(new ImpexConfiguration(colorManager,impexDataDeffinition));
		setDocumentProvider(new ImpexDocumentProvider());
	}
	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

}
