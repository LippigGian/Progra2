package application;

import java.util.Scanner;

public class TestExercise extends Exercise {

    public TestExercise(Scanner scanner) {
        super(scanner);
    }

    @Override
    protected void exerciseLogic() {
        System.out.println("\n===== Bienvenido al Ejercicio de Prueba =====");
        System.out.println("Este ejercicio solo es para verificar que el programa funciona correctamente.");
        System.out.println("Fase actual: " + currentPhase);

        repeatOperationCheck("Fin de la operación de prueba.");
    }
}
