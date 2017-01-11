package agh.cs.pdonkey;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * pDonkey
 * Created by luknw on 09.01.2017
 */

class DataProcessorImplTest {
    private static final DataProcessor DP = new DataProcessorImpl();
    private static final MP VOLUNTEER = new MPImpl("Jarosław Kaczyński");
    private static final MP NEMESIS = new MPImpl("Bronisław Komorowski");

    private static final String UNEXPECTED_IO_EXCEPTION = "Unexpected IO exception";
    private static final String INVALID_MP_NO_EXCEPTION = "Invalid MP didn't cause exception";
    private static final double EPSILON = 1e-6;

    @Test
    void getExpensesSumJustWorks() {
        double expected = 418320.43;

        double actual = 0;
        try {
            actual = DP.getExpensesSum(VOLUNTEER);
        } catch (IOException e) {
            fail(UNEXPECTED_IO_EXCEPTION);
        }

        assertEquals(expected, actual, EPSILON);
    }

    @Test
    void getMinorExpensesJustWorks() {
        double expected = 0;

        double actual = 1;
        try {
            actual = DP.getMinorExpenses(VOLUNTEER);
        } catch (IOException e) {
            fail(UNEXPECTED_IO_EXCEPTION);
        }

        assertEquals(expected, actual, EPSILON);
    }

    @Test
    void getAvgMpsExpensesJustWorks() {
        double expected = 272247.607894;

        double actual = 1;
        try {
            actual = DP.getAvgMpsExpenses();
        } catch (IOException e) {
            fail(UNEXPECTED_IO_EXCEPTION);
        }

        assertEquals(expected, actual, EPSILON);
    }

    @Test
    void getTopTravellerMpJustWorks() {
        MP expected = new MPImpl("Tadeusz Iwiński");

        MP actual = null;
        try {
            actual = DP.getTopTravellerMp();
        } catch (IOException e) {
            fail(UNEXPECTED_IO_EXCEPTION);
        }

        assertEquals(expected, actual);
    }

    @Test
    void getTopTimeAbroadMpJustWorks() {
        MP expected = new MPImpl("Tadeusz Iwiński");

        MP actual = null;
        try {
            actual = DP.getTopTimeAbroadMp();
        } catch (IOException e) {
            fail(UNEXPECTED_IO_EXCEPTION);
        }

        assertEquals(expected, actual);
    }

    @Test
    void getMostExpensiveTravelMpJustWorks() {
        MP expected = new MPImpl("Adam Szejnfeld");

        MP actual = null;
        try {
            actual = DP.getMostExpensiveTravelMp();
        } catch (IOException e) {
            fail(UNEXPECTED_IO_EXCEPTION);
        }

        assertEquals(expected, actual);
    }

    @Test
    void getMacaronierisJustWorks() {
        List<String> mpNames = Arrays.asList("Adam Abramowicz", "Adam Rogacki", "Agnieszka Pomaska", "Andrzej Biernat", "Andrzej Buła", "Andrzej Czerwiński", "Andrzej Gałażewski", "Andrzej Gut-Mostowy", "Anna Fotyga", "Anna Nemś", "Antoni Mężydło", "Artur Górczyński", "Beata Bublewicz", "Bogusław Wontor", "Cezary Grabarczyk", "Cezary Kucharski", "Cezary Tomczyk", "Eugeniusz Tomasz Grzeszczak", "Ewa Kopacz", "Grzegorz Raniewicz", "Grzegorz Schetyna", "Ireneusz Raś", "Jacek Falfus", "Jadwiga Zakrzewska", "Jakub Rutnicki", "Jan Bury", "Jan Dziedziczak", "Janusz Piechociński", "Jerzy Fedorowicz", "Joanna Fabisiak", "Józef Zych", "Krystyna Skowrońska", "Krzysztof Szczerski", "Marek Matuszewski", "Marek Rząsa", "Mariusz Grad", "Michał Jaros", "Piotr Tomański", "Rafał Grupiński", "Robert Tyszkiewicz", "Roman Jacek Kosecki", "Stanisława Prządka", "Stanisław Wziątek", "Stefan Niesiołowski", "Sławomir Neumann", "Tadeusz Iwiński", "Tomasz Garbowski", "Wojciech Penkalski", "Wojciech Ziemniak");
        List<MP> expected = mpNames.stream().map(MPImpl::new).collect(Collectors.toList());

        List<MP> actual = null;
        try {
            actual = DP.getMacaronieris();
        } catch (IOException e) {
            fail(UNEXPECTED_IO_EXCEPTION);
        }

        assertEquals(expected, actual);
    }

    @Test
    void getExpensesSumThrowsExceptionOnInvalidMP() {
        try {
            DP.getExpensesSum(NEMESIS);
            fail(INVALID_MP_NO_EXCEPTION);
        } catch (IOException ignored) {
        }
    }

    @Test
    void getMinorExpensesThrowsExceptionOnInvalidMP() {
        try {
            DP.getMinorExpenses(NEMESIS);
            fail(INVALID_MP_NO_EXCEPTION);
        } catch (IOException ignored) {
        }
    }
}