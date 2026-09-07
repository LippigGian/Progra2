import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import browserModule.BrowserExercise;
import browserModule.Navegador;
import stackModule.MyArrayStack;
import stackModule.MyStack;

/** Pruebas sin dependencias externas. Ejecutar con java -cp out BrowserStackTest. */
public class BrowserStackTest {
    public static void main(String[] args) {
        MyStack<Integer> stack = new MyArrayStack<>();
        check(stack.isEmpty() && stack.size() == 0, "Pila inicial");
        expect(IllegalStateException.class, () -> stack.pop());
        expect(IllegalStateException.class, () -> stack.peek());
        expect(IllegalArgumentException.class, () -> stack.push(null));
        for (int i = 0; i < 100; i++) {
            stack.push(i);
        }
        check(stack.peek() == 99 && stack.size() == 100, "Crecimiento y peek no destructivo");
        for (int i = 99; i >= 0; i--) {
            check(stack.pop() == i, "Orden LIFO tras crecer");
        }
        stack.push(1);
        stack.push(2);
        stack.clear();
        stack.clear();
        check(stack.isEmpty() && stack.size() == 0, "clear repetido");
        stack.push(3);
        check(stack.pop() == 3 && stack.isEmpty(), "Reutilizar pila");

        Navegador browser = new Navegador();
        check(browser.getPaginaActual() == null, "Sin pagina inicial");
        check(!browser.retroceder() && !browser.avanzar(), "Limites iniciales");
        browser.visitar(" A ");
        check(browser.getPaginaActual().equals("A") && !browser.retroceder(), "Primera visita");
        browser.visitar("B");
        browser.visitar("C");
        check(browser.retroceder() && browser.getPaginaActual().equals("B"), "C a B");
        check(browser.retroceder() && browser.getPaginaActual().equals("A"), "B a A");
        check(!browser.retroceder() && browser.getPaginaActual().equals("A"), "Limite atras");
        check(browser.avanzar() && browser.getPaginaActual().equals("B"), "A a B");
        expect(IllegalArgumentException.class, () -> browser.visitar("   "));
        expect(IllegalArgumentException.class, () -> browser.visitar(null));
        check(browser.getPaginaActual().equals("B") && browser.puedeAvanzar(), "Visita invalida preserva historial");
        check(browser.avanzar() && browser.getPaginaActual().equals("C"), "B a C");
        check(!browser.avanzar() && browser.getPaginaActual().equals("C"), "Limite adelante");
        browser.retroceder();
        browser.visitar("D");
        check(!browser.puedeAvanzar(), "Visita nueva elimina futuro");
        check(browser.retroceder() && browser.getPaginaActual().equals("B"), "Conserva B");
        check(browser.retroceder() && browser.getPaginaActual().equals("A"), "Conserva A");
        browser.avanzar();
        check(browser.avanzar() && browser.getPaginaActual().equals("D"), "Futuro nuevo D");
        browser.visitar("D");
        check(browser.retroceder() && browser.getPaginaActual().equals("D"), "Visitas repetidas");

        // La consola debe recuperarse de opciones y nombres invalidos sin perder el estado.
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (Scanner input = new Scanner("2\n3\nabc\n-1\n\n1\n   \n1\nA\n1\nB\n2\n3\n0\n");
             PrintStream captured = new PrintStream(output)) {
            System.setOut(captured);
            new BrowserExercise(input).run();
        } finally {
            System.setOut(original);
        }
        String console = output.toString();
        check(console.contains("Opcion invalida"), "Opcion invalida informada");
        check(console.contains("no puede estar vacio"), "Nombre vacio informado");
        check(console.contains("Pagina actual: A") && console.contains("Pagina actual: B"), "Consola sigue navegando");
        System.out.println("OK: Stack, historial y validaciones de consola.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expect(Class<? extends RuntimeException> type, Runnable operation) {
        try {
            operation.run();
        } catch (RuntimeException e) {
            if (type.isInstance(e)) {
                return;
            }
            throw e;
        }
        throw new AssertionError("Se esperaba " + type.getSimpleName());
    }
}
