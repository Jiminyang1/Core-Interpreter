public class Loop {
    // <loop> ::= while <cond> do <stmt-seq> end
    Cond cond;
    StmtSeq stmtSeq;

    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.WHILE);
        Parser.nextToken();
        cond = new Cond();
        cond.parse(symbolTable);
        Parser.expectedToken(Core.DO);
        Parser.nextToken();
        // enter loop scope
        symbolTable.enterScope();
        stmtSeq = new StmtSeq();
        stmtSeq.parse(symbolTable);
        Parser.expectedToken(Core.END);
        Parser.nextToken();
        // exit loop scope
        symbolTable.exitScope();
    }

    public void execute(SymbolTable symbolTable) {
        while (cond.execute(symbolTable)) {
            // enter loop scope
            symbolTable.enterScope();
            stmtSeq.execute(symbolTable);
            // exit loop scope
            symbolTable.exitScope();
        }
    }

    public void print(int indentLevel) {
        Parser.printIdentation(indentLevel);
        System.out.print("while ");
        cond.print();
        System.out.println(" do");
        stmtSeq.print(indentLevel + 1);
        Parser.printIdentation(indentLevel);
        System.out.println("end");
    }
}
