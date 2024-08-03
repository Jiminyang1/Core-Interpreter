// <expr> ::= <term> | <term> + <expr> | <term> – <expr>

public class Expr {
    Term term;
    Expr expr;
    boolean isTerm = false;
    boolean isTermPlusExpr = false;
    boolean isTermMinusExpr = false;

    public void parse(SymbolTable symbolTable) {
        term = new Term();
        term.parse(symbolTable);
        if (Parser.currentToken() == Core.ADD) {
            isTermPlusExpr = true;
            Parser.nextToken();
            expr = new Expr();
            expr.parse(symbolTable);
        } else if (Parser.currentToken() == Core.SUBTRACT) {
            isTermMinusExpr = true;
            Parser.nextToken();
            expr = new Expr();
            expr.parse(symbolTable);
        } else {
            isTerm = true;
        }
    }

    public int execute(SymbolTable symbolTable) {
        int res = 0;
        if (isTerm) {
            res = term.execute(symbolTable);
        } else if (isTermPlusExpr) {
            res = term.execute(symbolTable) + expr.execute(symbolTable);
        } else if (isTermMinusExpr) {
            res = term.execute(symbolTable) - expr.execute(symbolTable);
        } else {
            // throw error
            System.out.println("ERROR: Expected term, term + expr, or term - expr but got " + Parser.currentToken());
            System.exit(0);
        }
        return res;

    }

    public void print() {
        term.print();
        if (isTermPlusExpr) {
            System.out.print(" + ");
            expr.print();
        } else if (isTermMinusExpr) {
            System.out.print(" - ");
            expr.print();
        }
    }
}
