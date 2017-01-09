package agh.cs.pdonkey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * pDonkey
 * Created by luknw on 25.12.2016
 */

class ApiHelper {
    //url metadata
    static final String API_ENTRY = "https://api-v3.mojepanstwo.pl/dane/";
    static final String QUERY_OPTIONS = "?";
    static final String WITH_LIMIT = "&limit=";
    static final String WITH_EXPENSES = "&layers=wydatki";
    static final String WITH_TERM = "&conditions[poslowie.kadencja]=";
    //url paths
    static final String MP_BY_ID = "poslowie/";
    static final String MP_LIST = "poslowie.json";
    static final String TRAVELS_LIST = "poslowie_wyjazdy.json";
    //json metadata
    static final String DATA = "data";
    static final String DATAOBJECT = "Dataobject";
    static final String LAYERS = "layers";
    static final String EXPENSES = "wydatki";
    static final String YEARS = "roczniki";
    static final String FIELDS = "pola";
    static final int MINOR_EXPENSES = 12;
    //json data
    static final String MP_ID = "id";
    static final String YEAR = "rok";
    static final String MP_NAME = "poslowie.nazwa";
    static final String TRAVEL_COST = "poslowie_wyjazdy.wartosc_koszt";
    static final String TRAVEL_DAYS = "poslowie_wyjazdy_wydarzenia.liczba_dni";
    static final String TRAVEL_LOCATION = "poslowie_wyjazdy_wydarzenia.lokalizacja";
    static final String START_DATE = "poslowie_wyjazdy_wydarzenia.data_start";
    //errors
    private static final String API_ERROR = "Internet webservice error";
    private static final String INVALID_URL = "Invalid URL: ";
    private static final String JSON_PROCESSING_ERROR = "Json processing error at ";

    //paging (experimental)
    private static final int PARALLELISM = 30;
    private static final int ELEMENTS_PER_PAGE = 17;
    private static final String COMMON_FORK_JOIN_POOL_SIZE = "java.util.concurrent.ForkJoinPool.common.parallelism";
    private static final String COUNT = "Count";
    private static final String PAGE = "&page=";


    URL getUrl(String urlPath, List<String> queryOptions) throws IOException {
        String urlString = getUrlBuilder(urlPath, queryOptions).toString();
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new IOException(INVALID_URL + urlString, e);
        }
    }

    JsonNode getNode(URL source) throws IOException {
        JsonNode node;
        try {
            node = new ObjectMapper().readTree(source);
        } catch (JsonProcessingException e) {
            throw new IOException(JSON_PROCESSING_ERROR + e.getLocation(), e);
        } catch (IOException e) {
            throw new IOException(API_ERROR, e);
        }
        return node;
    }

    Stream<JsonNode> streamList(String urlPath, List<String> queryOptions, List<String> jsonPath)
            throws IOException {

        StringBuilder urlBase = getUrlBuilder(urlPath, queryOptions);
        urlBase.append(WITH_LIMIT);

        int pageCount = getPageCount(new URL(urlBase.toString() + 1));
        String baseUrl = urlBase.append(ELEMENTS_PER_PAGE).append(PAGE).toString();

        //change size of the common fork join pool to speed up parallel streams
        System.setProperty(COMMON_FORK_JOIN_POOL_SIZE, Integer.toString(PARALLELISM));

        Stream<JsonNode> result;
        try {
            result = IntStream
                    .rangeClosed(1, pageCount)
                    .parallel()
                    .mapToObj(
                            page -> {
                                try {
                                    return getNode(new URL(baseUrl + page));
                                } catch (MalformedURLException e) {
                                    throw new UncheckedIOException(INVALID_URL + e.getMessage(), e);
                                } catch (IOException e) {
                                    throw new UncheckedIOException(e.getMessage(), e);
                                }
                            })
                    .flatMap(node -> StreamSupport
                            .stream(followPath(node, jsonPath).spliterator(), true));
        } catch (UncheckedIOException e) {
            throw new IOException(e.getMessage(), e);
        }

        return result;
    }


    private StringBuilder getUrlBuilder(String urlPath, List<String> queryOptions) {
        StringBuilder sb =
                new StringBuilder(API_ENTRY)
                        .append(urlPath)
                        .append(QUERY_OPTIONS);
        if (queryOptions != null) {
            for (String option : queryOptions) {
                sb.append(option);
            }
        }
        return sb;
    }

    private JsonNode followPath(JsonNode node, List<String> jsonPath) {
        if (jsonPath != null) {
            for (String path : jsonPath) {
                node = node.path(path);
            }
        }
        return node;
    }

    private int getPageCount(URL url) throws IOException {
        int count = getNode(url).path(COUNT).asInt();
        int pageCount = count / ELEMENTS_PER_PAGE;
        if (count % ELEMENTS_PER_PAGE != 0) {
            ++pageCount;
        }
        return pageCount;
    }
}
