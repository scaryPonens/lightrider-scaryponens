package com.scaryponens.monads;

/**
 * Created by Reuben on 5/20/2017.
 */
public class Pair<F,L> extends Tuple2<F,L> {

    private boolean empty = false;

    public Pair(F f, L l) {
        super(f, l);
    }

    public Pair<F,L> setEmpty(boolean empty) {
        this.empty = empty;
        return this;
    }

    public static <F,L> Pair<F,L> of(F fst, L lst) {
        return new Pair(fst,lst);
    }

    public static <F,L> Pair<F,L> empty() {
        return new Pair<F,L>(null,null).setEmpty(true);
    }
}
