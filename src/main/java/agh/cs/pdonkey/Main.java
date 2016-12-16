package agh.cs.pdonkey;

/**
 * pDonkey
 * Created by luknw on 15.12.2016
 */

public class Main {
    public static void main(String[] args) {
        AppData config = null;
        DataProcessor dao = null;

        switch (config.getMode()) {
            case SUM_EXPENSES:
                System.out.println(dao.getSumExpenses(config.getMP()));
                break;
            case MINOR_EXPENSES:
                System.out.println(dao.getMinorExpenses(config.getMP()));
                break;
            case AVG_MP_EXPENSES:
                System.out.println(dao.getAvgMpExpenses());
                break;
            case TOP_TRAVELS:
                System.out.println(dao.getTopTravelsMp());
                break;
            case TOP_TIME_ABROAD:
                System.out.println(dao.getTopTimeAbroadMp());
                break;
            case MACARONIERIS:
                System.out.println(dao.getMacaronieris());
                break;
        }
//        try {
//            URL foo = new URL("https://api-v3.mojepanstwo.pl/dane/poslowie");
//            new BufferedReader(new InputStreamReader(foo.openStream())).lines().forEachOrdered(System.out::println);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
