package agh.cs.pdonkey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
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

    //paging (experimental)
    static final int PARALLELISM = 4;
    static final int ELEMENTS_PER_PAGE = 130;
    static final String COUNT = "Count";
    static final String PAGE = "&page=";


    URL getUrl(String urlPath, List<String> queryOptions) throws MalformedURLException {
        return new URL(getUrlBuilder(urlPath, queryOptions).toString());
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


    JsonNode getNode(URL source) throws IOException {
        return getNode(source, null);
    }

    JsonNode getNode(URL source, List<String> jsonPath) throws IOException {
        JsonNode node = new ObjectMapper().readTree(source);
        return followPath(node, jsonPath);
    }


    JsonNode followPath(JsonNode node, List<String> jsonPath) {
        if (jsonPath != null) {
            for (String path : jsonPath) {
                node = node.path(path);
            }
        }
        return node;
    }


    Stream<JsonNode> streamList(String urlPath, List<String> queryOptions, List<String> jsonPath)
            throws IOException {

        StringBuilder urlBase = getUrlBuilder(urlPath, queryOptions);
        urlBase.append(WITH_LIMIT);

        int pageCount = getPageCount(new URL(urlBase.toString() + 1));
        String baseUrl = urlBase.append(ELEMENTS_PER_PAGE).append(PAGE).toString();

        // Ugly as hell workaround found in internetz for two Java issues
        // 1. Parallel streams and thread pools -> ForkJoinPool
        // 2. Checked exceptions inside lambda expressions -> rethrow as unchecked
        // I don't even. The curve is appreciable, though.
        try {
            return new ForkJoinPool(PARALLELISM)
                    .submit(() -> IntStream
                            .rangeClosed(1, pageCount)
                            .parallel()
                            .mapToObj(
                                    page -> {
                                        try {
                                            return getNode(new URL(baseUrl + page));
                                        } catch (IOException e) {
                                            throw new UncheckedIOException(e);
                                        }
                                    })
                            .flatMap(node -> StreamSupport
                                    .stream(followPath(node, jsonPath).spliterator(), true))
                    ).get();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        } catch (InterruptedException | ExecutionException e) {
            throw new IOException(e);
        }
    }

    int getPageCount(URL url) throws IOException {
        int count = getNode(url).get(COUNT).asInt();
        int pageCount = count / ELEMENTS_PER_PAGE;
        if (count % ELEMENTS_PER_PAGE != 0) {
            ++pageCount;
        }
        return pageCount;
    }
}
