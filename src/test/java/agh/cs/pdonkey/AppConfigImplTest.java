package agh.cs.pdonkey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * pDonkey
 * Created by luknw on 08.01.2017
 */

class AppConfigImplTest {

    private static final MP MP = new MPImpl("foo");
    private static final AppConfig CONFIG = new AppConfigImpl(AppMode.MACARONIERIS, MP);

    @Test
    void getModeJustWorks() {
        assertEquals(AppMode.MACARONIERIS, CONFIG.getMode());
    }

    @Test
    void getMPJustWorks() {
        assertEquals(MP, CONFIG.getMP());
    }

}