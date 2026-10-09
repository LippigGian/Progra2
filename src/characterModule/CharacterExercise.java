package characterModule;

import java.util.Scanner;
import application.Exercise;
import listModule.MyList;
import setModule.MyArraySet;
import setModule.MySet;

public class CharacterExercise extends Exercise {
    private final MySet<Personaje> personajes = new MyArraySet<>();
    private final MySet<Habilidad> habilidades = new MyArraySet<>();

    public CharacterExercise(Scanner scanner) {
        super(scanner);
        Habilidad espada = new Habilidad("Espada");
        Habilidad arco = new Habilidad("Arco");
        Habilidad magia = new Habilidad("Magia");
        Habilidad sigilo = new Habilidad("Sigilo");
        habilidades.add(espada);
        habilidades.add(arco);
        habilidades.add(magia);
        habilidades.add(sigilo);
        Personaje link = new Personaje("Link");
        link.asignar(espada);
        link.asignar(arco);
        Personaje geralt = new Personaje("Geralt");
        geralt.asignar(espada);
        geralt.asignar(magia);
        Personaje lara = new Personaje("Lara Croft");
        lara.asignar(arco);
        lara.asignar(sigilo);
        personajes.add(link);
        personajes.add(geralt);
        personajes.add(lara);
        personajes.add(new Personaje("Novato"));
    }

    @Override
    protected void exerciseLogic() {
        System.out.println("\n===== PERSONAJES DE VIDEOJUEGOS - TDA SET =====");
        System.out.println("1 - Listar personajes y sus habilidades");
        System.out.println("2 - Listar habilidades disponibles");
        System.out.println("3 - Crear personaje");
        System.out.println("4 - Crear habilidad");
        System.out.println("5 - Asignar habilidad a un personaje");
        System.out.println("6 - Quitar habilidad de un personaje");
        System.out.println("7 - Comparar dos personajes");
        System.out.println("0 - Volver al menu principal");
        String opcion = leer("Seleccione una opcion: ");
        if (opcion == null) return;
        switch (opcion) {
            case "1":
                MyList<Personaje> lista = personajes.toList();
                for (int i = 0; i < lista.size(); i++) {
                    Personaje p = lista.get(i);
                    System.out.println(p + ": " + describir(p.getHabilidades()));
                }
                break;
            case "2": System.out.println("Catalogo: " + describir(habilidades)); break;
            case "3": crear(true); break;
            case "4": crear(false); break;
            case "5": modificar(true); break;
            case "6": modificar(false); break;
            case "7": comparar(); break;
            case "0": running = false; break;
            default: System.out.println("Opcion invalida. Ingrese un numero de 0 a 7.");
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

    private void crear(boolean personaje) {
        String nombre = leer("Nombre (0 para cancelar): ");
        if (nombre == null || nombre.equals("0")) return;
        if (nombre.isBlank()) {
            System.out.println("El nombre no puede estar vacio.");
            return;
        }
        if (personaje) registrar(personajes, new Personaje(nombre));
        else registrar(habilidades, new Habilidad(nombre));
    }

    private <T> void registrar(MySet<T> conjunto, T elemento) {
        if (conjunto.contains(elemento)) {
            System.out.println("Ya existe un registro con ese nombre.");
            return;
        }
        conjunto.add(elemento);
        System.out.println("Creado: " + elemento);
    }

    private <T> T seleccionar(String titulo, MySet<T> conjunto) {
        MyList<T> opciones = conjunto.toList();
        if (opciones.isEmpty()) {
            System.out.println("No hay opciones disponibles: " + titulo);
            return null;
        }
        System.out.println(titulo);
        for (int i = 0; i < opciones.size(); i++) {
            System.out.println((i + 1) + " - " + opciones.get(i));
        }
        while (running) {
            String entrada = leer("Numero (0 para cancelar): ");
            if (entrada == null || entrada.equals("0")) return null;
            try {
                int numero = Integer.parseInt(entrada);
                if (numero >= 1 && numero <= opciones.size()) return opciones.get(numero - 1);
            } catch (NumberFormatException e) {
                // Texto, decimales y enteros fuera del rango de int son entrada invalida.
            }
            System.out.println("Seleccion invalida. Ingrese un entero entre 0 y " + opciones.size() + ".");
        }
        return null;
    }

    private void modificar(boolean asignar) {
        Personaje personaje = seleccionar("Personaje:", personajes);
        if (personaje == null) return;
        MySet<Habilidad> opciones = asignar
                ? habilidades.difference(personaje.getHabilidades()) : personaje.getHabilidades();
        Habilidad habilidad = seleccionar(asignar ? "Habilidades sin asignar:" : "Habilidades asignadas:", opciones);
        if (habilidad == null) return;
        if (asignar) personaje.asignar(habilidad);
        else personaje.quitar(habilidad);
        System.out.println("Habilidad " + (asignar ? "asignada a " : "quitada de ") + personaje + ": " + habilidad);
    }

    private void comparar() {
        Personaje primero = seleccionar("Primer personaje:", personajes);
        if (primero == null) return;
        MySet<Personaje> restantes = personajes.union(new MyArraySet<>());
        restantes.remove(primero);
        Personaje segundo = seleccionar("Segundo personaje:", restantes);
        if (segundo == null) return;
        MySet<Habilidad> a = primero.getHabilidades();
        MySet<Habilidad> b = segundo.getHabilidades();
        System.out.println("Comparacion: " + primero + " / " + segundo);
        System.out.println("En comun (interseccion): " + describir(a.intersection(b)));
        System.out.println("Solo " + primero + " (A - B): " + describir(a.difference(b)));
        System.out.println("Solo " + segundo + " (B - A): " + describir(b.difference(a)));
        System.out.println("Todas las distintas (union): " + describir(a.union(b)));
    }

    private <T> String describir(MySet<T> conjunto) {
        return conjunto.isEmpty() ? "(ninguna)" : conjunto.toList().toString();
    }
}
