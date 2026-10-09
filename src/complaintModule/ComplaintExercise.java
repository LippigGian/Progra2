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
        cargarDatosDeEjemplo();
    }

    /**
     * Datos de ejemplo cargados a proposito en desorden de urgencia, y con
     * urgencias repetidas, para verificar que la cola:
     *  - atiende siempre primero el mas urgente, y
     *  - entre reclamos de igual urgencia respeta el orden de llegada (FIFO).
     *
     * Orden de atencion esperado:
     *  1. Servidor de archivos caido      (Critico, llego 3ro)
     *  2. Sin internet en sucursal Norte  (Critico, llego 5to)
     *  3. No llegan los correos           (Alto)
     *  4. Impresora sin toner             (Medio, llego 1ro)
     *  5. Teclado con teclas trabadas     (Medio, llego 6to)
     *  6. Silla rota en recepcion         (Bajo)
     */
    private void cargarDatosDeEjemplo() {
        agregar("Impresora sin toner",
                "La impresora del segundo piso no imprime por falta de toner.", Urgencia.MEDIO);
        agregar("Silla rota en recepcion",
                "Una de las sillas de recepcion tiene una rueda rota.", Urgencia.BAJO);
        agregar("Servidor de archivos caido",
                "Nadie puede acceder a las carpetas compartidas desde las 9 hs.", Urgencia.CRITICO);
        agregar("No llegan los correos",
                "El area de ventas no recibe mails externos desde ayer.", Urgencia.ALTO);
        agregar("Sin internet en sucursal Norte",
                "La sucursal Norte quedo sin conexion y no puede facturar.", Urgencia.CRITICO);
        agregar("Teclado con teclas trabadas",
                "En administracion un teclado tiene varias teclas que no responden.", Urgencia.MEDIO);
    }

    private void agregar(String titulo, String descripcion, Urgencia urgencia) {
        reclamos.enqueue(new Reclamo(titulo, descripcion, urgencia), urgencia.getPrioridad());
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
        // Atiende reclamos en cadena mientras el usuario quiera continuar.
        while (running) {
            if (reclamos.isEmpty()) {
                System.out.println("No hay reclamos pendientes.");
                return;
            }
            //Muestra siempre el reclamo con mayor prioridad (urgencia) sin eliminarlo de la cola.
            Reclamo siguiente = reclamos.peek();
            System.out.println("\n===== SIGUIENTE RECLAMO =====");
            System.out.println(siguiente);

            Boolean resuelto = preguntarSiNo("¿Marcar como resuelto? (s/n): ");
            if (resuelto == null) {
                return;
            }
            if (!resuelto) {
                System.out.println("El reclamo permanece pendiente en la cola.");
                Boolean reabrir = preguntarSiNo(
                        "¿Quieres volver a abrir el ticket? (s = volver a abrir / n = volver al menu de reclamos): ");
                if (reabrir == null) {
                    return;
                }
                if (!reabrir) {
                    // Vuelve al menu de reclamos (exerciseLogic se vuelve a ejecutar).
                    return;
                }

                continue;
            }

            reclamos.dequeue();
            System.out.println("Reclamo resuelto y eliminado de la cola.");

            if (reclamos.isEmpty()) {
                System.out.println("No quedan reclamos pendientes.");
                return;
            }

            System.out.println("Reclamos pendientes: " + reclamos.size());
            Boolean continuar = preguntarSiNo(
                    "¿Desea continuar con el siguiente reclamo? (s = continuar / n = volver al menu de reclamos): ");
            if (continuar == null) {
                return;
            }
            if (!continuar) {
                // Vuelve al menu de reclamos (exerciseLogic se vuelve a ejecutar).
                return;
            }
            // Si continua, el while vuelve a mostrar el siguiente reclamo.
        }
    }

    /** Pide una respuesta s/n hasta que sea valida. Devuelve null si se corta la entrada. */
    private Boolean preguntarSiNo(String mensaje) {
        while (running) {
            String opcion = leer(mensaje);
            if (opcion == null) {
                return null;
            }
            if (opcion.equalsIgnoreCase("s")) {
                return true;
            }
            if (opcion.equalsIgnoreCase("n")) {
                return false;
            }
            System.out.println("Respuesta invalida. Ingrese 's' o 'n'.");
        }
        return null;
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
