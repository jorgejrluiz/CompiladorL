import java.io.BufferedReader;

public class AnalisadorSintatico {
  AnalisadorLexico analisadorlexico;
  TabelaDeSimbolos tabelasimbolos;
  Simbolo simbolo;
  Simbolo novoSimbolo = new Simbolo();
  Simbolo simboloAnterior = new Simbolo();
  BufferedReader file;
  public boolean debugMode = false;


  /**
  * Construtor da classe
  *
  *
  */
  public AnalisadorSintatico(BufferedReader file) {
    this.file = file;
    this.tabelasimbolos = new TabelaDeSimbolos();
    this.analisadorlexico = new AnalisadorLexico(this.file, this.tabelasimbolos);
    this.simbolo = analisadorlexico.maquinaDeEstados();
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
    System.out.println("Token: "+ this.simbolo.token + " \tLexema: " + this.simbolo.lexema + " \testado atual: " + estadoAtual + " \tproximo estado: "+estadoFinal);
  }


  /**
  * S -> {D} main "{" {C} "}"
  */
  public void S() {
    if (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR ||
    this.simbolo.token == this.tabelasimbolos.FINAL) {
      while (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR ||
      this.simbolo.token == this.tabelasimbolos.FINAL) {
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
    System.out.println((analisadorlexico.linha-1)+" linhas compiladas.");
    System.exit(0);
  }

  /*
  * D -> T X ; | final id = V;
  */
  public void D() {
    if (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR) {
      MostrarTransicao(this.simbolo, "D", "T");
      T();
      CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
    } else if(this.simbolo.token == this.tabelasimbolos.FINAL) {
      CasaToken(this.tabelasimbolos.FINAL);
      CasaToken(this.tabelasimbolos.IDENTIFICADOR);
      CasaToken(this.tabelasimbolos.IGUAL);
      MostrarTransicao(this.simbolo, "D", "V");
      V();
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

  /*
  * T -> int | boolean | char
  */
  public void T() {
    if (this.simbolo.token == this.tabelasimbolos.INT){
      CasaToken(this.tabelasimbolos.INT);
      MostrarTransicao(this.simbolo, "T", "X");
      X();
    } else if (this.simbolo.token == this.tabelasimbolos.CHAR){
      CasaToken(this.tabelasimbolos.CHAR);
      MostrarTransicao(this.simbolo, "T", "X");
      X();
    }
  }

  /*
  * X -> { id ([ = V ] | "["constante"]" ) [,] }+
  */
  public void X() {
    while(this.simbolo.token == this.tabelasimbolos.IDENTIFICADOR){
      CasaToken(this.tabelasimbolos.IDENTIFICADOR);

      if(this.simbolo.token == this.tabelasimbolos.IGUAL){
        CasaToken(this.tabelasimbolos.IGUAL);
        MostrarTransicao(this.simbolo, "X", "V");
        V();
      } else if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
        CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
        CasaToken(this.tabelasimbolos.CONSTANTE);
        CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
      }
      if(this.simbolo.token == this.tabelasimbolos.VIRGULA){
        CasaToken(this.tabelasimbolos.VIRGULA);
        MostrarTransicao(this.simbolo, "X", "X");
        X();
      }
    }
  }

  /*
  * V -> [ + | - ] constante
  */
  public void V() {
    if (this.simbolo.token == this.tabelasimbolos.MAIS) {
      CasaToken(this.tabelasimbolos.MAIS);
      CasaToken(this.tabelasimbolos.CONSTANTE);
    } else if (this.simbolo.token == this.tabelasimbolos.MENOS) {
      CasaToken(this.tabelasimbolos.MENOS);
      CasaToken(this.tabelasimbolos.CONSTANTE);
    } else {
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
      CasaToken(this.tabelasimbolos.IDENTIFICADOR);

      if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
        CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
        Exp();
        CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
      }

      CasaToken(this.tabelasimbolos.DOIS_PONTOS_IGUAL);
      Exp();
      if(this.simbolo.token == this.tabelasimbolos.PONTO_VIRGULA){
        CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
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
        MostrarTransicao(this.simbolo, "C", "C");
        C();
        CasaToken(this.tabelasimbolos.CHAVES_FECHADA);
      } else {
        MostrarTransicao(this.simbolo, "C", "C");
        C();
      }
      if(this.simbolo.token == this.tabelasimbolos.ELSE){
        CasaToken(this.tabelasimbolos.ELSE);
        if(this.simbolo.token == this.tabelasimbolos.CHAVES_ABERTA){
          CasaToken(this.tabelasimbolos.CHAVES_ABERTA);
          MostrarTransicao(this.simbolo, "C", "C");
          C();
          CasaToken(this.tabelasimbolos.CHAVES_FECHADA);
        } else {
          MostrarTransicao(this.simbolo, "C", "C");
          C();
        }
      }

    } else if(this.simbolo.token == this.tabelasimbolos.READLN){
      CasaToken(this.tabelasimbolos.READLN);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      CasaToken(this.tabelasimbolos.IDENTIFICADOR);
      if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
        CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
        Exp();
        CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
      }
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
      CasaToken(this.tabelasimbolos.PONTO_VIRGULA);
    } else if(this.simbolo.token == this.tabelasimbolos.WRITE){
      CasaToken(this.tabelasimbolos.WRITE);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      MostrarTransicao(this.simbolo, "C", "A");
      A();
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
      CasaToken(this.tabelasimbolos.PONTO_VIRGULA);

    } else if(this.simbolo.token == this.tabelasimbolos.WRITELN){
      CasaToken(this.tabelasimbolos.WRITELN);
      CasaToken(this.tabelasimbolos.PARENTESES_ABERTO);
      MostrarTransicao(this.simbolo, "C", "A");
      A();
      CasaToken(this.tabelasimbolos.PARENTESES_FECHADO);
      CasaToken(this.tabelasimbolos.PONTO_VIRGULA);

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
    ExpS();
    if(this.simbolo.token == this.tabelasimbolos.IGUAL){
      CasaToken(this.tabelasimbolos.IGUAL);
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
    } else if(this.simbolo.token == this.tabelasimbolos.DIFERENTE){
      CasaToken(this.tabelasimbolos.DIFERENTE);
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
    } else if(this.simbolo.token == this.tabelasimbolos.MENOR){
      CasaToken(this.tabelasimbolos.MENOR);
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
    } else if(this.simbolo.token == this.tabelasimbolos.MAIOR){
      CasaToken(this.tabelasimbolos.MAIOR);
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
    } else if(this.simbolo.token == this.tabelasimbolos.MENOR_IGUAL){
      CasaToken(this.tabelasimbolos.MENOR_IGUAL);
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
    } else if(this.simbolo.token == this.tabelasimbolos.MAIOR_IGUAL){
      CasaToken(this.tabelasimbolos.MAIOR_IGUAL);
      MostrarTransicao(this.simbolo, "Exp", "ExpS");
      ExpS();
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
      CasaToken(this.tabelasimbolos.CONSTANTE);
    } else if(this.simbolo.token == this.tabelasimbolos.IDENTIFICADOR){
      CasaToken(this.tabelasimbolos.IDENTIFICADOR);
      if(this.simbolo.token == this.tabelasimbolos.COLCHETE_ABERTO){
        CasaToken(this.tabelasimbolos.COLCHETE_ABERTO);
        MostrarTransicao(this.simbolo, "F", "Exp");
        Exp();
        CasaToken(this.tabelasimbolos.COLCHETE_FECHADO);
      }
    }
  }
}
