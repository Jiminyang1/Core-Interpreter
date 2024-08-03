//<stmt> ::= <assign> | <if> | <loop> | <out> | <decl> | <call>
public class Stmt {
    Assign assign;
    If ifStmt;
    Loop loopStmt;
    Out outStmt;
    Decl decl;
    Call call;

    boolean isAssign = false;
    boolean isIf = false;
    boolean isLoop = false;
    boolean isOut = false;
    boolean isDecl = false;
    boolean isCall = false;

    public void parse(SymbolTable symbolTable) {
        //System.out.println("current token in Stmt: " + Parser.currentToken());
        if (Parser.currentToken() == Core.ID) {
            isAssign = true;
            assign = new Assign();
            assign.parse(symbolTable);
        } else if (Parser.currentToken() == Core.IF) {
            isIf = true;
            ifStmt = new If();
            ifStmt.parse(symbolTable);
        } else if (Parser.currentToken() == Core.WHILE) {
            isLoop = true;
            loopStmt = new Loop();
            loopStmt.parse(symbolTable);
        } else if (Parser.currentToken() == Core.OUT) {
            isOut = true;
            outStmt = new Out();
            outStmt.parse(symbolTable);
        } else if (Parser.currentToken() == Core.INTEGER || Parser.currentToken() == Core.OBJECT) {
            isDecl = true;
            decl = new Decl();
            decl.parse(symbolTable);
        } else if(Parser.currentToken() == Core.BEGIN){
            //System.out.println("in CALL" + Parser.currentToken());
            isCall = true;
            call = new Call();
            call.parse(symbolTable);
        } else {
            System.out
                    .println("ERROR: Expected ID, IF, WHILE, OUT, INTEGER, or OBJECT but got " + Parser.currentToken());
            System.exit(0);
        }
    }

    public void execute(SymbolTable symbolTable) {
        if (isAssign) {
            assign.execute(symbolTable);
        } else if (isIf) {
            ifStmt.execute(symbolTable);
        } else if (isLoop) {
            loopStmt.execute(symbolTable);
        } else if (isOut) {
            outStmt.execute(symbolTable);
        } else if (isDecl) {
            decl.execute(symbolTable);
        } else if (isCall) {
            call.execute(symbolTable);
        }
    }

    public void print(int indentLevel) {
        if (isAssign) {
            assign.print(indentLevel);
        } else if (isIf) {
            ifStmt.print(indentLevel);
        } else if (isLoop) {
            loopStmt.print(indentLevel);
        } else if (isOut) {
            outStmt.print(indentLevel);
        } else if (isDecl) {
            decl.print(indentLevel);
        } else if (isCall) {
            call.print(indentLevel);
        }
    }
}