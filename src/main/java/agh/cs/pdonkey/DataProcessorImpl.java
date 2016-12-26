package agh.cs.pdonkey;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static agh.cs.pdonkey.ApiHelper.*;
import static java.util.Collections.singletonList;

/**
 * pDonkey
 * Created by luknw on 22.12.2016
 */

public class DataProcessorImpl implements DataProcessor {
    private static final int TERM = 7;
    private static final int TERM_START_YEAR = 2011;
    private static final int TERM_END_YEAR = 2015;
    private static final String NOODLE_LAND = "WŁOCHY";

    private ApiHelper api = new ApiHelper();

//    private Map<MP, Long> daysAbroad;
//    private MP currentCandidate;
//    private Set<MP> macaronieris;
//
//    private double maxTravelCost;

    @Override
    public double getExpensesSum(MP mp) throws IOException {
        return getExpensesSumById(getMpId(mp));
    }

    private double getExpensesSumById(int mpId) throws IOException {
        return streamExpensesByYear(mpId)
                .filter(this::termFilter)
                .flatMap(year -> StreamSupport.stream(year.path(FIELDS).spliterator(), false))
                .mapToDouble(JsonNode::asDouble)
                .sum();
    }

    private int getMpId(MP mp) throws IOException {
        return streamMpNodes()
                .filter(
                        mpNode -> {
                            String name = mpNode.path(DATA).path(MP_NAME).asText();
                            return mp.getName().equals(name);
                        })
                .mapToInt(mpNode -> mpNode.path(MP_ID).asInt())
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private Stream<JsonNode> streamMpNodes() throws IOException {
        return api.streamList(MP_LIST, singletonList(WITH_TERM + TERM), singletonList(DATAOBJECT));
    }

    private Stream<JsonNode> streamExpensesByYear(int mpId) throws IOException {
        JsonNode mpDetails = api.getNode(api.getUrl(MP_BY_ID + mpId, singletonList(WITH_EXPENSES)));
        return StreamSupport.stream(
                mpDetails
                        .path(LAYERS)
                        .path(EXPENSES)
                        .path(YEARS)
                        .spliterator(),
                true);
    }

    private boolean termFilter(JsonNode year) {
        return year.path(YEAR).asInt() >= TERM_START_YEAR
                && year.path(YEAR).asInt() <= TERM_END_YEAR;
    }


    @Override
    public double getMinorExpenses(MP mp) throws IOException {
        return streamExpensesByYear(getMpId(mp))
                .filter(this::termFilter)
                .mapToDouble(
                        year -> year
                                .path(FIELDS)
                                .path(MINOR_EXPENSES)
                                .asDouble())
                .sum();
    }


    @Override
    public double getAvgMpsExpenses() throws IOException {
        // Includes ugly workaround for the inability to throw
        // checked exceptions from inside lambdas.
        try {
            return streamMpNodes()
                    .mapToDouble(
                            mpNode -> {
                                double tmp = 0;
                                try {
                                    tmp = getExpensesSumById(mpNode.path(MP_ID).asInt());
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e);
                                }
                                return tmp;
                            })
                    .average()
                    .orElseThrow(NoSuchElementException::new);
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }


    @Override
    public MP getTopTravellerMp() throws IOException {
        return streamTravels()
                .filter(this::withTerm)
                .map(node -> new MPImpl(node.path(MP_NAME).asText()))
                .collect(Collectors
                        .groupingByConcurrent(
                                Function.identity(),
                                Collectors.counting()))
                .entrySet()
                .parallelStream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(NoSuchElementException::new)
                .getKey();
    }

    private Stream<JsonNode> streamTravels() throws IOException {
        return api
                .streamList(TRAVELS_LIST, null, singletonList(DATAOBJECT))
                .map(travel -> travel.path(DATA));
    }

    private boolean withTerm(JsonNode travel) {
        //API date format: "2016-02-21"
        int startYear =
                Integer.parseInt(
                        travel.path(START_DATE)
                                .asText()
                                .substring(0, 4));
        return startYear >= TERM_START_YEAR
                && startYear <= TERM_END_YEAR;
    }

//    private void count(JsonNode travel) {
//        travel = travel.path(DATA);
//        MP mp = new MPImpl(travel.path(MP_NAME).asText());
//        int currentTravelsAbroad = travelsAbroad.getOrDefault(mp, 0);
//        currentTravelsAbroad++;
//        travelsAbroad.put(mp, currentTravelsAbroad);
//    }


    @Override
    public MP getTopTimeAbroadMp() throws IOException {
        return streamTravels()
                .filter(this::withTerm)
                .collect(Collectors.groupingBy(
                        travel -> new MPImpl(travel.path(MP_NAME).asText()),
                        Collectors.summingLong(travel -> travel.path(TRAVEL_DAYS).asLong())))
                .entrySet()
                .parallelStream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow(NoSuchElementException::new)
                .getKey();
    }
//
//    private void updateTimes(JsonNode travel) {
//        travel = travel.path(DATA);
//        int travelDays = travel.path(TRAVEL_DAYS).asInt();
//        MP mp = new MPImpl(travel.path(MP_NAME).asText());
//
//        int currentDaysAbroad = daysAbroad.getOrDefault(mp, 0);
//        currentDaysAbroad += travelDays;
//        daysAbroad.put(mp, currentDaysAbroad);
//    }


    @Override
    public MP getMostExpensiveTravelMp() throws IOException {
        return streamTravels()
                .filter(this::withTerm)
                .max(Comparator.comparingDouble(
                        travel -> travel
                                .path(TRAVEL_COST)
                                .asDouble()))
                .map(travel -> new MPImpl(travel.path(MP_NAME).asText()))
                .orElseThrow(NoSuchElementException::new);
    }
//
//    private void compareCost(JsonNode travel) {
//        travel = travel.path(DATA);
//        double travelCost = travel.path(TRAVEL_COST).asDouble();
//        if (travelCost > maxTravelCost) {
//            currentCandidate = new MPImpl(travel.path(MP_NAME).asText());
//            maxTravelCost = travelCost;
//        }
//    }


    @Override
    public List<MP> getMacaronieris() throws IOException {
        return streamTravels()
                .filter(this::withTerm)
                .filter(this::withNoodles)
                .map(travel -> new MPImpl(travel.path(MP_NAME).asText()))
                .sorted((a, b) ->
                        Collator.getInstance()
                                .compare(a.getName(), b.getName()))
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean withNoodles(JsonNode travel) {
        return NOODLE_LAND
                .equals(travel
                        .path(TRAVEL_LOCATION)
                        .asText()
                        .split("\\s", 2)
                        [0]);
    }
//
//    private void noodleInspection(JsonNode travel) {
//        travel = travel.path(DATA);
//        //API location format: "WŁOCHY Rzym"
//        String country = travel.path(TRAVEL_LOCATION).asText().split("\\s", 2)[0];
//        if (NOODLE_LAND.equals(country)) {
//            macaronieris.add(new MPImpl(travel.path(MP_NAME).asText()));
//        }
//    }
}
