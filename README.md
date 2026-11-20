# Banco em Java

O Banco em Java é um sistema bancário simples que funciona pelo terminal, desenvolvido utilizando Java puro, sem servidor, Maven ou bibliotecas externas.

Com ele, é possível criar contas, realizar depósitos, saques, transferências e consultar o extrato através de um menu de opções.

## Como executar

Para rodar o projeto, é necessário ter o **JDK 17 ou superior** instalado.

- **Linux/macOS:** execute `./rodar.sh`.
- **Windows:** execute o arquivo `rodar.bat` ou rode pelo terminal.

Os dados ficam armazenados apenas enquanto o programa está aberto. Ao encerrá-lo, todas as contas e movimentações são perdidas.

## Como o sistema funciona

O projeto foi dividido em algumas partes para facilitar a organização e a manutenção do código.

```text
console (Menu) → servico (BancoService) → modelo (Conta, Transacao)
```

Cada parte tem uma responsabilidade:

- **Console:** apresenta o menu e recebe as informações digitadas.
- **Serviço:** executa as operações e verifica as regras do banco.
- **Modelo:** armazena os dados das contas e suas movimentações.

### Exemplo de depósito

Imagine que uma pessoa queira depositar R$ 50,00 na conta de número 1.

1. O `Menu.depositar()` solicita o número da conta e o valor.
2. A classe `Dinheiro` transforma o valor digitado em um `BigDecimal`.
3. O `BancoService.depositar()` verifica se o valor é válido e se a conta existe.
4. A classe `Conta` atualiza o saldo e registra a movimentação no extrato.
5. O sistema informa que o depósito foi realizado.

Caso aconteça algum problema, como uma conta inexistente ou saldo insuficiente, o sistema apresenta uma mensagem de erro sem encerrar o programa.

## Estrutura de pastas

```text
banco-java/
  rodar.sh
  rodar.bat
  README.md
  .gitignore
  src/banco/
    Main.java
    modelo/
      Conta.java
      Transacao.java
      NegocioException.java
    servico/
      BancoService.java
    console/
      Menu.java
    util/
      Dinheiro.java
```

## Entendendo as classes

### Main.java

É onde o programa começa. Sua função é criar o serviço bancário e iniciar o menu principal.

### Transacao.java

Representa cada movimentação realizada na conta.

Armazena quatro informações:

- `descricao`: identifica a movimentação, como depósito, saque ou transferência.
- `valor`: positivo quando o dinheiro entra e negativo quando sai.
- `saldoApos`: mostra quanto ficou na conta depois da operação.
- `data`: registra o dia e o horário da movimentação.

### Conta.java

É responsável por armazenar os dados de cada conta, como número, titular, CPF, saldo e histórico de movimentações.

Possui dois métodos principais:

- `creditar(valor, descricao)`: adiciona dinheiro ao saldo e registra a movimentação.
- `debitar(valor, descricao)`: retira dinheiro da conta, desde que exista saldo suficiente.

Também possui o método `getExtrato()`, que permite consultar as movimentações sem deixar que outras partes do programa alterem o histórico diretamente.

Os atributos são privados, evitando alterações indevidas nos dados da conta.

### NegocioException.java

É a classe responsável por tratar os erros relacionados às operações bancárias.

Alguns exemplos são:

- Saldo insuficiente.
- Conta não encontrada.
- Valor inválido.
- CPF já cadastrado.

Dessa forma, o sistema consegue informar o que aconteceu sem precisar interromper sua execução.

### BancoService.java

É a parte responsável pelas operações e regras do banco. Ela mantém as contas cadastradas em um `Map`, utilizando o número da conta como referência.

Entre suas principais funções estão:

- `abrirConta(nome, cpf, depositoInicial)`: cadastra uma nova conta, valida os dados e verifica se o CPF já está cadastrado.
- `buscar(numero)`: procura uma conta pelo número informado.
- `depositar(numero, valor)`: realiza um depósito após validar as informações.
- `sacar(numero, valor)`: verifica o saldo e realiza o saque.
- `transferir(origem, destino, valor)`: transfere dinheiro entre duas contas.
- `extrato(numero)`: retorna o histórico de movimentações, começando pelas mais recentes.

O sistema também possui uma validação para impedir operações com valores menores ou iguais a zero e valores acima de R$ 1 bilhão.

Na transferência, primeiro é realizado o débito da conta de origem. Depois, o valor é creditado na conta de destino. Caso não exista saldo suficiente, a operação é interrompida.

### Menu.java

É a parte que aparece para o usuário no terminal.

Apresenta as opções disponíveis e recebe os dados necessários para cada operação.

O menu possui as seguintes funcionalidades:

- Abrir conta.
- Consultar saldo.
- Depositar.
- Sacar.
- Transferir.
- Consultar extrato.
- Sair do sistema.

Cada operação possui seu próprio método, deixando o código mais organizado.

Também utiliza `try/catch` para tratar erros e apresentar mensagens ao usuário.

### Dinheiro.java

Essa classe cuida da leitura e formatação dos valores monetários.

O método `ler(texto)` converte o valor digitado em `BigDecimal`, permitindo trabalhar com valores como:

- `150`
- `150,50`
- `1.234,56`

Também verifica se o valor foi digitado corretamente e impede a utilização de mais de duas casas decimais.

Já o método `formatar(valor)` transforma os números no padrão brasileiro, por exemplo: `R$ 1.234,56`.

## Decisões utilizadas no projeto

Durante o desenvolvimento, algumas escolhas foram feitas para deixar o sistema mais organizado e evitar problemas.

**1. Utilização de BigDecimal**

Foi utilizado `BigDecimal` no lugar de `double` para trabalhar com dinheiro. Isso evita problemas de precisão em operações com valores decimais e garante resultados mais confiáveis nos cálculos.

**2. Separação das responsabilidades**

O código foi dividido em classes com funções diferentes. Assim, as regras bancárias ficam separadas da parte visual do terminal, facilitando futuras alterações.

**3. Tratamento de erros**

Foi criada uma exceção própria chamada `NegocioException` para identificar problemas nas operações bancárias e apresentar mensagens mais claras.

**4. Armazenamento em memória**

As contas são armazenadas em um `Map` enquanto o programa está em execução. Essa escolha deixa o projeto mais simples, mas não mantém os dados após fechar o sistema.

No futuro, seria possível adicionar um banco de dados para salvar as contas e o histórico de movimentações.