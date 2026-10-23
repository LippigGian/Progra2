package leaderboardModule;

import java.util.Scanner;

/** Lectura de consola con validacion y sin excepciones por texto invalido. */
public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) return null;
        return scanner.nextLine();
    }

    //Lee los input cargados por consola para crear un nuevo ScoreEntry.
    public int readInt(String prompt, int min, int max) {
        while (true) {
            String raw = readLine(prompt);
            if (raw == null) return min - 1;
            try {
                int value = Integer.parseInt(raw.trim());
                if (value >= min && value <= max) return value;
            } catch (NumberFormatException e) {
                // Se informa abajo y se vuelve a pedir.
            }
            System.out.println("Ingrese un numero entero entre " + min + " y " + max + ".");
        }
    }

    //Lee los nombres ingresados
    public String readName(String prompt) {
        while (true) {
            String name = readLine(prompt);
            if (name == null) return null;
            name = ScoreEntry.cleanName(name);
            if (!name.isEmpty() && name.length() <= ScoreEntry.MAX_NAME_LENGTH) return name;
            System.out.println("El nombre debe tener entre 1 y " + ScoreEntry.MAX_NAME_LENGTH + " caracteres.");
        }
    }
    public boolean readYesNo(String prompt) {
        while (true) {
            String answer = readLine(prompt);
            if (answer == null) return false;
            answer = answer.strip().toLowerCase();
            if (answer.equals("s") || answer.equals("si") || answer.equals("sí")) return true;
            if (answer.equals("n") || answer.equals("no")) return false;
            System.out.println("Responda s o n.");
        }
    }
}
