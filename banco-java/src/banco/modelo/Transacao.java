package banco.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Valor positivo = entrou dinheiro; negativo = saiu. */
public record Transacao(String descricao, BigDecimal valor, BigDecimal saldoApos, LocalDateTime data) {
}
