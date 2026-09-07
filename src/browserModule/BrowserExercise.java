package browserModule;

import java.util.Scanner;

import application.Exercise;

/** Simulador de historial por consola correspondiente al TP 04. */
public class BrowserExercise extends Exercise {

    private final Navegador navegador = new Navegador();

    public BrowserExercise(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void exerciseLogic() {
        System.out.println("\n===== TP 04: HISTORIAL DE NAVEGADOR WEB =====");
        String actual = navegador.getPaginaActual();
        System.out.println("Pagina actual: " + (actual == null ? "(ninguna)" : actual));
        System.out.println("1 - Visitar una pagina");
        System.out.println("2 - Volver atras" + (navegador.puedeRetroceder() ? "" : " (no disponible)"));
        System.out.println("3 - Volver adelante" + (navegador.puedeAvanzar() ? "" : " (no disponible)"));
        System.out.println("0 - Volver al menu principal");
        System.out.print("Seleccione una opcion: ");

        String opcion = scanner.nextLine().trim();
        switch (opcion) {
            case "1":
                visitarPagina();
                break;
            case "2":
                if (!navegador.retroceder()) {
                    System.out.println("No hay paginas para volver atras.");
                }
                break;
            case "3":
                if (!navegador.avanzar()) {
                    System.out.println("No hay paginas para volver adelante.");
                }
                break;
            case "0":
                running = false;
                break;
            default:
                System.out.println("Opcion invalida. Ingrese 0, 1, 2 o 3.");
        }
    }

    private void visitarPagina() {
        System.out.print("Nombre de la pagina: ");
        String pagina = scanner.nextLine().trim();
        if (pagina.isEmpty()) {
            System.out.println("El nombre de la pagina no puede estar vacio.");
            return;
        }
        navegador.visitar(pagina);
    }
}
