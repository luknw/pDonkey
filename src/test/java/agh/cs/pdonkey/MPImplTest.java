package agh.cs.pdonkey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * pDonkey
 * Created by luknw on 08.01.2017
 */

class MPImplTest {
    private static final String adventurousMP = "Bruce Wayne";
    private static final String weDontNeedAnother = "Batman";
    private static final MP mp = new MPImpl(adventurousMP);

    @Test
    void getNameJustWorks() {
        assertEquals(adventurousMP, mp.getName());
    }

    @Test
    void equalsTrueWhenSame() {
        MP alterEgo = new MPImpl(adventurousMP);

        assertTrue(alterEgo.equals(mp));
        assertTrue(mp.equals(alterEgo));
    }

    @Test
    void equalsFalseWhenDifferent() {
        MP alterEgo = new MPImpl(weDontNeedAnother);

        assertFalse(alterEgo.equals(mp));
        assertFalse(mp.equals(alterEgo));
    }

    @Test
    void hashCodeSameWhenSame() {
        MP alterEgo = new MPImpl(adventurousMP);

        assertEquals(alterEgo.hashCode(), mp.hashCode());
    }

    @Test
    void hashCodeDifferentWhenDifferent() {
        MP alterEgo = new MPImpl(weDontNeedAnother);

        assertNotEquals(alterEgo.hashCode(), mp.hashCode());
    }

    @Test
    void toStringJustWorks() {
        assertEquals(adventurousMP, mp.toString());
    }

}