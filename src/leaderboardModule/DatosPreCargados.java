package leaderboardModule;

/** Datos de ejemplo para probar el leaderboard desde la opcion 6 del menu. */
final class DatosPreCargados {
    private DatosPreCargados() { }

    static ScoreEntry[] obtenerPartidas() {
        return new ScoreEntry[] {
                new ScoreEntry("Emilia", 90, 1500), new ScoreEntry("Santiago", 55, 300),
                new ScoreEntry("Valentina", 62, 480), new ScoreEntry("Sofia", 70, 720),
                new ScoreEntry("Camila", 48, 420), new ScoreEntry("Bautista", 35, 200),
                new ScoreEntry("Valentina", 50, 540), new ScoreEntry("Lucia", 40, 600),
                new ScoreEntry("Mateo", 40, 650), new ScoreEntry("Martina", 25, 400),
                new ScoreEntry("Thiago", 25, 400), new ScoreEntry("Agustina", 0, 95)
        };
    }
}
