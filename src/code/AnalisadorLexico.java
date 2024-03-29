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
        //System.out.println("IF");
        if(lexema.charAt(0) == '"' || lexema.charAt(0) == '\'' || Util.EhDigito(lexema.charAt(0))) {
          Simbolo simboloConst = new Simbolo(tabelaDeSimbolos.CONSTANTE, lexema, tipoConst);
          return simboloConst;
        } else {
          Simbolo simboloIdentificador = tabelaDeSimbolos.InserirIdentificador(lexema);
          return simboloIdentificador;
        }
      } else {
        //System.out.println("ELSE1:" + lexema);
        return tabelaDeSimbolos.BuscarLexema(lexema);
      }
    } else {
      //System.out.println("ELSE2");
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
        //System.out.println(">"+(int)caracter+"<");
        System.out.println("caractere invalido.");
        System.exit(0);
      }
    }

    errorCompilacao = true;
    System.exit(0);
  }

  public void MostrarTransicao(char caracter, int estadoAtual, int estadoFinal){
    if(debugMode)
    System.out.println("caracter = " + " >" + caracter + "< " + "\tvalor = "+(int)caracter +" \testado atual: "+ estadoAtual + " \tproximo estado: "+estadoFinal);
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
      lexema += caracter;
      MostrarTransicao(caracter, 0, 12);
      return 12;
    } else if(caracter == '0'){
      lexema += caracter;
      MostrarTransicao(caracter, 0, 13);
      return 13;
    } else if(Util.EhQuebraDeLinha(caracter)) {
      if(Util.barraN == caracter || Util.novalinha == caracter) {
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
    char caracter = LerCaracter();

    if(caracter == Util.asterisco){
      MostrarTransicao(caracter, 3, 3);
      return 3;
    } else if(caracter != Util.barra && caracter != Util.asterisco ) {
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
    char caracter = LerCaracter();

    if(caracter == Util.asterisco){
      MostrarTransicao(caracter, 4, 3);
      return 3;
    } else if(caracter != Util.asterisco && caracter != Util.fimDeArquivo){
      if(Util.barraN == caracter || Util.novalinha == caracter) {
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

    if(Util.EhDigito(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 13, 14);
      return 14;
    } else if(Util.EhHexadecimal(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 13, 17);
      return 17;
    } else if(Util.EhCaracterValido(caracter)) {
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
      lexema += caracter;
      MostrarTransicao(caracter, 14, 15);
      return 15;
    } else if(Util.EhHexadecimal(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 13, 18);
      return 18;
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
      lexema += caracter;
      MostrarTransicao(caracter, 15, 19);
      return 19;
    } else if(Util.EhDigito(caracter)){
      lexema += caracter;
      MostrarTransicao(caracter, 15, 16);
      return 16;
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
