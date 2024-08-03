// <assign>::= id=<expr>; |id[<expr>]=<expr>; |id=new object(<expr>); |id:id;
// semantic check: For assign: “id = new object(<expr>);” and “id[<expr>] = <expr>;”, the id here needs to have been declared as object, not integer


public class Assign {
    // set boolan for each condtions
    boolean idExpr = false; // id=<expr>;
    boolean idExprExpr = false; // id[<expr>]=<expr>;
    boolean idNewObjectExpr = false; // id=new object(<expr>);
    boolean idId = false; // id:id;
    Expr expr;
    Expr expr2; // for id[<expr>]=<expr>;
    // get the id for printing
    String id1;
    // get id2 from id:id or id[<expr>]=<expr>;
    String id2;

    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.ID);
        id1 = Parser.scanner.getId();
        Parser.nextToken();
        // 1. // id=<expr>; or id=new object(<expr>);
        if (Parser.currentToken() == Core.ASSIGN) {
            Parser.nextToken();
            if (Parser.currentToken() == Core.NEW) { // id=new object(<expr>);
                idNewObjectExpr = true;
                // semantic check: id should not be declared and must be a object
                Core typeId1 = symbolTable.getType(id1);
                if (typeId1 != Core.OBJECT) {
                    System.out.println("ERROR: " + id1 + " is not an object in id=new object(<expr>)");
                    System.exit(0);
                }

                Parser.nextToken();
                Parser.expectedToken(Core.OBJECT);
                Parser.nextToken();
                Parser.expectedToken(Core.LPAREN);
                Parser.nextToken();
                expr = new Expr();
                expr.parse(symbolTable);
                Parser.expectedToken(Core.RPAREN);
                Parser.nextToken();
                Parser.expectedToken(Core.SEMICOLON);
                Parser.nextToken();
            } else { // id=<expr>;
                idExpr = true;
                // semantic check: id should be declared
                symbolTable.getType(id1);
                expr = new Expr();
                expr.parse(symbolTable);
                Parser.expectedToken(Core.SEMICOLON);
                Parser.nextToken();
            }
        } else if (Parser.currentToken() == Core.LBRACE) { // id[<expr>]=<expr>;
            idExprExpr = true;
            // semantic check: id must be declared and the type must be object
            Core type = symbolTable.getType(id1);
            if (type != Core.OBJECT) {
                System.out.println("ERROR: " + id1 + " is not an object in id[<expr>]");
                System.exit(0);
            }
            Parser.nextToken();
            expr = new Expr();
            expr.parse(symbolTable);
            Parser.expectedToken(Core.RBRACE);
            Parser.nextToken();
            Parser.expectedToken(Core.ASSIGN);
            Parser.nextToken();
            expr2 = new Expr();
            expr2.parse(symbolTable);
            Parser.expectedToken(Core.SEMICOLON);
            Parser.nextToken();
        } else if (Parser.currentToken() == Core.COLON) { // id:id;
            idId = true;
            // semantic check: both id should be declared, id1 and id2 should have the same
            // type and as object
            // check if id1 is declared and is an object
            if (symbolTable.getType(id1) != Core.OBJECT) {
                System.out.println("ERROR: " + id1 + " is not an object in id:id");
                System.exit(0);
            }

            Parser.nextToken();
            Parser.expectedToken(Core.ID);
            id2 = Parser.scanner.getId();
            // check if id2 is declared and is an object
            if (symbolTable.getType(id2) != Core.OBJECT) {
                System.out.println("ERROR: " + id2 + " is not an object in id:id");
                System.exit(0);
            }

            Parser.nextToken();
            Parser.expectedToken(Core.SEMICOLON);
            Parser.nextToken();
        } else {
            System.out.println("ERROR: Expected ASSIGN, LBRACKET, NEW, or COLON but got " + Parser.currentToken());
            System.exit(0);
        }

    }

    public void execute(SymbolTable symbolTable) {
        if (idExpr) {
            // id=<expr>;
            // When an object variable appears on the left hand side of an “id = <expr>”
            // type assignment without “[<expr>]”, we treat it as a shorthand for the index
            // 0.
            // we first check the type of the id
            Core type = symbolTable.getType(id1);
            if (type == Core.OBJECT) {
                symbolTable.assignArrayIndexValue(id1, 0, expr.execute(symbolTable));
            } else {
                symbolTable.assignInteger(id1, expr.execute(symbolTable));
            }
        } else if (idExprExpr) {
            // id[<expr>]=<expr>;
            int expr = this.expr.execute(symbolTable);
            int expr2 = this.expr2.execute(symbolTable);
            symbolTable.assignArrayIndexValue(id1, expr, expr2);
        } else if (idNewObjectExpr) {
            // id=new object(<expr>);
            int size = this.expr.execute(symbolTable);
            symbolTable.initializeArray(id1, size);

        } else if (idId) {
            // the left should be assigened to the right
            if(symbolTable.getVariable(id1).array != null && symbolTable.getVariable(id1).isAllocated){
                symbolTable.getVariable(id1).decrementReferenceCount();
                //System.out.println("reference count of " + id1 + " is " + symbolTable.getVariable(id1).referenceCount);
            }

            symbolTable.getVariable(id1).array = symbolTable.getVariable(id2).array;

            
            if(symbolTable.getVariable(id2).array != null && symbolTable.getVariable(id2).isAllocated){
                symbolTable.getVariable(id2).incrementReferenceCount();
            }
        }
    }

    public void print(int indentLevel) {
        if (idExpr) {
            Parser.printIdentation(indentLevel);
            System.out.print(id1 + " = ");
            expr.print();
            System.out.println(";");
        } else if (idExprExpr) {
            Parser.printIdentation(indentLevel);
            System.out.print(id1 + "[");
            expr.print();
            System.out.print("]= ");
            expr2.print();
            System.out.println(";");
        } else if (idNewObjectExpr) {
            Parser.printIdentation(indentLevel);
            System.out.print(id1 + " = new object(");
            expr.print();
            System.out.println(");");
        } else if (idId) {
            Parser.printIdentation(indentLevel);
            System.out.print(id1 + " : ");
            System.out.println(id2 + ";");
        }
    }
}
