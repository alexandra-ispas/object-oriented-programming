package entertainment;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a season of a tv show
 * <p>
 * DO NOT MODIFY
 */
public final class Season {
    /**
     * Number of current season
     */
    private final int currentSeason;
    /**
     * username-urile de la user-ii care au dat rate
     */
    private final ArrayList<String> usersWhoRated;
    /**
     * Duration in minutes of a season
     */
    private int duration;
    /**
     * List of ratings for each season
     */
    private List<Double> ratings;

    public Season(final int currentSeason, final int duration) {
        this.currentSeason = currentSeason;
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.usersWhoRated = new ArrayList<>();
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(final int duration) {
        this.duration = duration;
    }

    public List<Double> getRatings() {
        return ratings;
    }

    public void setRatings(final List<Double> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Episode{"
                + "currentSeason="
                + currentSeason
                + ", duration="
                + duration
                + '}';
    }

    /**
     * @return
     */
    public ArrayList<String> getUsersWhoRated() {
        return usersWhoRated;
    }

    /**
     * @param user
     */
    public void setUsersWhoRated(final String user) {
        this.usersWhoRated.add(user);
    }

    /**
     * calculeaza rating-ul final facand media aritmetica
     *
     * @return
     */
    public double getRatingSeason() {

        double sum = 0;

        //daca nu este niciun rating dat
        if (ratings.size() == 0) {
            return 0;
        }
        //iterez prin rating-uri
        for (int i = 0; i < ratings.size(); i++) {
            sum += ratings.get(i);
        }

        return sum / ratings.size();
    }
}

