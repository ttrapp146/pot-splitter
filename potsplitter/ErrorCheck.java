package com.ttrapp14622.potsplitter;

import android.content.Context;
import android.content.res.Resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorCheck {

    // Checks that higher ranks get higher reward
    // Checks if pot split percentages add up to 100%
    // Checks if pot split <= entrants
    // Checks if pot split is in correct format
    // Checks if pot split still shows initial value

    private static boolean awardsNotDescending = false;
    private static boolean winnersMoreThanEntrants = false;
    private static boolean potSplitNotExact = false;
    private static boolean noSplitSelected = false;
    private static boolean noSlash = false;
    private static String errors = "";
    private static Pattern potFormat = Pattern.compile("\\d\\d/\\d\\d?(/\\d\\d?)*/");


    public static boolean potSplitCheck(Context context, String potSplit, int entrants) {
        int index = 0;
        int rank = 1;
        int mathPower = 1;
        int powerUseCount = 0;
        int potSplitCheck = 0;
        int prizeWinners = 0;
        double previousRankAward = 200;
        String potSplitCheckFormula = "";
        Resources resources = context.getResources();

        //resets all booleans before running tests

        noSlash = false;
        winnersMoreThanEntrants = false;
        potSplitNotExact = false;
        awardsNotDescending = false;
        noSplitSelected = false;

        errors = "";
        Matcher potFormatMatcher = potFormat.matcher(potSplit);

        if (potSplit.contains(resources.getString(R.string.splitlist_initialvalue)))
            noSplitSelected = true;
        else if (!potFormatMatcher.matches())
            noSlash = true;
        else {


            while (potSplit.contains("/")) {
                index = potSplit.indexOf('/');
                if (rank < 5) {
                    potSplitCheck += Double.valueOf(potSplit.substring(0, index));
                    potSplitCheckFormula = potSplitCheckFormula.concat(resources.getString(R.string.rank) + rank + " :: " + Double.valueOf(potSplit.substring(0, index)).toString() + "\n");
                    if (previousRankAward < Double.valueOf(potSplit.substring(0, index)))
                        awardsNotDescending = true;
                    prizeWinners = rank;
                    rank++;
                    previousRankAward = Double.valueOf(potSplit.substring(0, index));
                    // System.out.println(potSplitCheck);
                } else {
                    if (powerUseCount < 2) {
                        potSplitCheck += Double.valueOf(potSplit.substring(0, index)) * Math.pow(2, mathPower);
                        potSplitCheckFormula = potSplitCheckFormula.concat(resources.getString(R.string.rank) + rank + " :: " + Double.valueOf(potSplit.substring(0, index)).toString() + " * " + Math.pow(2, mathPower) + "\n");
                        prizeWinners = (int) (rank - 1 + Math.pow(2, mathPower));
                        rank = (int) (rank + Math.pow(2, mathPower));
                        powerUseCount++;
                        if (previousRankAward < Double.valueOf(potSplit.substring(0, index)))
                            awardsNotDescending = true;
                        previousRankAward = Double.valueOf(potSplit.substring(0, index));

                    } else {
                        mathPower++;
                        prizeWinners = (int) (rank - 1 + Math.pow(2, mathPower));
                        potSplitCheck += Double.valueOf(potSplit.substring(0, index)) * Math.pow(2, mathPower);
                        potSplitCheckFormula = potSplitCheckFormula.concat(resources.getString(R.string.rank) + rank + " :: " + Double.valueOf(potSplit.substring(0, index)).toString() + " * " + Math.pow(2, mathPower) + "\n");
                        rank = (int) (rank + Math.pow(2, mathPower));
                        powerUseCount = 1;
                        if (previousRankAward < Double.valueOf(potSplit.substring(0, index)))
                            awardsNotDescending = true;
                        previousRankAward = Double.valueOf(potSplit.substring(0, index));
                    }
                }
                potSplit = potSplit.substring(index + 1);
            }
        }

        //builds error messages

        if (potSplitCheck != 100)
            potSplitNotExact = true;

        if (prizeWinners > entrants)
            winnersMoreThanEntrants = true;
        if (noSplitSelected == true)
            errors = errors.concat(resources.getString(R.string.no_split_selected_error));
        else if (noSlash == true)
            errors = errors.concat(resources.getString(R.string.format_error));
        else if (potSplitNotExact == true)
//            errors = errors.concat(
//                    "Error:  PotSplit does not add up to 100%.  Remember 5th place and below are ties so two people will get x% of the pot.  Count the x value twice for 5th, 4 times for 9th, etc."
//                            + "\n% of pot awarded = " + potSplitCheck + "\n" + potSplitCheckFormula + "\n\n");
            errors = errors.concat(
                    "Error:  PotSplit does not add up to 100% for " + prizeWinners +
                            " prize winners.  See math below \n" + potSplitCheckFormula + "\n% of pot awarded = " + potSplitCheck + "\n\n");


        if (awardsNotDescending == true)
            errors = errors.concat(
                    "Error:  Awards from first to last are not in descending order.  Please make sure pot split for higher rank is more than or equal to lower rank.\n\n");

        if (winnersMoreThanEntrants == true)
            errors = errors.concat("Error:  Prize winners cannot be more than entrants\nEntrants:  " + entrants
                    + "\nPrize winners:  " + prizeWinners + "\n\n");

        if (winnersMoreThanEntrants == true || potSplitNotExact == true || awardsNotDescending == true || noSplitSelected == true || noSlash == true)
            return true;
        else
            return false;
    }

    public static String displayErrors() {

        return errors;
    }
}

