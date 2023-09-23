import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

/**
 * Compilador
 */
public class Compilador {

    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        atribuicaoLexer lexer = new atribuicaoLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        atribuicaoParser parser = new atribuicaoParser(tokens);
        ParseTree tree = parser.init();
        ParseTreeWalker walker = new ParseTreeWalker();

        String linguagem = args[0];
        if(linguagem.equals("java")){
            walker.walk(new Tradutor(), tree);
        }
        else if(linguagem.equals("c")){
            walker.walk(new TradutorC(), tree);
        }
    }
}