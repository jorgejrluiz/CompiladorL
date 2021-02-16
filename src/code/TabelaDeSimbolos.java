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

    public final static byte identificador = 0;
    
    public final static byte CONST = 1;
    public final static byte VAR = 2;
    public final static byte INTEGER = 3;
    public final static byte CHAR = 4;
    public final static byte FOR = 5;
    public final static byte IF = 6;
    public final static byte ELSE = 7;
    public final static byte AND = 8;
    public final static byte OR = 9;
    public final static byte NOT = 10;
    public final static byte IGUAL = 11;
    public final static byte TO = 12;
    public final static byte PARENTESES_ABERTO = 13;
    public final static byte PARENTESES_FECHADO = 14;
    public final static byte MENOR = 15;
    public final static byte MAIOR = 16;
    public final static byte DIFERENTE = 17;
    public final static byte MAIOR_IGUAL = 18;
    public final static byte MENOR_IGUAL = 19;
    public final static byte VIRGULA = 20; 
    public final static byte MAIS = 21;
    public final static byte MENOS = 22;
    public final static byte ASTERISCO = 23;
    public final static byte BARRA = 24;
    public final static byte PONTO_VIRGULA = 25;
    public final static byte CHAVES_FECHADO = 26;
    public final static byte CHAVES_ABERTO = 27;
    public final static byte THEN = 28;
    public final static byte READLN = 29;
    public final static byte STEP = 30;
    public final static byte WRITE = 31;
    public final static byte WRITELN = 32;
    public final static byte PORCENTAGEM = 33;
    public final static byte COLCHETE_ABERTO = 34;
    public final static byte COLCHETE_FECHADO = 35;
    public final static byte DO = 36;
    public final static byte CIFRAO = 37;
    public final static byte constante = 38;

    /**
     * Metodo construtor da tabela de simbolos
     */
    public TabelaDeSimbolos(){
        tabela.put("id", new Simbolo(identificador, "id"));
        tabela.put("var", new Simbolo(VAR, "var"));
        tabela.put("integer", new Simbolo(INTEGER, "integer"));
        tabela.put("char", new Simbolo(CHAR, "char"));
        tabela.put("for", new Simbolo(FOR, "for"));
        tabela.put("if", new Simbolo(IF, "if"));
        tabela.put("else", new Simbolo(ELSE, "else"));

        tabela.put("and", new Simbolo(AND, "and"));
        tabela.put("or", new Simbolo(OR, "or"));
        tabela.put("not", new Simbolo(NOT, "not"));
        tabela.put("to", new Simbolo(TO, "to"));
        tabela.put("<>", new Simbolo(DIFERENTE, "<>"));
        tabela.put("=", new Simbolo(IGUAL, "="));
        tabela.put("<=", new Simbolo(MENOR_IGUAL, "<="));

        tabela.put(">=", new Simbolo(MAIOR_IGUAL, ">="));
        tabela.put(">", new Simbolo(MAIOR, ">"));
        tabela.put("<", new Simbolo(MENOR, "<"));
        tabela.put("(", new Simbolo(PARENTESES_ABERTO, "("));
        tabela.put(")", new Simbolo(PARENTESES_FECHADO, ")"));
        tabela.put(",", new Simbolo(VIRGULA, ","));
        tabela.put("+", new Simbolo(MAIS, "+"));

        tabela.put( "-", new Simbolo(MENOS, "-"));
        tabela.put("*", new Simbolo(ASTERISCO, "*"));
        tabela.put( "/", new Simbolo(BARRA, "/"));
        tabela.put(";", new Simbolo(PONTO_VIRGULA, ";"));
        tabela.put( "{", new Simbolo(CHAVES_ABERTO, "{"));
        tabela.put("}", new Simbolo(CHAVES_FECHADO, "}"));
        tabela.put("then", new Simbolo(THEN, "then"));

        tabela.put("readln", new Simbolo(READLN, "readln"));
        tabela.put("step", new Simbolo(STEP, "step"));
        tabela.put("write", new Simbolo(WRITE, "write"));
        tabela.put("writeln", new Simbolo(WRITELN, "writeln"));

        tabela.put("%", new Simbolo(PORCENTAGEM, "%"));
        tabela.put("[", new Simbolo(COLCHETE_ABERTO, "["));
        tabela.put("]", new Simbolo(COLCHETE_FECHADO, "]"));
        tabela.put("do", new Simbolo(DO, "do"));
        tabela.put("const", new Simbolo(CONST, "const"));
        tabela.put("$", new Simbolo(CIFRAO, "cifrao"));
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
        Simbolo simbolo = new Simbolo (identificador,lexema.toLowerCase());
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


    public Simbolo InserirConstante(String lexema) {
        Simbolo simbolo = new Simbolo (constante,lexema);
        tabela.put(lexema,simbolo);
        return simbolo;
    }

}