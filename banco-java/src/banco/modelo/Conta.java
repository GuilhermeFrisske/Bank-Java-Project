package banco.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conta {

    private final int numero;
    private final String titular;
    private final String cpf;
    private BigDecimal saldo = BigDecimal.ZERO.setScale(2);
    private final List<Transacao> extrato = new ArrayList<>();

    public Conta(int numero, String titular, String cpf) {
        this.numero = numero;
        this.titular = titular;
        this.cpf = cpf;
    }

    public void creditar(BigDecimal valor, String descricao) {
        saldo = saldo.add(valor);
        extrato.add(new Transacao(descricao, valor, saldo, LocalDateTime.now()));
    }

    public void debitar(BigDecimal valor, String descricao) {
        if (saldo.compareTo(valor) < 0) {
            throw new NegocioException("Saldo insuficiente.");
        }
        saldo = saldo.subtract(valor);
        extrato.add(new Transacao(descricao, valor.negate(), saldo, LocalDateTime.now()));
    }

    public int getNumero() { return numero; }
    public String getTitular() { return titular; }
    public String getCpf() { return cpf; }
    public BigDecimal getSaldo() { return saldo; }

    /** Movimentações da mais antiga para a mais recente (lista somente leitura). */
    public List<Transacao> getExtrato() {
        return Collections.unmodifiableList(extrato);
    }
}
