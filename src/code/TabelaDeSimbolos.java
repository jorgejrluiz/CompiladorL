/**
* Trabalho de compiladores - Tabela de Simbolos
* Professor: Alexei Machado
*
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*/

import java.util.HashMap;

//Criacao de tabela de simbolos
public class TabelaDeSimbolos{
    public HashMap<String, Simbolo> tabela = new HashMap<>();

    public final static byte FINAL = 0;
    public final static byte INT = 1;
    public final static byte CHAR = 2;
    public final static byte FOR = 3;
    public final static byte IF = 4;
    public final static byte TRUE = 5;
    public final static byte ELSE = 6;
    public final static byte AND = 7;
    public final static byte OR = 8;
    public final static byte NOT = 9;
    public final static byte DOIS_PONTOS_IGUAL = 10;
    public final static byte IGUAL = 11;
    public final static byte PARENTESES_ABERTO = 12;
    public final static byte PARENTESES_FECHADO = 13;
    public final static byte MENOR = 14;
    public final static byte MAIOR = 15;
    public final static byte DIFERENTE = 16;
    public final static byte MAIOR_IGUAL = 17;
    public final static byte MENOR_IGUAL = 18;
    public final static byte VIRGULA = 19;
    public final static byte MAIS = 20;
    public final static byte MENOS = 21;
    public final static byte ASTERISCO = 22;
    public final static byte BARRA = 23;
    public final static byte PONTO_VIRGULA = 24;
    public final static byte CHAVES_ABERTA = 25;
    public final static byte CHAVES_FECHADA = 26;
    public final static byte THEN = 27;
    public final static byte READLN = 28;
    public final static byte FALSE = 29;
    public final static byte WRITE = 30;
    public final static byte WRITELN = 31;
    public final static byte PORCENTAGEM = 32;
    public final static byte COLCHETE_ABERTO = 33;
    public final static byte COLCHETE_FECHADO = 34;
    public final static byte MAIN = 35;
    public final static byte CIFRAO = 36;
    public final static byte CONSTANTE = 37;
    public final static byte IDENTIFICADOR = 38;

    /**
     * Metodo construtor da tabela de simbolos
     */
    public TabelaDeSimbolos(){
      tabela.put("final", new Simbolo(FINAL, "final"));
      tabela.put("int", new Simbolo(INT, "int"));
      tabela.put("char", new Simbolo(CHAR, "char"));
      tabela.put("for", new Simbolo(FOR, "for"));
      tabela.put("if", new Simbolo(IF, "if"));
      tabela.put("TRUE", new Simbolo(TRUE, "TRUE"));
      tabela.put("else", new Simbolo(ELSE, "else"));
      tabela.put("and", new Simbolo(AND, "and"));
      tabela.put("or", new Simbolo(OR, "or"));
      tabela.put("not", new Simbolo(NOT, "not"));
      tabela.put(":=", new Simbolo(DOIS_PONTOS_IGUAL, ":="));
      tabela.put("=", new Simbolo(IGUAL, "="));
      tabela.put("(", new Simbolo(PARENTESES_ABERTO, "("));
      tabela.put(")", new Simbolo(PARENTESES_FECHADO, ")"));
      tabela.put("<", new Simbolo(MENOR, "<"));
      tabela.put(">", new Simbolo(MAIOR, ">"));
      tabela.put("<>", new Simbolo(DIFERENTE, "<>"));
      tabela.put(">=", new Simbolo(MAIOR_IGUAL, ">="));
      tabela.put("<=", new Simbolo(MENOR_IGUAL, "<="));
      tabela.put(",", new Simbolo(VIRGULA, ","));
      tabela.put("+", new Simbolo(MAIS, "+"));
      tabela.put("-", new Simbolo(MENOS, "-"));
      tabela.put("*", new Simbolo(ASTERISCO, "*"));
      tabela.put("/", new Simbolo(BARRA, "/"));
      tabela.put(";", new Simbolo(PONTO_VIRGULA, ";"));
      tabela.put("{", new Simbolo(CHAVES_ABERTA, "{"));
      tabela.put("}", new Simbolo(CHAVES_FECHADA, "}"));
      tabela.put("then", new Simbolo(THEN, "then"));
      tabela.put("readln", new Simbolo(READLN, "readln"));
      tabela.put("FALSE", new Simbolo(FALSE, "FALSE"));
      tabela.put("write", new Simbolo(WRITE, "write"));
      tabela.put("writeln", new Simbolo(WRITELN, "writeln"));
      tabela.put("%", new Simbolo(PORCENTAGEM, "%"));
      tabela.put("[", new Simbolo(COLCHETE_ABERTO, "["));
      tabela.put("]", new Simbolo(COLCHETE_FECHADO, "]"));
      tabela.put("main", new Simbolo(MAIN, "main"));
      tabela.put("$", new Simbolo(CIFRAO, "cifrao"));
      tabela.put("const", new Simbolo(CONSTANTE, "const"));
      tabela.put("id", new Simbolo(IDENTIFICADOR, "id"));
    }

    /**
     * Metodo para buscar o lexema na tabela
     * @param lexema String lexema
     * @return Simbolo
     */
    public Simbolo BuscarLexema(String lexema) {
        return tabela.get(lexema.toLowerCase());
    }

    public void PrintTabela() {
        int i = 0;
        for (String lexema : tabela.keySet()) {
            if(tabela.get(lexema.toLowerCase()).token == 0 || tabela.get(lexema.toLowerCase()).token == 37)
            System.out.println("Token: " + tabela.get(lexema.toLowerCase()).token + " Lexema: " + tabela.get(lexema.toLowerCase()).lexema + " Tamanho: " + tabela.get(lexema.toLowerCase()).tamanho + " Tipo: " + tabela.get(lexema.toLowerCase()).tipo);
            i++;
        }
    }
    /**
     * Metodo para inserir um identificador na tabela
     * @param lexema String lexema
     * @return Simbolo simbolo adicionado
     */
    public Simbolo InserirIdentificador(String lexema) {
        Simbolo simbolo = new Simbolo (IDENTIFICADOR,lexema.toLowerCase());
        tabela.put(lexema.toLowerCase(),simbolo);
        return simbolo;
    }
    /**
     * Metodo para printar a tabela
     */
    public void MostrarTabela(){
        int i = 0;
        for (String lexema : tabela.keySet()) {
            System.out.println(tabela.get(lexema).token + " " + tabela.get(lexema).lexema);
            i++;
        }
        System.out.println("São " + i + " chaves ao todo.");
    }
    /**
     * Metodo para inserir uma constante na tabela
     */
    public Simbolo InserirConstante(String lexema) {
        Simbolo simbolo = new Simbolo (CONSTANTE,lexema);
        tabela.put(lexema,simbolo);
        return simbolo;
    }

}
