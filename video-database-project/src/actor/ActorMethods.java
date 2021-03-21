package actor;

import common.Constants;
import fileio.ActorInputData;
import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import utils.Utils;
import video.MovieMethods;
import video.ShowMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

public final class ActorMethods {

    private ActorMethods() {
    }

    /**
     * calculeaza rating-ul unui actor
     * in functie de filmele in care a jucat
     *
     * @param actor
     * @param input
     * @return
     */
    public static double ratingActor(final ActorInputData actor, final Input input) {
        double sum = 0;
        int counter = 0;

        //itereaza prin filmele actorului
        for (int i = 0; i < actor.getFilmography().size(); i++) {

            //ia filmul corespunxator titlului
            MovieInputData movie = MovieMethods.getMovie(input, actor.getFilmography().get(i));

            //daca rating-ul filmului este > 0, il adauga in suma
            if (movie != null && MovieMethods.getRatingMovie(movie) != 0) {
                sum += MovieMethods.getRatingMovie(movie);
                counter++;
            }

            //face acelasi lucru cu serialele
            SerialInputData serial = ShowMethods.getSerial(input, actor.getFilmography().get(i));
            if (serial != null && ShowMethods.getRatingSerial(serial) != 0) {
                sum += ShowMethods.getRatingSerial(serial);
                counter++;
            }
        }

        //daca nu am gasit niciun film care sa aiba rating
        if (counter == 0) {
            return 0;
        }

        return sum / counter;
    }

    /**
     * face un map cu rating-ul si actorii care il au
     *
     * @param input
     * @param sortMethod
     * @return
     */
    public static TreeMap<Double, ArrayList<String>> getAverageActors(final Input input,
                                                                      final String sortMethod) {

        TreeMap<Double, ArrayList<String>> actors = new TreeMap<>();

        for (ActorInputData actor : input.getActors()) {

            double rate = ratingActor(actor, input);

            if (rate != 0) {
                Utils.addToMap(actors, rate, actor.getName());
            }
        }

        return Utils.sortMap(actors, sortMethod);
    }

    /**
     * verifica daca un actor are award-urile cerute
     *
     * @param awardsRequired
     * @param actor
     * @return
     */
    public static boolean isAwarded(final ArrayList<String> awardsRequired,
                                    final ActorInputData actor) {
        for (String award : awardsRequired) {
            int ok = 0;
            //parcurg toate premiile lui
            for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
                //am gasit award ul
                if (entry.getKey().toString().equals(award)) {
                    ok = 1;
                    break;
                }
            }
            if (ok == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * ia numarul de awards al unui actor
     *
     * @param actor
     * @return
     */
    public static double awardsNo(final ActorInputData actor) {
        double nr = 0;
        for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
            nr += entry.getValue();
        }
        return nr;
    }

    /**
     * afiseaza primii N actori, sortati dupa nuamrul de premii
     * care au toate award-urile necesare
     *
     * @param awardsRequired
     * @param sortMethod
     * @param actorsData
     * @return
     */
    public static String getAwardedActors(final ArrayList<String> awardsRequired,
                                          final String sortMethod,
                                          final List<ActorInputData> actorsData) {

        TreeMap<Double, ArrayList<String>> actors = new TreeMap<>();

        for (ActorInputData actor : actorsData) {

            //daca are toate award-urile necesare
            if (isAwarded(awardsRequired, actor)) {

                //calculeaza numarul total de awards
                Double number = awardsNo(actor);

                if (number != 0) {
                    Utils.addToMap(actors, number, actor.getName());
                }
            }
        }
        actors = Utils.sortMap(actors, sortMethod);

        ArrayList<String> result = new ArrayList<>();

        for (Map.Entry<Double, ArrayList<String>> entry : actors.entrySet()) {
            result.addAll(entry.getValue());
        }
        return ("Query result: " + result);
    }

    /**
     * verifica daca un actor are toare cuvintele necesare in career description
     *
     * @param actor
     * @param words
     * @return
     */
    public static boolean checkCareerDescription(final ActorInputData actor,
                                                 final ArrayList<String> words) {

        for (String word : words) {
            int ok = 0;

            //iterez prin descriere
            StringTokenizer st = new StringTokenizer(actor.getCareerDescription(), ",. !-?");
            while (st.hasMoreTokens()) {

                if (st.nextToken().toUpperCase().equals(word.toUpperCase())) {
                    ok = 1;
                    break;
                }
            }

            //daca nu am gasit un cuvant
            if (ok == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * afiseaza actorii care au cuvintele cerute in career description
     *
     * @param actorsData
     * @param words
     * @param sortMethod
     * @return
     */
    public static String getActorsWithCareerDescription(final List<ActorInputData> actorsData,
                                                        final ArrayList<String> words,
                                                        final String sortMethod) {
        ArrayList<String> actors = new ArrayList<>();

        for (ActorInputData actor : actorsData) {

            if (checkCareerDescription(actor, words)) {
                actors.add(actor.getName());
            }
        }
        if (sortMethod.equals(Constants.DESCENDING)) {
            Collections.sort(actors, Collections.reverseOrder());
        } else {
            Collections.sort(actors);
        }
        return ("Query result: " + actors.toString());
    }
}
