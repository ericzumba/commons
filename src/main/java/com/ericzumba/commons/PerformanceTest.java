package com.ericzumba.commons;

import com.google.common.cache.CacheBuilder;
import com.google.common.collect.MapMaker;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Class.forName;
import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;
import static java.lang.Thread.sleep;

/**
 * Created by erictorti on 11/10/16.
 */
public class PerformanceTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InterruptedException {
        out.println("10 seconds to start");
        sleep(10 * 1000);

        Chooser<String, Integer> c = new Chooser<>(guava(), ques -> -1, ques -> 1);
        c.installRule((answ) -> (answ < 0) ? c.choices(1) : c.choices(0));

        long experiments = parseLong(args[0]);
        long start = currentTimeMillis();
        for(int i = 0; i < experiments; i++)
            if(!c.choose(valueOf(i)).equals(-1)) throw new RuntimeException();

        int wrongDecisions = 0;
        for(long i = experiments - 1; i >= 0; i--)
            if(!c.choose(valueOf(i)).equals(1)) wrongDecisions++;

        long elapsed = currentTimeMillis() - start;
        out.println(format("%d choices in %d seconds", experiments, elapsed / 1000));
        out.println(format("%.4f%% bad decisions", ((100.0 * wrongDecisions) / experiments)));
    }

    private static Map guava() {
        return CacheBuilder.newBuilder()
                .maximumSize(200000)
                .build()
                .asMap();
    }

    private static Map map(String impl) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> mapImp = forName(impl);
        return (Map) mapImp.newInstance();
    }
}
