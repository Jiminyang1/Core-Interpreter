import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class StoreMap {
    private final Stack<Map<String, Variable>> scopes = new Stack<>();
    private final Map<String, FunctionDefinition> functions = new HashMap<>();
    private int gcCounter = 0;

    public StoreMap() {
        enterScope();
    }

    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    public void exitScope() {
        Map<String, Variable> scope = scopes.pop();
        for (Variable var : scope.values()) {
            if (var.isAllocated()) {
                var.decrementReferenceCount();
            }
        }
    }

    public void declareVariable(String id, Core type) {
        Map<String, Variable> currentScope = scopes.peek();
        if (currentScope.containsKey(id)) {
            System.out.println("ERROR: Variable " + id + " is already declared in this scope.");
            System.exit(0);
        }
        currentScope.put(id, new Variable(type));
    }

    public Variable findVariable(String id) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Map<String, Variable> scope = scopes.get(i);
            if (scope.containsKey(id)) {
                return scope.get(id);
            }
        }
        return null;
    }

    public void declareFunction(String name, FunctionDefinition func) {
        if (functions.containsKey(name)) {
            System.out.println("ERROR: Function " + name + " is already declared.");
            System.exit(0);
        }
        functions.put(name, func);
    }

    public FunctionDefinition findFunction(String name) {
        return functions.get(name);
    }

    public int getGcCounter() {
        return gcCounter;
    }

    public void incrementGcCounter() {
        gcCounter++;
    }

    public void decrementGcCounter() {
        gcCounter--;
    }

    public static class Variable {
        private final Core type;
        private int referenceCount = 0;
        private boolean isAllocated = false;
        public Integer integer;
        public int[] array;

        public Variable(Core type) {
            this.type = type;
            if (type == Core.INTEGER) {
                this.integer = 1;
            }
        }

        public Core getType() {
            return type;
        }

        public void initializeArray(int size) {
            this.array = new int[size];
            isAllocated = true;
            referenceCount = 1;
        }

        public void incrementReferenceCount() {
            referenceCount++;
        }

        public void decrementReferenceCount() {
            referenceCount--;
            if (referenceCount == 0) {
                array = null;
                isAllocated = false;
            }
        }

        public boolean isAllocated() {
            return isAllocated;
        }

        public int getReferenceCount() {
            return referenceCount;
        }
    }

    public static class FunctionDefinition {
        private final List<String> parameters;
        private final StmtSeq body;

        public FunctionDefinition(List<String> parameters, StmtSeq body) {
            this.parameters = parameters;
            this.body = body;
        }

        public List<String> getParameters() {
            return parameters;
        }

        public StmtSeq getBody() {
            return body;
        }
    }
}
