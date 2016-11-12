package com.ericzumba.commons;

import com.google.common.cache.CacheBuilder;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by erictorti on 11/11/16.
 */
public class GuavaUseCase {

    @Test
    public void whenCacheIsLargeEnoughThereAreNoWrongDecisions() throws Exception {
        int choices = 10000;
        int maximumCacheSize = 15000;                                       // cache needs room to spare
        int wrongDecisions = getWrongDecisions(choices, maximumCacheSize);  // as it starts evicting before
                                                                            // hitting maximum capacity
        assertThat("wrongDecisions", wrongDecisions, is(0));
    }

    @Test
    public void whenCacheIsNotLargeEnoughSomeBadDecisionsAreMade() throws Exception {
        int choices = 20000;
        int maximumCacheSize = 10000;
        int wrongDecisions = getWrongDecisions(choices, maximumCacheSize);

        assertThat("wrongDecisions", wrongDecisions, is(10000));
    }

    private int getWrongDecisions(int experiments, int maximumCacheSize) {

        Function<Integer, Integer> negative = (req) -> -1;
        Function<Integer, Integer> positive = (req) -> 1;
        Function<Integer, Function<Integer, Integer>> flipFlop = (index) -> (index < 0) ? positive : negative;
        Chooser c = new Chooser(negative, flipFlop,  makeCache(maximumCacheSize));

        for(int i = 0; i < experiments; i++)
            if(!c.iterate(i).equals(-1)) throw new RuntimeException();

        int wrongDecisions = 0;
        for(int i = experiments - 1; i >= 0; i--)
            if(!c.iterate(i).equals(1)) wrongDecisions++;

        return wrongDecisions;
    }

    private Map makeCache(int maxSize) {
        return CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .build()
                .asMap();
    }
}
