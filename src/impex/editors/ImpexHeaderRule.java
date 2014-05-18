package impex.editors;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

public class ImpexHeaderRule extends WordRule {

	private StringBuffer fheaderBuffer;
	public ImpexHeaderRule(IWordDetector detector) {
		super(detector);
		this.fheaderBuffer = new StringBuffer();
	}

	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		if ((c != -1)
				&& (this.fDetector.isWordStart((char) c))
				&& (((this.fColumn == -1) || (this.fColumn == scanner
						.getColumn() - 1)))) {
			this.fheaderBuffer.setLength(0);
			do {
				this.fheaderBuffer.append((char) c);
				c = scanner.read();
			} while ((c != -1) && (this.fDetector.isWordPart((char) c)));
			scanner.unread();
			
			String buffer = this.fheaderBuffer.toString();
			IToken token = (IToken) this.fWords.get(buffer);
			if (token != null) {
				c=scanner.read();
				if('['== c || ';'==c || '('==c || c==' '){
					scanner.unread();
					return token;
				}
				
			}
			if (this.fDefaultToken.isUndefined()) {
				unreadBuffer(scanner);
			}
			return this.fDefaultToken;
		}

		scanner.unread();
		return Token.UNDEFINED;
	}
}
