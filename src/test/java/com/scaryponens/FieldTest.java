package com.scaryponens;

import com.scaryponens.Game.Field;
import com.scaryponens.Game.Move;
import com.scaryponens.monads.Pair;
import com.scaryponens.monads.Measurements;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by Reuben on 5/19/2017.
 */
public class FieldTest {

    private Field f = Field.of(16, 16, '0', '1', Field.genStartingField.apply(16l*16l));
    private Random R = new Random(System.currentTimeMillis());


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
    public void testDistanceMeasurementUp() {
        int n = R.nextInt(16);
        int m = R.nextInt(16);
        int pos = m*f.getWidth() + n;
        f.markBotOrCrash.apply(f.getBot1Id(), pos);
        Pair<String,Integer> meas = Measurements.distanceToCrashUP.apply(f, pos);
        assertThat(meas._1, is("up"));
        assertThat(meas._2, is(m));
    }

    @Test
    public void testDistanceMeasurementUpWithWall() {
        int n = 0;
        int m = 2;
        int pos = m*f.getWidth() + n;
        f.markBotOrCrash.apply(f.getBot1Id(), pos);
        Pair<String,Integer> meas = Measurements.distanceToCrashUP.apply(f, pos);
        assertThat(meas._1, is("up"));
        assertThat(meas._2, is(2));

        f.markX.apply(0);
        meas = Measurements.distanceToCrashUP.apply(f, pos);
        assertThat(meas._2, is(1));

        f.markX.apply(16);
        meas = Measurements.distanceToCrashUP.apply(f, pos);
        assertThat(meas._2, is(0));
    }

    @Test
    public void testDistanceMeasurementDown() {
        int n = 0;
        int m = 2;
        int pos = m*f.getWidth() + n;
        f.markBotOrCrash.apply(f.getBot1Id(), pos);
        Pair<String,Integer> meas = Measurements.distanceToCrashDOWN.apply(f, pos);
        assertThat(meas._1, is("down"));
        assertThat(meas._2, is(13));
    }

    @Test
    public void testDistanceMeasurementDownWithWall() {
        int n = 0;
        int m = 2;
        int pos = m*f.getWidth() + n;
        f.markBotOrCrash.apply(f.getBot1Id(), pos);
        Pair<String,Integer> meas = Measurements.distanceToCrashDOWN.apply(f, pos);
        assertThat(meas._1, is("down"));
        assertThat(meas._2, is(13));

        f.markX.apply(240);
        meas = Measurements.distanceToCrashDOWN.apply(f, pos);
        assertThat(meas._2, is(12));

        f.markX.apply(224);
        meas = Measurements.distanceToCrashDOWN.apply(f, pos);
        assertThat(meas._2, is(11));
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
