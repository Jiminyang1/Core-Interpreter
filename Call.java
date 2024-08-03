import java.util.List;


public class Call {
    //<call> ::= begin ID ( <parameters> ) ;
    String id;
    Parameters parameters;
    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.BEGIN);
        Parser.nextToken();
        Parser.expectedToken(Core.ID);
        id = Parser.scanner.getId();
        Parser.nextToken();
        Parser.expectedToken(Core.LPAREN);
        Parser.nextToken();
        parameters = new Parameters(false);
        //set as a argument
        parameters.parse(symbolTable);
        Parser.expectedToken(Core.RPAREN);
        Parser.nextToken();
        Parser.expectedToken(Core.SEMICOLON);
        Parser.nextToken();
    }

    public void print(int indentLevel) {
        Parser.printIdentation(indentLevel);
        System.out.print("begin " + id + " (");
        if (parameters != null) {
            parameters.print();
        }
        System.out.println(");");
    }

    public void execute(SymbolTable symbolTable) {
        // Retrieve the function definition from the symbol table
        SymbolTable.FunctionDefinition functionDef = symbolTable.getFunction(id);
    
        // Execute parameters to get the list of actual parameters (actualParameterNames should be a list of variable names, not values)
        List<String> actualParameterNames = parameters.execute(symbolTable);
    
        // Verify the correct number of parameters
        if (functionDef.parameters.size() != actualParameterNames.size()) {
            System.out.println("ERROR: Incorrect number of parameters provided for function " + id + ".");
            System.exit(0);
        }
    
       // Enter a new function scope
       symbolTable.enterFunction();
    
        // Map actual parameters to formal parameters by reference
        for (int i = 0; i < actualParameterNames.size(); i++) {
            String formalParam = functionDef.parameters.get(i);
            String actualParam = actualParameterNames.get(i);
            
            // Assuming actual parameters are always variables, not literals
            SymbolTable.Variable actualVariable = symbolTable.getVariable(actualParam);
            // Share the reference with the formal parameter
            if (actualVariable.type != Core.OBJECT) {
                System.out.println("ERROR: Actual parameter " + actualParam + " must be an object variable.");
                System.exit(0);
            }
            symbolTable.declare(formalParam, Core.OBJECT, true); // Declare the formal parameter in the new scope
            
            symbolTable.getVariable(formalParam).array = actualVariable.array; // Set the formal parameter to reference the same array as the actual parameter

            //increment the reference count of the actual parameter
            // if(actualVariable.array!=null && actualVariable.isAllocated){
            //     actualVariable.incrementReferenceCount();
            // }
        }

        // Execute the function body
        
        functionDef.body.execute(symbolTable);
        // Exit the function scope
        symbolTable.exitFunction();
    }
    
    
    
}
