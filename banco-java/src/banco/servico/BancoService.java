package banco.servico;

import banco.modelo.Conta;
import banco.modelo.NegocioException;
import banco.modelo.Transacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BancoService {

    private static final BigDecimal LIMITE = new BigDecimal("1000000000");

    private final Map<Integer, Conta> contas = new LinkedHashMap<>();
    private int proximoNumero = 1;

    public Conta abrirConta(String nome, String cpf, BigDecimal depositoInicial) {
        nome = nome.trim();
        cpf = cpf.replaceAll("\\D", "");
        if (nome.length() < 3) {
            throw new NegocioException("Informe o nome completo.");
        }
        if (!cpf.matches("\\d{11}")) {
            throw new NegocioException("CPF deve ter 11 dígitos.");
        }
        for (Conta c : contas.values()) {
            if (c.getCpf().equals(cpf)) {
                throw new NegocioException("Já existe uma conta com esse CPF.");
            }
        }
        if (depositoInicial != null) {
            validarValor(depositoInicial);
        }

        Conta conta = new Conta(proximoNumero++, nome, cpf);
        contas.put(conta.getNumero(), conta);
        if (depositoInicial != null) {
            conta.creditar(depositoInicial, "Depósito inicial");
        }
        return conta;
    }

    public Conta buscar(int numero) {
        Conta conta = contas.get(numero);
        if (conta == null) {
            throw new NegocioException("Conta não encontrada.");
        }
        return conta;
    }

    public Conta depositar(int numero, BigDecimal valor) {
        validarValor(valor);
        Conta conta = buscar(numero);
        conta.creditar(valor, "Depósito");
        return conta;
    }

    public Conta sacar(int numero, BigDecimal valor) {
        validarValor(valor);
        Conta conta = buscar(numero);
        conta.debitar(valor, "Saque");
        return conta;
    }

    /** Retorna a conta de origem já atualizada. */
    public Conta transferir(int numeroOrigem, int numeroDestino, BigDecimal valor) {
        if (numeroOrigem == numeroDestino) {
            throw new NegocioException("Origem e destino devem ser contas diferentes.");
        }
        validarValor(valor);
        Conta origem = buscar(numeroOrigem);
        Conta destino = buscar(numeroDestino);
        origem.debitar(valor, "Transferência para a conta " + numeroDestino); // falha aqui = nada muda
        destino.creditar(valor, "Transferência da conta " + numeroOrigem);
        return origem;
    }

    public List<Transacao> extrato(int numero) {
        List<Transacao> lista = new ArrayList<>(buscar(numero).getExtrato());
        Collections.reverse(lista);
        return lista;
    }

    private void validarValor(BigDecimal valor) {
        if (valor.signum() <= 0) {
            throw new NegocioException("O valor deve ser maior que zero.");
        }
        if (valor.compareTo(LIMITE) > 0) {
            throw new NegocioException("Valor acima do limite permitido.");
        }
    }
}
