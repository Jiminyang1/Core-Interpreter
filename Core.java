// This enum serves as our tokens
enum Core {
    // Keywords
    PROCEDURE,
    BEGIN,
    IS,
    END,
    IF,
    ELSE,
    IN,
    INTEGER,
    RETURN,
    DO,
    NEW,
    NOT,
    AND,
    OR,
    OUT,
    OBJECT,
    THEN,
    WHILE,
    // Symbols
    ADD,
    SUBTRACT, // -
    MULTIPLY, // *
    DIVIDE, // /
    ASSIGN, // =
    EQUAL, // ==
    LESS, // <
    COLON, // :
    SEMICOLON, // ;
    PERIOD, // .
    COMMA, // ,
    LPAREN, // (
    RPAREN, // )
    LBRACE, // [
    RBRACE, // ]
    // Others
    CONST,
    ID,
    EOS,
    ERROR
}