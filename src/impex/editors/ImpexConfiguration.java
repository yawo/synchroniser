package impex.editors;

import impex.editors.completion.ImpexCompletionProposalComputer;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

public class ImpexConfiguration extends SourceViewerConfiguration {
	private XMLDoubleClickStrategy doubleClickStrategy;
	private ImpexScanner scanner;
	private ColorManager colorManager;
	private ImpexDataDeffinition impexDataDeffinition;

	public ImpexConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	public ImpexConfiguration(ColorManager colorManager,ImpexDataDeffinition impexDataDeffinition) {
		this.colorManager = colorManager;
		this.impexDataDeffinition=impexDataDeffinition;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE,
			ImpexPartitionScanner.IMPEX_COMMENT, };
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new XMLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		ContentAssistant assistant= new ContentAssistant();
		assistant.enableAutoActivation(true);
		assistant.setContentAssistProcessor(new ImpexCompletionProposalComputer(impexDataDeffinition), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setContentAssistProcessor(new ImpexCompletionProposalComputer(impexDataDeffinition), IDocument.DEFAULT_CONTENT_TYPE);

		return assistant;
	}
	protected ImpexScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new ImpexScanner(colorManager,impexDataDeffinition);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ImpexColorConstants.DEFAULT))));
		}
		return scanner;
	}
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ImpexColorConstants.XML_COMMENT)));
		reconciler.setDamager(ndr, ImpexPartitionScanner.IMPEX_COMMENT);
		reconciler.setRepairer(ndr, ImpexPartitionScanner.IMPEX_COMMENT);
		return reconciler;
	}

}