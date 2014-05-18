package impex.editors.completion;

import impex.editors.ImpexColorConstants;
import impex.editors.ImpexDataDeffinition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;

import synchroniser.Activator;

import com.eclipsesource.json.JsonValue;

public class ImpexCompletionProposalComputer implements IContentAssistProcessor {

	String[] fgProposals = new String[] { "A", "B", "C" };
	private static final ICompletionProposal[] NO_PROPOSALS = new ICompletionProposal[0];
	
	private ImpexDataDeffinition impexDataDeffinition;

	public ImpexCompletionProposalComputer(
			ImpexDataDeffinition impexDataDeffinition) {
		this.impexDataDeffinition = impexDataDeffinition;
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int documentOffset) {

		String prefix = null;
		try {
			prefix = getPrefix(viewer, documentOffset);

			if (prefix == null || prefix.length() == 0)
				return NO_PROPOSALS;

		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IContextInformation info = new ContextInformation(prefix, prefix);
		return createProposals(prefix, documentOffset, info);
	}

	private CompletionProposal createProposal(int documentOffset,
			String prefix, String keyword, IContextInformation info, Image image) {
		return new CompletionProposal(keyword, documentOffset
				- prefix.length(), prefix.length(), keyword.length(), image,
				keyword, info, keyword);
	}

	private String getPrefix(ITextViewer viewer, int offset)
			throws BadLocationException {
		IDocument doc = viewer.getDocument();
		if (doc == null || offset > doc.getLength())
			return null;

		int length = 0;
		while (--offset >= 0
				&& Character.isJavaIdentifierPart(doc.getChar(offset)))
			length++;

		return doc.get(offset + 1, length);
	}

	private CompletionProposal[]  createProposals(String searchPrefix, int documentOffset, IContextInformation info) {
		List<String> result = new ArrayList<String>();
		
		List<CompletionProposal> proposals=new ArrayList<CompletionProposal>();
		
		for (String keywords : ImpexColorConstants.IMPEX_KEYWORDS.keySet()) {
			if (keywords.startsWith(searchPrefix) && !result.contains(keywords)) {
				result.add(keywords);
				Image image=Activator.getDefault().getImageRegistry().get(ImpexColorConstants.KEYWORD_IMAGE_ID);
				proposals.add(createProposal(documentOffset, searchPrefix, keywords, info, image));
			}
		}
		for (String headerTyp : impexDataDeffinition.getImpexDataDef().keySet()) {
			if (headerTyp.startsWith(searchPrefix) && !result.contains(headerTyp)) {
				Image image=Activator.getDefault().getImageRegistry().get(ImpexColorConstants.TYPE_IMAGE_ID);
				proposals.add(createProposal(documentOffset, searchPrefix, headerTyp, info, image));
				result.add(headerTyp);
			}
			for (JsonValue string : impexDataDeffinition.getImpexDataDef().get(
					headerTyp)) {
				if (string.asString().startsWith(searchPrefix) &&  !result.contains(string.asString())) {
					Image image=Activator.getDefault().getImageRegistry().get(ImpexColorConstants.TYPE_IMAGE_ID);
					proposals.add(createProposal(documentOffset, searchPrefix, string.asString(), info, image));
					result.add(string.asString());
				}
			}
		}
		return proposals.toArray(new CompletionProposal[proposals.size()]);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer arg0,
			int arg1) {
		System.out.println("computeContextInformation :");
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

}
