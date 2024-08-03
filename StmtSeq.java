//<stmt-seq> ::= <stmt> | <stmt><stmt-seq>
//<stmt> ::= <assign> | <if> | <loop> | <out> | <decl> | <call>
public class StmtSeq {
    Stmt stmt;
    StmtSeq stmtSeq;

    public void parse(SymbolTable symbolTable) {
        // <stmt>
        stmt = new Stmt();
        stmt.parse(symbolTable);
        // <stmt-seq>
        if (Parser.currentToken() == Core.ID || Parser.currentToken() == Core.IF || Parser.currentToken() == Core.WHILE
                || Parser.currentToken() == Core.OUT || Parser.currentToken() == Core.INTEGER
                || Parser.currentToken() == Core.OBJECT || Parser.currentToken() == Core.BEGIN){
            stmtSeq = new StmtSeq();
            stmtSeq.parse(symbolTable);
        }
    }

    public void execute(SymbolTable symbolTable) {
        stmt.execute(symbolTable);
        if (stmtSeq != null) {
            stmtSeq.execute(symbolTable);
        }
    }

    public void print(int indentLevel) {
        stmt.print(indentLevel);
        if (stmtSeq != null) {
            stmtSeq.print(indentLevel);
        }
    }
}
