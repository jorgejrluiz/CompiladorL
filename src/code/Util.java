/**
* Trabalho de compiladores - Util
* Professor: Alexei Machado
* 
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*
* Classe Util 
*
*/

public class Util{

    public static char sublinhado = '_';
    public static char pontoFinal = '.';
    public static char virgula = ',';
    public static char pontoEVirgula = ';';
    public static char eComercial = '&';
    public static char doisPontos = ':';
    public static char abreParenteses = '(';
    public static char fechaParenteses = ')';
    public static char abreColchetes = '[';
    public static char fechaColchetes = ']';
    public static char abreChaves = '{';
    public static char fechaChaves = '}';
    public static char mais = '+';
    public static char menos = '-';
    public static char aspas = '\"';
    public static char apostofro = '\'';
    public static char barra = '/';
    public static char porcentagem = '%';
    public static char circunflexo = '^';
    public static char arroba = '@';
    public static char esclamacao = '!';
    public static char interrogacao = '?';
    public static char menor = '<';
    public static char maior = '>';
    public static char igual = '=';
    public static char asterisco = '*';
    public static char cifrao = '$';

    public static char tab = 9;
    public static char barraN = 10;
    public static char novalinha = 11;
    public static char cursorInicio = 13;
    public static char fimDeArquivo = 65535;
    public static char espaco = 32;
    
    /**
     * Metodo para verificar se eh letra
     * @param caracter caracter que sera avaliado
     * @return boolean
     */
    public static boolean EhLetra(char caracter){
        return (caracter >= 'a' && caracter <= 'z') || (caracter >= 'A' && caracter <= 'Z');
    }

    /**
     * Metodo para verificar se caracter eh digito
     * @param caracter caracter que sera avaliado
     * @return boolean
     */
    public static boolean EhDigito(char caracter){
        return caracter >= '0' && caracter <= '9';
    }

    /**
     * Metodo para verificar se eh hexadecimal ou nao
     * @param c caracter que sera avaliado
     * @return boolean
     */
    public static boolean EhHexadecimal(char caracter) {
        return caracter >= 'A' && caracter <= 'F';
    }
    
    /**
     * Metodo para verificar se eh caracter especial
     * @param caracter caracter que sera avaliado
     * @return boolean
     */
    public static boolean EhCaracterEspecial(char caracter) {
        return caracter == sublinhado || caracter == pontoFinal || caracter == virgula || caracter == pontoEVirgula || caracter == eComercial ||
        caracter == doisPontos || caracter == abreParenteses || caracter == fechaParenteses || caracter == abreColchetes || caracter == fechaColchetes ||
        caracter == abreChaves || caracter == fechaChaves || caracter == mais || caracter == menor || caracter == aspas || caracter == apostofro ||
        caracter == barra || caracter == porcentagem || caracter == circunflexo || caracter == arroba || caracter == esclamacao || caracter == interrogacao ||
        caracter == menor || caracter == maior || caracter == igual || caracter == asterisco || caracter == menos || caracter == espaco;
    }

    /**
     * Metodo para verificar se eh caracter especial e token
     * @param c caracter que sera avaliado
     * @return boolean 
     */
    public static boolean EhCaracterEspecialEToken(char caracter) {
        return caracter == sublinhado || caracter == pontoFinal || caracter == virgula || caracter == pontoEVirgula ||
        caracter == abreParenteses || caracter == fechaParenteses || caracter == abreColchetes || caracter == fechaColchetes ||
        caracter == abreChaves || caracter == fechaChaves || caracter == mais || caracter == menor || caracter == aspas || caracter == apostofro ||
        caracter == barra || caracter == porcentagem || caracter == menor || caracter == maior || caracter == igual || caracter == asterisco|| caracter == menos;
    }

    /**
     * Metodo para verificar se eh quebra de linhas
     * @param caracter caracter que sera avaliado
     * @return boolean 
     */
    public static boolean EhQuebraDeLinha(char caracter) {
        return caracter == barraN || caracter == novalinha || caracter == cursorInicio || caracter == espaco;
    }        

    /**
     * Metodo para verificar se eh caracter valido
     * @param caracter caracter que sera avaliado
     * @return boolean 
     */
    public static boolean EhCaracterValido(char caracter) {
        return ( EhLetra(caracter) || EhDigito(caracter) || EhCaracterEspecial(caracter));
    }

}