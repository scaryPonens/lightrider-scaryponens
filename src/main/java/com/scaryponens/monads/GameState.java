package com.scaryponens.monads;

import com.scaryponens.Game.Field;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by Reuben on 5/20/2017.
 */
public class GameState {

    private boolean IS_OVER = false;

    private Function<Field, Pair<Optional<Field>,Field>> runState;

    private Pair<Optional<Field>, Field> state;

    private int gameRound = 0;

    public GameState(Function<Field, Pair<Optional<Field>, Field>> runState) {
        this.runState = runState;
    }

    public Pair<Optional<Field>,Field> runState(Field state) {
        gameRound++;
        this.state = this.runState.apply(state);
        if (!this.state._1.isPresent()) {
            setGameOver(true);
        }
        return this.state;
    }

    public static GameState of(Function<Field, Pair<Optional<Field>,Field>> runState) {
        return new GameState(runState);
    }

    public GameState flatMap(Function<Field, Pair<Optional<Field>,Field>> famb) {
        if (isOver()) return GameState.gameOver();
        GameState gs = new GameState(f -> {
                Pair<Optional<Field>,Field> next = runState(f);
                GameState res = GameState.of(g -> famb.apply(next._2));
                return next._1.map(field -> res.runState(field)).orElse(Pair.of(Optional.empty(),next._2));
            });
        return gs;
    }

    public static GameState gameOver() {
        return GameState.of(null).setGameOver(true);
    }

    public boolean isOver() {
        return IS_OVER;
    }

    public GameState setGameOver(boolean gameOver) {
        IS_OVER = gameOver;
        return this;
    }

    public int getGameRound() {
        return gameRound;
    }
}
