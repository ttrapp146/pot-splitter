package com.ttrapp14622.potsplitter;

import android.content.Context;
import android.content.res.Resources;

import java.text.DecimalFormat;

public class DisplayResults {

    // formats calculations
    static private String results;

    public static void formatResults(Context context, double prizeTotalCheck, int prizeWinners, double firstPrize, String prizeDistribution, int entrants, double entreeFee, double potBonus, double prizeTotal, String potSplit, String mathFormula, boolean showFormula) {

        final Resources resources = context.getResources();
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);

        prizeTotalCheck *= 100;
        prizeTotalCheck = Math.round(prizeTotalCheck);
        prizeTotalCheck /= 100;
        if (prizeTotalCheck < prizeTotal) {
            firstPrize += prizeTotal - prizeTotalCheck;
            firstPrize *= 100;
            firstPrize = Math.round(firstPrize);
            firstPrize /= 100;
        }

        results = (resources.getString(R.string.prizes_to_distribute_results) + prizeWinners);
        results = results.concat("\n\n1" + resources.getString(R.string.st_place) + df.format(firstPrize) + "\n" + prizeDistribution);
        results = results.concat("\n" + resources.getString(R.string.entrants_results) + entrants);
        results = results.concat("\n" + resources.getString(R.string.entree_fee_results) + df.format(entreeFee));
        results = results.concat("\n" + resources.getString(R.string.pot_bonus_added_results) + df.format(potBonus));
        results = results.concat("\n" + resources.getString(R.string.pot_total_results) + df.format(prizeTotal));
        results = results.concat("\n" + resources.getString(R.string.pot_split_results) + potSplit);

        if (showFormula == true) {
            results = results.concat("\n\n" + resources.getString(R.string.math_formula_results));
            results = results.concat("\n" + df.format(firstPrize) + mathFormula);
        }
    }

    public static String displayResults() {
        return results;
    }

}
