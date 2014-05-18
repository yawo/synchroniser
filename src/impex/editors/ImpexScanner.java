package impex.editors;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import com.eclipsesource.json.JsonValue;

public class ImpexScanner extends RuleBasedScanner {

	private String state;
	public ImpexScanner(ColorManager manager,ImpexDataDeffinition impexDataDeffinition) {
		IToken procInstr = new Token(new TextAttribute(
				manager.getColor(ImpexColorConstants.PROC_INSTR)));

		IRule[] rules = new IRule[7];
		// Add rule for processing instructions
		rules[0] = new SingleLineRule("<?", "?>", procInstr);
		// Add generic whitespace rule.
		rules[1] = new WhitespaceRule(new ImpexWhitespaceDetector());

		// Impew Rules
		
		//IMPEX CMD RULES
		
		WordRule impx_cmd_rule=new WordRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		});
		
		
		WordRule impex_modifierRule=new WordRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		});
		
		WordRule impexAtomicRule=new WordRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		});
		
		ImpexHeaderRule rule = new ImpexHeaderRule(new IWordDetector() {
			public boolean isWordStart(char c) {
				return Character.isJavaIdentifierStart(c);
			}

			public boolean isWordPart(char c) {
				return Character.isJavaIdentifierPart(c);
			}
		});
		
		Token cmd_keyword = new Token(new TextAttribute(
				manager.getColor(ImpexColorConstants.IMPEX_COMMAND), null,
				SWT.BOLD));
		Token impex_modifier = new Token(new TextAttribute(
				manager.getColor(ImpexColorConstants.IMPEX_MODIFIER), null,
				SWT.BOLD));
		Token impex_atomic = new Token(new TextAttribute(
				manager.getColor(ImpexColorConstants.IMPEX_ATOMIC), null,
				SWT.BOLD));
		
		Token impex_headerType = new Token(new TextAttribute(
				manager.getColor(ImpexColorConstants.IMPEX_HEADER_TYPE), null,
				SWT.BOLD));
		
		Token impex_headerTypeAttribute = new Token(new TextAttribute(
				manager.getColor(ImpexColorConstants.IMPEX_HEADER_TYPE)));

		for (String impex_keyword : ImpexColorConstants.IMPEX_KEYWORDS.keySet()) {
			if (ImpexColorConstants.IMPEX_KEYWORDS.get(impex_keyword) == "impex_cmd") {
				impx_cmd_rule.addWord(impex_keyword, cmd_keyword);
				
			} else if (ImpexColorConstants.IMPEX_KEYWORDS.get(impex_keyword) == "impex_modifier") {
				impex_modifierRule.addWord(impex_keyword, impex_modifier);
				
			} else if (ImpexColorConstants.IMPEX_KEYWORDS.get(impex_keyword) == "impex_atomic") {
				impexAtomicRule.addWord(impex_keyword, impex_atomic);
			}
		}
		
		for (String headerTyp : impexDataDeffinition.getImpexDataDef().keySet()) {
			rule.addWord(headerTyp, impex_headerType);
			
			for (JsonValue string : impexDataDeffinition.getImpexDataDef().get(headerTyp)) {
				rule.addWord(string.asString(), impex_headerTypeAttribute);
			}
		}
		
		//rules[2] = impexHeaderRule;
		// Variables
		rules[2] = new ImpexVariableRule(new Token(new TextAttribute(
						manager.getColor(ImpexColorConstants.IMPEX_VARIABLE), null,
						SWT.BOLD)));
		
		rules[3] = impx_cmd_rule;
		rules[5] = impex_modifierRule;
		rules[4] = impexAtomicRule;
		rules[6] = rule;

		setRules(rules);
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
}
