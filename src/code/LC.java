/**
* Trabalho de compiladores - LC
* Arquivo contendo classes  AnalisadorLexico, AnalisadorSintatico, Simbolo, TabelaSimbolos, Util e LC
* Professor: Alexei Machado
*
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;
import java.util.*;
import java.io.BufferedWriter;
import java.util.HashMap;


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

class AnalisadorLexico{

  public BufferedReader codigo;
  public TabelaDeSimbolos tabelaDeSimbolos;
  public String lexema;
  public byte tipoConst;
  public char ultimaLetra;
  public int linha;
  public boolean errorCompilacao;
  public boolean devolve;
  public boolean fimDeArquivo;
  public boolean debugMode;
  public boolean ehDigito = false;


  /**
  * Metodo construtor analisador lexico
  * @param bufferedReader arquivo que sera lido
  * @param tabelaDeSimbolos tabela de simbolos
  */
  public AnalisadorLexico(BufferedReader bufferedReader, TabelaDeSimbolos tabelaDeSimbolos, boolean debugMode){
    this.codigo = bufferedReader;
    this.tabelaDeSimbolos = tabelaDeSimbolos;
    this.debugMode = debugMode;
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
    int estadoFinal = 19;

    while(estadoAtual != estadoFinal){
      switch (estadoAtual) {
        case 0:
        estadoAtual = Estado0();
        break;
        case 1:
        estadoAtual = Estado1();
        break;
        case 2:
        estadoAtual = Estado2();
        break;
        case 3:
        estadoAtual = Estado3();
        break;
        case 4:
        estadoAtual = Estado4();
        break;
        case 5:
        estadoAtual = Estado5();
        break;
        case 6:
        estadoAtual = Estado6();
        break;
        case 7:
        estadoAtual = Estado7();
        break;
        case 8:
        estadoAtual = Estado8();
        break;
        case 9:
        estadoAtual = Estado9();
        break;
        case 10:
        estadoAtual = Estado10();
        break;
        case 11:
        estadoAtual = Estado11();
        break;
        case 12:
        estadoAtual = Estado12();
        break;
        case 13:
        estadoAtual = Estado13();
        break;
        case 14:
        estadoAtual = Estado14();
        break;
        case 15:
        estadoAtual = Estado15();
        break;
        case 16:
        estadoAtual = Estado16();
        break;
        case 17:
        estadoAtual = Estado17();
        break;
        case 18:
        estadoAtual = Estado18();
        break;
        case 19:
        break;
        default:
        break;
      }
    }
    if(!fimDeArquivo) {
      if(tabelaDeSimbolos.BuscarLexema(lexema) == null) {
        if(lexema.charAt(0) == '"' || lexema.charAt(0) == '\'' || Util.EhDigito(lexema.charAt(0))) {
/*
        System.out.println("EU ENTREI AQUI " + lexema + " " +devolve + " " +ehDigito);
          if(devolve && ehDigito) {
              lexema = lexema.substring(0, lexema.length()-1);
          }*/
          Simbolo simboloConst = new Simbolo(tabelaDeSimbolos.CONSTANTE, lexema, ehDigito);
          return simboloConst;
        } else {
          Simbolo simboloIdentificador = tabelaDeSimbolos.InserirIdentificador(lexema);
          return simboloIdentificador;
        }
      } else {
        return tabelaDeSimbolos.BuscarLexema(lexema);
      }
    } else {
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
      System.exit(0);
    }
    return ultimaLetra;
  }

  /**
  * Metodo para mostrar erros durante a execucao do analisador lexico
  * @param char caracter que gerou o erro
  */
  public void MostrarErro(char caracter) {
    //System.out.println("C: " + caracter);
    //System.out.println("L: " + lexema);

    if(lexema == ""){
      lexema += caracter;
    }
    if(Util.EhCaracterValido(caracter)) {
      System.out.println(linha);
      System.out.println("lexema nao identificado " + '[' + lexema + "].");
      System.exit(0);
    }else{
      if(Util.fimDeArquivo == caracter) {
        System.out.println(linha);
        System.out.println("fim de arquivo nao esperado.");
        System.exit(0);
      }else {
        System.out.println(linha);
        System.out.println("caractere invalido.");
        System.exit(0);
      }
    }

    errorCompilacao = true;
    System.exit(0);
  }

  public void MostrarTransicao(char caracter, int estadoAtual, int estadoFinal){
    if(debugMode)
    System.out.println("LEXICO: caracter = " + " >" + caracter + "< " + "\tvalor = "+(int)caracter +" \testado atual: "+ estadoAtual + " \tproximo estado: "+estadoFinal);
  }

  /* Mapa do Estado 0
  * se espaco volta para 0
  * se '.' , ',', ';', '(', ')', '[', ']', '{', '}', '+', '-', '%', '@', '!', '?', '=', '*' vai para 19
  * se sublinhado vai para 1
  * se letra vai para 2
  * se / vai para 5
  * se aspas vai para 6
  * se apostofro vai para 7
  * se : vai para 9
  * se < vai para 10
  * se > vai para 11
  * se 1...9 vai para 12
  * se 0 vai para 13
  */
  public int Estado0() {
    lexema = "";
    char caracter = LerCaracter();
    ehDigito = false;
    if(caracter == Util.espaco || caracter == Util.cursorInicio){
      MostrarTransicao(caracter, 0, 0);
      return 0;
    } else if (Util.EhTransicaoDireta(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 0, 19);
      return 19;
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
      MostrarTransicao(caracter, 0, 5);
      return 5;
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
      MostrarTransicao(caracter, 0, 9);
      return 9;
    } else if(caracter == Util.menor){
      lexema += caracter;
      MostrarTransicao(caracter, 0, 10);
      return 10;
    } else if(caracter == Util.maior){
      lexema += caracter;
      MostrarTransicao(caracter, 0, 11);
      return 11;
    } else if(caracter != '0' && Util.EhDigito(caracter)){
      ehDigito = true;
      lexema += caracter;
      MostrarTransicao(caracter, 0, 12);
      return 12;
    } else if(caracter == '0'){
      ehDigito = true;
      lexema += caracter;
      MostrarTransicao(caracter, 0, 13);
      return 13;
    } else if(Util.EhQuebraDeLinha(caracter)) {
      if(10 == (int)caracter) {
        linha++;
      }
      return 0;
    } else if(Util.fimDeArquivo == caracter){
      if(Util.barraN == ultimaLetra || Util.novalinha == ultimaLetra){
        linha--;
      }
      fimDeArquivo = true;
      return 19;
    }

    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 1
  * se sublinhado volta para 1
  * se letra ou digito vai para 2
  */
  public int Estado1() {
    ehDigito = false;
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
    System.out.println(caracter);

    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 2
  * se letra, digito ou sublinhado volta para 2
  * diferente de letra, digito ou sublinhado devolve e vai para 19
  */
  public int Estado2() {
    ehDigito = false;
    char caracter = LerCaracter();

    if(caracter == Util.sublinhado || Util.EhLetra(caracter) || Util.EhDigito(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 2, 2);
      return 2;
    } else if(Util.EhCaracterValido(caracter)) {
      MostrarTransicao(caracter, 2, 19);
      devolve = true;
      return 19;
    }

    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 3
  * se * volta para 3
  * diferente de * ou diferente de / vai para 4
  * se / volta para 0
  */
  public int Estado3() {
    ehDigito = false;
    char caracter = LerCaracter();

    if(caracter == Util.asterisco){
      MostrarTransicao(caracter, 3, 3);
      return 3;
    } else if(caracter != Util.barra && caracter != Util.asterisco && Util.EhCaracterValido(caracter)) {
      MostrarTransicao(caracter, 3, 4);
      return 4;
    } else if(caracter == Util.barra) {
      MostrarTransicao(caracter, 3, 0);
      return 0;
    }

    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 4
  * se * vai para 3
  * diferente de * volta para 4
  */
  public int Estado4() {
    ehDigito = false;
    char caracter = LerCaracter();

    if(caracter == Util.asterisco){
      MostrarTransicao(caracter, 4, 3);
      return 3;
    } else if(caracter != Util.asterisco && caracter != Util.fimDeArquivo && Util.EhCaracterValido(caracter)){
      if(10 == (int)caracter) {
          linha++;
      }
      MostrarTransicao(caracter, 4, 4);
      return 4;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 5
  * se * vai para 4
  * diferente de * devolve e vai para 19
  */
  public int Estado5() {
    ehDigito = false;
    char caracter = LerCaracter();
    if(caracter == Util.asterisco){
      MostrarTransicao(caracter, 5, 4);
      return 4;
    } else if(caracter != Util.asterisco){
      MostrarTransicao(caracter, 5, 19);
      devolve = true;
      return 19;
    }

    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 6
  * se letra, digito, diferente de \n ou diferente de $ volta para 6
  * se aspas vai para 19
  */
  public int Estado6() {
    ehDigito = false;
    char caracter = LerCaracter();
    if (caracter == Util.aspas) {
      MostrarTransicao(caracter, 6, 19);
      return 19;
    } else if(Util.EhLetra(caracter) || Util.EhDigito(caracter) || Util.EhCaracterEspecial(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 6, 6);
      return 6;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 7
  * se letra, digito, aspas, \n ou $ vai para 8
  */
  public int Estado7(){
    ehDigito = false;
    char caracter = LerCaracter();

    if(Util.EhLetra(caracter) || Util.EhDigito(caracter) || caracter == Util.aspas || caracter == Util.barraN || caracter == Util.cifrao){
      lexema += caracter;
      MostrarTransicao(caracter, 7, 8);
      return 8;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 8
  * se ''' vai para 19
  */
  public int Estado8(){
    ehDigito = false;
    char caracter = LerCaracter();
    if (caracter == Util.apostofro) {
      MostrarTransicao(caracter, 8, 19);
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 9
  * se = vai para 19
  * se diferente de = devolve e vai para 19
  */
  public int Estado9(){
    ehDigito = false;
    char caracter = LerCaracter();

    if(caracter == Util.igual){
      lexema += caracter;
      MostrarTransicao(caracter, 9, 19);
      return 19;
    } else if(Util.EhCaracterValido(caracter)) {
      MostrarTransicao(caracter, 9, 19);
      devolve = true;
      return 19;
    }
    MostrarErro(caracter);
    return 19;

  }

  /* Mapa do Estado 10
  * se > ou = vai para 19
  * se diferente de > ou diferente de = devolve e vai para 19
  */
  public int Estado10(){
    ehDigito = false;
    char caracter = LerCaracter();

    if(caracter == Util.maior || caracter == Util.igual){
      lexema += caracter;
      MostrarTransicao(caracter, 10, 19);
      return 19;
    }  else if(Util.EhCaracterValido(caracter)) {
      MostrarTransicao(caracter, 10, 19);
      devolve = true;
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 11
  * se = vai para 19
  * se diferente de = devolve e vai para 19
  */
  public int Estado11(){
    ehDigito = false;
    char caracter = LerCaracter();

    if(caracter == Util.igual){
      lexema += caracter;
      MostrarTransicao(caracter, 11, 19);
      return 19;
    } else if(Util.EhCaracterValido(caracter)) {
      MostrarTransicao(caracter, 11, 19);
      devolve = true;
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 12
  * se digito volta para 12
  * se diferente de digito devolve e vai para 19
  */
  public int Estado12(){
    char caracter = LerCaracter();

    if(Util.EhDigito(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 12, 12);
      return 12;
    } else if(Util.EhCaracterValido(caracter)) {
      MostrarTransicao(caracter, 12, 19);
      devolve = true;
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 13
  * se digito vai para 14
  * se A...F vai para 17
  * se diferente de digito, ou diferente de A...F devolve e vai para 19
  */
  public int Estado13(){
    char caracter = LerCaracter();
    //System.out.println("___________________" + caracter);
    if(Util.EhDigito(caracter)){
      ehDigito = true;
      lexema += caracter;
      MostrarTransicao(caracter, 13, 14);
      return 14;
    } else if(Util.EhHexadecimal(caracter)){
      ehDigito = false;
      lexema += caracter;
      MostrarTransicao(caracter, 13, 17);
      return 17;
    } else if(!Util.EhDigito(caracter) && !Util.EhHexadecimal(caracter) && caracter != 'h') {
      MostrarTransicao(caracter, 13, 19);
      devolve = true;
      return 19;
    }

    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 14
  * se digito vai para 15
  * se A...F vai para 18
  */
  public int Estado14(){

    char caracter = LerCaracter();

    if(Util.EhDigito(caracter)){
      ehDigito = true;
      lexema += caracter;
      MostrarTransicao(caracter, 14, 15);
      return 15;
    } else if(Util.EhHexadecimal(caracter)){
      ehDigito = false;
      lexema += caracter;
      MostrarTransicao(caracter, 13, 18);
      return 18;
    } else if(!Util.EhDigito(caracter) && !Util.EhHexadecimal(caracter)) {
      MostrarTransicao(caracter, 13, 19);
      devolve = true;
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 15
  * se 'h' vai para 19
  * se digito vai para 16
  */
  public int Estado15(){
    char caracter = LerCaracter();

    if(caracter == 'h'){
      ehDigito = false;
      lexema += caracter;
      MostrarTransicao(caracter, 15, 19);
      if (caracter != 'h'){
        devolve = true;
        System.out.println("PASSO AQUI NO 15");
      }
      return 19;
    } else if(Util.EhDigito(caracter)){
      ehDigito = true;
      lexema += caracter;
      MostrarTransicao(caracter, 15, 16);
      return 16;
    } else if (Util.EhCaracterValido(caracter)){
      MostrarTransicao(caracter, 15, 19);
      devolve = true;
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 16
  * diferente de digito devolve e vai para 19
  */
  public int Estado16(){
    char caracter = LerCaracter();

    if(Util.EhDigito(caracter)){
      ehDigito = true;
      lexema += caracter;
      MostrarTransicao(caracter, 16, 16);
      return 16;
    } else if(Util.EhCaracterValido(caracter)) {
      MostrarTransicao(caracter, 16, 19);
      devolve = true;
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 17
  * se digito, ou se A...F vai para 18
  */
  public int Estado17(){
    char caracter = LerCaracter();

    if(Util.EhDigito(caracter) || Util.EhHexadecimal(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 17, 18);
      return 18;
    }
    MostrarErro(caracter);
    return 19;
  }

  /* Mapa do Estado 18
  * se 'h' vai para 19
  */
  public int Estado18(){
    char caracter = LerCaracter();

    if(caracter == 'h'){
      lexema += caracter;
      MostrarTransicao(caracter, 18, 19);
      return 19;
    }
    MostrarErro(caracter);
    return 19;
  }

}


class AnalisadorSintatico {
  AnalisadorLexico analisadorlexico;
  TabelaDeSimbolos tabelasimbolos;
  Simbolo simbolo;
  Simbolo novoSimbolo = new Simbolo();
  Simbolo simboloAnterior = new Simbolo();
  Simbolo simboloAnteriorComando = new Simbolo();
  BufferedReader file;
  public boolean debugMode;
  public boolean segundoBloco = false;
  public boolean ehComp = false;

  //Simbolo simboloPrimeiroComp = new Simbolo();
  Simbolo simboloSegundoComp = new Simbolo();


  /**
  * Construtor da classe
  *
  *
  */
  public AnalisadorSintatico(BufferedReader file, boolean debugMode) {
    this.file = file;
    this.tabelasimbolos = new TabelaDeSimbolos();
    this.analisadorlexico = new AnalisadorLexico(this.file, this.tabelasimbolos, debugMode);
    this.simbolo = analisadorlexico.maquinaDeEstados();
    this.debugMode = debugMode;
  }

  /**
  *Metodo casa token recebe o que se espera e compara com o que chegou. Pode dar errado, printa msg
  *
  */
  public void CasaToken(byte tokenesperado) {
    if(debugMode){
      System.out.println("esperado: " + tokenesperado + " \trecebido: " + this.simbolo.token + " " + this.simbolo.lexema);
    }
    if (this.simbolo.token == tokenesperado) {
      this.simbolo = analisadorlexico.maquinaDeEstados();
    } else {

      if (analisadorlexico.fimDeArquivo) {
        System.out.println(analisadorlexico.linha + "\nfim de arquivo nao esperado.");
        System.exit(0);
      } else {
        System.out.println(analisadorlexico.linha + "\ntoken nao esperado [" + this.simbolo.lexema + "].");
        //tabelasimbolos.MostrarTabela();
        System.exit(0);
      }
    }
  }

  public void MostrarTransicao(Simbolo simbolo, String estadoAtual, String estadoFinal){
    if(debugMode)
    System.out.println("SINTATICO Token: "+ this.simbolo.token + " \tLexema: " + this.simbolo.lexema + " \testado atual: " + estadoAtual + " \tproximo estado: "+estadoFinal);
  }


  /**
  * S -> {D} main "{" {C} "}"
  */
  public void S() {
    if (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR ||
        this.simbolo.token == this.tabelasimbolos.FINAL || this.simbolo.token == this.tabelasimbolos.BOOLEAN) {
      while (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR ||
             this.simbolo.token == this.tabelasimbolos.FINAL || this.simbolo.token == this.tabelasimbolos.BOOLEAN) {
        MostrarTransicao(this.simbolo, "S", "D");
        D();
      }
    }
    if (this.simbolo.token == this.tabelasimbolos.MAIN){
      CasaToken(this.tabelasimbolos.MAIN);
      CasaToken(this.tabelasimbolos.CHAVES_ABERTA);
      if (this.simbolo.token == this.tabelasimbolos.IDENTIFICADOR || this.simbolo.token == this.tabelasimbolos.FOR ||
      this.simbolo.token == this.tabelasimbolos.IF || this.simbolo.token == this.tabelasimbolos.READLN ||
      this.simbolo.token == this.tabelasimbolos.WRITE || this.simbolo.token == this.tabelasimbolos.WRITELN ||
      this.simbolo.token == this.tabelasimbolos.PONTO_VIRGULA) {

        while (this.simbolo.token == this.tabelasimbolos.IDENTIFICADOR || this.simbolo.token == this.tabelasimbolos.FOR ||
        this.simbolo.token == this.tabelasimbolos.IF || this.simbolo.token == this.tabelasimbolos.READLN ||
        this.simbolo.token == this.tabelasimbolos.WRITE || this.simbolo.token == this.tabelasimbolos.WRITELN ||
        this.simbolo.token == this.tabelasimbolos.PONTO_VIRGULA) {
          MostrarTransicao(this.simbolo, "S", "C");
          C();
        }
      }
      CasaToken(this.tabelasimbolos.CHAVES_FECHADA);
    }

    if (analisadorlexico.fimDeArquivo == false) {
      System.out.println(analisadorlexico.linha + "\ntoken nao esperado [" + this.simbolo.lexema + "].");
      System.exit(0);
    }
    System.out.println((analisadorlexico.linha)+" linhas compiladas.");
    System.exit(0);
  }

  /*
  * D -> T X ; | final id = V;
  * D -> T (1) X ; | final (6) id = V (7);
  */
  public void D() {
    if (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR ||
        this.simbolo.token == this.tabelasimbolos.BOOLEAN) {
      MostrarTransicao(this.simbolo, "D", "T");
      novoSimbolo.classe = 1; // eh uma variavel
      T(novoSimbolo);
      //CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
    } else if(this.simbolo.token == this.tabelasimbolos.FINAL) {

      CasaToken(this.tabelasimbolos.FINAL);

      //caso já tenha tipo, o identifcador ja foi declarado
      if(this.simbolo.tipo != 0){
        System.out.println(this.analisadorlexico.linha + "\nidentificador ja declarado [" + this.simbolo.lexema + "].");
        System.exit(0);
      } else {
          //caso nao criamos um novo
          this.simbolo.classe = 2; // eh uma constante
          simboloAnterior = this.simbolo;

          CasaToken(this.tabelasimbolos.IDENTIFICADOR);
          CasaToken(this.tabelasimbolos.IGUAL);
          MostrarTransicao(this.simbolo, "D", "V");
          V(novoSimbolo);
          simboloAnterior.tipo = novoSimbolo.tipo; // recebe o tipo do valor atribuido
          tabelasimbolos.AlterarTipoLexema(simboloAnterior.lexema, simboloAnterior);
          CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
        }
      } else {
            if (analisadorlexico.fimDeArquivo) {
                System.out.println(analisadorlexico.linha + "\nfim de arquivo nao esperado.");
                System.exit(0);
            } else {
                System.out.println(analisadorlexico.linha + "\ntoken nao esperado [" + this.simbolo.lexema + "].");
                System.exit(0);
            }
        }
  }

  /*
  * T -> int | boolean | char
  */
  public void T(Simbolo novoSimbolo) {
    if (this.simbolo.token == this.tabelasimbolos.INT){
      CasaToken(this.tabelasimbolos.INT);
      MostrarTransicao(this.simbolo, "T", "X");
      novoSimbolo.tipo = 2; //TIPO INT
      X(novoSimbolo);
    } else if (this.simbolo.token == this.tabelasimbolos.CHAR){
      CasaToken(this.tabelasimbolos.CHAR);
      MostrarTransicao(this.simbolo, "T", "X");
      novoSimbolo.tipo = 3; //TIPO CHAR
      X(novoSimbolo);
    } else if (this.simbolo.token == this.tabelasimbolos.BOOLEAN){
      CasaToken(this.tabelasimbolos.BOOLEAN);
      MostrarTransicao(this.simbolo, "T", "X");
      novoSimbolo.tipo = 1; //TIPO BOOLEAN
      X(novoSimbolo);
    } else {
      if (analisadorlexico.fimDeArquivo) {
          System.out.println(analisadorlexico.linha + "\nfim de arquivo nao esperado.");
          System.exit(0);
      } else {
          System.out.println(analisadorlexico.linha + "\ntoken nao esperado [" + this.simbolo.lexema + "].");
          System.exit(0);
      }
    }
  }

  /*
  * X -> { id ([ := V ] | "["constante"]" ) [,] }+
  * X -> { (2) id ([ :=  V (4)] | "[" (5) constante"]" ) [,] }+
  */
  public void X(Simbolo novoSimbolo) {
    while(this.simbolo.token == this.tabelasimbolos.IDENTIFICADOR){
      //caso já tenha tipo, o identifcador ja foi declarado
      if(this.simbolo.tipo != 0){
        System.out.println(this.analisadorlexico.linha + "\nidentificador ja declarado [" + this.simbolo.lexema + "].");
        System.exit(0);
      } else {
            //caso nao criamos um novo
            this.simbolo.tipo = novoSimbolo.tipo;
            this.simbolo.classe = novoSimbolo.classe;
            simboloAnterior = this.simbolo;
            CasaToken(this.tabelasimbolos.IDENTIFICADOR);
            if(this.simbolo.token == this.tabelasimbolos.DOIS_PONTOS_IGUAL){
              CasaToken(this.tabelasimbolos.DOIS_PONTOS_IGUAL);
              MostrarTransicao(this.simbolo, "X", "V");
              V(novoSimbolo);
              if(simboloAnterior.tipo != novoSimbolo.tipo){
                    System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
                    System.exit(0);
              }

            } else if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO) {
              CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
              this.simbolo.tamanho = Integer.valueOf(this.simbolo.lexema);
              simboloAnterior.tamanho = this.simbolo.tamanho;
              tabelasimbolos.AlterarTipoLexema(simboloAnterior.lexema, simboloAnterior);
              //verifica se o valor do vetor ultrapassa o permitido
              if(this.simbolo.tamanho > Util.tamanhoMaxVetor){
                  System.out.println(this.analisadorlexico.linha + "\ntamanho do vetor excede o maximo permitido.");
                  System.exit(0);
              }
              CasaToken(this.tabelasimbolos.CONSTANTE);
              CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
            }
            if(this.simbolo.token == this.tabelasimbolos.PONTO_VIRGULA) {
              CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
            } else {//if(this.simbolo.token == this.tabelasimbolos.VIRGULA){
              CasaToken(this.tabelasimbolos.VIRGULA);
              MostrarTransicao(this.simbolo, "X", "X");
              X(novoSimbolo);
            }
        }
    }
  }

  /*
  * V -> [ + | - ] (3) constante | true | false
  */
  public void V(Simbolo novoSimbolo) {
    if (this.simbolo.token == this.tabelasimbolos.MAIS) {
      //EH UM INT
      CasaToken(this.tabelasimbolos.MAIS);
      novoSimbolo.tipo = 2; //TIPO INT
      novoSimbolo.lexema = this.simbolo.lexema;
      CasaToken(this.tabelasimbolos.CONSTANTE);
    } else if (this.simbolo.token == this.tabelasimbolos.MENOS) {
      //EH UM INT
      CasaToken(this.tabelasimbolos.MENOS);
      novoSimbolo.tipo = 2; //TIPO INT
      novoSimbolo.lexema = this.simbolo.lexema;
      CasaToken(this.tabelasimbolos.CONSTANTE);
    } else if (this.simbolo.token == this.tabelasimbolos.TRUE)  {
      // verifica se o tipo do valor eh igual ao tipo declarado
      novoSimbolo.tipo = 1; //TIPO BOOLEAN
      novoSimbolo.lexema = this.simbolo.lexema;
      CasaToken(this.tabelasimbolos.TRUE);
    } else if (this.simbolo.token == this.tabelasimbolos.FALSE)  {
      // verifica se o tipo do valor eh igual ao tipo declarado
      novoSimbolo.tipo = 1; //TIPO BOOLEAN
      novoSimbolo.lexema = this.simbolo.lexema;
      CasaToken(this.tabelasimbolos.FALSE);
    } else {
      // OU INT OU CHAR
      /*
      if(this.simbolo.lexema.contains("$") || Util.validarString(this.simbolo.lexema)){
        novoSimbolo.tipo = 3; //EH CHAR
        novoSimbolo.lexema = this.simbolo.lexema;
      } else {
        novoSimbolo.tipo = 2;  // EH INT
        novoSimbolo.lexema = this.simbolo.lexema;
      }*/
      if(this.simbolo.ehDigito){
        novoSimbolo.tipo = 2;  // EH INT
        novoSimbolo.lexema = this.simbolo.lexema;
      } else {
        novoSimbolo.tipo = 3;  //EH CHAR
        novoSimbolo.lexema = this.simbolo.lexema;
      }
      //System.out.println(this.simbolo.ehDigito + " " + this.simbolo.lexema);
      CasaToken(this.tabelasimbolos.CONSTANTE);

    }
  }

  /**
  * C ->  id [ "["Exp"]" ] := Exp; |
  *       for "(" {J}; Exp; {J}; ")" ( C | "{" {C}+ "}" ) |
  *       if "(" Exp ")" then (C |"{"{C}"}") [else (C |"{"{C}"}")] |
  *       readln "(" id [ "[" Exp "]" ] ")"; |
  *       write "(" {A}+ ")" ; |
  *       writeln "(" {A}+ ")" ; |
  *       ;
  */

  public void C() {
    if(this.simbolo.token == this.tabelasimbolos.IDENTIFICADOR){
      if(this.simbolo.tipo == 0){
        System.out.println(this.analisadorlexico.linha + "\nidentificador nao declarado [" + this.simbolo.lexema + "].");
        System.exit(0);
      } else {
        //MUDEI AQUI
        simboloAnteriorComando = this.simbolo;
        Simbolo identificador = this.tabelasimbolos.BuscarLexema(this.simbolo.lexema);
        CasaToken(this.tabelasimbolos.IDENTIFICADOR);
        if(simboloAnteriorComando.tamanho == 0 && this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
          System.out.println(this.analisadorlexico.linha + "\nclasse de identificador incompativel [" + simboloAnteriorComando.lexema + "].");
          System.exit(0);
        } else if(simboloAnteriorComando.tamanho != 0 && this.simbolo.token != this.tabelasimbolos.COLCHETE_ABERTO){
          System.out.println(this.analisadorlexico.linha + "\nclasse de identificador incompativel [" + simboloAnteriorComando.lexema + "].");
          System.exit(0);
        } else {
          if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
              CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
              this.simbolo.tamanho = Integer.valueOf(this.simbolo.lexema);
              if(simboloAnteriorComando.tamanho <= this.simbolo.tamanho){
                  System.out.println(this.analisadorlexico.linha + "\ntamanho do vetor excede o maximo permitido.");
                  System.exit(0);
              }
              Exp();
              //verifica se o valor do vetor ultrapassa o permitido
              if(this.simbolo.tamanho > Util.tamanhoMaxVetor){
                  System.out.println(this.analisadorlexico.linha + "\ntamanho do vetor excede o maximo permitido.");
                  System.exit(0);
              }
              CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
        }

        CasaToken(this.tabelasimbolos.DOIS_PONTOS_IGUAL);
        Exp();
        if(simboloAnteriorComando.classe == 2){ //eh constante não pode alterar valor
          System.out.println(this.analisadorlexico.linha + "\nclasse de identificador incompativel [" + simboloAnteriorComando.lexema + "].");
          System.exit(0);
        } else if(simboloAnteriorComando.tipo != novoSimbolo.tipo){
          System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
          System.exit(0);
        }
        if(!this.segundoBloco){
          CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
          this.segundoBloco = false;
        }
      }

      }
    } else if(this.simbolo.token == this.tabelasimbolos.FOR){
      CasaToken(this.tabelasimbolos.FOR);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      MostrarTransicao(this.simbolo, "C", "J");
      J();
      //CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
      Exp();
      CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
      MostrarTransicao(this.simbolo, "C", "J");
      this.segundoBloco = true;
      J();
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
      if(this.simbolo.token == this.tabelasimbolos.CHAVES_ABERTA){
        CasaToken(this.tabelasimbolos.CHAVES_ABERTA);
        while (this.simbolo.token != this.tabelasimbolos.CHAVES_FECHADA){
          MostrarTransicao(this.simbolo, "C", "C");
          C();
        }
        CasaToken(this.tabelasimbolos.CHAVES_FECHADA);
      } else {
        MostrarTransicao(this.simbolo, "C", "C");
        C();
      }

    } else if(this.simbolo.token == this.tabelasimbolos.IF){
      CasaToken(this.tabelasimbolos.IF);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      Exp();
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
      CasaToken(this.tabelasimbolos.THEN);
      if(this.simbolo.token == this.tabelasimbolos.CHAVES_ABERTA){
        CasaToken(this.tabelasimbolos.CHAVES_ABERTA);
        while (this.simbolo.token != this.tabelasimbolos.CHAVES_FECHADA){
          MostrarTransicao(this.simbolo, "C", "C");
          C();
        }
        CasaToken(this.tabelasimbolos.CHAVES_FECHADA);
      } else {
        MostrarTransicao(this.simbolo, "C", "C");
        C();
        CasaToken(this.tabelasimbolos.PONTO_VIRGULA);

      }
      if(this.simbolo.token == this.tabelasimbolos.ELSE){
        CasaToken(this.tabelasimbolos.ELSE);
        if(this.simbolo.token == this.tabelasimbolos.CHAVES_ABERTA){
          CasaToken(this.tabelasimbolos.CHAVES_ABERTA);
          while (this.simbolo.token != this.tabelasimbolos.CHAVES_FECHADA){
            MostrarTransicao(this.simbolo, "C", "C");
            C();
          }
          CasaToken(this.tabelasimbolos.CHAVES_FECHADA);
        } else {
          MostrarTransicao(this.simbolo, "C", "C");
          C();
        }
      }

    } else if(this.simbolo.token == this.tabelasimbolos.READLN){
      CasaToken(this.tabelasimbolos.READLN);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      if(this.simbolo.tipo == 0){
        System.out.println(this.analisadorlexico.linha + "\nidentificador nao declarado [" + this.simbolo.lexema + "].");
        System.exit(0);
      } else {
        Simbolo identificador = this.tabelasimbolos.BuscarLexema(this.simbolo.lexema);
        novoSimbolo.tipo = identificador.tipo;
        novoSimbolo.classe = identificador.classe;
        CasaToken(this.tabelasimbolos.IDENTIFICADOR);
            if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
              CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
              //this.simbolo.tamanho = Integer.valueOf(this.simbolo.lexema);
              Exp();
              //verifica se o valor do vetor ultrapassa o permitido
              //if(this.simbolo.tamanho > Util.tamanhoMaxVetor){
              //    System.out.println(this.analisadorlexico.linha + "\ntamanho do vetor excede o maximo permitido.");
              //    System.exit(0);
              //}
              CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
            }
            CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
            if(!this.segundoBloco){
              CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
              this.segundoBloco = false;
            }
      }
    } else if(this.simbolo.token == this.tabelasimbolos.WRITE){
      CasaToken(this.tabelasimbolos.WRITE);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      MostrarTransicao(this.simbolo, "C", "A");
      A();
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
      if(!this.segundoBloco){
        CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
        this.segundoBloco = false;
      }

    } else if(this.simbolo.token == this.tabelasimbolos.WRITELN){
      CasaToken(this.tabelasimbolos.WRITELN);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      MostrarTransicao(this.simbolo, "C", "A");
      A();
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
      if(!this.segundoBloco){
        CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
        this.segundoBloco = false;
      }

    } else if(this.simbolo.token == this.tabelasimbolos.PONTO_VIRGULA){
      CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
    } else {
            if (analisadorlexico.fimDeArquivo) {
                System.out.println(analisadorlexico.linha + "\nfim de arquivo nao esperado.");
                System.exit(0);
            } else {
                System.out.println(analisadorlexico.linha + "\ntoken nao esperado [" + this.simbolo.lexema + "].");
                System.exit(0);
            }
        }
  }



  /**
  * J -> C [,]
  */
  public void J() {
    MostrarTransicao(this.simbolo, "J", "C");
    C();
    if(this.simbolo.token == this.tabelasimbolos.VIRGULA){
      CasaToken(this.tabelasimbolos.VIRGULA);
      MostrarTransicao(this.simbolo, "J", "J");
      J();
    }
  }

  /**
  *A -> Exp [,]
  */
  public void A() {
    Exp();
    if(this.simbolo.token == this.tabelasimbolos.VIRGULA){
      CasaToken(this.tabelasimbolos.VIRGULA);
      MostrarTransicao(this.simbolo, "A", "A");
      A();
    }
  }

  /**
  * Exp -> ExpS [{ = | <> | < | > | <= | >= } ExpS]
  */
  public void Exp() {
    MostrarTransicao(this.simbolo, "Exp", "ExpS");
    Simbolo simboloPrimeiroComp = new Simbolo();
    simboloPrimeiroComp.lexema = this.simbolo.lexema;
    simboloPrimeiroComp.tipo = this.simbolo.tipo;

    ExpS();
    if(this.simbolo.token == this.tabelasimbolos.DOIS_PONTOS_IGUAL){
      CasaToken(this.tabelasimbolos.DOIS_PONTOS_IGUAL);
      ehComp = true;
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
      if(simboloPrimeiroComp.tipo != simboloSegundoComp.tipo){
        System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
        System.exit(0);
      }
    } else if(this.simbolo.token == this.tabelasimbolos.DIFERENTE){
      CasaToken(this.tabelasimbolos.DIFERENTE);
      ehComp = true;
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
      if(simboloPrimeiroComp.tipo != simboloSegundoComp.tipo){
        System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
        System.exit(0);
      }
    } else if(this.simbolo.token == this.tabelasimbolos.MENOR){
      CasaToken(this.tabelasimbolos.MENOR);
      ehComp = true;
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
      if(simboloPrimeiroComp.tipo != simboloSegundoComp.tipo){
        System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
        System.exit(0);
      }
    } else if(this.simbolo.token == this.tabelasimbolos.MAIOR){
      CasaToken(this.tabelasimbolos.MAIOR);
      ehComp = true;
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
      if(simboloPrimeiroComp.tipo != simboloSegundoComp.tipo){
        System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
        System.exit(0);
      }
    } else if(this.simbolo.token == this.tabelasimbolos.MENOR_IGUAL){
      CasaToken(this.tabelasimbolos.MENOR_IGUAL);
      ehComp = true;
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
      if(simboloPrimeiroComp.tipo != simboloSegundoComp.tipo){
        System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
        System.exit(0);
      }

    } else if(this.simbolo.token == this.tabelasimbolos.MAIOR_IGUAL){
      CasaToken(this.tabelasimbolos.MAIOR_IGUAL);
      ehComp = true;
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
      if(simboloPrimeiroComp.tipo != simboloSegundoComp.tipo){
        System.out.println(this.analisadorlexico.linha + "\ntipos incompativeis.");
        System.exit(0);
      }
    }
  }

  /**
  * ExpS -> [ + | - ] Termo {(+ | - | or) Termo}
  */
  public void ExpS() {
    if (this.simbolo.token == this.tabelasimbolos.MAIS) {
      CasaToken(this.tabelasimbolos.MAIS);
    } else if (this.simbolo.token == this.tabelasimbolos.MENOS) {
      CasaToken(this.tabelasimbolos.MENOS);
    }
    MostrarTransicao(this.simbolo, "ExpS", "Termo");
    Termo();
    while (this.simbolo.token == this.tabelasimbolos.MAIS ||
    this.simbolo.token == this.tabelasimbolos.MENOS ||
    this.simbolo.token == this.tabelasimbolos.OR) {
      if (this.simbolo.token == this.tabelasimbolos.MAIS) {
        CasaToken(this.tabelasimbolos.MAIS);
        MostrarTransicao(this.simbolo, "ExpS", "Termo");
        Termo();
      } else if (this.simbolo.token == this.tabelasimbolos.MENOS) {
        CasaToken(this.tabelasimbolos.MENOS);
        MostrarTransicao(this.simbolo, "ExpS", "Termo");
        Termo();
      } else if (this.simbolo.token == this.tabelasimbolos.OR) {
        CasaToken(this.tabelasimbolos.OR);
        MostrarTransicao(this.simbolo, "ExpS", "Termo");
        Termo();
      }
    }
  }

  /**
  *Termo -> F {( * | / | % | and ) F}
  */
  public void Termo() {
    MostrarTransicao(this.simbolo, "Termo", "F");
    F();
    if(this.simbolo.token == this.tabelasimbolos.ASTERISCO){
      CasaToken(this.tabelasimbolos.ASTERISCO);
      MostrarTransicao(this.simbolo, "Termo", "F");
      F();
    } else if(this.simbolo.token == this.tabelasimbolos.BARRA){
      CasaToken(this.tabelasimbolos.BARRA);
      MostrarTransicao(this.simbolo, "Termo", "F");
      F();
    } else if(this.simbolo.token == this.tabelasimbolos.PORCENTAGEM){
      CasaToken(this.tabelasimbolos.PORCENTAGEM);
      MostrarTransicao(this.simbolo, "Termo", "F");
      F();
    } else if(this.simbolo.token == this.tabelasimbolos.AND){
      CasaToken(this.tabelasimbolos.AND);
      MostrarTransicao(this.simbolo, "Termo", "F");
      F();
    }
  }

  /**
  * F -> not F | "(" Exp ")" | constante | id ["["Exp"]"]
  */
  public void F() {
    if(this.simbolo.token == this.tabelasimbolos.NOT){
      CasaToken(this.tabelasimbolos.NOT);
      MostrarTransicao(this.simbolo, "F", "F");
      F();
    } else if(this.simbolo.token == this.tabelasimbolos.PARENTESES_ABERTO){
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      MostrarTransicao(this.simbolo, "F", "Exp");
      Exp();
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
    } else if(this.simbolo.token == this.tabelasimbolos.CONSTANTE){
      if(this.simbolo.ehDigito){
        novoSimbolo.tipo = 2;  // EH INT
        novoSimbolo.lexema = this.simbolo.lexema;
      } else {
        novoSimbolo.tipo = 3;  //EH CHAR
        novoSimbolo.lexema = this.simbolo.lexema;
      }
      if(ehComp){
        simboloSegundoComp = novoSimbolo;
        ehComp = false;
      }
      CasaToken(this.tabelasimbolos.CONSTANTE);
    } else if (this.simbolo.token == this.tabelasimbolos.TRUE)  {
      this.simbolo.tipo = 1; //EH BOOLEAN
      novoSimbolo.tipo = 1;
      CasaToken(this.tabelasimbolos.TRUE);
    } else if (this.simbolo.token == this.tabelasimbolos.FALSE)  {
      this.simbolo.tipo = 1; //EH BOOLEAN
      novoSimbolo.tipo = 1;
      CasaToken(this.tabelasimbolos.FALSE);
    } else if(this.simbolo.token == this.tabelasimbolos.IDENTIFICADOR){
      if(this.simbolo.tipo == 0) {
        System.out.println(this.analisadorlexico.linha + "\nidentificador nao declarado [" + this.simbolo.lexema + "].");
        System.exit(0);
      } else {
        novoSimbolo = this.simbolo;
/*
        if(ehComp){
          simboloSegundoComp.tipo = novoSimbolo.tipo;
          simboloSegundoComp.lexema = novoSimbolo.lexema;
          simboloSegundoComp.tamanho = novoSimbolo.tamanho;
          ehComp = false;
        }
*/
        CasaToken(this.tabelasimbolos.IDENTIFICADOR);
/*
        if(novoSimbolo.tamanho == 0 && this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
          System.out.println(this.analisadorlexico.linha + "\nclasse de identificador incompativel [" + novoSimbolo.lexema + "].");
          System.exit(0);
        } else if(novoSimbolo.tamanho != 0 && this.simbolo.token != this.tabelasimbolos.COLCHETE_ABERTO){
          //19 cai aqui
          System.out.println(this.analisadorlexico.linha + "\nclasse de identificador incompativel [" + novoSimbolo.lexema + "].");
          System.exit(0);
        } else { */
            if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
              CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
              MostrarTransicao(this.simbolo, "F", "Exp");
              //this.simbolo.tamanho = Integer.valueOf(this.simbolo.lexema);
              Exp();
              //verifica se o valor do vetor ultrapassa o permitido
              //if(this.simbolo.tamanho > Util.tamanhoMaxVetor){
              //    System.out.println(this.analisadorlexico.linha + "\ntamanho do vetor excede o maximo permitido.");
              //    System.exit(0);
              //}
              CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
            }
          //}
      }
    } else {
      if (analisadorlexico.fimDeArquivo) {
          System.out.println(analisadorlexico.linha + "\nfim de arquivo nao esperado.");
          System.exit(0);
      } else {
          System.out.println(analisadorlexico.linha + "\ntoken nao esperado [" + this.simbolo.lexema + "].");
          System.exit(0);
      }
    }
  }
}


/**
* Trabalho de compiladores - simbolo
* Professor: Alexei Machado
*
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*
* Classe simbolo
*
*/
class Simbolo{

    public byte token;
    public String lexema;
    public byte tipo;
    public byte classe;
    public int tamanho;
    public boolean ehDigito = false;


    public static final byte classeVAR = 1;
    public static final byte classeCONST = 2;

    public static final byte semTipo = 0;
    public static final byte tipoBoolean = 1;
    public static final byte tipoInt = 2;
    public static final byte tipoChar = 3;


    public Simbolo(){
    }

    public Simbolo(byte token, String lexema){
        this.token = token;
        this.lexema = lexema;
        this.tipo = 0;
        this.classe = 0;
        this.ehDigito = false;
    }

    public Simbolo(byte token, String lexema, byte tipo) {
        this.token = token;
        this.lexema = lexema;
        this.tipo = tipo;
    }

    public Simbolo(byte token, String lexema, boolean ehDigito){
        this.token = token;
        this.lexema = lexema;
        this.tipo = 0;
        this.classe = 0;
        this.ehDigito = ehDigito;
    }

    public Simbolo(byte token, String lexema, byte tipo, int tamanho) {
        this.tipo = tipo;
        this.classe = 0;
        this.ehDigito = false;
        this.token = token;
        this.lexema = lexema;
        this.tamanho = tamanho;
    }
}

/**
* Trabalho de compiladores - Tabela de Simbolos
* Professor: Alexei Machado
*
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*/

//Criacao de tabela de simbolos
class TabelaDeSimbolos{
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
    public final static byte PONTO = 39;
    public final static byte BOOLEAN = 40;

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
      tabela.put(".", new Simbolo(PONTO, "."));
      tabela.put("boolean", new Simbolo(BOOLEAN, "boolean"));

    }

    /**
     * Metodo para buscar o lexema na tabela
     * @param lexema String lexema
     * @return Simbolo
     */
    public Simbolo BuscarLexema(String lexema) {
        return tabela.get(lexema);
    }

    /**
     * Metodo para alterar o tipo do simbolo na tabela
     * @param String lexema
     * @param Simbolo simbolo
     * @return Void
     */
    public void AlterarTipoLexema(String lexema, Simbolo simbolo) {
        tabela.put(lexema, simbolo);
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
        Simbolo simbolo = new Simbolo (IDENTIFICADOR,lexema);
        tabela.put(lexema,simbolo);
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

class Util{

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
    public static int tamanhoMaxVetor = 65536;
    public static int tamanhoMaxVetorInteiro = 32768;


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

    public static boolean validarString(String lexema){
      boolean retorna = true;
        for(char letra: lexema.toCharArray()){
          retorna = EhLetra(letra) && retorna;
        }
        return retorna;
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
     * '.' , ',', ';', '(', ')', '[', ']', '{', '}', '+', '-', '%', '@', '!', '?', '=', '*'
     * @param caracter caracter que sera avaliado
     * @return boolean
     */
    public static boolean EhTransicaoDireta(char caracter) {
        return caracter == pontoFinal || caracter == virgula || caracter == pontoEVirgula || caracter == abreParenteses ||
               caracter == fechaParenteses || caracter == abreColchetes || caracter == fechaColchetes || caracter == abreChaves ||
               caracter == fechaChaves || caracter == mais || caracter == menos || caracter == porcentagem || caracter == arroba ||
               caracter == esclamacao || caracter == interrogacao || caracter == igual || caracter == asterisco || caracter == espaco;
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
        caracter == menor || caracter == maior || caracter == igual || caracter == asterisco || caracter == menos || caracter == espaco || caracter == cursorInicio;
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
        return caracter == barraN || caracter == novalinha || caracter == cursorInicio;// || caracter == espaco;
    }

    /**
     * Metodo para verificar se eh caracter valido
     * @param caracter caracter que sera avaliado
     * @return boolean
     */
    public static boolean EhCaracterValido(char caracter) {
        return ( EhLetra(caracter) || EhDigito(caracter) || EhCaracterEspecial(caracter) || EhQuebraDeLinha(caracter));
    }

}

public class LC{

    public static void main(String[] args) throws Exception{
        boolean debugMode = false;
        try{
            InputStreamReader isr = new InputStreamReader(System.in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            TabelaDeSimbolos tabelaSimbolos = new TabelaDeSimbolos();
            AnalisadorSintatico sintatico = new AnalisadorSintatico(br, debugMode);
            sintatico.S();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
