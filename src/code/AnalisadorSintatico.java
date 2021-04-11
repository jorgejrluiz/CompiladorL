public class AnalisadorSintatico {
    AnalisadorLexico analisadorlexico;
    TabelaDeSimbolos tabela;
    Simbolo simbolo;
    Simbolo novoSimbolo = new Simbolo();
    Simbolo simboloAnterior = new Simbolo();
    BufferedReader file;
    public int programCounter;

    /**
     * Construtor da classe
     *
     *
     */
    public AnalisadorSintatico(BufferedReader file) {
        this.file = file;
        this.tabela = new TabelaDeSimbolos();
        this.analisadorlexico = new AnalisadorLexico(this.file, this.tabela);
        this.simbolo = analisadorlexico.maquinaDeEstados();
    }

    /**
     *Metodo casa token recebe o que se espera e compara com o que chegou. Pode dar errado, printa msg
     *
     */
    public void CasaToken(byte tokenesperado) {
        if (this.simbolo.token == tokenesperado) {

            this.simbolo = analisadorlexico.maquinaDeEstados();
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
     * S -> {D} main "{" {C} "}"
     */
    public void S() {
      if (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR ||
          this.simbolo.token == this.tabelasimbolos.FINAL) {
          while (this.simbolo.token == this.tabelasimbolos.INT || this.simbolo.token == this.tabelasimbolos.CHAR ||
                 this.simbolo.token == this.tabelasimbolos.FINAL) {
                   D();
          }
      } else if (this.simbolo.token == this.tabelasimbolos.MAIN){
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
                            C();
                }
          }
          CasaToken(this.tabelasimbolos.CHAVES_FECHADA);
      }

      if (analisadorlexico.fimDeArquivo == false) {
          System.out.println(analisadorlexico.linha + "\ntoken nao esperado [" + this.simbolo.lexema + "].");
      }
      System.out.println((analisadorlexico.linha-1)+" linhas compiladas.");
    }

    /*
     * D -> T X ; | final id = V;
     */
     public void D() {

     }

    /*
     * X -> { id ([ = V ] | "["constante"]" ) [,] }+
     */
     public void X() {

     }

    /*
     * T -> int | boolean | char
     */
     public void T() {

     }

    /*
     * V -> [ + | - ] constante
     */
     public void V() {

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

     }

    /**
     * J -> C [,]
     */
     public void J() {

     }

    /**
     *A -> Exp [,]
     */
     public void A() {

     }

    /**
     * Exp -> ExpS [{ = | <> | < | > | <= | >= } ExpS]
     */
     public void Exp() {

     }

    /**
     * ExpS -> [ + | - ] Termo {(+ | - | or) Termo}
     */
     public void ExpS() {

     }

    /**
     *Termo -> F {( * | / | % | and ) F}
     */
     public void Termo() {

     }

    /**
     * F -> not F | "(" Exp ")" | constante | id ["["Exp"]"]
     */
     public void F() {

     }


}
