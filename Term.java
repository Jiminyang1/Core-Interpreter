// <term> ::= <factor> | <factor> * <term> | <factor> / <term>
public class Term {
    Term term;
    Factor factor;
    boolean isFactor = false;
    boolean isFactorTimesTerm = false;
    boolean isFactorDividedByTerm = false;

    public void parse(SymbolTable symbolTable) {
        factor = new Factor();
        factor.parse(symbolTable);
        if (Parser.currentToken() == Core.MULTIPLY) {
            isFactorTimesTerm = true;
            Parser.nextToken();
            term = new Term();
            term.parse(symbolTable);
        } else if (Parser.currentToken() == Core.DIVIDE) {
            isFactorDividedByTerm = true;
            Parser.nextToken();
            term = new Term();
            term.parse(symbolTable);
        } else {
            isFactor = true;
        }
    }

    public int execute(SymbolTable symbolTable) {
        int res;
        if (isFactor) {
            res = factor.execute(symbolTable);
        } else if (isFactorTimesTerm) {
            res = factor.execute(symbolTable) * term.execute(symbolTable);
        } else if (isFactorDividedByTerm) {
            int divider = term.execute(symbolTable);
            if (divider == 0) {
                System.out.println("ERROR: Division by zero");
                System.exit(0);
            }
            res = factor.execute(symbolTable) / divider;
        } else {
            // throw error
            System.out.println(
                    "ERROR: Expected factor, factor * term, or factor / term but got " + Parser.currentToken());
            System.exit(0);
            res = 0;
        }
        return res;
    }

    public void print() {
        factor.print();
        if (isFactorTimesTerm) {
            System.out.print(" * ");
            term.print();
        } else if (isFactorDividedByTerm) {
            System.out.print(" / ");
            term.print();
        }
    }
}
