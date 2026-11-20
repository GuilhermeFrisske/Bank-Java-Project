package banco.util;

import banco.modelo.NegocioException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public final class Dinheiro {

    private static final NumberFormat REAIS = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));

    private Dinheiro() {
    }

    /** Aceita 150, 150.50 ou 1.234,56. No máximo 2 casas decimais. */
    public static BigDecimal ler(String texto) {
        String t = texto.replace("R$", "").replace(" ", "");
        if (t.contains(",")) {
            t = t.replace(".", "").replace(",", ".");
        }
        BigDecimal valor;
        try {
            valor = new BigDecimal(t);
        } catch (NumberFormatException e) {
            throw new NegocioException("Valor inválido. Exemplo: 150,00");
        }
        try {
            return valor.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new NegocioException("Use no máximo 2 casas decimais.");
        }
    }

    public static String formatar(BigDecimal valor) {
        return REAIS.format(valor);
    }
}
