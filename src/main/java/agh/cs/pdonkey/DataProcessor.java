package agh.cs.pdonkey;

import java.io.IOException;
import java.util.List;

/**
 * pDonkey
 * Created by luknw on 15.12.2016
 */

public interface DataProcessor {
    double getExpensesSum(MP mp) throws IOException;

    double getMinorExpenses(MP mp) throws IOException;

    double getAvgMpsExpenses() throws IOException;

    MP getTopTravellerMp() throws IOException;

    MP getTopTimeAbroadMp() throws IOException;

    MP getMostExpensiveTravelMp() throws IOException;

    List<MP> getMacaronieris() throws IOException;
}
