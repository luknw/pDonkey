package agh.cs.pdonkey;

import java.util.List;

/**
 * pDonkey
 * Created by luknw on 15.12.2016
 */

public interface DataProcessor {
    double getSumExpenses(MP mp);

    double getMinorExpenses(MP mp);

    double getAvgMpExpenses();

    MP getTopTravelsMp();

    MP getTopTimeAbroadMp();

    List<MP> getMacaronieris();
}
