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
                case 0:
                    estadoAtual = Estado0():
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

    /* Mapa do Estado 0
     * se espaco volta para 0
     * se '.' , ',', ';', '(', ')', '[', ']', '{', '}', '+', '-', '%', '@', '!', '?', '=', '*' vai para 18
     * se sublinhado vai para 1
     * se letra vai para 2
     * se / vai para 3
     * se aspas vai para 6
     * se apostofro vai para 7
     * se : vai para 8
     * se < vai para 9
     * se > vai para 10
     * se 1...9 vai para 11
     * se 0 vai para 12
     */
    public int Estado0() {
      char caracter = LerCaracter();

      if(caracter == Util.espaco){
          MostrarTransicao(caracter, 0, 0);
          return 0;
      } else if (Util.EhEspecial(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 18);
          return 18;
      } else if (caracter == Util.sublinhado){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 1);
          return 1;
      } else if(Util.EhLetra(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 2);
          return 2;
      } else if(caracter == Util.barra){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 3);
          return 3;
      } else if(caracter == Util.aspas){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 6);
          return 6;
      } else if(caracter == Util.apostofro){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 7);
          return 7;
      } else if(caracter == Util.doisPontos){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 8);
          return 8;
      } else if(caracter == Util.menor){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 9);
          return 9;
      } else if(caracter == Util.maior){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 10);
          return 10;
      } else if(caracter != '0' && Util.EhDigito(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 11);
          return 11;
      } else if(caracter == '0'){
          lexema += caracter;
          MostrarTransicao(caracter, 0, 12);
          return 12;
      }
    }

    /* Mapa do Estado 1
     * se sublinhado volta para 1
     * se letra ou digito vai para 2
     */
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

    /* Mapa do Estado 2
     * se letra, digito ou sublinhado volta para 2
     * diferente de letra, digito ou sublinhado devolve e vai para 18
     */
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

    /* Mapa do Estado 3
     * se * volta para 3
     * diferente de * ou diferente de / vai para 4
     */
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

    /* Mapa do Estado 4
     * diferente de * volta para 4
     * se * vai para 3
     */
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

    /* Mapa do Estado 5
     * se * vai para 4
     * diferente de * devolve e vai para 18
     */
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

    /* Mapa do Estado 6
     * se letra, digito, diferente de \n ou diferente de $ volta para 6
     * se aspas vai para 18
     */
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
