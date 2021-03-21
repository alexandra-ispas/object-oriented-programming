package fileio;

import java.util.ArrayList;
import java.util.List;

/**
 * Information about a movie, retrieved from parsing the input test files
 * <p>
 * DO NOT MODIFY
 */
public final class MovieInputData extends ShowInput {
    /**
     * Duration in minutes of a season
     */
    private final int duration;
    /**
     * lista de rating-uri date filmului
     */
    private final List<Double> ratings;
    /**
     * lista cu username-urile celor care au dat rating
     */
    private final ArrayList<String> usersWhoRated;

    public MovieInputData(final String title, final ArrayList<String> cast,
                          final ArrayList<String> genres, final int year,
                          final int duration) {
        super(title, year, cast, genres);
        this.duration = duration;
        this.ratings = new ArrayList<>();
        this.usersWhoRated = new ArrayList<>();
    }

    public ArrayList<String> getUsersWhoRated() {
        return usersWhoRated;
    }

    /**
     * @param usersname
     */
    public void setUsersWhoRated(final String usersname) {
        this.usersWhoRated.add(usersname);
    }

    public List<Double> getRatings() {
        return ratings;
    }

    /**
     * @param ratings
     */
    public void setRatings(final Double ratings) {
        this.ratings.add(ratings);
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "MovieInputData{" + "title= "
                + super.getTitle() + "year= "
                + super.getYear() + "duration= "
                + duration + "cast {"
                + super.getCast() + " }\n"
                + "genres {" + super.getGenres() + " }\n ";
    }
}
