package com.scaryponens.monads;

import com.scaryponens.Game.Field;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Reuben on 5/20/2017.
 */
public class RandomStateTest {

    @Test
    public void testGetRandom() {
        Field field = Field.of(16, 16, '0', '1', Field.genStartingField.apply(16l*16l));
        field.markBotOrCrash.apply('0', 0);
        field.markBotOrCrash.apply('1', 15);
        GameState gs = new GameState((Field f) -> Pair.of(f.takeRandomMove('0'), field))
            .flatMap(f -> Pair.of(f.takeRandomMove('1'), f))
            .flatMap(f -> Pair.of(f.takeRandomMove('0'), f))
            .flatMap(f -> Pair.of(f.takeRandomMove('1'), f));

        Pair<Field,Field> round1 = gs.runState(field);
        round1._1.showField();
        round1._2.showField();
    }
}
