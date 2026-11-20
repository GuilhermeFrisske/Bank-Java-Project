package banco.console;

import banco.modelo.Conta;
import banco.modelo.NegocioException;
import banco.modelo.Transacao;
import banco.servico.BancoService;
import banco.util.Dinheiro;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Menu {

    private static final DateTimeFormatter DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final BancoService banco;
    private final Scanner entrada = new Scanner(System.in);

    public Menu(BancoService banco) {
        this.banco = banco;
    }

    public void iniciar() {
        System.out.println("=== Banco ===");
        try {
            while (true) {
                mostrarOpcoes();
                String opcao = perguntar("Escolha: ");
                if (opcao.equals("0")) {
                    break;
                }
                try {
                    executar(opcao);
                } catch (NegocioException e) {
                    System.out.println("Erro: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {

        }
        System.out.println("Até logo!");
    }

    private void mostrarOpcoes() {
        System.out.println();
        System.out.println("1) Abrir conta");
        System.out.println("2) Ver saldo");
        System.out.println("3) Depositar");
        System.out.println("4) Sacar");
        System.out.println("5) Transferir");
        System.out.println("6) Extrato");
        System.out.println("0) Sair");
    }

    private void executar(String opcao) {
        switch (opcao) {
            case "1" -> abrirConta();
            case "2" -> verSaldo();
            case "3" -> depositar();
            case "4" -> sacar();
            case "5" -> transferir();
            case "6" -> mostrarExtrato();
            default -> System.out.println("Opção inválida.");
        }
    }


    private void abrirConta() {
        String nome = perguntar("Nome completo: ");
        String cpf = perguntar("CPF (11 dígitos): ");
        String texto = perguntar("Depósito inicial (Enter para pular): ");
        BigDecimal inicial = texto.isEmpty() ? null : Dinheiro.ler(texto);

        Conta conta = banco.abrirConta(nome, cpf, inicial);
        System.out.println("Conta aberta. Anote o número: " + conta.getNumero());
    }

    private void verSaldo() {
        Conta conta = banco.buscar(lerNumeroConta("Número da conta: "));
        System.out.println(conta.getTitular() + ", saldo: " + Dinheiro.formatar(conta.getSaldo()));
    }

    private void depositar() {
        int numero = lerNumeroConta("Número da conta: ");
        BigDecimal valor = Dinheiro.ler(perguntar("Valor: "));
        Conta conta = banco.depositar(numero, valor);
        System.out.println("Depósito realizado. Saldo: " + Dinheiro.formatar(conta.getSaldo()));
    }

    private void sacar() {
        int numero = lerNumeroConta("Número da conta: ");
        BigDecimal valor = Dinheiro.ler(perguntar("Valor: "));
        Conta conta = banco.sacar(numero, valor);
        System.out.println("Saque realizado. Saldo: " + Dinheiro.formatar(conta.getSaldo()));
    }

    private void transferir() {
        int origem = lerNumeroConta("Conta de origem: ");
        int destino = lerNumeroConta("Conta de destino: ");
        BigDecimal valor = Dinheiro.ler(perguntar("Valor: "));
        Conta conta = banco.transferir(origem, destino, valor);
        System.out.println("Transferência realizada para " + banco.buscar(destino).getTitular()
                + ". Saldo: " + Dinheiro.formatar(conta.getSaldo()));
    }

    private void mostrarExtrato() {
        Conta conta = banco.buscar(lerNumeroConta("Número da conta: "));
        var movimentacoes = banco.extrato(conta.getNumero());
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação ainda.");
            return;
        }
        System.out.println("Extrato de " + conta.getTitular() + " (mais recente primeiro):");
        for (Transacao t : movimentacoes) {
            System.out.printf("%s  %-34s %15s   saldo %s%n",
                    t.data().format(DATA), t.descricao(),
                    Dinheiro.formatar(t.valor()), Dinheiro.formatar(t.saldoApos()));
        }
    }

    private String perguntar(String texto) {
        System.out.print(texto);
        return entrada.nextLine().trim();
    }

    private int lerNumeroConta(String texto) {
        try {
            return Integer.parseInt(perguntar(texto));
        } catch (NumberFormatException e) {
            throw new NegocioException("Conta não encontrada.");
        }
    }
}
