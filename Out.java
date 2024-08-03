
public class Out {
    // <out> ::= out ( <expr> ) ;
    Expr expr;

    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.OUT);
        Parser.nextToken();
        Parser.expectedToken(Core.LPAREN);
        Parser.nextToken();
        expr = new Expr();
        expr.parse(symbolTable);
        Parser.expectedToken(Core.RPAREN);
        Parser.nextToken();
        Parser.expectedToken(Core.SEMICOLON);
        Parser.nextToken();
    }

    public void execute(SymbolTable symbolTable) {
        System.out.println(expr.execute(symbolTable));
    }

    public void print(int indentLevel) {
        Parser.printIdentation(indentLevel);
        System.out.print("out(");
        expr.print();
        System.out.println(");");
    }
}
