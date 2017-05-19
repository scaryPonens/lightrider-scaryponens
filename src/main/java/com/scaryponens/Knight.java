package com.scaryponens;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Reuben on 5/18/2017.
 */
public class Knight {
    @Test
    public void canReachIn3() {
        assertThat(KnightPos.of(6,2).canReachIn3.apply(KnightPos.of(6,1)), is(true));
        assertThat(KnightPos.of(6,2).canReachIn3.apply(KnightPos.of(7,3)), is(false));
    }
}

class KnightPos {
    private final int _1;
    private final int _2;

    public KnightPos(int x, int y) {
        _1 = x;
        _2 = y;
    }

    public Predicate<KnightPos> onBoard = p ->
            1 <= p._1 && p._1 <= 8 && 1 <= p._2 && p._2 <= 8;

    public Function<KnightPos, Stream<KnightPos>> moveKnight = p -> {
        int c = p._1, r = p._2;
        return Arrays.asList(
                of(c + 2, r - 1), of(c + 2, r + 1), of(c - 2, r - 1), of(c - 2, r + 1),
                of(c + 1, r - 2), of(c + 1, r + 2), of(c - 1, r - 2), of(c - 1, r + 2))
                .stream()
                .filter(onBoard);
    };

    public static KnightPos of(int x, int y) {
        return new KnightPos(x, y);
    }

    private List<KnightPos> fmap(Function<KnightPos, List<KnightPos>> f) {
        return f.apply(this);
    }

    public Function<KnightPos, Stream<KnightPos>> in3 = p ->
            Stream.of(p)
                .flatMap(moveKnight)
                .flatMap(moveKnight)
                .flatMap(moveKnight);

    public List<KnightPos> toList(Stream<KnightPos> ks) {
        return ks.collect(Collectors.toList());
    }

    public Function<KnightPos,Boolean> canReachIn3 = (end) ->
        in3.apply(this).collect(Collectors.toList()).contains(end);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KnightPos knightPos = (KnightPos) o;

        if (_1 != knightPos._1) return false;
        return _2 == knightPos._2;
    }

    @Override
    public int hashCode() {
        int result = _1;
        result = 31 * result + _2;
        return result;
    }
}
