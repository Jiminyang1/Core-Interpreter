import java.util.ArrayList;
import java.util.List;

public class Function {
    String funcName;
    Parameters parameters;
    StmtSeq stmtseq;
    // <function> ::= procedure ID ( <parameters> ) is <stmt-seq> end
    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.PROCEDURE);
        Parser.nextToken();
        Parser.expectedToken(Core.ID);
        //symbolTable.declare(funcName, Core.OBJECT);
        //put the function name into the gobal scope
        funcName = Parser.scanner.getId();
        Parser.nextToken();
        Parser.expectedToken(Core.LPAREN);
        Parser.nextToken();
        symbolTable.enterScope();   // enter a new scope
        parameters = new Parameters(true);
        //define the function in the symbol table
        
        symbolTable.declareFunction(funcName, null, stmtseq);
        parameters.parse(symbolTable);
        Parser.expectedToken(Core.RPAREN);
        Parser.nextToken();
        Parser.expectedToken(Core.IS);
        Parser.nextToken();
        // StmtSeq
        if (Parser.currentToken() != Core.END) {
            stmtseq = new StmtSeq();
            //set it is a parameter
            stmtseq.parse(symbolTable);
            //check current token
        }else{
            System.out.println("ERROR: Function body is empty.");
            System.exit(0);
        }
        symbolTable.exitScope();    // exit the current scope
        //System.out.println("current token after stmtseq: " + Parser.currentToken());
        Parser.expectedToken(Core.END);
        Parser.nextToken();
        //System.out.println("current token after end: " + Parser.currentToken());
    }

    public void print(int indentLevel){
        Parser.printIdentation(indentLevel);
        System.out.print("procedure " + funcName + " (");
        if (parameters != null) {
            parameters.print();
        }
        System.out.println(") is");
        if (stmtseq != null) {
            stmtseq.print(indentLevel + 1);
        }
        Parser.printIdentation(indentLevel);
        System.out.println("end");
    }

    public void execute(SymbolTable symbolTable){
        //get the function name and the parameters
        // put parameters into a list
        List<String> parametersList = new ArrayList<>();
        parametersList = parameters.execute(symbolTable);
        symbolTable.declareFunction(funcName, parametersList, stmtseq);
    }
}