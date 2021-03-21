package video;

import common.Constants;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.UserInputData;
import user.UserMethods;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public final class MovieMethods {

    private MovieMethods() {
    }

    /**
     * da rate unui film
     *
     * @param movie
     * @param rate
     * @param user
     * @return
     */
    public static String rateMovie(final MovieInputData movie, final double rate,
                                   final UserInputData user) {
        /**
         * verifica daca a mai dat rate acelui film
         */
        if (movie.getUsersWhoRated().contains(user.getUsername())) {
            return ("error -> " + movie.getTitle() + " has been already rated");
        }

        //verifica daca a vazut filmul
        if (UserMethods.isSeen(movie.getTitle(), user) != null) {

            //adauga valoarea data
            movie.setRatings(rate);

            //adauga user-ul in lista celoar care au dat rate
            movie.getUsersWhoRated().add(user.getUsername());

            //incrementeaza numarul de rating-uri date de user
            user.setNumberOfRatings(user.getNumberOfRatings() + 1);

            return ("success -> " + movie.getTitle() + " was rated with " + rate + " by "
                    + user.getUsername());
        }
        //filmul nu este vazut
        return ("error -> " + movie.getTitle() + " is not seen");

    }

    /**
     * intoarce filmul corespunzator unui titlu dat
     *
     * @param input
     * @param title
     * @return
     */
    public static MovieInputData getMovie(final Input input, final String title) {

        //parcurg filmele
        for (MovieInputData movie : input.getMovies()) {
            if (movie.getTitle().equals(title)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * calculeaza rating-ul final al unui film
     *
     * @param movie
     * @return
     */
    public static double getRatingMovie(final MovieInputData movie) {
        double sum = 0;

        //daca nu este dat niciun rating
        if (movie.getRatings().size() == 0) {
            return 0;
        }

        //calculeaza media
        for (int i = 0; i < movie.getRatings().size(); i++) {
            sum += movie.getRatings().get(i);
        }
        return sum / movie.getRatings().size();
    }

    /**
     * face un map cu rating-uri si titlurile filmeolor asociate
     *
     * @param moviesData
     * @param action
     * @param type
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> movieRatings(
            final List<MovieInputData> moviesData,
            final ActionInputData action,
            final String type) {
        TreeMap<Double, ArrayList<String>> movies = new TreeMap<>();

        for (MovieInputData movie : moviesData) {

            if (VideoMethods
                    .checkFilters(action.getFilters(), movie.getYear(), movie.getGenres())) {

                double rate = getRatingMovie(movie);
                if (type.equals(Constants.QUERY)) {
                    if (rate != 0) {
                        Utils.addToMap(movies, rate, movie.getTitle());
                    }
                } else {
                    Utils.addToMap(movies, rate, movie.getTitle());
                }
            }
        }
        if (action.getSortType() == null) {
            return Utils.sortMap(movies, Constants.NONE);
        }
        return Utils.sortMap(movies, action.getSortType());
    }

    /**
     * calculeaza de cate ori apare un film in lista de favorite a userilor
     *
     * @param users
     * @param movie
     * @return
     */
    public static double countFavorite(final List<UserInputData> users,
                                       final MovieInputData movie) {
        double nr = 0;
        for (UserInputData user : users) {
            if (user.getFavoriteMovies().contains(movie.getTitle())) {
                nr++;
            }
        }
        return nr;
    }

    /**
     * creeaza un map cu numarul de aparitii in lista de favorite
     * si numele filmelor corespunzatoare numarului
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> favoriteMovie(final Input input,
                                                                   final ActionInputData action) {

        TreeMap<Double, ArrayList<String>> movies = new TreeMap<>();

        for (MovieInputData movie : input.getMovies()) {
            //filmul trebuie as respecte si conditiile din filters
            if (VideoMethods
                    .checkFilters(action.getFilters(), movie.getYear(), movie.getGenres())) {

                double number = countFavorite(input.getUsers(), movie);

                if (number != 0) {
                    Utils.addToMap(movies, number, movie.getTitle());
                }
            }
        }
        return Utils.sortMap(movies, action.getSortType());
    }

    /**
     * map cu durate si numele filmelor corespunzatoare
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> longestMovie(final Input input,
                                                                  final ActionInputData action) {
        TreeMap<Double, ArrayList<String>> movies = new TreeMap<>();

        for (MovieInputData movie : input.getMovies()) {

            //daca filmul verifica filtrele cerute
            if (VideoMethods
                    .checkFilters(action.getFilters(), movie.getYear(), movie.getGenres())) {

                double duration = movie.getDuration();
                Utils.addToMap(movies, duration, movie.getTitle());
            }
        }
        //se sorteaza
        return Utils.sortMap(movies, action.getSortType());
    }

    /**
     * map cu filmele si vizualizarile lor
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> movieViews(final Input input,
                                                                final ActionInputData action) {
        TreeMap<Double, ArrayList<String>> movies = new TreeMap<>();

        for (MovieInputData movie : input.getMovies()) {

            //se verifica filtrele
            if (VideoMethods
                    .checkFilters(action.getFilters(), movie.getYear(), movie.getGenres())) {

                double number = VideoMethods.videoViewsTotal(movie.getTitle(), input.getUsers());

                if (number != 0) {
                    Utils.addToMap(movies, number, movie.getTitle());
                }
            }
        }
        return Utils.sortMap(movies, action.getSortType());
    }

    /**
     * map pentru best unseen recommendation
     * contine rating ul si titlurile filmelor corespunzatoare
     * care respecta genul cerut
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> unseenMovie(final Input input,
                                                                 final ActionInputData action) {

        TreeMap<Double, ArrayList<String>> movies = new TreeMap<>();

        for (MovieInputData movie : input.getMovies()) {

            //verifica daca au genul cerut
            if (movie.getGenres().contains(action.getGenre())) {

                double rate = getRatingMovie(movie);
                Utils.addToMap(movies, rate, movie.getTitle());
            }
        }

        //nu sorteaza map-ul
        return Utils.sortMap(movies, Constants.NONE);
    }
}
