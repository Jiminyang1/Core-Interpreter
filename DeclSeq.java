
public class DeclSeq {
    // <decl-seq> ::= <decl> | <decl><decl-seq> | <function> | <function><decl-seq>
    private Decl decl;
    private DeclSeq declSeq;
    SymbolTable symbolTable;
    private Function function;
    boolean isFunction = false;

    public void parse(SymbolTable symbolTable) {
        // <decl> or <decl><decl-seq>
        if(Parser.currentToken() == Core.INTEGER || Parser.currentToken() == Core.OBJECT){
            decl = new Decl();
            decl.parse(symbolTable);
            if(Parser.currentToken() == Core.INTEGER || Parser.currentToken() == Core.OBJECT || Parser.currentToken() == Core.PROCEDURE){
                declSeq = new DeclSeq();
                declSeq.parse(symbolTable);
            }
        }
        //check if next is <function> or <function><decl-seq>
        if (Parser.currentToken() == Core.PROCEDURE) {
            isFunction = true;
            function = new Function();
            function.parse(symbolTable);
            if(Parser.currentToken() == Core.INTEGER || Parser.currentToken() == Core.OBJECT || Parser.currentToken() == Core.PROCEDURE){
                declSeq = new DeclSeq();
                declSeq.parse(symbolTable);
            }
        }
    }

    public void execute(SymbolTable symbolTable) {
        if(isFunction){
            function.execute(symbolTable);
            if (declSeq != null) {
                declSeq.execute(symbolTable);
            }
        } else {
            decl.execute(symbolTable);
            if (declSeq != null) {
                declSeq.execute(symbolTable);
            }
        }
    }

    public void print(int indentLevel) {
       if(isFunction){
           function.print(indentLevel);
              if (declSeq != null) {
                declSeq.print(indentLevel);
              }
       } else {
           decl.print(indentLevel);
           if (declSeq != null) {
               declSeq.print(indentLevel);
           }
       }
    }

}
