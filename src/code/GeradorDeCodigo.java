/**
* Trabalho de compiladores - LC
* Arquivo contendo a geracao de codigo
* Professor: Alexei Machado
*
* @author Ana Flavia
* @author Jorge Luiz
* @author Stefany Gaspar
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.*;
import java.util.*;
public class GeradorDeCodigo {

    public int programCounter;
    public int rotCounter;
    public int tempCounter;
    public BufferedWriter arquivoAsm;

    public static final byte classeVAR_asm = 1;
    public static final byte classeCONST_asm = 2;

    public static final byte semTipo_asm = 0;
    public static final byte tipoBoolean_asm = 1;
    public static final byte tipoInt_asm = 2;
    public static final byte tipoChar_asm = 3;

    GeradorDeCodigo(String nome) {
        try {
            programCounter = 0x4000;
            rotCounter = 1;
            tempCounter = 0;
            this.arquivoAsm = new BufferedWriter(new FileWriter(nome));
        }catch(Exception e){
            System.out.println("Erro ao criar arquivo asm"); //arquivoAsm.append(linha + "\n"); arquivoAsm.close();
        }
    }

    public void inicioPrograma() {
        try {
            arquivoAsm.append("sseg SEGMENT STACK  ;início seg. pilha" + "\n");
            arquivoAsm.append("byte 4000h DUP(?)   ;dimensiona pilha" + "\n");
            arquivoAsm.append("sseg ENDS           ;fim seg. pilha" + "\n");
            arquivoAsm.append("dseg SEGMENT PUBLIC ;início seg. dados" + "\n");
            arquivoAsm.append("byte 4000h DUP(?)   ;temporários" + "\n");
            arquivoAsm.append(";Definições de variáveis e constantes" + "\n");
        }catch(Exception E){
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    public void inicio_asm() {
        try {
            arquivoAsm.append("dseg ENDS            ;fim seg. dados" + "\n");
            arquivoAsm.append("cseg SEGMENT PUBLIC  ;início seg. código" + "\n");
            arquivoAsm.append("ASSUME CS:cseg, DS:dseg" + "\n");
            arquivoAsm.append("strt:                ;início do programa" + "\n");
            arquivoAsm.append("mov ax, dseg" + "\n");
            arquivoAsm.append("mov ds, ax" + "\n");
        }catch(Exception E){
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    public void fim_asm() {
        try {
            arquivoAsm.append("mov ah,4Ch" + "\n");
            arquivoAsm.append("int 21h" + "\n");
            arquivoAsm.append("cseg ENDS           ;fim seg. código" + "\n");
            arquivoAsm.append("END strt            ;fim programa" + "\n");
            arquivoAsm.close();
        }catch(Exception E){
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    public void adicionarInteiro(Simbolo simbolo,String valor) {
        try {
            if(valor == null) {
                arquivoAsm.append("sword ?" + "; inteiro na posicao de memoria: " + programCounter + ", com o nome: " + simbolo.lexema + "\n");
                simbolo.endereco = programCounter;
                programCounter+= 2;
            }else{
                arquivoAsm.append("sword " + Integer.parseInt(valor) + "; inteiro na posicao de memoria: " + programCounter + ", com o nome:"+ simbolo.lexema +"\n");
                simbolo.endereco = programCounter;
                programCounter+= 2;
            }
        } catch (Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    public void adicionarCaractere(Simbolo simbolo, String valor) {
        try {
            if(valor == null) {
                arquivoAsm.append("byte ?" + "; Caractere sem valor na posicao de memoria: " + programCounter + ", com o nome: " + simbolo.lexema + "\n");
                simbolo.endereco = programCounter;
                programCounter+= 1;
            }else{
                arquivoAsm.append("byte " + valor + "; caractere na posicao de memoria: " + programCounter + ", com o nome: "+ simbolo.lexema +"\n");
                simbolo.endereco = programCounter;
                programCounter+= 1;
            }
        } catch (Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    public void adicionarVetor(Simbolo simbolo) {
        try {
            if(simbolo.tipo == tipoChar_asm) {
                arquivoAsm.append("byte " + simbolo.tamanho + " DUP(?)" + "; caractere vetor na posicao de memoria: " + programCounter + ", com o nome: " + simbolo.lexema + "\n");
                simbolo.endereco = programCounter;
                programCounter+= simbolo.tamanho;
            }else{
                arquivoAsm.append("sword " + simbolo.tamanho + " DUP(?)" + "; inteiro vetor na posicao de memoria: " + programCounter + ", com o nome: " + simbolo.lexema + "\n");
                simbolo.endereco = programCounter;
                programCounter+= simbolo.tamanho * 2;
            }
        } catch (Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    public void declararString(String lexema) {
        try{
            arquivoAsm.append("dseg SEGMENT PUBLIC ; declarando string\n");
            arquivoAsm.append("byte "+ "\"" + lexema + "\" ; string" + "\n");
            arquivoAsm.append("dseg ENDS ; fim da string\n");
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }
    public void escreverComandos(String comando) {
        try{
            arquivoAsm.append(comando + "\n");
        }catch(Exception e){
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    public void imprimirString (int endereco) {
        try{
            arquivoAsm.append("mov dx, " + endereco + "; comeco string\n");
            arquivoAsm.append("mov ah, 09h\n");
            arquivoAsm.append("int 21h\n");
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }
    public void imprimirInteiro(int endereco) {
        try{
            int adressTemp = newTemp(3);
            int rot1 = newRot();
            int rot2 = newRot();
            int rot3 = newRot();

            arquivoAsm.append(";A saida para o video eh sempre do tipo string\n");
            arquivoAsm.append("mov di, " + adressTemp + "   ;String temporario\n");
            arquivoAsm.append("mov cx, 0\n");
            arquivoAsm.append("mov ax, DS:[" + endereco + "]\n");
            arquivoAsm.append("cmp ax, 0\n");
            arquivoAsm.append("jge R" + rot1 + "\n");
            arquivoAsm.append("mov bl, 2Dh\n");
            arquivoAsm.append("mov DS:[di], bl\n");
            arquivoAsm.append("add di , 1           ;Incrementa o indice\n");
            arquivoAsm.append("neg ax               ;toma modulo do numero\n");
            arquivoAsm.append("R" + rot1 + ":\n");
            arquivoAsm.append("mov bx, 10           ; divisor \n");
            arquivoAsm.append("R" + rot2 + ":\n");
            arquivoAsm.append("add cx, 1            ;Incrementa o contador\n");
            arquivoAsm.append("mov dx, 0\n");
            arquivoAsm.append("idiv bx\n");
            arquivoAsm.append("push dx\n");
            arquivoAsm.append("cmp ax, 0\n");
            arquivoAsm.append("jne R" + rot2 + "\n");

            arquivoAsm.append(";Desempilha os valores e escreve o string\n");
            arquivoAsm.append("R" + rot3 + ":\n");
            arquivoAsm.append("pop dx\n");
            arquivoAsm.append("add dx, 30h\n");
            arquivoAsm.append("mov DS:[di] , dl\n");
            arquivoAsm.append("add di, 1\n");
            arquivoAsm.append("add cx, -1\n");
            arquivoAsm.append("cmp cx, 0\n");
            arquivoAsm.append("jne R" + rot3 + "\n");

            arquivoAsm.append(";Grava fim de string\n");
            arquivoAsm.append("mov dl, 024h       ;fim da string\n");
            arquivoAsm.append("mov DS:[di], dl    ;grava '$'\n");

            arquivoAsm.append(";Mostra a string\n");
            arquivoAsm.append("mov dx, " + adressTemp + "\n");
            arquivoAsm.append("mov ah, 09h\n");
            arquivoAsm.append("int 21h\n");
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }
    public void lerInteiro(int endereco) {
        int adressTemp = newTemp(254);
        int rot1 = newRot();
        int rot2 = newRot();
        int rot3 = newRot();

        try{
            arquivoAsm.append("mov dx, " + adressTemp + "\n");
            arquivoAsm.append("mov al, 0FFh\n");
            arquivoAsm.append("mov DS:[" + adressTemp + "], al\n");
            arquivoAsm.append("mov ah, 0Ah\n");
            arquivoAsm.append("int 21h\n");

            barraN();

            arquivoAsm.append("mov di, " + (adressTemp+2) + "\n");
            arquivoAsm.append("mov ax, 0\n");
            arquivoAsm.append("mov cx, 10\n");
            arquivoAsm.append("mov dx, 1\n");
            arquivoAsm.append("mov bh, 0\n");
            arquivoAsm.append("mov bl, DS:[di]\n");
            arquivoAsm.append("cmp bx, 2Dh\n");
            arquivoAsm.append("jne R"+ rot1 + "\n");
            arquivoAsm.append("mov dx, -1\n");
            arquivoAsm.append("add di, 1\n");
            arquivoAsm.append("mov bl, DS:[di]\n");

            arquivoAsm.append("R"+ rot1 + ":\n");
            arquivoAsm.append("push dx\n");
            arquivoAsm.append("mov dx, 0\n");

            arquivoAsm.append("R"+ rot2 + ":\n");
            arquivoAsm.append("cmp bx, 0Dh\n");
            arquivoAsm.append("je R" + rot3 + "\n");
            arquivoAsm.append("imul cx\n");
            arquivoAsm.append("add bx, -48\n");
            arquivoAsm.append("add ax, bx\n");
            arquivoAsm.append("add di, 1\n");
            arquivoAsm.append("mov bh, 0\n");
            arquivoAsm.append("mov bl, DS:[di]\n");
            arquivoAsm.append("jmp R"+ rot2 + "\n");

            arquivoAsm.append("R"+ rot3 + ":\n");
            arquivoAsm.append("pop cx\n");
            arquivoAsm.append("imul cx\n");
            arquivoAsm.append("mov DS:["+ endereco + "], ax\n");
        }catch(Exception e) {
            System.out.println("Erro no asm");
        }
    }
    public void lerString(int endereco, int tamanho) {
        int adressTemp = newTemp(tamanho + 3); // tamanho do vetor + 3 posicoes
        int rot1 = newRot();
        int rot2 = newRot();

        try{
            arquivoAsm.append("mov dx, " + adressTemp + "\n");
            arquivoAsm.append("mov al, " + (tamanho + 1) + "\n");
            arquivoAsm.append("mov DS:[" + adressTemp + "], al\n");
            arquivoAsm.append("mov ah, 0Ah\n");
            arquivoAsm.append("int 21h\n");

            barraN();

            arquivoAsm.append("mov di, " + (adressTemp+2) + "\n"); //posicao do string
            arquivoAsm.append("mov si, " + endereco + "\n"); //Posicao do vetor/char
            arquivoAsm.append("R" + rot1 + ":\n");
            arquivoAsm.append("mov al, DS:[di]\n");
            arquivoAsm.append("cmp al, 0Dh\n");
            arquivoAsm.append("je R" + rot2 + "\n");
            arquivoAsm.append("mov DS:[si], al\n"); //Pegar caractere para o vetor
            arquivoAsm.append("add di, 1\n"); //Proxima posicao
            arquivoAsm.append("add si, 1\n"); //Proxima posicao
            arquivoAsm.append("jmp R" + rot1 + "\n");
            arquivoAsm.append("R" + rot2 + ":\n");
            arquivoAsm.append("mov al, 024h\n");
            arquivoAsm.append("mov DS:[si], al\n");//Guarda a ultima $
        }catch(Exception e) {
            System.out.println("Erro no asm");
        }
    }
    public void barraN() {
        try{
            arquivoAsm.append("mov ah, 02h\n");
            arquivoAsm.append("mov dl, 0Dh\n");
            arquivoAsm.append("int 21h\n");
            arquivoAsm.append("mov DL, 0Ah\n");
            arquivoAsm.append("int 21h\n");
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }
    public void imprimirChar (int endereco) {
        try{
            int adressTemp = newTemp(2); //caractere + $
            arquivoAsm.append("mov di, " + "DS:[" + endereco + "]\n");
            arquivoAsm.append("mov DS:[" + adressTemp + "], " + "di\n");
            arquivoAsm.append("mov di, 024h\n");
            arquivoAsm.append("mov DS:[" + (adressTemp + 1) + "], " + "di\n");
            imprimirString(adressTemp);
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }
    public void imprimirVetorChar (int endereco,int tamanho) {
        try{
            int adressTemp = newTemp(tamanho + 1); //Vetor Caractere + $
            arquivoAsm.append("mov di, " + "DS:[" + endereco + "]\n");
            arquivoAsm.append("mov DS:[" + adressTemp + "], " + "di\n");
            arquivoAsm.append("mov di, 024h\n");
            arquivoAsm.append("mov DS:[" + (adressTemp + 1 + tamanho) + "], " + "di\n");
            imprimirString(adressTemp);
        }catch(Exception e) {
            System.out.println("Erro ao escrever no arquivo asm.");
        }
    }

    //Cria novo temporario
    public int newTemp(int i){
        int newTemp = tempCounter;
        tempCounter += i;
        return newTemp;
    }

    //Cria novo rotulo
    public int newRot(){
        int newTemp = rotCounter;
        rotCounter += 1;
        return newTemp;
    }

    //reseta a quantidade de temporario
    public void resetTemp() {
        tempCounter = 0;
    }

}
