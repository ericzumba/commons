package com.ericzumba.commons;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

import static java.util.Optional.ofNullable;

/*
 * Redefines the transformation from A => B based
 * on the particular characterists of a *b* given
 * obtained from a particular instance *a*.
 *
 * @author ericzumba, @date 11/10/16 12:13 AM
 */
public class Chooser<A, B> {

    private final Function<A, B> first;
    private final Function<B, Function<A, B>>  next;
    private Map<Integer, Function<A, B>> knowledge;

    /**
     * Defaults to use a WeakHashMap as the knowledge base
     *
     * @param first first function A => B to iterate through
     * @param next defines the function A => B to be used on the next iteration
     */
    public Chooser(Function<A, B> first,
                   Function<B, Function<A, B>> next) {
        this(first, next, new WeakHashMap<Integer, Function<A, B>>());
    }

    /**
     *
     * @param first first function A => B to iterate through
     * @param next defines the function A => B to be used on the next iteration
     * @param knowledge stores the information about which is the next function
     *                  to be used for each particular instance of A.
     *                  IMPORTANT: there is no automatic clean up of the map,
     *                  thus it could potentially grow infinitely and cause
     *                  an application to go out of memory. It's strongly
     *                  recommended to use a Map that will free up memory
     *                  given some criterion, e.g. WeakHashMap
     */
    public Chooser(Function<A, B> first,
                   Function<B, Function<A, B>> next,
                   Map<Integer, Function<A, B>> knowledge) {
        this.first = first;
        this.next = next;
        this.knowledge = knowledge;
    }

    /**
     *
     * @param arg an argument to be used for the transformation A => B
     * @return the application of a chosen function A => B. Either
     * the first one provided, or another one based on what is the knowledge
     * obtained from a previous iteration
     */
    public B iterate(A arg) {
        B resp = ofNullable(knowledge.get(arg.hashCode()))
                .orElse(first)
                .apply(arg);

        knowledge.put(arg.hashCode(), next.apply(resp));

        return resp;
    }
}