// <cond> ::= <cmpr> | not <cond> | [ <cond> ] | <cmpr> or <cond> | <cmpr> and <cond>
public class Cond {
    Cond cond;
    Cmpr cmpr;

    boolean isCmpr = false;
    boolean isNotCond = false;
    boolean isLeftBracketCondRightBracket = false;
    boolean isCmprOrCond = false;
    boolean isCmprAndCond = false;

    public void parse(SymbolTable symbolTable) {
        if (Parser.currentToken() == Core.NOT) { // not <cond>
            isNotCond = true;
            Parser.nextToken();
            cond = new Cond();
            cond.parse(symbolTable);
        } else if (Parser.currentToken() == Core.LBRACE) { // [ <cond> ]
            isLeftBracketCondRightBracket = true;
            Parser.nextToken();
            cond = new Cond();
            cond.parse(symbolTable);
            Parser.expectedToken(Core.RBRACE);
            Parser.nextToken();
        } else { // <cmpr> | <cmpr> or <cond> | <cmpr> and <cond>
            cmpr = new Cmpr();
            cmpr.parse(symbolTable);
            if (Parser.currentToken() == Core.OR) { // <cmpr> or <cond>
                isCmprOrCond = true;
                Parser.nextToken();
                cond = new Cond();
                cond.parse(symbolTable);
            } else if (Parser.currentToken() == Core.AND) { // <cmpr> and <cond>
                isCmprAndCond = true;
                Parser.nextToken();
                cond = new Cond();
                cond.parse(symbolTable);
            } else { // <cmpr>
                isCmpr = true;
            }
        }
    }

    public boolean execute(SymbolTable symbolTable) {
        boolean res = false;
        if (isCmpr) {
            res = cmpr.execute(symbolTable);
        } else if (isNotCond) {
            res = !cond.execute(symbolTable);
        } else if (isLeftBracketCondRightBracket) {
            res = cond.execute(symbolTable);
        } else if (isCmprOrCond) {
            res = cmpr.execute(symbolTable) || cond.execute(symbolTable);
        } else if (isCmprAndCond) {
            res = cmpr.execute(symbolTable) && cond.execute(symbolTable);
        } else {
            // throw error
            System.out.println(
                    "ERROR: Expected cmpr, not <cond>, [ <cond> ], <cmpr> or <cond>, or <cmpr> and <cond> but got "
                            + Parser.currentToken());
            System.exit(0);
        }
        return res;
    }

    public void print() {
        if (isNotCond) { // not <cond>
            System.out.print("not ");
            cond.print();
        } else if (isLeftBracketCondRightBracket) { // [ <cond> ]
            System.out.print("[");
            cond.print();
            System.out.print("]");
        } else { // <cmpr> | <cmpr> or <cond> | <cmpr> and <cond>
            cmpr.print(); // <cmpr>
            if (isCmprOrCond) { // <cmpr> or <cond>
                System.out.print(" or ");
                cond.print();
            } else if (isCmprAndCond) { // <cmpr> and <cond>
                System.out.print(" and ");
                cond.print();
            }
        }
    }
}
