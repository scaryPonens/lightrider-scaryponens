package com.scaryponens;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Created by Reuben on 5/19/2017.
 */
public class Field {
    private final int width;
    private final int height;
    private final char bot;
    private final char[] field;

    public Field(int width, int height, char bot, char[] field) {
        this.width = width;
        this.height = height;
        this.bot = bot;
        this.field = field;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char getBot() {
        return bot;
    }

    public char[] getField() {
        return field;
    }

    public Function<Field, Stream<Move>> moveBot = field ->
        Arrays.asList(Move.UP, Move.LEFT, Move.DOWN, Move.RIGHT).stream()
            .map(f -> f.apply(field)).filter(m -> onField.test(m.getField()));

    public static Predicate<Field> onField = f -> {
        Optional<Integer> bot = getBot(f);
        int end = (f.width * f.height);
        return
                bot.isPresent() && 0 <= bot.get() && bot.get() < end;
    };

    public static Optional<Integer> getBot(Field f) {
        int i = 0;
        for (char c : f.field) {
            if (c == f.bot)
                return Optional.of(i);
            i++;
        }
        return Optional.empty();
    }

    public static Field of(int w, int h, char bot, char[] field) {
        return new Field(w, h, bot, field);
    }

    public Function<Integer,Field> markX = (pos) -> {
        this.getField()[pos] = 'x';
        return this;
    };

    public Function<Integer,Field> markBot = (pos) -> {
        this.getField()[pos] = this.getBot();
        return this;
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field1 = (Field) o;

        if (width != field1.width) return false;
        if (height != field1.height) return false;
        return Arrays.equals(field, field1.field);
    }

    @Override
    public int hashCode() {
        int result = width;
        result = 31 * result + height;
        result = 31 * result + Arrays.hashCode(field);
        return result;
    }
}

enum Direction {
    UP, RIGHT, DOWN, LEFT;
}

class Move {
    private Direction direction;
    private Field field;

    public Move(Direction d, Field f) {
        direction = d;
        this.field = f;
    }

    public Direction getDirection() {
        return direction;
    }

    public Field getField() {
        return field;
    }

    public static BiFunction<Field,Direction,Field> move = (field,d) -> {
        int start = Field.getBot(field).orElseThrow(IllegalArgumentException::new);
        field.markX.apply(start);
        switch (d) {
            case UP:
                field.markBot.apply(start - field.getWidth());
                break;
            case LEFT:
                field.markBot.apply(start - 1);
                break;
            case DOWN:
                field.markBot.apply(start + field.getWidth());
                break;
            case RIGHT:
                field.markBot.apply(start + 1);
                break;
        }
        return field;
    };

    public static final Function<Field, Move> UP = f ->
            new Move(
                    Direction.UP,
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), move.apply(f,Direction.UP).getField()));

    public static final Function<Field, Move> LEFT = f ->
            new Move(
                    Direction.LEFT,
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), move.apply(f,Direction.LEFT).getField()));

    public static final Function<Field, Move> DOWN = f ->
            new Move(
                    Direction.DOWN,
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), move.apply(f,Direction.DOWN).getField()));

    public static final Function<Field, Move> RIGHT = f ->
            new Move(
                    Direction.RIGHT,
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), move.apply(f,Direction.RIGHT).getField()));
}