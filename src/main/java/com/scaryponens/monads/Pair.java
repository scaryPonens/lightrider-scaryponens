package com.scaryponens.monads;

/**
 * Created by Reuben on 5/20/2017.
 */
public class Pair<F,L> extends Tuple2<F,L> {

    public Pair(F f, L l) {
        super(f, l);
    }

    public static <F,L> Pair<F,L> of(F fst, L lst) {
        return new Pair(fst,lst);
    }
}
