public class Parser {

    public static Scanner scanner;
    public static Scanner scannerData;
    private SymbolTable symbolTableForPaser;
    private SymbolTable symbolTableForExecute;

    public Parser() {
        symbolTableForPaser = new SymbolTable();
        symbolTableForExecute = new SymbolTable();
    }

    public static void expectedToken(Core id) {
        if (scanner.currentToken() != id) {
            System.out.println("ERROR: Expected " + id + " but got " + scanner.currentToken());
            System.exit(0);
        }
    }

    public static Core currentToken() {
        return scanner.currentToken();
    }

    public static void nextToken() {
        scanner.nextToken();
    }

    public void parse() {
        Procedure procedure = new Procedure();
        procedure.parse(symbolTableForPaser);
        //procedure.print();
        procedure.execute(symbolTableForExecute);
    }

    public static void printIdentation(int identationLevel) {
        for (int i = 0; i < identationLevel; i++) {
            System.out.print("    ");
        }
    }

}
