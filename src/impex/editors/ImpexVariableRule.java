package impex.editors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class ImpexVariableRule implements IRule {

	IToken token;
    private final Pattern pattern=Pattern.compile("^\\$[a-zA-Z0-9_-]+");
    boolean spanNewLine = false;
    
    public ImpexVariableRule(IToken token ){
            this.token = token;     
    }
    
    public IToken evaluate(ICharacterScanner scanner) {

            String stream = "";
            int c = scanner.read();
            int count = 1;

            while( c != ICharacterScanner.EOF  ){
                                    
                    stream += (char) c;             
                    
                    Matcher m = pattern.matcher( stream );
                    if( m.matches()){
                    	c=scanner.read();
                    	if((']'== c || ')'==c || '='==c || '['==c || '('==c  || ';'==c ||c=='\n' ||c=='\r' )){
                    		scanner.unread();
                    		return token;
                    	}else {
                    		scanner.unread();
                    	}
                            
                    }
                    
                    if( !spanNewLine && ( '\n' == c || '\r' == c ) ){
                            break;
                    }
                    
                    count++;
                    c = scanner.read();
            }

            //put the scanner back to the original position if no match
            for( int i = 0; i < count; i++){
                    scanner.unread();
            }
            
            return Token.UNDEFINED;
            
    }

}
