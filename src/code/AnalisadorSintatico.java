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
     * Metodo correspondente ao simboolo nao-terminal da gramatica S -> { A } { B }*
     */
    public void S() {
        
    }

    
}