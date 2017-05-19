package com.scaryponens;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scaryponens.Field.moveBot;

/**
 * Created by Reuben on 5/19/2017.
 */
public class FieldTest {

    @Test
    public void testMoveBot() {
        Field f = Field.of(16, 16, '0', Field.genStartingField.apply(16l*16l));
        f.showField();
        f.markBot.apply(0);
        f.showField();
        List<Move> moves = moveBot.apply(f).collect(Collectors.toList());
        moves.stream().forEach(Move::printMove);
        List<List<Move>> pairs = moves.stream().map(m -> moveBot.apply(m.getField().get()).collect(Collectors.toList())).collect(Collectors.toList());
        System.out.println(pairs);
    }
}
