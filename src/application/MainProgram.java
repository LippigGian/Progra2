package application;

import java.util.Scanner;

import listModule.ListExercise;
import browserModule.BrowserExercise;
import linkedBrowserModule.LinkedBrowserExercise;
import playlistModule.PlaylistExercise;

public class MainProgram {

    private boolean running = true;
    private Exercise exercise;

    public static void main(String[] args) {
        MainProgram program = new MainProgram();
        program.run();
    }

    private void run() {
        Scanner scanner = new Scanner(System.in);

        while (running) {
            selectExercise(scanner);

            if (exercise != null) {
                exercise.run();
            }
        }

        scanner.close();
        System.out.println("El programa ha finalizado.");
    }

    private void selectExercise(Scanner scanner) {
        exercise = null;
        boolean validOption = false;

        while (!validOption) {
            System.out.println("\n===== Menu Principal =====");
            System.out.println("1 - TP 01: Ejercicio de prueba");
            System.out.println("2 - TP 02: TDA List de Java");
            System.out.println("3 - TP 03: Playlist musical");
            System.out.println("4 - TP 04: Historial de navegador web (Arrays)");
            System.out.println("5 - TP 04: Historial de navegador web (Linked nodes)");
            System.out.println("0 - Salir");
            System.out.print("Seleccione una opcion: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    exercise = new TestExercise(scanner);
                    validOption = true;
                    break;
                case "2":
                    exercise = new ListExercise(scanner);
                    validOption = true;
                    break;
                case "3":
                    exercise = new PlaylistExercise(scanner);
                    validOption = true;
                    break;
                case "4":
                    exercise = new BrowserExercise(scanner);
                    validOption = true;
                    break;
                case "5":
                    exercise = new LinkedBrowserExercise(scanner);
                    validOption = true;
                    break;
                case "0":
                    running = false;
                    validOption = true;
                    break;
                default:
                    System.out.println("Opcion invalida. Por favor, seleccione una opcion valida.");
            }
        }
    }
}
