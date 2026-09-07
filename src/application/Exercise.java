package application;

import java.util.Scanner;

public abstract class Exercise {

    protected boolean running = true;
    protected int currentPhase = 1;
    protected Scanner scanner;

    public Exercise(Scanner scanner) {
        this.scanner = scanner;
    }

    public void run() {
        running = true;
        currentPhase = 1;

        while (running) {
            exerciseLogic();
        }
    }

    protected abstract void exerciseLogic();

    protected void repeatOperationCheck(String promptMessage) {
        System.out.println(promptMessage);
        System.out.print("¿Desea repetir la operación? (s/n): ");
        String answer = scanner.nextLine().trim().toLowerCase();

        while (!answer.equals("s") && !answer.equals("n")) {
            System.out.print("Respuesta inválida. Ingrese 's' para sí o 'n' para no: ");
            answer = scanner.nextLine().trim().toLowerCase();
        }

        if (answer.equals("n")) {
            running = false;
        } else {
            currentPhase = 1;
        }
    }
}
