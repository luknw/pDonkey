package agh.cs.pdonkey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * pDonkey
 * Created by luknw on 08.01.2017
 */

class AppConfigImplTest {

    private final MP mp = new MPImpl("foo");
    private final AppConfig config = new AppConfigImpl(AppMode.MACARONIERIS, mp);

    @Test
    void getModeJustWorks() {
        assertEquals(AppMode.MACARONIERIS, config.getMode());
    }

    @Test
    void getMPJustWorks() {
        assertEquals(mp, config.getMP());
    }

}