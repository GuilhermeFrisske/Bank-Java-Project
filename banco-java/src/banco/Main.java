package banco;

import banco.console.Menu;
import banco.servico.BancoService;

public class Main {
    public static void main(String[] args) {
        new Menu(new BancoService()).iniciar();
    }
}
