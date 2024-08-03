// <cmpr> ::= <expr> == <expr> | <expr> < <expr>
public class Cmpr {
    Cmpr cmpr;
    Expr expr1;
    Expr expr2;

    boolean isExprEqualsExpr = false;
    boolean isExprLessThanExpr = false;

    public void parse(SymbolTable symbolTable) {
        expr1 = new Expr();
        expr1.parse(symbolTable);
        if (Parser.currentToken() == Core.EQUAL) {
            isExprEqualsExpr = true;
            Parser.nextToken();
            expr2 = new Expr();
            expr2.parse(symbolTable);
        } else if (Parser.currentToken() == Core.LESS) {
            isExprLessThanExpr = true;
            Parser.nextToken();
            expr2 = new Expr();
            expr2.parse(symbolTable);
        } else {
            System.out.println("Error: Expected == or <" + " but got " + Parser.currentToken() + " in Cmpr");
            System.exit(0);
        }
    }

    public boolean execute(SymbolTable symbolTable) {
        boolean res = false;
        if (isExprEqualsExpr) {
            res = expr1.execute(symbolTable) == expr2.execute(symbolTable);
        } else if (isExprLessThanExpr) {
            res = expr1.execute(symbolTable) < expr2.execute(symbolTable);
        } else {
            System.out.println("Error: Expected == or <" + " but got " + Parser.currentToken() + " in Cmpr");
            System.exit(0);
        }
        return res;
    }

    public void print() {
        expr1.print();
        if (isExprEqualsExpr) {
            System.out.print(" == ");
            expr2.print();
        } else if (isExprLessThanExpr) {
            System.out.print(" < ");
            expr2.print();
        }
    }
}
