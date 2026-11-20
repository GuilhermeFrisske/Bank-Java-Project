package banco.modelo;

/** Erro de regra do banco (saldo insuficiente, conta inexistente, valor inválido). */
public class NegocioException extends RuntimeException {
    public NegocioException(String mensagem) {
        super(mensagem);
    }
}
