package com.scaryponens;

import com.scaryponens.Game.Field;
import com.scaryponens.Game.Move;
import com.scaryponens.monads.Pair;
import com.scaryponens.monads.Measurements;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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

    @Test
    public void testApplyUnite() {
        for (int i = 0; i < 16; i++)
            f.markX.apply(8+f.getWidth()*i);
        for (int i = 0; i < 16; i++)
            f.markX.apply(128+i);
        f.showField();
        f.applyUnite();
        assertThat(f.find.apply(0, 255), is(false));
        assertThat(f.find.apply(0, 15), is(false));
        assertThat(f.find.apply(0, 16), is(true));
        assertThat(f.find.apply(0, 64), is(true));
        assertThat(f.find.apply(0, 80), is(true));
        assertThat(f.find.apply(0, 144), is(false));
    }

    @Test
    public void testWhichInternalQuadrant() {
        Optional<Integer> internalQuad = Field.internalQuadrant.apply(f, 0);
        assertThat(internalQuad.get(), is(0));
        internalQuad = Field.internalQuadrant.apply(f, 15);
        assertThat(internalQuad.get(), is(3));
        internalQuad = Field.internalQuadrant.apply(f, 255);
        assertThat(internalQuad.get(), is(15));
    }

    @Test
    public void testWhichQuadrant() {
        Optional<Integer> internalQuad = Field.quadrant.apply(f, 0);
        assertThat(internalQuad.get(), is(0));
        internalQuad = Field.quadrant.apply(f, 15);
        assertThat(internalQuad.get(), is(1));
        internalQuad = Field.quadrant.apply(f, 240);
        assertThat(internalQuad.get(), is(2));
        internalQuad = Field.quadrant.apply(f, 255);
        assertThat(internalQuad.get(), is(3));
    }

    @Test
    public void testDensityInQuadrant() {
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.quadrant.apply(f,i).map(q -> q == 1).orElse(false))
                .forEach(i -> f.markX.apply(i));
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.internalQuadrant.apply(f,i).map(q -> q == 14 || q == 15).orElse(false))
                .forEach(i -> f.markX.apply(i));
        f.showField();
        assertThat(Field.quadDensity.apply(f, 1).getAsDouble(), is(1.0d));
        assertThat(Field.quadDensity.apply(f, 0).getAsDouble(), is(0d));
        assertThat(Field.quadDensity.apply(f, 3).getAsDouble(), is(0.5d));
    }

    @Test
    public void testRankedQuads() {
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.quadrant.apply(f,i).map(q -> q == 1).orElse(false))
                .forEach(i -> f.markX.apply(i));
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.internalQuadrant.apply(f,i).map(q -> q == 14 || q == 15).orElse(false))
                .forEach(i -> f.markX.apply(i));
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.internalQuadrant.apply(f,i).map(q -> q == 0).orElse(false))
                .forEach(i -> f.markX.apply(i));
        f.showField();
        List<Pair<Integer,Double>> ranked = IntStream.range(0,4).parallel().mapToObj(i -> Pair.of(i, Field.quadDensity.apply(f,i).getAsDouble()))
                .sorted((l,r) -> l._2.compareTo(r._2))
                .collect(Collectors.toList());
        assertThat(ranked.get(0)._1, is(2));
        assertThat(ranked.get(1)._1, is(0));
        assertThat(ranked.get(2)._1, is(3));
        assertThat(ranked.get(3)._1, is(1));
    }

    @Test
    public void testRankedDestInQuad() {
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.quadrant.apply(f,i).map(q -> q == 1).orElse(false))
                .forEach(i -> f.markX.apply(i));
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.internalQuadrant.apply(f,i).map(q -> q == 14 || q == 15).orElse(false))
                .forEach(i -> f.markX.apply(i));
        IntStream.range(0, f.getField().length).parallel()
                .filter(i -> Field.internalQuadrant.apply(f,i).map(q -> q == 0).orElse(false))
                .forEach(i -> f.markX.apply(i));
        f.showField();
        List<Pair<Integer,Double>> ranked = IntStream.range(0,4).parallel().mapToObj(i -> Pair.of(i, Field.quadDensity.apply(f,i).getAsDouble()))
                .sorted((l,r) -> l._2.compareTo(r._2))
                .collect(Collectors.toList());
        int r = 240 / 16;
        List<Pair<Integer,Pair<String,Integer>>> rankedCommand = IntStream.range(0, 8).parallel().mapToObj(i -> Pair.of(r*16+i, Measurements.distanceToCrashUP.apply(f, r*16+i)))
                .sorted((a,b) -> b._2._2.compareTo(a._2._2))
                .collect(Collectors.toList());
        assertThat(rankedCommand.get(0)._1, is(244));
        assertThat(rankedCommand.get(0)._2._2, is(15));
    }
}
