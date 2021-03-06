package com.ericzumba.commons;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.function.Function;

import static java.lang.Integer.valueOf;
import static org.junit.Assert.assertEquals;

/*
 * This Java source file was auto generated by running 'gradle init --type java-library'
 * by 'ericzumba' at '11/10/16 12:13 AM' with Gradle 3.1
 *
 * @author ericzumba, @date 11/10/16 12:13 AM
 */
public class ChooserTest {

    private Chooser<Integer, Integer> c;

    @Before
    public void setUp() throws Exception {
        Function<Integer, Integer> negative = (req) -> -1;
        Function<Integer, Integer> positive = (req) -> 1;
        Function<Integer, Function<Integer, Integer>> flipFlop = (index) -> (index < 0) ? positive : negative;
        c = new Chooser(negative, flipFlop,  new HashMap<>());
    }

    @Test
    public void alwaysStartsByUsingTheFirstChoice() {;
        Integer request = 42;
        assertEquals(valueOf(-1), c.iterate(42));
    }

    @Test
    public void choosesWiselyAfterFirstTime() throws Exception {
        Integer request = 42;
        assertEquals(valueOf(-1), c.iterate(request));
        assertEquals(valueOf(1), c.iterate(request));
        assertEquals(valueOf(-1), c.iterate(request));
        assertEquals(valueOf(1), c.iterate(request));
    }
}
