/* Exemplo de Programa na linguagem L - Fatorial*/

int n;
int fatorial;
int resultado;

main {
/* Programa Principal */

  write("Qual o numero para calculo de fatorial: ");
  readln(fatorial);/*Le o valor*/
  
  for (n:=1; n<=fatorial; n:=n+1)
	resultado:=resultado*n;
	
  writeln("F(x) = ",resultado);
}