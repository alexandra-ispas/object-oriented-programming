package video;

import common.Constants;
import entertainment.Season;
import fileio.ActionInputData;
import fileio.Input;
import fileio.SerialInputData;
import fileio.UserInputData;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

public final class ShowMethods {

    private ShowMethods() {
    }

    /**
     * da rate unui sezon
     *
     * @param season
     * @param rate
     * @param user
     * @param title
     * @return
     */
    public static String rateSeason(final Season season, final double rate,
                                    final UserInputData user, final String title) {
        //daca user-ul a mai dat rate acelui sezon
        if (season.getUsersWhoRated().contains(user.getUsername())) {
            return ("error -> " + title + " has been already rated");
        }

        //verifica daca user-ul a vazut serialul
        if (user.getHistory().containsKey(title)) {

            //adauga valoarea de rating
            season.getRatings().add(rate);

            //adauga unser-ul in lista celor care au votat
            season.setUsersWhoRated(user.getUsername());

            //incrementeaza numarul de ratings date de user
            user.setNumberOfRatings(user.getNumberOfRatings() + 1);

            return ("success -> " + title + " was rated with " + rate + " by "
                    + user.getUsername());
        }

        //utilizatorul nu a vazut serialul
        return ("error -> " + title + " is not seen");
    }

    /**
     * returneaza serialul corespunzator unui titlu dat
     *
     * @param input
     * @param title
     * @return
     */
    public static SerialInputData getSerial(final Input input, final String title) {

        //iterez prin seriale
        for (SerialInputData serial : input.getSerials()) {

            //verific daca am gasit titlul
            if (serial.getTitle().equals(title)) {

                return serial;
            }
        }

        //nu am gasit niciun serial cu acel titlu
        return null;
    }

    /**
     * calculeaza rating-ul total al unui serial
     * facand media pe sezoane
     *
     * @param serial
     * @return
     */
    public static double getRatingSerial(final SerialInputData serial) {
        double sum = 0;

        //iterez prin sezoane
        for (int i = 0; i < serial.getNumberSeason(); i++) {
            sum += serial.getSeasons().get(i).getRatingSeason();
        }

        return sum / serial.getNumberSeason();
    }

    /**
     * map cu serialele si rating-urile corespunzatoare
     *
     * @param serialsData
     * @param action
     * @param type
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> serialRatings(
            final List<SerialInputData> serialsData,
            final ActionInputData action, final String type) {

        TreeMap<Double, ArrayList<String>> serials = new TreeMap<>(Collections.reverseOrder());

        for (SerialInputData serial : serialsData) {
            //daca verifica filtrele
            if (VideoMethods
                    .checkFilters(action.getFilters(), serial.getYear(), serial.getGenres())) {

                double rate = getRatingSerial(serial);

                //daca este query, trebuie sa adaug doar seriale care au rating != 0
                if (type.equals(Constants.QUERY)) {
                    if (rate != 0) {
                        Utils.addToMap(serials, rate, serial.getTitle());
                    }

                    //pentru recommendations, imi trebuie si cele cu rating = 0
                } else {
                    Utils.addToMap(serials, rate, serial.getTitle());
                }
            }
        }

        return Utils.sortMap(serials, action.getSortType());
    }

    /**
     * numar de cate ori un serial a fost adaugat la favorite de un array de users
     *
     * @param users
     * @param serial
     * @return
     */
    public static double countFavorite(final List<UserInputData> users,
                                       final SerialInputData serial) {
        double nr = 0;

        for (UserInputData user : users) {
            //verific daca se afla in lista de favorite a user-ului
            if (user.getFavoriteMovies().contains(serial.getTitle())) {
                nr++;
            }
        }
        return nr;
    }

    /**
     * map cu serialul si de cate ori a aparut la favorite
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> favoriteSerial(final Input input,
                                                                    final ActionInputData action) {

        TreeMap<Double, ArrayList<String>> serials = new TreeMap<>();

        for (SerialInputData serial : input.getSerials()) {

            //daca verifica filtrele
            if (VideoMethods
                    .checkFilters(action.getFilters(), serial.getYear(), serial.getGenres())) {

                //numarul de aparitii in listele de favorite
                double number = countFavorite(input.getUsers(), serial);

                //daca a aparut cel putin odata
                if (number != 0) {
                    Utils.addToMap(serials, number, serial.getTitle());
                }
            }
        }
        //se sorteaza map-ul
        return Utils.sortMap(serials, action.getSortType());
    }

    /**
     * calculeaza durata unui show complet
     *
     * @param serial
     * @return
     */
    public static int getDuration(final SerialInputData serial) {
        int duration = 0;
        for (Season season : serial.getSeasons()) {
            duration += season.getDuration();
        }
        return duration;
    }

    /**
     * map cu seriale si duratele lor
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> longestSerial(final Input input,
                                                                   final ActionInputData action) {
        TreeMap<Double, ArrayList<String>> serials = new TreeMap<>();

        for (SerialInputData serial : input.getSerials()) {
            //verifica filtrele
            if (VideoMethods
                    .checkFilters(action.getFilters(), serial.getYear(), serial.getGenres())) {

                double duration = getDuration(serial);

                Utils.addToMap(serials, duration, serial.getTitle());
            }
        }
        return Utils.sortMap(serials, action.getSortType());
    }

    /**
     * map cu numarul de vizualizari al unui serial
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> serialViews(final Input input,
                                                                 final ActionInputData action) {

        TreeMap<Double, ArrayList<String>> serials = new TreeMap<>();

        for (SerialInputData serial : input.getSerials()) {

            if (VideoMethods
                    .checkFilters(action.getFilters(), serial.getYear(), serial.getGenres())) {

                double number = VideoMethods.videoViewsTotal(serial.getTitle(), input.getUsers());

                if (number != 0) {
                    Utils.addToMap(serials, number, serial.getTitle());
                }
            }
        }
        return Utils.sortMap(serials, action.getSortType());
    }

    /**
     * face map-ul pentru best unseen recommendation
     * contine serialele care au genul cerut si rating-urile lor
     *
     * @param input
     * @param action
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> unseenSerial(final Input input,
                                                                  final ActionInputData action) {

        TreeMap<Double, ArrayList<String>> serials = new TreeMap<>();

        for (SerialInputData serial : input.getSerials()) {

            if (serial.getGenres().contains(action.getGenre())) {

                double rate = getRatingSerial(serial);

                Utils.addToMap(serials, rate, serial.getTitle());
            }
        }
        //sortarea dupa rating se face crescator
        return Utils.sortMap(serials, Constants.ASCENDING);
    }
}
