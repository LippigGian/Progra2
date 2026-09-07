package listModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.Exercise;

public class ListExercise extends Exercise {

    // Fases: 0 = menú | 1 = agregar | 2 = remover por índice
    //        3 = remover por referencia | 4 = limpiar
    private int currentPhase = 0;
    private boolean firstTime = true;
    private List<String> list;

    public ListExercise(Scanner scanner) {
        super(scanner);
        this.list = new ArrayList<>();
        // Para probar la otra implementación, cambiar solo esta línea:
        // this.list = new LinkedList<>();   (requiere import java.util.LinkedList)
    }

    @Override
    protected void exerciseLogic() {
        switch (currentPhase) {
            case 0:
                menuLogic();
                break;
            case 1:
                addElement();
                break;
            case 2:
                removeByIndex();
                break;
            case 3:
                removeByReference();
                break;
            case 4:
                clearList();
                break;
            default:
                currentPhase = 0;
        }
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\n===== Ejercicio de TDA List =====");
            System.out.println("Implementación en uso: " + list.getClass().getSimpleName());
            firstTime = false;
        } else {
            showListState();
        }

        System.out.println("\n--- Operaciones disponibles ---");
        System.out.println("1 - Agregar un elemento");
        System.out.println("2 - Remover un elemento por índice");
        System.out.println("3 - Remover un elemento por referencia");
        System.out.println("4 - Limpiar la lista");
        System.out.println("0 - Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        String input = scanner.nextLine().trim();

        switch (input) {
            case "1":
                currentPhase = 1;
                break;
            case "2":
                currentPhase = 2;
                break;
            case "3":
                currentPhase = 3;
                break;
            case "4":
                currentPhase = 4;
                break;
            case "0":
                running = false;
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
        }
    }

    private void addElement() {
        System.out.print("\nIngrese el elemento a agregar: ");
        String element = scanner.nextLine().trim();

        if (element.isEmpty()) {
            System.out.println("No se puede agregar un elemento vacío.");
        } else {
            list.add(element);
            System.out.println("Elemento agregado correctamente.");
        }

        printElements();

        if (!askRepeat()) {
            currentPhase = 0;
        }
    }

    private void removeByIndex() {
        if (list.isEmpty()) {
            System.out.println("\nLa lista está vacía: no hay elementos para remover.");
            currentPhase = 0;
            return;
        }

        System.out.println();
        printElements();
        System.out.print("Ingrese el índice a remover (de 0 a " + (list.size() - 1) + "): ");
        String input = scanner.nextLine().trim();

        try {
            int index = Integer.parseInt(input);

            if (index < 0 || index >= list.size()) {
                System.out.println("El índice está fuera de rango.");
            } else {
                String removed = list.remove(index);
                System.out.println("Se removió el elemento: " + removed);
            }
        } catch (NumberFormatException e) {
            System.out.println("Debe ingresar un número entero.");
        }

        printElements();

        if (!askRepeat()) {
            currentPhase = 0;
        }
    }

    private void removeByReference() {
        if (list.isEmpty()) {
            System.out.println("\nLa lista está vacía: no hay elementos para remover.");
            currentPhase = 0;
            return;
        }

        System.out.println();
        printElements();
        System.out.print("Ingrese el elemento a remover: ");
        String element = scanner.nextLine().trim();

        boolean removed = list.remove(element);

        if (removed) {
            System.out.println("Se removió la primera aparición de: " + element);
        } else {
            System.out.println("El elemento no se encuentra en la lista.");
        }

        printElements();

        if (!askRepeat()) {
            currentPhase = 0;
        }
    }

    private void clearList() {
        if (list.isEmpty()) {
            System.out.println("\nLa lista ya estaba vacía.");
        } else {
            list.clear();
            System.out.println("\nLa lista fue vaciada.");
        }

        printElements();
        currentPhase = 0;
    }

    private void showListState() {
        System.out.println("\n--- Estado actual de la lista ---");
        printElements();
        System.out.println("Cantidad de elementos: " + list.size());
        System.out.println("¿Está vacía? " + (list.isEmpty() ? "Sí" : "No"));
    }

    private void printElements() {
        if (list.isEmpty()) {
            System.out.println("Elementos: (la lista está vacía)");
        } else {
            System.out.println("Elementos: " + String.join(", ", list));
        }
    }

    private boolean askRepeat() {
        System.out.print("¿Desea repetir la operación? (s/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        while (!answer.equals("s") && !answer.equals("n")) {
            System.out.print("Respuesta inválida. Ingrese 's' o 'n': ");
            answer = scanner.nextLine().trim().toLowerCase();
        }

        return answer.equals("s");
    }
}
