package com.scaryponens;

import com.scaryponens.Game.Field;
import com.scaryponens.Game.Move;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Reuben on 5/19/2017.
 */
public class FieldTest {

    private Field f = Field.of(16, 16, '0', '1', Field.genStartingField.apply(16l*16l));

    @Test
    public void testMoveBot() {
        f.markBotOrCrash.apply(f.getBot1Id(),16);
        f.markX.apply(2);
        f.markX.apply(18);
        f.markX.apply(34);
        f.markX.apply(33);
        f.markX.apply(32);
        f.showField();
        Stream<Integer> moves = Field.validNeighbours.apply(f,Optional.of(16));
        System.out.println(moves);
        assertThat(moves.count(), is(2l));
    }

    @Test
    public void testRightEdge() {
        assertThat(Move.isRightEdge.apply(f, 15), is(true));
        assertThat(Move.isRightEdge.apply(f, 31), is(true));
        assertThat(Move.isRightEdge.apply(f, 47), is(true));
        assertThat(Move.isRightEdge.apply(f, 63), is(true));
        assertThat(Move.isRightEdge.apply(f, 79), is(true));
        assertThat(Move.isRightEdge.apply(f, 95), is(true));
        assertThat(Move.isRightEdge.apply(f, 111), is(true));
    }
}
