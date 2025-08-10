import java.util.List;

class SymbolTable {
    private final StoreMap storeMap = new StoreMap();

    public void printGC() {
        System.out.println("gc:" + storeMap.getGcCounter());
    }

    public void enterScope() {
        storeMap.enterScope();
    }

    public void exitScope() {
        storeMap.exitScope();
    }

    public void enterFunction() {
        storeMap.enterScope();
    }

    public void exitFunction() {
        storeMap.exitScope();
    }

    public void declare(String id, Core type, boolean isParameter) {
        storeMap.declareVariable(id, type);
    }

    public Core getType(String id) {
        StoreMap.Variable var = storeMap.findVariable(id);
        if (var == null) {
            System.out.println("ERROR: Variable " + id + " is not declared.");
            System.exit(0);
        }
        return var.getType();
    }

    public StoreMap.Variable getVariable(String id) {
        StoreMap.Variable var = storeMap.findVariable(id);
        if (var == null) {
            System.out.println("ERROR: Variable " + id + " is not declared.");
            System.exit(0);
        }
        return var;
    }

    public void assignInteger(String id, int value) {
        StoreMap.Variable var = getVariable(id);
        if (var.getType() != Core.INTEGER) {
            System.out.println("ERROR: Variable " + id + " is not an integer.");
            System.exit(0);
        }
        var.integer = value;
    }

    public int getInteger(String id) {
        StoreMap.Variable var = getVariable(id);
        if (var.getType() != Core.INTEGER) {
            System.out.println("ERROR: Variable " + id + " is not an integer.");
            System.exit(0);
        }
        return var.integer;
    }

    public void initializeArray(String id, int size) {
        StoreMap.Variable var = getVariable(id);
        if (var.getType() != Core.OBJECT) {
            System.out.println("ERROR: Variable " + id + " is not an array.");
            System.exit(0);
        }
        var.initializeArray(size);
        storeMap.incrementGcCounter();
        printGC();
    }

    public void assignArrayIndexValue(String id, int index, int value) {
        StoreMap.Variable var = getVariable(id);
        if (var.getType() != Core.OBJECT) {
            System.out.println("ERROR: Variable " + id + " is not an array.");
            System.exit(0);
        }
        if (var.array == null) {
            System.out.println("ERROR: Array " + id + " is not initialized.");
            System.exit(0);
        }
        if (index < 0 || index >= var.array.length) {
            System.out.println("ERROR: Array index out of bounds.");
            System.exit(0);
        }
        var.array[index] = value;
    }

    public int getArrayIndexValue(String id, int index) {
        StoreMap.Variable var = getVariable(id);
        if (var.getType() != Core.OBJECT) {
            System.out.println("ERROR: Variable " + id + " is not an array.");
            System.exit(0);
        }
        if (var.array == null) {
            System.out.println("ERROR: Array " + id + " is not initialized.");
            System.exit(0);
        }
        if (index < 0 || index >= var.array.length) {
            System.out.println("ERROR: Array index out of bounds.");
            System.exit(0);
        }
        return var.array[index];
    }

    public void declareFunction(String id, List<String> parameters, StmtSeq body) {
        storeMap.declareFunction(id, new StoreMap.FunctionDefinition(parameters, body));
    }

    public StoreMap.FunctionDefinition getFunction(String id) {
        StoreMap.FunctionDefinition func = storeMap.findFunction(id);
        if (func == null) {
            System.out.println("ERROR: Function " + id + " is not declared.");
            System.exit(0);
        }
        return func;
    }
}
