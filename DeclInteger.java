
class DeclInteger {
    // <decl-integer> ::= integer id ;
    String id;

    public void parse(SymbolTable symbolTable) {
        if (Parser.currentToken() != Core.INTEGER) {
            System.out.println("ERROR: Expected INTEGER but got " + Parser.currentToken() + "in DeclInteger");
            System.exit(0);
        }
        Parser.nextToken();
        Parser.expectedToken(Core.ID);
        id = Parser.scanner.getId();

        // Declare the variable in the symbol table
        symbolTable.declare(id, Core.INTEGER, false);

        Parser.nextToken();
        Parser.expectedToken(Core.SEMICOLON);
        Parser.nextToken();
    }

    public void execute(SymbolTable symbolTable) {
        symbolTable.declare(id, Core.INTEGER, false);
        // default value for integer is 0
        symbolTable.assignInteger(id, 0);
        return;
    }

    public void print(int indentLevel) {
        Parser.printIdentation(indentLevel);
        System.out.println("integer " + id + ";");
    }
}
