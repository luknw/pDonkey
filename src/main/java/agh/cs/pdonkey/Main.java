package agh.cs.pdonkey;

import java.io.IOException;

/**
 * pDonkey
 * Created by luknw on 15.12.2016
 */

public class Main {
    public static void main(String[] args) {
        AppConfig config;
        try {
            config = new OptionsParserImpl().parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            return;
        }

        DataProcessor data = new DataProcessorImpl();
        try {
            switch (config.getMode()) {
                case SUM_EXPENSES:
                    System.out.println(data.getExpensesSum(config.getMP()));
                    break;
                case MINOR_EXPENSES:
                    System.out.println(data.getMinorExpenses(config.getMP()));
                    break;
                case AVG_EXPENSES:
                    System.out.println(data.getAvgMpsExpenses());
                    break;
                case TOP_TRAVELS_ABROAD:
                    System.out.println(data.getTopTravellerMp());
                    break;
                case TOP_TIME_ABROAD:
                    System.out.println(data.getTopTimeAbroadMp());
                    break;
                case MOST_EXPENSIVE_TRAVEL:
                    System.out.println(data.getMostExpensiveTravelMp());
                    break;
                case MACARONIERIS:
                    data.getMacaronieris().forEach(System.out::println);
                    break;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}