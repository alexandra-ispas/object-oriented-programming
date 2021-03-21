package video;


import common.Constants;
import entertainment.Season;
import fileio.ActionInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import user.UserMethods;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public final class VideoMethods {

    private VideoMethods() {
    }

    /**
     * verifica daca un video este serial sau nu
     *
     * @param action
     * @return
     */
    public static boolean isShow(final ActionInputData action) {
        return action.getSeasonNumber() != 0;
    }

    /**
     * da rate unui video, fara a sti daca este film sau serual
     *
     * @param action
     * @param input
     * @return
     */
    public static String rateVideo(final ActionInputData action, final Input input) {

        String s;

        //daca este serial
        if (isShow(action)) {

            SerialInputData serial = ShowMethods.getSerial(input, action.getTitle());

            Season season = serial.getSeasons().get(action.getSeasonNumber() - 1);

            s = ShowMethods.rateSeason(season, action.getGrade(),
                    UserMethods.getUser(action.getUsername(), input.getUsers()), action.getTitle());

            //daca este film
        } else {
            MovieInputData movie = MovieMethods.getMovie(input, action.getTitle());

            s = MovieMethods.rateMovie(movie, action.getGrade(),
                    UserMethods.getUser(action.getUsername(), input.getUsers()));
        }
        return s;
    }

    /**
     * verifica daca anul si genurile unui video se incadreaza in filtre
     *
     * @param filters
     * @param year
     * @param genre
     * @return
     */
    public static boolean checkFilters(final List<List<String>> filters, final int year,
                                       final ArrayList<String> genre) {
        int yearOk = 0;
        int genre0k = 0;

        ArrayList<String> compare = new ArrayList<>();

        //verific daca am filtru pentru gen
        if ((filters.equals(compare)) || (filters.get(0) == null)
                || ((filters.get(0).size() == 1) && (filters.get(0).get(0) == null))) {
            yearOk = 1;

            //verific daca se respecta genul
        } else {
            if (filters.get(0).get(0).equals("" + year)) {
                yearOk = 1;
            }
        }
        //verific daca am filtru pentru an
        if ((filters.equals(compare)) || (filters.get(1) == null)
                || ((filters.get(1).size() == 1) && (filters.get(1).get(0) == null))) {
            genre0k = 1;

            //verific daca este anul cerut
        } else {
            if (genre.contains(filters.get(1).get(0))) {
                genre0k = 1;
            }
        }
        //ambele conditii trebuie sa fie indeplinite
        return genre0k == 1 && yearOk == 1;
    }

    /**
     * primele N video-uri sortate dupa rating
     *
     * @param action
     * @param input
     * @return
     */
    public static String ratingsQuery(final ActionInputData action, final Input input) {

        String s;

        if (action.getObjectType().equals(Constants.MOVIES)) {
            s = Utils.firstFromMap(
                    MovieMethods.movieRatings(input.getMovies(), action, Constants.QUERY),
                    action.getNumber(), Constants.QUERY);
        } else {
            s = Utils.firstFromMap(
                    ShowMethods.serialRatings(input.getSerials(), action, Constants.QUERY),
                    action.getNumber(), Constants.QUERY);
        }
        return s;
    }

    /**
     * primele N video-uri sortate dupa numarul de aparitii in listele de favorite
     *
     * @param action
     * @param input
     * @return
     */
    public static String favoriteQuery(final ActionInputData action, final Input input) {
        String s;
        if (action.getObjectType().equals(Constants.MOVIES)) {
            s = Utils.firstFromMap(MovieMethods.favoriteMovie(input, action),
                    action.getNumber(), Constants.QUERY);
        } else {
            s = Utils.firstFromMap(ShowMethods.favoriteSerial(input, action), action.getNumber(),
                    Constants.QUERY);
        }
        return s;
    }

    /**
     * ia primele N cele mai lungi video-uri
     *
     * @param action
     * @param input
     * @return
     */
    public static String longestQuery(final ActionInputData action, final Input input) {
        String s;
        //daca e film
        if (action.getObjectType().equals(Constants.MOVIES)) {

            s = Utils.firstFromMap(MovieMethods.longestMovie(input, action),
                    action.getNumber(), Constants.QUERY);
        } else {
            //daca e serial
            s = Utils.firstFromMap(ShowMethods.longestSerial(input, action), action.getNumber(),
                    Constants.QUERY);
        }
        return s;
    }

    /**
     * calculeaza numarul total de vizualizari pentru un vide
     *
     * @param video
     * @param usersData
     * @return
     */
    public static int videoViewsTotal(final String video, final List<UserInputData> usersData) {
        int nr = 0;
        for (UserInputData user : usersData) {
            nr += UserMethods.getVideoViews(video, user);
        }
        return nr;
    }

    /**
     * primele N video-uri sortatde dupa numarul de vizualizari
     *
     * @param action
     * @param input
     * @return
     */
    public static String viewedQuery(final ActionInputData action, final Input input) {
        String s;
        if (action.getObjectType().equals(Constants.MOVIES)) {
            s = Utils.firstFromMap(MovieMethods.movieViews(input, action),
                    action.getNumber(), Constants.QUERY);
        } else {
            s = Utils.firstFromMap(ShowMethods.serialViews(input, action), action.getNumber(),
                    Constants.QUERY);
        }
        return s;
    }

    /**
     * creaza un arraylist cu titlurile tuturor video-urilor
     *
     * @param input
     * @return
     */
    public static ArrayList<String> computeVideos(final Input input) {
        ArrayList<String> videos = new ArrayList<>();

        //adauga mai intai titlurile filmelor
        for (MovieInputData movie : input.getMovies()) {
            videos.add(movie.getTitle());
        }

        for (SerialInputData serial : input.getSerials()) {
            videos.add(serial.getTitle());
        }
        return videos;
    }

    /**
     * face un map cu toate genurile si vizualizarile acelui gen
     *
     * @param action
     * @param input
     * @return
     */
    public static TreeMap<String, Integer> computeGenres(final ActionInputData action,
                                                         final Input input) {

        TreeMap<String, Integer> genreViews = new TreeMap<>();

        //ia toate datele din filme
        for (MovieInputData movie : input.getMovies()) {

            int views = videoViewsTotal(movie.getTitle(), input.getUsers());

            Utils.createMapOfGenres(genreViews, movie.getGenres(), views);
        }
        //ia toate datele din seriale
        for (SerialInputData serial : input.getSerials()) {

            int views = videoViewsTotal(serial.getTitle(), input.getUsers());

            Utils.createMapOfGenres(genreViews, serial.getGenres(), views);
        }
        //sortez map dupa value folosind comparatorul
        TreeMap sortedMap = new TreeMap(new ValueComparator(genreViews));
        sortedMap.putAll(genreViews);

        return sortedMap;
    }

    /**
     * primeste un gen si intoarce toate video-urile care au acel gen
     *
     * @param genre
     * @param input
     * @return
     */
    public static ArrayList<String> getVideoByGenre(final String genre, final Input input) {

        ArrayList<String> list = new ArrayList<>();

        //adauga mai intei filmele
        for (MovieInputData movie : input.getMovies()) {
            if (movie.getGenres().contains(genre)) {
                list.add(movie.getTitle());
            }
        }
        //adauga serialele
        for (SerialInputData serial : input.getSerials()) {
            if (serial.getGenres().contains(genre)) {
                list.add(serial.getTitle());
            }
        }
        return list;
    }

    /**
     * map cu toate video-urile si de cate ori au aparut in lista de favorite
     *
     * @param action
     * @param input
     * @param user
     * @return
     */
    public static String favoriteRecommendation(final ActionInputData action, final Input input,
                                                final UserInputData user) {

        //sorteaza video-urile in ordinea inversa a nr de aparitii
        TreeMap<Double, ArrayList<String>> favSerial = new TreeMap<>(Collections.reverseOrder());
        TreeMap<Double, ArrayList<String>> favMovie = new TreeMap<>(Collections.reverseOrder());

        favSerial.putAll(ShowMethods.favoriteSerial(input, action));
        favMovie.putAll(MovieMethods.favoriteMovie(input, action));

        //face merge intre map-uri
        Utils.mergeMaps(favSerial, favMovie, Constants.NONE);

        //ia primul video nevazut de user
        String s = UserMethods.favRecommendation(favMovie, user);

        return s;
    }

    /**
     * cel mai bun video nevazut de un user
     *
     * @param action
     * @param input
     * @param user
     * @return
     */
    public static String bestUnseenRecommendation(final ActionInputData action, final Input input,
                                                  final UserInputData user) {

        //pune in map-uri video-urile si rating-urile lor
        TreeMap<Double, ArrayList<String>> rateSerial = new TreeMap<>(Collections.reverseOrder());
        TreeMap<Double, ArrayList<String>> rateMovie = new TreeMap<>(Collections.reverseOrder());

        rateSerial.putAll(ShowMethods
                .serialRatings(input.getSerials(), action, Constants.RECOMMENDATION));
        rateMovie.putAll(MovieMethods
                .movieRatings(input.getMovies(), action, Constants.RECOMMENDATION));

        //merge maps
        Utils.mergeMaps(rateSerial, rateMovie, Constants.NONE);

        //ia primul video nevazut de user
        String s = UserMethods.bestUnseenRecommendation(rateMovie, user);

        return s;
    }

    /**
     * face un map cu toate video-urile si rating-urile lor
     * aplica metoda pentru search recommendation
     *
     * @param action
     * @param input
     * @param user
     * @return
     */
    public static String searchRecommendation(final ActionInputData action, final Input input,
                                              final UserInputData user) {

        TreeMap<Double, ArrayList<String>> searchSerial = new TreeMap<>();
        TreeMap<Double, ArrayList<String>> searchMovie = new TreeMap<>();

        //video cu rating-urile in ordine crescatoare
        searchSerial.putAll(ShowMethods.unseenSerial(input, action));
        searchMovie.putAll(MovieMethods.unseenMovie(input, action));

        //merge intre maps
        Utils.mergeMaps(searchSerial, searchMovie, Constants.ASCENDING);

        String s = UserMethods.getSearch(searchMovie, user);

        return s;
    }
}
