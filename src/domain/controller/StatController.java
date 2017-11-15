package domain.controller;

import domain.controller.data.DataController;
import persistence.model.StatDataModel;

import java.util.HashMap;

/**
 * The type Stat controller.
 */
public class StatController {
    private static final StatController mInstance = new StatController();
    private final DataController statDataController = StatDataModel.getInstance();

    private static HashMap<String, Long> pointRanking;
    private static HashMap<String, Long> timeRanking;

    private StatController() {
        pointRanking = (HashMap<String, Long>) statDataController.get("pointRanking");
        timeRanking = (HashMap<String, Long>) statDataController.get("timeRanking");

        if (pointRanking == null) pointRanking = new HashMap<>();
        if (timeRanking == null) timeRanking = new HashMap<>();
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static StatController getInstance() {
        return mInstance;
    }

    /**
     * Add score.
     *
     * @param userName  the user name
     * @param gameTitle the game title
     * @param points    the points
     * @param time      the time
     */
    public void addScore(String userName, String gameTitle, long points, long time) {
        if (pointRanking.containsKey(userName)) {
            pointRanking.replace(userName, pointRanking.get(userName) + points);
        } else {
            pointRanking.put(userName, points);
        }
        timeRanking.put(gameTitle, time);

        statDataController.replace("pointRanking", pointRanking);
        statDataController.replace("timeRanking", timeRanking);
    }

    public HashMap<String, Long> getPointRanking() {
        return pointRanking;
    }

    public HashMap<String, Long> getTimeRanking() {
        return timeRanking;
    }
}


