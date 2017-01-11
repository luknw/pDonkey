package agh.cs.pdonkey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * pDonkey
 * Created by luknw on 08.01.2017
 */

class OptionsParserImplTest {
    private static final String EMPTY_ARGS_NO_EXCEPTION = "Empty args list didn't cause IllegalArgumentException";
    private static final String MISSING_MP_NO_EXCEPTION = "Missing MP identity didn't cause IllegalArgumentException";

    private static final OptionsParser PARSER = new OptionsParserImpl();

    @Test
    void parseEmptyArgs() {
        String[] args = {};

        try {
            PARSER.parse(args);
            fail(EMPTY_ARGS_NO_EXCEPTION);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Test
    void parseValidArgsWithTail() {
        String[] args = {"travels", "dummy", "hummy"};
        AppConfig validConfig = new AppConfigImpl(AppMode.TOP_TRAVELS_ABROAD, new MPImpl("dummy"));

        AppConfig config = PARSER.parse(args);

        assertEquals(validConfig.getMode(), config.getMode());
    }

    @Test
    void parseReportsMissingMPIdentity() {
        String[] args = {"sum"};

        try {
            PARSER.parse(args);
            fail(MISSING_MP_NO_EXCEPTION);
        } catch (IllegalArgumentException ignored) {
        }
    }
}