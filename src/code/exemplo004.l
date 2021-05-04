/* Exemplo de Programa na linguagem L - Com erro de tamanho de vetor char
seu vetor com ate 8kbytes (8000 bytes) ou seja int n[8000]
*/

char n[8001], max:=0, i;

main
{
  for (i:=0; i<3; i:=i+1) {
    write("Digite um numero: ");
    readln(n[i]);
  }
  if (n[0] > n[1]) then {
    if (n[0] > n[2]) then max:=n[0]; else max:=n[2];
  } else if (n[1] > n[2]) then max:=n[1]; else max:=n[2]; 
  writeln("O maior numero é ",max);
}  