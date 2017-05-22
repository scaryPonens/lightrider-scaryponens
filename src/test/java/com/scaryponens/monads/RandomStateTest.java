package com.scaryponens.monads;

import com.scaryponens.Game.Field;
import org.junit.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Reuben on 5/20/2017.
 */
public class RandomStateTest {

    @Test
    public void testGetRandom() {
        List<Tuple3<Integer,GameState,Stack<Field>>> games = IntStream.range(0, 100).parallel().mapToObj(i -> {
            Field field, init = Field.of(16, 16, '0', '1', Field.genStartingField.apply(16l*16l));
            init.markBotOrCrash.apply('0', 0);
            init.markBotOrCrash.apply('1', 15);
            GameState gs = moveBots('0', '1').apply(init);
            field = gs.runState(init)._2;
            Stack<Field> fields = playGame(gs, field, new Stack<>());
            return Tuple3.of(gs.getGameRound(), gs, fields);
        })
        .sorted((p1, p2) -> Integer.compare(p1._1, p2._1))
        .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
            Collections.reverse(collected);
            return collected;
        }));

        GameState gs = games.get(0)._2;
        Stack<Field> fields = games.get(0)._3;

        if (gs.isOver())
            System.out.println("Game is over!");

        System.out.println(String.format("# of game rounds: %d", gs.getGameRound()));
        Pair<Optional<Integer>,Optional<Integer>> bots =
                Pair.of(fields.peek().getBot1.get(), fields.peek().getBot2.get());
        char winner = bots._1.isPresent() ? fields.peek().getBot1Id() : fields.peek().getBot2Id();
        System.out.println(String.format("Bot %s won!", winner));
        fields.peek().showField();
    }

    public static Function<Field,GameState> moveBots(char bot1, char bot2) {
        return field ->
                GameState.of(f -> Pair.of(f.takeRandomMove(bot1), field))
                .flatMap(f -> Pair.of(f.takeRandomMove(bot2), field));
    }

    public static Stack<Field> playGame(GameState gs, Field f, Stack<Field> fields) {
        if (gs.isOver())
            return fields;
        else {
            Field next = gs.runState(f)._2;
            fields.push(next);
            return playGame(gs, next, fields);
        }
    }
}
