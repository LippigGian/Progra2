package leaderboardModule;

import java.util.Scanner;
import application.Exercise;

/** Adaptador para ejecutar el leaderboard desde el menu principal del proyecto. */
public class LeaderboardExercise extends Exercise {
    private final LeaderboardApp app;

    public LeaderboardExercise(Scanner scanner) {
        super(scanner);
        app = new LeaderboardApp(scanner);
    }

    @Override
    protected void exerciseLogic() {
        app.run();
        running = false;
    }
}
