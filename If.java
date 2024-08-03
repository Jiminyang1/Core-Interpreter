public class If {
    Cond cond;
    StmtSeq thenStmtSeq;
    StmtSeq elseStmtSeq;

    // <if> ::= if <cond> then <stmt-seq> end
    // |if <cond> then <stmt-seq> else <stmt-seq> end
    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.IF);
        Parser.nextToken();
        // enter if scope
        symbolTable.enterScope();
        cond = new Cond();
        cond.parse(symbolTable);
        Parser.expectedToken(Core.THEN);
        Parser.nextToken();
        thenStmtSeq = new StmtSeq();
        thenStmtSeq.parse(symbolTable);
        //System.out.println("current token after then: " + Parser.currentToken());
        // exit if scope
        symbolTable.exitScope();
        if (Parser.currentToken() == Core.ELSE) {
            Parser.nextToken();
            // enter else scope
            symbolTable.enterScope();
            elseStmtSeq = new StmtSeq();
            elseStmtSeq.parse(symbolTable);
            // exit else scope
            symbolTable.exitScope();
        }
        //System.out.println("current token after else: " + Parser.currentToken());
        Parser.expectedToken(Core.END);
        Parser.nextToken();
    }

    public void execute(SymbolTable symbolTable) {
        if (cond.execute(symbolTable)) {
            // enter then scope
            symbolTable.enterScope();
            thenStmtSeq.execute(symbolTable);
            // exit then scope
            symbolTable.exitScope();
        } else if (elseStmtSeq != null) {
            // enter else scope
            symbolTable.enterScope();
            elseStmtSeq.execute(symbolTable);
            // exit else scope
            symbolTable.exitScope();
        }
    }

    public void print(int indentLevel) {
        Parser.printIdentation(indentLevel);
        System.out.print("if ");
        cond.print();
        System.out.println(" then");
        thenStmtSeq.print(indentLevel + 1);
        if (elseStmtSeq != null) {
            Parser.printIdentation(indentLevel);
            System.out.println("else");
            elseStmtSeq.print(indentLevel + 1);
        }
        Parser.printIdentation(indentLevel);
        System.out.println("end");
    }
}
