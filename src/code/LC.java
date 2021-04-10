/**
* Trabalho de compiladores - Util
* Professor: Alexei Machado
*
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*
* Classe LC main
*
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;
import java.util.*;
import java.io.BufferedWriter;

public class LC{

    public static void main(String[]args) throws Exception{
        try{
            InputStreamReader isr = new InputStreamReader(System.in, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            TabelaDeSimbolos tabelaSimbolos = new TabelaDeSimbolos();
            AnalisadorSintatico sintatico = new AnalisadorSintatico(br);
            
            sintatico.S();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
