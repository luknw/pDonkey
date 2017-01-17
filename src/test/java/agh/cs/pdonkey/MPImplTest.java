package agh.cs.pdonkey;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * pDonkey
 * Created by luknw on 08.01.2017
 */

class MPImplTest {
    private static final String ADVENTUROUS_MP = "Bruce Wayne";
    private static final String WE_DONT_NEED_ANOTHER = "Batman";
    private final MP mp = new MPImpl(ADVENTUROUS_MP);

    @Test
    void getNameJustWorks() {
        assertEquals(ADVENTUROUS_MP, mp.getName());
    }

    @Test
    void equalsTrueWhenSame() {
        MP alterEgo = new MPImpl(ADVENTUROUS_MP);

        assertTrue(alterEgo.equals(mp));
        assertTrue(mp.equals(alterEgo));
    }

    @Test
    void equalsFalseWhenDifferent() {
        MP alterEgo = new MPImpl(WE_DONT_NEED_ANOTHER);

        assertFalse(alterEgo.equals(mp));
        assertFalse(mp.equals(alterEgo));
    }

    @Test
    void hashCodeSameWhenSame() {
        MP alterEgo = new MPImpl(ADVENTUROUS_MP);

        assertEquals(alterEgo.hashCode(), mp.hashCode());
    }

    @Test
    void hashCodeDifferentWhenDifferent() {
        MP alterEgo = new MPImpl(WE_DONT_NEED_ANOTHER);

        assertNotEquals(alterEgo.hashCode(), mp.hashCode());
    }

    @Test
    void toStringJustWorks() {
        assertEquals(ADVENTUROUS_MP, mp.toString());
    }

}