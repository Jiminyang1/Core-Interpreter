// <factor>::=id|id[<expr>]|const|(<expr>)|in()
// For factor: “id[<expr>]”, the id here needs to have been declared as object, not integer.
public class Factor {
    String id;
    Expr expr;
    int constant;
    boolean isId = false;
    boolean isIdExpr = false;
    boolean isConstant = false;
    boolean isExpr = false;
    boolean isIn = false;

    public void parse(SymbolTable symbolTable) {
        if (Parser.currentToken() == Core.ID) { // id or id[<expr>]
            id = Parser.scanner.getId();
            Parser.nextToken();
            if (Parser.currentToken() == Core.LBRACE) { // id[<expr>]
                isIdExpr = true;
                // semantic check: id must be declared and the type must be object
                Core type = symbolTable.getType(id);
                if (type != Core.OBJECT) {
                    System.out.println("ERROR: " + id + " is not an object");
                    System.exit(0);
                }
                Parser.nextToken();
                expr = new Expr();
                expr.parse(symbolTable);
                Parser.expectedToken(Core.RBRACE);
                Parser.nextToken();
            } else { // id
                isId = true;
                // semantic check: id must be declared, if not, throw an error and exit
                symbolTable.getType(id);
            }
        } else if (Parser.currentToken() == Core.CONST) { // const
            isConstant = true;
            constant = Parser.scanner.getConst();
            Parser.nextToken();
        } else if (Parser.currentToken() == Core.LPAREN) { // (<expr>)
            isExpr = true;
            Parser.nextToken();
            expr = new Expr();
            expr.parse(symbolTable);
            Parser.expectedToken(Core.RPAREN);
            Parser.nextToken();
        } else if (Parser.currentToken() == Core.IN) { // in()
            isIn = true;
            Parser.nextToken();
            Parser.expectedToken(Core.LPAREN);
            Parser.nextToken();
            Parser.expectedToken(Core.RPAREN);
            Parser.nextToken();
        } else { // Error
            System.out.println(
                    "ERROR: Expected ID, CONST, LPAREN, or IN but got " + Parser.currentToken() + " in Factor");
            System.exit(0);
        }
    }

    public int execute(SymbolTable symbolTable) {
        int res = 0;
        if (isId) { // id
            // When an object variable is used in a factor without “[<expr>]”, we treat it
            // as a shorthand for accessing index 0.
            // check the type of id, if it is an object, return the value of index 0
            if (symbolTable.getType(id) == Core.OBJECT) {
                res = symbolTable.getArrayIndexValue(id, 0);
            } else {
                res = symbolTable.getInteger(id);
            }
        } else if (isIdExpr) { // id[<expr>]
            int index = expr.execute(symbolTable);
            res = symbolTable.getArrayIndexValue(id, index);
        } else if (isConstant) {
            res = constant;
        } else if (isExpr) { // (<expr>)
            res = expr.execute(symbolTable);
        } else if (isIn) {
            // initial a scanner and wait for input
            if (Parser.scannerData.currentToken() == Core.CONST) {
                res = Parser.scannerData.getConst();
                Parser.scannerData.nextToken();
            } else {
                // if reach the end of the file but still require input, throw an error and exit
                if (Parser.scannerData.currentToken() == Core.EOS) {
                    System.out.println("ERROR: No more available input for in() function.");
                    System.exit(0);
                }
                System.out.println("ERROR: Invalid input, Expected CONST but got " + Parser.scannerData.currentToken());
                System.exit(0);
            }
        }
        return res;
    }

    public void print() {
        if (isId) {
            System.out.print(id);
        } else if (isIdExpr) {
            System.out.print(id + "[");
            expr.print();
            System.out.print("]");
        } else if (isConstant) {
            System.out.print(constant);
        } else if (isExpr) {
            System.out.print("(");
            expr.print();
            System.out.print(")");
        } else if (isIn) {
            System.out.print("in()");
        }
    }
}
