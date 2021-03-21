package user;

import fileio.ActionInputData;
import fileio.Input;
import fileio.UserInputData;
import utils.Utils;
import video.VideoMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class UserMethods {

    private UserMethods() {
    }

    /**
     * verifica daca un video este vazut de un utilizator
     * daca da, intoarce filmul
     *
     * @param video
     * @param user
     * @return
     */
    public static Map.Entry<String, Integer> isSeen(final String video, final UserInputData user) {
        //cauta filmul in history
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().equals(video)) {
                return entry;
            }
        }
        return null;
    }

    /**
     * gaseste user-ul care are username-ul dat
     *
     * @param username
     * @param usersData
     * @return
     */
    public static UserInputData getUser(final String username,
                                        final List<UserInputData> usersData) {
        assert usersData != null;

        for (UserInputData user : usersData) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * adauga un video la favorite
     *
     * @param movie
     * @param user
     * @return
     */
    public static String addToFav(final String movie, final UserInputData user) {

        //daca este deja in lista de favorite a user-ului
        if (user.getFavoriteMovies().contains(movie)) {
            return ("error -> " + movie + " is already in favourite list");
        }
        //verifica daca video-ul a fost vazut de user
        if (isSeen(movie, user) != null) {
            user.getFavoriteMovies().add(movie);
            return ("success -> " + movie + " was added as favourite");
        }
        return ("error -> " + movie + " is not seen");
    }

    /**
     * adauga un video in lista de vizualizari
     *
     * @param
     * @param user
     * @return
     */
    public static String addtoView(UserInputData user, String show) {
        if (!user.getHistory().containsKey(show)) {
            user.getHistory().put(show, 1);
            return "success -> " + show + " was viewed with total views of 1";
        }
        else {
            int value = user.getHistory().get(show).intValue();
            user.getHistory().put(show, value + 1);
            return "success -> " + show + " was viewed with total views of " + user.getHistory().get(show);
        }

    }

    /**
     * face un map cu numarul de ratings si numele utilizatorilor
     *
     * @param action
     * @param input
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> userRates(final ActionInputData action,
                                                               final Input input) {

        TreeMap<Double, ArrayList<String>> users = new TreeMap<>();

        for (UserInputData user : input.getUsers()) {

            double number = user.getNumberOfRatings();

            if (number != 0) {
                Utils.addToMap(users, number, user.getUsername());
            }
        }
        return Utils.sortMap(users, action.getSortType());
    }

    /**
     * intoarce de cate ori a vizionat un useru un video dat
     *
     * @param video
     * @param user
     * @return
     */
    public static int getVideoViews(final String video, final UserInputData user) {
        int nr = 0;
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().equals(video)) {
                nr += entry.getValue();
            }
        }
        return nr;
    }

    /**
     * ia primul video nevazut de un user pentru standard recommendation
     *
     * @param videos
     * @param user
     * @return
     */
    public static String getFirstUnseen(final ArrayList<String> videos, final UserInputData user) {

        for (String video : videos) {
            if (!user.getHistory().containsKey(video)) {
                return ("StandardRecommendation result: " + video);
            }
        }
        return "StandardRecommendation cannot be applied!";
    }

    /**
     * ia primul video nevazut de un utiliizator si care are genul cel mai popular
     * @param input
     * @param videos
     * @param user
     * @return
     */
    public static String getPopularVideo(final Input input, final TreeMap<String, Integer> videos,
                                         final UserInputData user) {
        if (user.getSubscriptionType().equals("BASIC")) {
            return "PopularRecommendation cannot be applied!";
        }

        for (Map.Entry<String, Integer> entry : videos.entrySet()) {

            //primul video cu genul cerut
            ArrayList<String> video = VideoMethods.getVideoByGenre(entry.getKey(), input);

            for (String vid : video) {

                if (!user.getHistory().containsKey(vid)) {
                    return ("PopularRecommendation result: " + vid);
                }
            }
        }
        return "PopularRecommendation cannot be applied!";
    }

    /**
     * primul video nevazut de un user dintre cele favorite
     *
     * @param videos
     * @param user
     * @return
     */
    public static String favRecommendation(final TreeMap<Double, ArrayList<String>> videos,
                                           final UserInputData user) {
        if (user.getSubscriptionType().equals("BASIC") || videos.size() == 0) {
            return "FavoriteRecommendation cannot be applied!";
        }
        for (Map.Entry<Double, ArrayList<String>> entry : videos.entrySet()) {

            for (String video : entry.getValue()) {

                if (!user.getHistory().containsKey(video)) {
                    return ("FavoriteRecommendation result: " + video);
                }
            }
        }
        return "FavoriteRecommendation cannot be applied!";
    }

    /**
     * primul video nevazut dintre cele cu rating mare
     *
     * @param videos
     * @param user
     * @return
     */
    public static String bestUnseenRecommendation(final TreeMap<Double, ArrayList<String>> videos,
                                                  final UserInputData user) {
        for (Map.Entry<Double, ArrayList<String>> entry : videos.entrySet()) {
            for (String video : entry.getValue()) {

                if (!user.getHistory().containsKey(video)) {
                    return ("BestRatedUnseenRecommendation result: " + video);
                }
            }
        }
        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    /**
     * toate video-urile nevazute de un user de un anumit gen
     * @param map
     * @param user
     * @return
     */
    public static String getSearch(final TreeMap<Double, ArrayList<String>> map,
                                   final UserInputData user) {
        ArrayList<String> videos = new ArrayList<>();

        for (Map.Entry<Double, ArrayList<String>> entry : map.entrySet()) {
            for (String video : entry.getValue()) {
                if (!user.getHistory().containsKey(video)) {
                    videos.add(video);
                }
            }
        }
        if (user.getSubscriptionType().equals("BASIC") || videos.size() == 0) {
            return "SearchRecommendation cannot be applied!";
        }
        return "SearchRecommendation result: " + videos.toString();
    }
}
