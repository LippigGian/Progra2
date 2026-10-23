package leaderboardModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import treeModule.BST;

/** Aplicacion de consola de leaderboard que usa el TDA BST. */
public class LeaderboardApp {
    private static final int TOP_SIZE = 10;
    private final BST<ScoreEntry> leaderboard = new BST<>();
    private final ConsoleInput input;

    public LeaderboardApp(Scanner scanner) {
        input = new ConsoleInput(scanner);
    }

    public static void main(String[] args) {
        new LeaderboardApp(new Scanner(System.in)).run();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int option = input.readInt("Seleccione una opcion: ", 0, 7);
            if (option == -1) return;

            switch (option) {
                case 1: registerMatch(); break;
                case 2: showRanking(Integer.MAX_VALUE); break;
                case 3: showRanking(TOP_SIZE); break;
                case 4: searchPlayer(); break;
                case 5: deleteEntry(); break;
                case 6: cargarDatosPreCargados(); break;
                case 7: clearLeaderboard(); break;
                case 0: running = false; break;
                default: break;
            }
        }
    }
    //Menu
    private void printMenu() {
        System.out.println("\n===== TP 09: LEADERBOARD (BST) =====");
        System.out.println("Registros: " + leaderboard.size());
        System.out.println("1 - Registrar partida");
        System.out.println("2 - Ver leaderboard completo");
        System.out.println("3 - Ver top " + TOP_SIZE);
        System.out.println("4 - Buscar puntajes de un jugador");
        System.out.println("5 - Eliminar un puntaje");
        System.out.println("6 - Cargar datos de prueba");
        System.out.println("7 - Vaciar leaderboard");
        System.out.println("0 - Volver al menu principal");
    }
    //Registrar nueva partida con su pumtaje.
    private void registerMatch() {
        String name = input.readName("Nombre del jugador: ");
        if (name == null) return;
        //Utiliza readInt que valida el rango de las kills
        int kills = input.readInt("Enemigos eliminados (0 a " + ScoreEntry.MAX_KILLS + "): ", 0, ScoreEntry.MAX_KILLS);
        if (kills == -1) return;
        // Utiliza readInt para validar el rango de los segundos.
        int seconds = input.readInt("Tiempo en segundos (" + ScoreEntry.MIN_SECONDS + " a "
                + ScoreEntry.MAX_SECONDS + "): ", ScoreEntry.MIN_SECONDS, ScoreEntry.MAX_SECONDS);
        if (seconds == -1) return;

        ScoreEntry entry = new ScoreEntry(name, kills, seconds);
        if (leaderboard.contains(entry)) {
            System.out.println("Ese jugador ya tiene registrado ese puntaje.");
            return;
        }
        leaderboard.insert(entry);
        System.out.println("Partida registrada: " + entry.getScore() + " puntos, posicion #" + rankOf(entry) + ".");
    }

    //Mostrar ranking segun el limite que elijamos.
    private void showRanking(int limit) {
        if (leaderboard.isEmpty()) {
            printEmptyMessage();
            return;
        }
        //Ordena los puntajes con inOrder de BST.
        List<ScoreEntry> entries = leaderboard.inOrder();

        int[] ranks = computeRanks(entries);
        //Limite para mostrar el top 10 o el leaderboard completo.
        int shown = Math.min(limit, entries.size());
        System.out.println(limit == Integer.MAX_VALUE ? "=== Leaderboard completo ===" : "=== Top " + TOP_SIZE + " ===");
        printTableHeader();
        for (int i = 0; i < shown; i++) printTableRow(ranks[i], entries.get(i));
    }

    //Busca jugador y muestra todos sus puntajes.
    private void searchPlayer() {
        if (leaderboard.isEmpty()) {
            printEmptyMessage();
            return;
        }
        String name = input.readName("Nombre del jugador: ");
        if (name == null) return;
        List<ScoreEntry> entries = leaderboard.inOrder();
        int[] ranks = computeRanks(entries);
        boolean found = false;
        for (int i = 0; i < entries.size(); i++) {
            //El belongsTo compara el nombre ingresado con el nombre de una partida especifica.
            if (entries.get(i).belongsTo(name)) {
                if (!found) printTableHeader();
                printTableRow(ranks[i], entries.get(i));
                found = true;
            }
        }
        if (!found) System.out.println("No hay puntajes para " + name + ".");
    }
    //Borrar un puntaje especifico de un jugador.
    private void deleteEntry() {
        if (leaderboard.isEmpty()) {
            printEmptyMessage();
            return;
        }
        String name = input.readName("Nombre del jugador: ");
        if (name == null) return;
        List<ScoreEntry> matches = findByPlayer(name);
        if (matches.isEmpty()) {
            System.out.println("No hay puntajes para " + name + ".");
            return;
        }
        for (int i = 0; i < matches.size(); i++) {
            ScoreEntry entry = matches.get(i);
            System.out.println((i + 1) + " - " + entry.getScore() + " puntos, " + entry.getKills()
                    + " bajas en " + entry.getSeconds() + " segundos.");
        }
        int option = input.readInt("Elija el puntaje a eliminar (0 para cancelar): ", 0, matches.size());
        if (option <= 0) return;
        ScoreEntry selected = matches.get(option - 1);
        if (input.readYesNo("¿Eliminar este puntaje? (s/n): ")) {
            leaderboard.remove(selected);
            System.out.println("Puntaje eliminado.");
        }
    }

    //Cargar datos pre-cargados de prueba.
    private void cargarDatosPreCargados() {
        int loaded = 0;
        for (ScoreEntry entry : DatosPreCargados.obtenerPartidas()) {
            if (!leaderboard.contains(entry)) {
                leaderboard.insert(entry);
                loaded++;
            }
        }
        System.out.println(loaded + " datos de prueba cargados.");
    }
    //Eliminar todos los registros del leaderboard con .Clear de BST.
    private void clearLeaderboard() {
        if (leaderboard.isEmpty()) {
            printEmptyMessage();
        } else if (input.readYesNo("¿Vaciar los " + leaderboard.size() + " registros? (s/n): ")) {
            leaderboard.clear();
            System.out.println("Leaderboard vaciado.");
        }
    }


  //Recibe la lista ordenada y les asigna el rank a cada uno, teniendo en cuenta los empates.
    private int[] computeRanks(List<ScoreEntry> entries) {
        int[] ranks = new int[entries.size()];

        for (int i = 0; i < entries.size(); i++) {
            if (i > 0 && entries.get(i).getScore() == entries.get(i - 1).getScore()) {
                ranks[i] = ranks[i - 1];
            } else {
                ranks[i] = i + 1;
            }
        }

        return ranks;
    }
  

    //Devuelve la posicion de una partida
    private int rankOf(ScoreEntry entry) {
        List<ScoreEntry> entries = leaderboard.inOrder();
        int[] ranks = computeRanks(entries);
        for (int i = 0; i < entries.size(); i++) if (entries.get(i).equals(entry)) return ranks[i];
        return -1;
    }

    //Buscar en todas las partidas un jugador especifico y devuelve una lista con todas sus partidas.
    private List<ScoreEntry> findByPlayer(String name) {
        List<ScoreEntry> result = new ArrayList<>();
        for (ScoreEntry entry : leaderboard.inOrder()) if (entry.belongsTo(name)) result.add(entry);
        return result;
    }

    //Print de la tabla de puntajes, con encabezado y filas.
    private void printTableHeader() {
        System.out.printf("%-5s %-20s %8s %6s %11s%n", "Pos", "Jugador", "Puntaje", "Bajas", "Tiempo (s)");
        System.out.println("----- -------------------- -------- ------ ---------");
    }

    //Print de cada fila de la tabla, con el rank y los datos de la partida.
    private void printTableRow(int rank, ScoreEntry entry) {
        System.out.printf("#%-4d %-20s %8d %6d %11d%n", rank, entry.getPlayerName(), entry.getScore(),
                entry.getKills(), entry.getSeconds());
    }

    private void printEmptyMessage() {
        System.out.println("El leaderboard esta vacio. Registre una partida o cargue datos de prueba.");
    }
}
