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
                case 3:
                    estadoAtual = Estado3():
                case 4:
                    estadoAtual = Estado4();
                case 5:
                    estadoAtual = Estado5();
                case 6:
                    estadoAtual = Estado6():
                case 7:
                    estadoAtual = Estado7();
                case 8:
                    estadoAtual = Estado8();
                case 9:
                    estadoAtual = Estado9():
                case 10:
                    estadoAtual = Estado10();
                case 11:
                    estadoAtual = Estado11();
                case 12:
                    estadoAtual = Estado12():
                case 13:
                    estadoAtual = Estado13();
                case 14:
                    estadoAtual = Estado14();
                case 15:
                    estadoAtual = Estado15():
                case 16:
                    estadoAtual = Estado16();
                case 17:
                    estadoAtual = Estado17();
                case 18:
                    estadoAtual = Estado18();
                    //final
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
      MostrarErro(caracter);
      return 18;
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

        if(caracter == Util.asterisco){
            MostrarTransicao(caracter, 5, 4);
            return 4;
        } else if(caracter != Util.asterisco){
            MostrarTransicao(caracter, 5, 18);
            devolve = true;
            return 18;
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

    /* Mapa do Estado 7
     * se letra, digito, aspas, \n ou $ volta para 7
     * se apostofro vai para 18
     */
    public int Estado7(){
      char caracter = LerCaracter();

      if(Util.EhLetra(caracter) || Util.EhDigito(caracter) || caracter == Util.aspas || caracter == Util.barraN || caracter == Util.cifrao){
          lexema += caracter;
          MostrarTransicao(caracter, 7, 7);
          return 7;
      } else if (caracter == Util.apostofro) {
          MostrarTransicao(caracter, 7, 18);
          return 18;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 8
     * se = vai para 18
     * se diferente de = devolve e vai para 18
     */
    public int Estado8(){
      char caracter = LerCaracter();

      if(caracter == Util.igual){
          lexema += caracter;
          MostrarTransicao(caracter, 8, 18);
          return 18;
      } else if(Util.EhCaracterValido(caracter)) {
          MostrarTransicao(caracter, 8, 18);
          devolve = true;
          return 18;
      }
      MostrarErro(caracter);
      return 18;

    }

    /* Mapa do Estado 9
     * se > ou = vai para 18
     * se diferente de > ou diferente de = devolve e vai para 18
     */
    public int Estado9(){
      char caracter = LerCaracter();

      if(caracter == Util.maior || Util.igual)){
          lexema += caracter;
          MostrarTransicao(caracter, 9, 18);
          return 18;
      } else if(Util.EhCaracterValido(caracter)) {
          MostrarTransicao(caracter, 9, 18);
          devolve = true;
          return 18;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 10
     * se = vai para 18
     * se diferente de = devolve e vai para 18
     */
    public int Estado10(){
      char caracter = LerCaracter();

      if(Util.igual)){
          lexema += caracter;
          MostrarTransicao(caracter, 10, 18);
          return 18;
      } else if(Util.EhCaracterValido(caracter)) {
          MostrarTransicao(caracter, 10, 18);
          devolve = true;
          return 18;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 11
     * se digito volta para 11
     * se diferente de digito devolve e vai para 18
     */
    public int Estado11(){
      char caracter = LerCaracter();

      if(Util.EhDigito(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 11, 11);
          return 11;
      } else if(Util.EhCaracterValido(caracter)) {
          MostrarTransicao(caracter, 11, 18);
          devolve = true;
          return 18;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 12
     * se digito vai para 13
     * se A...F vai para 16
     * se diferente de digito, ou diferente de A...F devolve e vai para 18
     */
    public int Estado12(){
      char caracter = LerCaracter();

      if(Util.EhDigito(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 12, 13);
          return 13;
      } else if(Util.EhHexadecimal(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 12, 16);
          return 16;
      } else if(Util.EhCaracterValido(caracter)) {
          MostrarTransicao(caracter, 12, 18);
          devolve = true;
          return 18;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 13
     * se digito vai para 14
     * se A...F vai para 17
     */
    public int Estado13(){
      char caracter = LerCaracter();

      if(Util.EhDigito(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 13, 14);
          return 14;
      } else if(Util.EhHexadecimal(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 13, 17);
          return 17;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 14
     * se 'h' vai para 18
     * se digito vai para 15
     */
    public int Estado14(){
      char caracter = LerCaracter();

      if(caracter == 'h'){
          lexema += caracter;
          MostrarTransicao(caracter, 14, 18);
          return 18;
      } else if(Util.EhDigito(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 14, 15);
          return 15;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 15
     * diferente de digito devolve e vai para 18
     */
    public int Estado15(){
      char caracter = LerCaracter();

      if(Util.EhDigito(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 15, 15);
          return 15;
      } else if(Util.EhCaracterValido(caracter)) {
          MostrarTransicao(caracter, 15, 18);
          devolve = true;
          return 18;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 16
     * se digito, ou se A...F vai para 17
     */
    public int Estado16(){
      char caracter = LerCaracter();

      if(Util.EhDigito(caracter) || Util.EhHexadecimal(caracter)){
          lexema += caracter;
          MostrarTransicao(caracter, 16, 17);
          return 17;
      }
      MostrarErro(caracter);
      return 18;
    }

    /* Mapa do Estado 17
     * se 'h' vai para 18
     */
    public int Estado17(){
      char caracter = LerCaracter();

      if(caracter == 'h'){
          lexema += caracter;
          MostrarTransicao(caracter, 17, 18);
          return 18;
      }
      MostrarErro(caracter);
      return 18;
    }

}
