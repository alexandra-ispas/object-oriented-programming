package utils;

import actor.ActorsAwards;
import common.Constants;
import entertainment.Genre;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The class contains static methods that helps with parsing.
 * <p>
 * We suggest you add your static methods here or in a similar class.
 */
public final class Utils {
    /**
     * for coding style
     */
    private Utils() {
    }

    /**
     * Transforms a string into an enum
     *
     * @param genre of video
     * @return an Genre Enum
     */
    public static Genre stringToGenre(final String genre) {
        return switch (genre.toLowerCase()) {
            case "action" -> Genre.ACTION;
            case "adventure" -> Genre.ADVENTURE;
            case "drama" -> Genre.DRAMA;
            case "comedy" -> Genre.COMEDY;
            case "crime" -> Genre.CRIME;
            case "romance" -> Genre.ROMANCE;
            case "war" -> Genre.WAR;
            case "history" -> Genre.HISTORY;
            case "thriller" -> Genre.THRILLER;
            case "mystery" -> Genre.MYSTERY;
            case "family" -> Genre.FAMILY;
            case "horror" -> Genre.HORROR;
            case "fantasy" -> Genre.FANTASY;
            case "science fiction" -> Genre.SCIENCE_FICTION;
            case "action & adventure" -> Genre.ACTION_ADVENTURE;
            case "sci-fi & fantasy" -> Genre.SCI_FI_FANTASY;
            case "animation" -> Genre.ANIMATION;
            case "kids" -> Genre.KIDS;
            case "western" -> Genre.WESTERN;
            case "tv movie" -> Genre.TV_MOVIE;
            default -> null;
        };
    }

    /**
     * Transforms a string into an enum
     *
     * @param award for actors
     * @return an ActorsAwards Enum
     */
    public static ActorsAwards stringToAwards(final String award) {
        return switch (award) {
            case "BEST_SCREENPLAY" -> ActorsAwards.BEST_SCREENPLAY;
            case "BEST_SUPPORTING_ACTOR" -> ActorsAwards.BEST_SUPPORTING_ACTOR;
            case "BEST_DIRECTOR" -> ActorsAwards.BEST_DIRECTOR;
            case "BEST_PERFORMANCE" -> ActorsAwards.BEST_PERFORMANCE;
            case "PEOPLE_CHOICE_AWARD" -> ActorsAwards.PEOPLE_CHOICE_AWARD;
            default -> null;
        };
    }

    /**
     * Transforms an array of JSON's into an array of strings
     *
     * @param array of JSONs
     * @return a list of strings
     */
    public static ArrayList<String> convertJSONArray(final JSONArray array) {
        if (array != null) {
            ArrayList<String> finalArray = new ArrayList<>();
            for (Object object : array) {
                finalArray.add((String) object);
            }
            return finalArray;
        } else {
            return null;
        }
    }

    /**
     * Transforms an array of JSON's into a map
     *
     * @param jsonActors array of JSONs
     * @return a map with ActorsAwardsa as key and Integer as value
     */
    public static Map<ActorsAwards, Integer> convertAwards(final JSONArray jsonActors) {
        Map<ActorsAwards, Integer> awards = new LinkedHashMap<>();

        for (Object iterator : jsonActors) {
            awards.put(stringToAwards(
                    (String) ((JSONObject) iterator).get(Constants.AWARD_TYPE)),
                    Integer.parseInt(((JSONObject) iterator).get(Constants.NUMBER_OF_AWARDS)
                            .toString()));
        }

        return awards;
    }

    /**
     * Transforms an array of JSON's into a map
     *
     * @param movies array of JSONs
     * @return a map with String as key and Integer as value
     */
    public static Map<String, Integer> watchedMovie(final JSONArray movies) {
        Map<String, Integer> mapVideos = new LinkedHashMap<>();

        if (movies != null) {
            for (Object movie : movies) {
                mapVideos.put((String) ((JSONObject) movie).get(Constants.NAME),
                        Integer.parseInt(((JSONObject) movie).get(Constants.NUMBER_VIEWS)
                                .toString()));
            }
        } else {
            System.out.println("NU ESTE VIZIONAT NICIUN FILM");
        }

        return mapVideos;
    }

    /**
     * adds an entry to a map
     *
     * @param map
     * @param key
     * @param value
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> addToMap(
            final TreeMap<Double, ArrayList<String>> map,
            final double key, final String value) {
        int ok = 0;
        for (Map.Entry<Double, ArrayList<String>> entry : map.entrySet()) {
            if (entry.getKey().equals(key)) {
                entry.getValue().add(value);
                ok = 1;
            }
        }
        if (ok == 0) {
            ArrayList<String> chainOfActors = new ArrayList<>();
            chainOfActors.add(value);
            map.put(key, chainOfActors);
        }
        return map;
    }

    /**
     * creaza un map cu genurile posibile si vizualizarile lor
     *
     * @param genresMap
     * @param genres
     * @param views
     */
    public static void createMapOfGenres(final TreeMap<String, Integer> genresMap,
                                         final ArrayList<String> genres, final Integer views) {
        for (String genre : genres) {
            int ok = 0;
            for (Map.Entry<String, Integer> entry : genresMap.entrySet()) {
                if (entry.getKey().equals(genre)) {
                    entry.setValue(entry.getValue() + views);
                    ok = 1;
                }
            }
            if (ok == 0) {
                genresMap.put(genre, views);
            }
        }
    }

    /**
     * sorteaza elementele dintr-un map
     *
     * @param map
     * @param sortMethod
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> sortMap(
            final TreeMap<Double, ArrayList<String>> map,
            final String sortMethod) {

        TreeMap<Double, ArrayList<String>> sortedMap;

        if (sortMethod == null) {
            return map;
        }
        //cauta metoda de sortare
        if (sortMethod.equals(Constants.ASCENDING)) {
            for (Map.Entry<Double, ArrayList<String>> entry : map.entrySet()) {
                Collections.sort(entry.getValue());
            }
            sortedMap = new TreeMap<>(map);

        } else if (sortMethod.equals(Constants.DESCENDING)) {
            for (Map.Entry<Double, ArrayList<String>> entry : map.entrySet()) {
                Collections.sort(entry.getValue(), Collections.reverseOrder());
            }
            sortedMap = new TreeMap<>(Collections.reverseOrder());

            sortedMap.putAll(map);
        } else {
            return map;
        }
        return sortedMap;
    }

    /**
     * ia primele N elemente dintr-un map
     *
     * @param actors
     * @param nr
     * @param type
     * @return
     */
    public static String firstFromMap(final TreeMap<Double, ArrayList<String>> actors, final int nr,
                                      final String type) {
        int counter = nr;
        ArrayList<String> firstActors = new ArrayList<>();

        for (Map.Entry<Double, ArrayList<String>> entry : actors.entrySet()) {

            for (int i = 0; i < entry.getValue().size(); i++) {
                if (counter > 0) {
                    firstActors.add(entry.getValue().get(i));
                    counter--;
                }
            }

        }
        //daca imi trebuie pentru query
        if (type.equals("query")) {
            return ("Query result: " + firstActors.toString());

            //daca imi trebuie pentru recommendation
        } else if (type.equals("favorite")) {
            return ("FavoriteRecommendation result: " + firstActors.get(0));
        }
        return "";
    }

    /**
     * face merge intre 2 maps de acelasi fel
     *
     * @param map1
     * @param map2
     * @param sortMethod
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> mergeMaps(
            final TreeMap<Double, ArrayList<String>> map1,
            final TreeMap<Double, ArrayList<String>> map2, final String sortMethod) {

        for (Map.Entry<Double, ArrayList<String>> entry1 : map1.entrySet()) {

            double key = entry1.getKey();

            ArrayList<String> value = entry1.getValue();

            int ok = 0;

            //parcutg primul map
            for (Map.Entry<Double, ArrayList<String>> entry2 : map2.entrySet()) {
                //daca mai exita acea cheie in map2, adaug in arraylist
                if (entry2.getKey().equals(key)) {
                    entry2.getValue().addAll(value);
                    ok = 1;
                }
            }

            //daca map2 nu are un entry cu acel key, fac un nou arraylist
            if (ok == 0) {
                ArrayList<String> chainOfSerials = new ArrayList<>();
                chainOfSerials.addAll(value);
                map2.put(key, chainOfSerials);
            }
        }
        return Utils.sortMap(map2, sortMethod);
    }

}
