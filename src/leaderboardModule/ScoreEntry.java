package leaderboardModule;

import java.util.Locale;

/** Registro inmutable de una partida, ordenado por puntaje descendente. */
public final class ScoreEntry implements Comparable<ScoreEntry> {
    public static final int MAX_NAME_LENGTH = 20;
    public static final int MAX_KILLS = 9999;
    public static final int MIN_SECONDS = 1;
    public static final int MAX_SECONDS = 24 * 60 * 60;
    public static final int POINTS_PER_KILL = 100;
    public static final int REFERENCE_SECONDS = 600;

    private final String playerName;
    private final int kills;
    private final int seconds;
    private final int score;

    public ScoreEntry(String playerName, int kills, int seconds) {
        String cleanName = cleanName(playerName);
        if (cleanName.isEmpty() || cleanName.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Nombre invalido.");
        }
        if (kills < 0 || kills > MAX_KILLS || seconds < MIN_SECONDS || seconds > MAX_SECONDS) {
            throw new IllegalArgumentException("Datos de partida fuera de rango.");
        }
        this.playerName = cleanName;
        this.kills = kills;
        this.seconds = seconds;
        this.score = calculateScore(kills, seconds);
    }

    public static String cleanName(String name) {
        return name == null ? "" : name.strip().replaceAll("\\s+", " ");
    }

    public static int calculateScore(int kills, int seconds) {
        double multiplier = 1.0 + Math.max(0, REFERENCE_SECONDS - seconds) / (double) REFERENCE_SECONDS;
        return (int) Math.round(kills * POINTS_PER_KILL * multiplier);
    }

    public boolean belongsTo(String name) {
        return playerName.equalsIgnoreCase(cleanName(name));
    }

    public String getPlayerName() { return playerName; }
    public int getKills() { return kills; }
    public int getSeconds() { return seconds; }
    public int getScore() { return score; }

    @Override
    public int compareTo(ScoreEntry other) {
        int byScore = Integer.compare(other.score, score);
        return byScore != 0 ? byScore : playerName.compareToIgnoreCase(other.playerName);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ScoreEntry)) return false;
        ScoreEntry other = (ScoreEntry) object;
        return score == other.score && playerName.equalsIgnoreCase(other.playerName);
    }

    @Override
    public int hashCode() {
        return 31 * score + playerName.toLowerCase(Locale.ROOT).hashCode();
    }
}
