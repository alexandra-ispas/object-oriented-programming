package main;

import actor.ActorMethods;
import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.UserInputData;
import fileio.Writer;
import org.json.simple.JSONArray;
import user.UserMethods;
import utils.Utils;
import video.VideoMethods;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {

        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation

        List<ActionInputData> actions = input.getCommands();

        //iterez prin comenzile date
        for (ActionInputData action : actions) {

            //daca se cere o actiune de tip comanda
            if (action.getActionType().equals(Constants.COMMAND)) {

                //scot user-ul corespunzator actiunii
                UserInputData user = UserMethods.getUser(action.getUsername(), input.getUsers());

                //daca se da comanda "favorite"
                if (action.getType().equals(Constants.FAVORITE)) {

                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            UserMethods.addToFav(action.getTitle(), user)));

                }

                //daca se da comanda "view"
                if (action.getType().equals(Constants.VIEW)) {

                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            UserMethods.addtoView(user, action.getTitle())));
                }

                //daca se da comanda "rating"
                if (action.getType().equals(Constants.RATING)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.rateVideo(action, input)));

                }
            }
            //daca actiunea este de tip query
            if (action.getActionType().equals(Constants.QUERY)) {

                if (action.getCriteria().equals(Constants.AVERAGE)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            Utils.firstFromMap(
                                    ActorMethods.getAverageActors(input, action.getSortType()),
                                    action.getNumber(), Constants.QUERY)));

                }
                if (action.getCriteria().equals(Constants.AWARDS)) {

                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            ActorMethods.getAwardedActors(action.getFilterAwards(),
                                    action.getSortType(), input.getActors())));
                }

                if (action.getCriteria().equals(Constants.FILTER_DESCRIPTIONS)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            ActorMethods.getActorsWithCareerDescription(input.getActors(),
                                    action.getFilterWords(), action.getSortType())));
                }
                if (action.getCriteria().equals(Constants.RATINGS)) {

                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.ratingsQuery(action, input)));

                }
                if (action.getCriteria().equals(Constants.FAVORITE)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.favoriteQuery(action, input)));
                }
                if (action.getCriteria().equals(Constants.LONGEST)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.longestQuery(action, input)));
                }

                if (action.getCriteria().equals(Constants.MOST_VIEWED)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.viewedQuery(action, input)));
                }
                if (action.getCriteria().equals(Constants.NUM_RATINGS)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            Utils.firstFromMap(UserMethods.userRates(action, input),
                                    action.getNumber(), Constants.QUERY)));
                }
            }

            //daca actiunea este o recomandare
            if (action.getActionType().equals(Constants.RECOMMENDATION)) {

                UserInputData user = UserMethods.getUser(action.getUsername(), input.getUsers());

                if (action.getType().equals(Constants.STANDARD)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            UserMethods.getFirstUnseen(VideoMethods.computeVideos(input), user)));
                }

                if (action.getType().equals(Constants.POPULAR)) {

                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            UserMethods.getPopularVideo(input,
                                    VideoMethods.computeGenres(action, input), user)));

                }
                if (action.getType().equals(Constants.FAVORITE)) {

                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.favoriteRecommendation(action, input, user)));
                }
                if (action.getType().equals(Constants.BEST_UNSEEN)) {
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.bestUnseenRecommendation(action, input, user)));
                }
                if (action.getType().equals(Constants.SEARCH)) {

                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null,
                            VideoMethods.searchRecommendation(action, input, user)));
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}

