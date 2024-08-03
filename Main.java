public class Main {
    public static void main(String[] args) {

        
        //String s = "9";
        //String filename = "Correct/"+s+".code";
        //String dataFileName = "Correct/"+s+".data";
        Parser parser = new Parser();
        Parser.scanner = new Scanner(args[0]);
        Parser.scannerData = new Scanner(args[1]);

        // Parser.scanner = new Scanner(filename);
        // Parser.scannerData = new Scanner(dataFileName);
        parser.parse();

    }
}