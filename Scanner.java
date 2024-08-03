import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

class Scanner {
    String id = "[a-zA-Z][a-zA-Z0-9]*";
    String constant = "[0-9]|[1-9][0-9]*";
    BufferedReader in;
    StringBuilder token;
    Core t;

    // Initialize the scanner
    Scanner(String filename) {
        try {
            this.in = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found");
        }
        this.t = this.nextToken();
    }

    // Advance to the next token
    public Core nextToken() {
        try {
            int c = this.in.read();
            // Skip all the white space
            while (Character.isWhitespace(c) && c != -1) {
                c = this.in.read();
            }
            // Handle the case of end of stream
            if (c == -1) {
                this.t = Core.EOS;
            } else {
                switch ((char) c) {
                    // Handle cases of all the special symbols with one character
                    case '+':
                        this.t = Core.ADD;
                        break;

                    case '-':
                        this.t = Core.SUBTRACT;
                        break;

                    case '*':
                        this.t = Core.MULTIPLY;
                        break;

                    case '/':
                        this.t = Core.DIVIDE;
                        break;

                    case ':':
                        this.t = Core.COLON;
                        break;

                    case '<':
                        this.t = Core.LESS;
                        break;

                    case ';':
                        this.t = Core.SEMICOLON;
                        break;

                    case '.':
                        this.t = Core.PERIOD;
                        break;

                    case ',':
                        this.t = Core.COMMA;
                        break;

                    case '(':
                        this.t = Core.LPAREN;
                        break;

                    case ')':
                        this.t = Core.RPAREN;
                        break;

                    case '[':
                        this.t = Core.LBRACE;
                        break;

                    case ']':
                        this.t = Core.RBRACE;
                        break;

                    // Handle cases of all the special symbols with one or more characters
                    case '=': {
                        this.in.mark(1);
                        int nextChar = this.in.read();
                        if ((char) nextChar == '=') {
                            this.t = Core.EQUAL;
                        } else {
                            this.in.reset();
                            this.t = Core.ASSIGN;
                        }
                        break;
                    }

                    // Handle case of ID, CONST, or keyword
                    default: {
                        boolean continued = true;
                        this.token = new StringBuilder();
                        // if the first character is digit, stop until we hit something non-digit
                        if (Character.isDigit((char) c)) {
                            while (continued) {
                                this.token.append((char) c);
                                this.in.mark(1);
                                c = this.in.read();
                                continued = c != -1
                                        && Character.isDigit((char) c);
                                if (!continued) {
                                    this.in.reset();
                                }
                            }
                        }
                        // if the first character is letter, stop until we hit something non-letter and
                        // non-digit
                        else if (Character.isLetter((char) c)) {
                            while (continued) {
                                this.token.append((char) c);
                                this.in.mark(1);
                                c = this.in.read();
                                continued = c != -1
                                        && Character.isLetterOrDigit((char) c);
                                if (!continued) {
                                    this.in.reset();
                                }
                            }
                        }
                        // if the first character is not letter, digit, or any special symbol above,
                        // stop reading
                        else {
                            this.token.append((char) c);
                        }

                        switch (this.token.toString()) {
                            // Handle cases of all the keywords
                            case "and":
                                this.t = Core.AND;
                                break;

                            case "begin":
                                this.t = Core.BEGIN;
                                break;

                            case "do":
                                this.t = Core.DO;
                                break;

                            case "else":
                                this.t = Core.ELSE;
                                break;

                            case "end":
                                this.t = Core.END;
                                break;

                            case "return":
                                this.t = Core.RETURN;
                                break;

                            case "if":
                                this.t = Core.IF;
                                break;

                            case "in":
                                this.t = Core.IN;
                                break;

                            case "integer":
                                this.t = Core.INTEGER;
                                break;

                            case "is":
                                this.t = Core.IS;
                                break;

                            case "new":
                                this.t = Core.NEW;
                                break;

                            case "not":
                                this.t = Core.NOT;
                                break;

                            case "or":
                                this.t = Core.OR;
                                break;

                            case "out":
                                this.t = Core.OUT;
                                break;

                            case "procedure":
                                this.t = Core.PROCEDURE;
                                break;

                            case "object":
                                this.t = Core.OBJECT;
                                break;

                            case "then":
                                this.t = Core.THEN;
                                break;

                            case "while":
                                this.t = Core.WHILE;
                                break;

                            default: {
                                // Handle the case of identifier
                                if (this.token.toString().matches(this.id)) {
                                    this.t = Core.ID;

                                }
                                // Handle the case of constant less than 256
                                else if (this.token.toString()
                                        .matches(this.constant)
                                        && Integer.parseInt(
                                                this.token.toString()) <= 999983) {
                                    this.t = Core.CONST;
                                }
                                // Handle cases of all the invalid input including invalid symbols, leading
                                // zeros, identifier with digit 0, constant greater than 999983 and etc.
                                else {
                                    throw new Exception();
                                }
                                break;
                            }

                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR: Invalid input " + this.token.toString());
            this.t = Core.ERROR;
        }
        return this.t;
    }

    // Return the current token
    public Core currentToken() {
        return this.t;
    }

    public String getId() {
        return this.token.toString();
    }

    public int getConst() {
        return Integer.parseInt(this.token.toString());
    }

}
