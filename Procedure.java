public class Procedure {
    String procName;
    DeclSeq declseq;
    StmtSeq stmtseq;
    int indentLevel = 0;
    //SymbolTable symbolTable;

    // <procedure> ::= procedure ID is <decl-seq> begin <stmt-seq> end
    // | procedure ID is begin <stmt-seq> end
    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.PROCEDURE);
        Parser.nextToken();
        Parser.expectedToken(Core.ID);
        procName = Parser.scanner.getId();
        Parser.nextToken();
        Parser.expectedToken(Core.IS);
        Parser.nextToken();
        // DeclSeq

        if (Parser.currentToken() != Core.BEGIN) {
            declseq = new DeclSeq();
            declseq.parse(symbolTable);
        }
        Parser.expectedToken(Core.BEGIN);
        Parser.nextToken();
        // StmtSeq
        if (Parser.currentToken() != Core.END) {
            stmtseq = new StmtSeq();
            // enter a new scope
            symbolTable.enterScope();
            stmtseq.parse(symbolTable);
            // exit the current scope
            symbolTable.exitScope();
        }
        Parser.expectedToken(Core.END);
        Parser.nextToken();
        Parser.expectedToken(Core.EOS);
        Parser.nextToken();
    }

    public void execute(SymbolTable symbolTable) {
        if (declseq != null) {
            declseq.execute(symbolTable);
        }
        // enter a new scope
        //symbolTable.enterScope();
        stmtseq.execute(symbolTable);
        // exit the current scope

        symbolTable.exitScope();

    }

    public void print() {
        Parser.printIdentation(indentLevel);
        System.out.println("procedure " + procName + " is");
        if (declseq != null) {
            declseq.print(indentLevel + 1);
        }
        Parser.printIdentation(indentLevel);
        System.out.println("begin");
        stmtseq.print(indentLevel + 1);
        Parser.printIdentation(indentLevel);
        System.out.println("end");
    }
}
