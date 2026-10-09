    package complaintModule;

import application.Exercise;
import java.util.Scanner;
import priorityQueueModule.SimpleArrayPriorityQueue;
import priorityQueueModule.SimplePriorityQueue;

/** Consola de atencion de reclamos basada en una cola de prioridad propia. */
public class ComplaintExercise extends Exercise {
    private final SimplePriorityQueue<Reclamo> reclamos = new SimpleArrayPriorityQueue<>();

    public ComplaintExercise(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void exerciseLogic() {
        System.out.println("\n===== TP 07: ATENCION DE RECLAMOS =====");
        System.out.println("Reclamos pendientes: " + reclamos.size());
        System.out.println("1 - Redactar un reclamo");
        System.out.println("2 - Visualizar el siguiente reclamo");
        System.out.println("0 - Volver al menu principal");

        String opcion = leer("Seleccione una opcion: ");
        if (opcion == null) {
            return;
        }
        switch (opcion) {
            case "1":
                redactarReclamo();
                break;
            case "2":
                visualizarReclamo();
                break;
            case "0":
                running = false;
                break;
            default:
                System.out.println("Opcion invalida. Ingrese 0, 1 o 2.");
        }
    }

    private void redactarReclamo() {
        String titulo = leer("Titulo (0 para cancelar): ");
        if (titulo == null || titulo.equals("0")) {
            return;
        }
        if (titulo.isBlank()) {
            System.out.println("El titulo no puede estar vacio.");
            return;
        }

        String descripcion = leer("Descripcion: ");
        if (descripcion == null) {
            return;
        }
        if (descripcion.isBlank()) {
            System.out.println("La descripcion no puede estar vacia.");
            return;
        }

        Urgencia urgencia = seleccionarUrgencia();
        if (urgencia == null) {
            return;
        }

        Reclamo reclamo = new Reclamo(titulo, descripcion, urgencia);
        //Encola un nuevo reclamo con la prioridad correspondiente a la urgencia seleccionada.
        reclamos.enqueue(reclamo, urgencia.getPrioridad());
        System.out.println("Reclamo registrado con urgencia " + urgencia + ".");
    }

    private Urgencia seleccionarUrgencia() {
        System.out.println("Urgencia:");
        System.out.println("1 - Critico");
        System.out.println("2 - Alto");
        System.out.println("3 - Medio");
        System.out.println("4 - Bajo");

        while (running) {
            String opcion = leer("Seleccione una urgencia (0 para cancelar): ");
            if (opcion == null || opcion.equals("0")) {
                return null;
            }
            switch (opcion) {
                case "1": return Urgencia.CRITICO;
                case "2": return Urgencia.ALTO;
                case "3": return Urgencia.MEDIO;
                case "4": return Urgencia.BAJO;
                default:
                    System.out.println("Urgencia invalida. Ingrese un numero entre 0 y 4.");
            }
        }
        return null;
    }

    private void visualizarReclamo() {
        if (reclamos.isEmpty()) {
            System.out.println("No hay reclamos pendientes.");
            return;
        }
        //Muestra siempre el reclamo con mayor prioridad (urgencia) sin eliminarlo de la cola.
        Reclamo siguiente = reclamos.peek();
        System.out.println("\n===== SIGUIENTE RECLAMO =====");
        System.out.println(siguiente);

        while (running) {
            String opcion = leer("¿Marcar como resuelto? (s/n): ");
            if (opcion == null) {
                return;
            }
            if (opcion.equalsIgnoreCase("s")) {
                reclamos.dequeue();
                System.out.println("Reclamo resuelto y eliminado de la cola.");
                return;
            }
            if (opcion.equalsIgnoreCase("n")) {
                System.out.println("El reclamo permanece pendiente en la cola.");
                return;
            }
            System.out.println("Respuesta invalida. Ingrese 's' o 'n'.");
        }
    }

    private String leer(String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            running = false;
            return null;
        }
        return scanner.nextLine().strip();
    }
}
