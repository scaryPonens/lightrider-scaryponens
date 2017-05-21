package com.scaryponens.monads;

import com.scaryponens.Game.Field;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by Reuben on 5/20/2017.
 */
public class GameState {

    private Function<Field, Pair<Field,Field>> runState;

    public GameState(Function<Field, Pair<Field, Field>> runState) {
        this.runState = runState;
    }

    public Pair<Field,Field> runState(Field state) {
        return this.runState.apply(state);
    }

    public static GameState of(Function<Field, Pair<Field,Field>> runState) {
        return new GameState(runState);
    }

    public GameState flatMap(Function<Field, Pair<Field,Field>> famb) {
        return new GameState(f -> {
            Pair<Field,Field> next = this.runState(f);
            GameState res = GameState.of(g -> famb.apply(next._2));
            return res.runState(next._1);
        });
    }
}
