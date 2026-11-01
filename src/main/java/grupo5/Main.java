package grupo5;

import grupo5.interfaz.MenuConsola;

/** Punto de entrada de la aplicación de consola. */
public final class Main {
    private Main() { }
    public static void main(String[] args) {
        new MenuConsola().iniciar();
    }
}
