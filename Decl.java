public class Decl {
    // <decl> ::= <decl-integer> | <decl-obj>

    DeclInteger declInteger;
    DeclObj declObj;
    boolean isInteger;
    boolean isObj;

    public void parse(SymbolTable symbolTable) {
        if (Parser.currentToken() == Core.INTEGER) {
            declInteger = new DeclInteger();
            declInteger.parse(symbolTable);
        } else if (Parser.currentToken() == Core.OBJECT) {
            declObj = new DeclObj();
            declObj.parse(symbolTable);
        } else {
            System.out
                    .println("ERROR: Expected INTEGER or OBJECT but got " + Parser.scanner.currentToken() + " in Decl");
            System.exit(0);
        }
    }

    public void execute(SymbolTable symbolTable) {
        if (declInteger != null) {
            declInteger.execute(symbolTable);
        } else if (declObj != null) {
            declObj.execute(symbolTable);
        }
        return;
    }

    public void print(int indentLevel) {
        if (declInteger != null) {
            declInteger.print(indentLevel);
        } else if (declObj != null) {
            declObj.print(indentLevel);
        }
    }

}
