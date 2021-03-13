/**
* Trabalho de compiladores - Analisador Lexico
* Professor: Alexei Machado
*
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*
*Classe para formar Lexema
*
*/

import java.io.*;

public class AnalisadorLexico{

    public BufferedReader codigo;
    public TabelaDeSimbolos tabelaDeSimbolos;
    public String lexema;
    public String tipoConst;
    public char ultimaLetra;
    public int linha;
    public boolean errorCompilacao;
    public boolean devolve;
    public boolean fimDeArquivo;
    public boolean debugMode = false;

    /**
     * Metodo construtor analisador lexico
     * @param bufferedReader arquivo que sera lido
     * @param tabelaDeSimbolos tabela de simbolos
     */
    public AnalisadorLexico(BufferedReader bufferedReader, TabelaDeSimbolos tabelaDeSimbolos){
        this.codigo = bufferedReader;
        this.tabelaDeSimbolos = tabelaDeSimbolos;
        ultimaLetra = ' ';
        linha = 1;
        errorCompilacao = devolve = fimDeArquivo = false;
    }


    /**
     * Metodo da maquina de estados do analisador lexico
     * @return Simbolo retorna o simbolo criado
     */
    public Simbolo maquinaDeEstados() {
        int estadoAtual = 0;
        int estadoFinal = 18;

        while(estadoAtual != estadoFinal){
            switch (estadoAtual) {
                case 1:
                    estadoAtual = Estado1();
                case 2:
                    estadoAtual = Estado2();
                default:
                    break;
              }
        }
        if(!fimDeArquivo) {
            if(tabelaDeSimbolos.BuscarLexema(lexema) == null) {
                if(lexema.charAt(0) == '"' || lexema.charAt(0) == '\'' || Util.EhDigito(lexema.charAt(0))) {
                    Simbolo simboloConst = new Simbolo(tabelaDeSimbolos.CONSTANTE, lexema, tipoConst);
                    return simboloConst;
                }else {
                    Simbolo simboloIdentificador = tabelaDeSimbolos.InserirIdentificador(lexema);
                    return simboloIdentificador;
                }
            }else{
                return tabelaDeSimbolos.BuscarLexema(lexema);
            }
        }else{
            return new Simbolo((byte)38,lexema);
        }
    }

    /**
     * Metodo para ler uma caracter
     * @return retorna a caracter lida
     */
    public char LerCaracter() {
        try {
            if(devolve) {
                devolve = false; //Ultimo caracter

            }else{
                ultimaLetra = (char) codigo.read(); //Novo caracter
            }
        }catch(Exception e){
            System.out.println("Error ao acessar arquivo");
            System.out.println(e);
        }
        return ultimaLetra;
    }

    /**
     * Metodo para mostrar erros durante a execucao do analisador lexico
     * @param char caracter que gerou o erro
     */
    public void MostrarErro(char caracter) {
        lexema += caracter;
        if(Util.EhCaracterValido(caracter)) {
            System.out.println(linha);
            System.out.println("lexema nao identificado " + '[' + lexema + "].");
        }else{
            if(Util.fimDeArquivo == caracter) {
                System.out.println(linha);
                System.out.println("fim de arquivo nao esperado.");
            }else {
                System.out.println(linha);
                System.out.println("caractere invalido.");
            }
        }

        errorCompilacao = true;
        System.exit(0);
    }

    public void MostrarTransicao(char caracter, int estadoAtual, int estadoFinal){
        if(debugMode)
            System.out.println("caracter = " + caracter +" estado atual: "+ estadoAtual + " proximo estado: "+estadoFinal);
    }

    public int Estado1() {
        char caracter = LerCaracter();

        if(caracter == Util.sublinhado){
            lexema += caracter;
            MostrarTransicao(caracter, 1, 1);
            return 1;
        } else if (Util.EhLetra(caracter) || Util.EhDigito(caracter)) {
            lexema += caracter;
            MostrarTransicao(caracter, 1, 2);
            return 2;
        }
        MostrarErro(caracter);
        return 18;
    }

    public int Estado2() {
        char caracter = LerCaracter();

        if(caracter == Util.sublinhado || Util.EhLetra(caracter) || Util.EhDigito(caracter)){
            lexema += caracter;
            MostrarTransicao(caracter, 2, 2);
            return 2;
        } else if(Util.EhCaracterValido(caracter)) {
            lexema += caracter;
            MostrarTransicao(caracter, 2, 18);
            devolve = true;
            return 18;
        }
        MostrarErro(caracter);
        return 18;
    }
    
    public int Estado3() {
        char caracter = LerCaracter();

        if(caracter == Util.asterisco){
            MostrarTransicao(caracter, 3, 3);
            return 3;
        } else if(caracter != Util.asterisco || caracter != Util.barra) {
            MostrarTransicao(caracter, 3, 4);
            return 4;
        }
        MostrarErro(caracter);
        return 18;
    }

    public int Estado4() {
        char caracter = LerCaracter();

        if(caracter == Util.asterisco){
            MostrarTransicao(caracter, 4, 3);
            return 3;
        } else {
            MostrarTransicao(caracter, 4, 4);
            return 4;
        }
    }

    public int Estado5() {
        char caracter = LerCaracter();

        if(caracter != Util.asterisco){
            MostrarTransicao(caracter, 5, 18);
            devolve = true;
            return 18;
        } else if(caracter == Util.asterisco){
            MostrarTransicao(caracter, 5, 4);
            return 4;
        } 
        MostrarErro(caracter);
        return 18;
    }

    public int Estado6() {
        char caracter = LerCaracter();

        if(caracter != Util.barraN || caracter != Util.cifrao || Util.EhLetra(caracter) || Util.EhDigito(caracter)){
            lexema += caracter;
            MostrarTransicao(caracter, 6, 6);
            return 6;
        } else if (caracter == Util.aspas) {
            MostrarTransicao(caracter, 6, 18);
            return 18;
        }
        MostrarErro(caracter);
        return 18;
    }

}