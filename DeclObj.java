//<decl-obj> ::= object id ;
public class DeclObj {
    String id;

    public void parse(SymbolTable symbolTable) {
        if (Parser.currentToken() != Core.OBJECT) {
            System.out.println(
                    "ERROR: Expected OBJECT but got " + Parser.currentToken() + "in DeclObj");
            System.exit(0);
        }
        Parser.nextToken();
        if (Parser.currentToken() != Core.ID) {
            System.out.println("ERROR: Expected ID but got " + Parser.currentToken() + "in DeclObj");
            System.exit(0);
        }
        id = Parser.scanner.getId();
        // semantic check: id should not be declared
        symbolTable.declare(id, Core.OBJECT, false);

        Parser.nextToken();
        Parser.expectedToken(Core.SEMICOLON);
        Parser.nextToken();
    }

    public void print(int indentLevel) {
        Parser.printIdentation(indentLevel);
        System.out.println("object " + id + ";");
    }

    public void execute(SymbolTable symbolTable) {
        symbolTable.declare(id, Core.OBJECT, false);
        return;
    }
}
