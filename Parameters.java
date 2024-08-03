import java.util.ArrayList;
import java.util.List;


public class Parameters {
    //<parameters> ::= ID | ID , <parameters>
    String id;
    Parameters parameters;
    //set a boolean to check if it is a argument or a parameter
    boolean isParameter;

     // a constructor to set the isParameter
     public Parameters(boolean isParameter) {
        this.isParameter = isParameter;
    }

    public void parse(SymbolTable symbolTable) {
        Parser.expectedToken(Core.ID);
        id = Parser.scanner.getId();
        //simplly declare the id as object right now
        if(isParameter){
            symbolTable.declare(id, Core.OBJECT, isParameter);
        }else{
            symbolTable.getVariable(id);
        }
        Parser.nextToken();
        if (Parser.currentToken() == Core.COMMA) {
            Parser.nextToken();
            // check right now in funtion or call use isParameter to set the next parameters
            if(isParameter){
                parameters = new Parameters(true);
            }else{
                parameters = new Parameters(false);
            }
            parameters.parse(symbolTable);
        }
    }

    public void print() {
        System.out.print(id);
        if (parameters != null) {
            System.out.print(", ");
            parameters.print();
        }
    }

    //execute the parameters return a list of parameters
    public List<String> execute(SymbolTable symbolTable) {
        List<String> parameterList = new ArrayList<>();
        Parameters currentParam = this;
        while (currentParam != null) {
            parameterList.add(currentParam.id);
            currentParam = currentParam.parameters;
        }
        return parameterList;
    }

}