/* Exemplo de Programa na linguagem L */

int n;
char nome[40];
boolean p;
final MAXITER=10;

main {
/* Programa Principal */

  write("Digite seu nome: ");
  readln(nome);
  for (n:=1; n<=MAXITER; n:=n+1)
    writeln("Ola' ",nome);
}
