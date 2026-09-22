package restaurantModule;

import java.util.Scanner;
import application.Exercise;

public class RestaurantExercise extends Exercise {
    private final Restaurante restaurante = new Restaurante();

    public RestaurantExercise(Scanner scanner) {
        super(scanner);
        // Datos de ejemplo: ambas mesas comienzan libres.
        restaurante.registrar("Ana", 2);
        restaurante.registrar("Luis", 1);
        restaurante.registrar("Carla", 4);
        restaurante.registrar("Diego", 3);
    }

    @Override
    protected void exerciseLogic() {
        System.out.println("\n===== TP 05: MESAS DE RESTAURANTE =====");
        mostrarMesa(restaurante.getMesa(2));
        mostrarMesa(restaurante.getMesa(4));
        System.out.println("1 - Anotar un grupo");
        System.out.println("2 - Hacer pasar al siguiente grupo");
        System.out.println("3 - Liberar una mesa");
        System.out.println("0 - Volver al menu principal");
        System.out.print("Seleccione una opcion: ");
        if (!scanner.hasNextLine()) {
            running = false;
            return;
        }
        switch (scanner.nextLine().trim()) {
            case "1":
                registrar();
                break;
            case "2":
                hacerPasar();
                break;
            case "3":
                liberar();
                break;
            case "0":
                running = false;
                break;
            default:
                System.out.println("Opcion invalida. Ingrese 0, 1, 2 o 3.");
        }
    }

    private void mostrarMesa(Mesa mesa) {
        System.out.println("\nMesa de " + mesa.getCapacidad() + ": "
                + (mesa.estaLibre() ? "Libre" : "Ocupada por " + mesa.getOcupante()));
        System.out.println("Grupos en espera: " + mesa.cantidadEnEspera());
        Grupo siguiente = mesa.siguienteGrupo();
        System.out.println("Siguiente: " + (siguiente == null ? "Ninguno" : siguiente));
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            running = false;
            return "";
        }
        return scanner.nextLine().trim();
    }

    private int leerNumero(String mensaje) {
        try {
            return Integer.parseInt(leer(mensaje));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void registrar() {
        String nombre = leer("Nombre del grupo: ");
        if (nombre.isEmpty()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        int personas = leerNumero("Cantidad de personas (1 a 4): ");
        if (personas < 1 || personas > 4) {
            System.out.println("Ingrese un numero entero entre 1 y 4. No se admiten grupos mayores.");
            return;
        }
        restaurante.registrar(nombre, personas);
        System.out.println("Grupo anotado para la mesa de " + (personas <= 2 ? 2 : 4) + ".");
    }

    private Mesa seleccionarMesa() {
        int capacidad = leerNumero("Capacidad de la mesa (2 o 4): ");
        if (capacidad != 2 && capacidad != 4) {
            System.out.println("Mesa invalida. Ingrese 2 o 4.");
            return null;
        }
        return restaurante.getMesa(capacidad);
    }

    private void hacerPasar() {
        Mesa mesa = seleccionarMesa();
        if (mesa == null) {
            return;
        }
        if (!mesa.estaLibre()) {
            System.out.println("La mesa esta ocupada. Debe liberarla primero.");
        } else if (mesa.cantidadEnEspera() == 0) {
            System.out.println("No hay grupos esperando esa mesa.");
        } else {
            System.out.println("Pasa el grupo: " + mesa.hacerPasar());
        }
    }

    private void liberar() {
        Mesa mesa = seleccionarMesa();
        if (mesa == null) {
            return;
        }
        if (mesa.liberar()) {
            System.out.println("Mesa liberada. Puede hacer pasar al siguiente grupo con la opcion 2.");
        } else {
            System.out.println("La mesa ya estaba libre.");
        }
    }
}
