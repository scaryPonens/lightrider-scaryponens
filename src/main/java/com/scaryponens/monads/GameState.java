package com.scaryponens.monads;

import com.scaryponens.Game.Field;

import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;

/**
 * Created by Reuben on 5/20/2017.
 */
public class GameState {

    private boolean IS_OVER = false;

    private Function<Field, Tuple3<String,Optional<Field>,Field>> runState;

    private Tuple3<String, Optional<Field>,Field> state;

    private Queue<String> moves = new LinkedBlockingQueue<>();

    private int gameRound = 0;

    public GameState(Function<Field, Tuple3<String, Optional<Field>, Field>> runState) {
        this.runState = runState;
    }

    public Queue<String> getMoves() {
        return this.moves;
    }

    public Tuple3<String, Optional<Field>,Field> runState(Field state) {
        gameRound++;
        this.state = this.runState.apply(state);
        moves.add(this.state._1);
        if (!this.state._2.isPresent()) {
            setGameOver(true);
        }
        return this.state;
    }

    public static GameState of(Function<Field, Tuple3<String, Optional<Field>,Field>> runState) {
        return new GameState(runState);
    }

    public GameState flatMap(Function<Field, Tuple3<String, Optional<Field>,Field>> famb) {
        if (isOver()) return GameState.gameOver();
        GameState gs = new GameState(f -> {
                Tuple3<String, Optional<Field>,Field> next = runState(f);
                GameState res = GameState.of(g -> famb.apply(next._3));
                return next._2.map(field -> res.runState(field)).orElse(Tuple3.of(next._1, Optional.empty(),next._3));
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
