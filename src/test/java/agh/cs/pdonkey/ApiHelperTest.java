package agh.cs.pdonkey;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * pDonkey
 * Created by luknw on 08.01.2017
 */

class ApiHelperTest {
    private static final String VALID_URL_STRING = "https://api-v3.mojepanstwo.pl/dane/";
    private static final String TEST_SETUP_BROKEN = "Test setup broken";
    private static final String COULD_NOT_CREATE_VALID_URL = "Error: ApiHelper.getUrl couldn't create valid URL";
    private static final String NONEXISTENT = "mikes_girlfriend";
    private static final String VALID_NONEXISTENT_URL_STRING = VALID_URL_STRING + NONEXISTENT;
    private static final String DID_NOT_HANDLE_VALID_REQUEST = "Didn't handle a valid request";
    private static final String EXPECTED_JSON_NODE_FILE_PATH = "src/test/resources/expectedNode.json";
    private static final String EXPECTED_NODE_ORIGIN_URL = "https://api-v3.mojepanstwo.pl/dane/poslowie/100";

    private static final ApiHelper API = new ApiHelper();

    @Test
    void getUrlWithoutOptions() {
        URL expected = null;
        try {
            expected = new URL(VALID_URL_STRING + "?");
        } catch (MalformedURLException e) {
            fail(TEST_SETUP_BROKEN);
        }

        URL actual = null;
        try {
            actual = API.getUrl("", null);
        } catch (IOException e) {
            fail(COULD_NOT_CREATE_VALID_URL);
        }

        assertEquals(expected, actual);
    }

    @Test
    void getUrlWithOptions() {
        int term = 8;
        List<String> queryOptions = asList(ApiHelper.WITH_TERM + term, ApiHelper.WITH_EXPENSES);
        URL expected = null;
        try {
            expected = new URL(VALID_URL_STRING
                    + ApiHelper.QUERY_OPTIONS
                    + ApiHelper.WITH_TERM + term
                    + ApiHelper.WITH_EXPENSES);
        } catch (MalformedURLException e) {
            fail(TEST_SETUP_BROKEN);
        }

        URL actual = null;
        try {
            actual = API.getUrl("", queryOptions);
        } catch (IOException e) {
            fail(COULD_NOT_CREATE_VALID_URL);
        }

        assertEquals(expected, actual);
    }

    @Test
    void getUrlCreatesValidNonExistentURL() {
        URL expected = null;
        try {
            expected = new URL(VALID_NONEXISTENT_URL_STRING + "?");
        } catch (MalformedURLException e) {
            fail(TEST_SETUP_BROKEN);
        }

        URL actual = null;
        try {
            actual = API.getUrl(NONEXISTENT, null);
        } catch (IOException e) {
            fail(COULD_NOT_CREATE_VALID_URL);
        }

        assertEquals(expected, actual);
    }

    @Test
    void getNodeHandlesValidRequest() {
        URL validUrl = null;
        JsonNode expected = null;
        try {
            validUrl = new URL(EXPECTED_NODE_ORIGIN_URL);
            expected = new ObjectMapper().readTree(new File(EXPECTED_JSON_NODE_FILE_PATH));
        } catch (IOException e) {
            fail(TEST_SETUP_BROKEN);
        }

        JsonNode actual = null;
        try {
            actual = API.getNode(validUrl);
        } catch (IOException e) {
            fail(DID_NOT_HANDLE_VALID_REQUEST);
        }

        assertEquals(expected, actual);
    }
}