import java.util.HashMap;
import java.util.List;
import java.util.Stack;

class SymbolTable {
    // do some changes in the data structure
    public class Variable {
        public int referenceCount = 0;
        boolean isAllocated = false;
        Core type;
        Integer integer;
        int[] array;

        public Variable(Core type) {
            this.type = type;
            if (type == Core.INTEGER) {
                this.integer = 1;
                this.array = null;
            } else {
                this.integer = null;
                this.array = null;
            }
        }

        // define a method to initialize the array later after we declare it
        public void initializeArray(int size) {
            this.array = new int[size];
            isAllocated = true;
            // increment the reference count
            referenceCount = 1;
            globalReachableObjectsCount++;
            printGC();
        }

        // Methods for garbage collection
        public void incrementReferenceCount() {
            referenceCount++;
            //System.err.println("did we go here? incrementReferenceCount");
        }

        public void decrementReferenceCount() {
            //System.out.println("did we go here? decrementReferenceCount");
            referenceCount--;
            if(referenceCount <= 0) {
                this.array = null;
                isAllocated = false;
                globalReachableObjectsCount--;
                printGC();
            }
        }
    }

    // define a class to store the function definition
    public class FunctionDefinition {
        List<String> parameters;
        StmtSeq body; // the body of the function
        public FunctionDefinition(List<String> parameters, StmtSeq body) {
            this.parameters = parameters;
            this.body = body;
        }
    }


    

    // then instead of HashMap<String, Core> we will use HashMap<String, Variable>
    private Stack<Stack<HashMap<String, Variable>>> stackFrames = new Stack<>();

    // A map to store the function definition
    private HashMap<String, FunctionDefinition> functions = new HashMap<>();

    private int globalReachableObjectsCount = 0; // for the total number of reachable objects in all scopes


    public SymbolTable() {
        Stack<HashMap<String, Variable>> globalScopeStack = new Stack<>();
        globalScopeStack.push(new HashMap<>()); // global scope
        stackFrames.push(globalScopeStack);
    }

    // ---------  funtion part  ------------

    public void declareFunction(String id, List<String> parameters, StmtSeq body) {
        if (functions.containsKey(id)) {
            System.out.println("ERROR: Function " + id + " is already declared.");
            System.exit(0);
        }
        functions.put(id, new FunctionDefinition(parameters, body));
    }

    // a function to find the function when call it
    public FunctionDefinition getFunction(String id) {
        if (!functions.containsKey(id)) {
            System.out.println("ERROR: Function " + id + " is not declared.");
            System.exit(0);
        }
        return functions.get(id);
    }

   

    // ---------  END funtion part  ------------


    // ---------  print gc part  ------------
    public void printGC() {
        System.out.println("gc:" + globalReachableObjectsCount);
    }

    // ---------  END print gc part  ------------

    public void enterScope() {
        stackFrames.peek().push(new HashMap<>()); // enter a new scope
    }

    public void exitScope() {
        //HashMap<String, Variable> currentScope = stackFrames.peek().get(0); // exit the current scope
        HashMap<String, Variable> currentScope = stackFrames.peek().pop();
        //System.out.println("the size of the current scope: " + currentScope.size());
        for(Variable variable : currentScope.values()) {
            if(variable.isAllocated) {
                //System.out.println("we are in exitScope, decrementReferenceCount, variable: " + variable);
                globalReachableObjectsCount--;
                printGC();
            }
        }
        //stackFrames.peek().pop();
    }

    public void enterFunction() {
        Stack<HashMap<String, Variable>> functionScopeStack = new Stack<>();
        functionScopeStack.push(new HashMap<>()); // create a gobal scope for the function
        stackFrames.push(functionScopeStack);
    }
    
    public void exitFunction() {
        Stack<HashMap<String, Variable>> functionScopeStack = stackFrames.pop();
        for(HashMap<String, Variable> scope : functionScopeStack) {
            for(Variable variable : scope.values()) {
                if(variable.isAllocated) {
                    //System.out.println("we are in exitFunction, decrementReferenceCount, variable: " + variable);
                    globalReachableObjectsCount--;
                    printGC();
                }
            }
        }
    }

    public void declare(String id, Core type, boolean isParameter) {
        HashMap<String, Variable> currentScope = stackFrames.peek().peek();
       
            // For arguments or local variables, we need to check if it is already declared
            if (currentScope.containsKey(id)) {
                System.out.println("ERROR: Variable " + id + " is already declared in this scope.");
                System.exit(0);
            } else {
                // if it is not declared, then we declare it
                currentScope.put(id, new Variable(type));
            }
        
        //NOTE: For arguments, we did not check if it is already declared, we will check it when we call the function.
    }

    public Core getType(String id) {
        // for (int i = stackFrames.peek().size() - 1; i >= 0; i--) {
        //     HashMap<String, Variable> scope = stackFrames.peek().get(i);
        //     if (scope.containsKey(id)) {
        //         return scope.get(id).type;
        //     }
        // }
        // System.out.println("ERROR: Variable " + id + " is not declared at getType.");
        // System.exit(0);
        // return null;
        // we shoud find from the top to the bottom
        // we first check current scope, then check the gobal scope
        for (int i = stackFrames.peek().size() - 1; i >= 0; i--) {
            HashMap<String, Variable> scope = stackFrames.peek().get(i);
            if (scope.containsKey(id)) {
                return scope.get(id).type;
            }
        }
        // check the global scope
        HashMap<String, Variable> globalScope = stackFrames.get(0).get(0);
        if (globalScope.containsKey(id)) {
            return globalScope.get(id).type;
        }
        System.out.println("ERROR: Variable " + id + " is not declared in current scope or global scope.");
        System.exit(0);
        return null;
    }

    // defince a method to get the variable
    public Variable getVariable(String id) {
        // two loop to find the variable
        for(int i = stackFrames.size() - 1; i >= 0; i--){
            Stack<HashMap<String, Variable>> scopeStack = stackFrames.get(i);
            for (int j = scopeStack.size() - 1; j >= 0; j--) {
                HashMap<String, Variable> scope = scopeStack.get(j);
                if (scope.containsKey(id)) {
                    return scope.get(id);
                }
            }
        }
        System.out.println("ERROR: Variable " + id + " is not declared in current scope or global scope.");
        System.exit(0);
        return null;
    }

    // assign integer value to the variable
    public void assignInteger(String id, int value) {
        for (int i = stackFrames.size() - 1; i >= 0; i--) {
            Stack<HashMap<String, Variable>> scopeStack = stackFrames.get(i);
            for (int j = scopeStack.size() - 1; j >= 0; j--) {
                HashMap<String, Variable> scope = scopeStack.get(j);
                if (scope.containsKey(id)) {
                    if (scope.get(id).type != Core.INTEGER) {
                        System.out.println("ERROR: Variable " + id + " is not an integer when trying to assign Integer.");
                        System.exit(0);
                    }
                    scope.get(id).integer = value;
                    return;
                }
            }
        }
        System.out.println("ERROR: Variable " + id + " is not declared when trying to assign Integer.");
        System.exit(0);
    }

    // get the value of the integer variable
    public int getInteger(String id) {
        for(int i = stackFrames.size() - 1; i >= 0; i--){
            Stack<HashMap<String, Variable>> scopeStack = stackFrames.get(i);
            for (int j = scopeStack.size() - 1; j >= 0; j--) {
                HashMap<String, Variable> scope = scopeStack.get(j);
                if (scope.containsKey(id)) {
                    if (scope.get(id).type != Core.INTEGER) {
                        System.out.println("ERROR: Variable " + id + " is not an integer at getInteger.");
                        System.exit(0);
                    }
                    return scope.get(id).integer;
                }
            }
        }
        System.out.println("ERROR: Variable " + id + " is not declared at getInteger.");
        System.exit(0);
        return 0;
    }

    // initialize the array
    public void initializeArray(String id, int size) {
        for(int i = stackFrames.size() - 1; i >= 0; i--){
            Stack<HashMap<String, Variable>> scopeStack = stackFrames.get(i);
            for (int j = scopeStack.size() - 1; j >= 0; j--) {
                HashMap<String, Variable> scope = scopeStack.get(j);
                if (scope.containsKey(id)) {
                    if (scope.get(id).type != Core.OBJECT) {
                        System.out.println("ERROR: Variable " + id + " is not an array when initializeArray.");
                        System.exit(0);
                    }
                    scope.get(id).initializeArray(size);
                    
                    return;
                }
            }
        }
        System.out.println("ERROR: Variable " + id + " is not declared when initializeArray.");
        System.exit(0);
    }

    // aaign array value to the variable
    public void assignArrayIndexValue(String id, int index, int value) {
        for(int i = stackFrames.size() - 1; i >= 0; i--){
            Stack<HashMap<String, Variable>> scopeStack = stackFrames.get(i);
            for (int j = scopeStack.size() - 1; j >= 0; j--) {
                HashMap<String, Variable> scope = scopeStack.get(j);
                if (scope.containsKey(id)) {
                    if (scope.get(id).type != Core.OBJECT) {
                        System.out.println("ERROR: Variable " + id + " is not an array when assignArray.");
                        System.exit(0);
                    }
                    // check it is initialized
                    if (scope.get(id).array == null) {
                        System.out.println("ERROR: Array " + id + " is not initialized when assignArray.");
                        System.exit(0);
                    }
                    if (index < 0 || index >= scope.get(id).array.length) {
                        System.out.println("ERROR: Array index out of bounds when assignArray.");
                        System.exit(0);
                    }
                    Variable variable = scope.get(id);
                    variable.array[index] = value;
                    return;
                }
            }
        }
        System.out.println("ERROR: Variable " + id + " is not declared when assignArray.");
        System.exit(0);
    }

    // get the value of the array variable
    public int getArrayIndexValue(String id, int index) {
        for(int i = stackFrames.size() - 1; i >= 0; i--){
            Stack<HashMap<String, Variable>> scopeStack = stackFrames.get(i);
            for (int j = scopeStack.size() - 1; j >= 0; j--) {
                HashMap<String, Variable> scope = scopeStack.get(j);
                if (scope.containsKey(id)) {
                    if (scope.get(id).type != Core.OBJECT) {
                        System.out.println("ERROR: Variable " + id + " is not an array at getArrayIndexValue.");
                        System.exit(0);
                    }
                    // check it is initialized
                    if (scope.get(id).array == null) {
                        System.out.println("ERROR: Array " + id + " is not initialized at getArrayIndexValue.");
                        System.exit(0);
                    }
                    if (index < 0 || index >= scope.get(id).array.length) {
                        System.out.println("ERROR: Array index out of bounds at getArrayIndexValue.");
                        System.exit(0);
                    }
                    return scope.get(id).array[index];
                }
            }
        }
        System.out.println("ERROR: Variable " + id + " is not declared at getArrayIndexValue.");
        System.exit(0);
        return 0;
    }

   

}
