/* Exemplo de Programa na linguagem L */

int n;
char nome[40];
final MAXITER = 01;

/* Programa Principal */
main {
  write("Digite seu nome: ");
  readln(nome);
  for (n:=1; n < MAXITER; n := n + 1) {
    writeln("Ola' ",nome);
  }
/**/}

