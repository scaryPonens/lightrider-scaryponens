package com.scaryponens;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Created by Reuben on 5/19/2017.
 */
public class Field {
    private final int width;
    private final int height;
    private final char bot;
    private final Character[] field;

    public Field(int width, int height, char bot, Character[] field) {
        this.width = width;
        this.height = height;
        this.bot = bot;
        this.field = field;
    }

    public static Function<Long, Character[]> genStartingField = (n) ->
            Stream.generate(() -> '.').limit(n).toArray(Character[]::new);

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char getBot() {
        return bot;
    }

    public Character[] getField() {
        return field;
    }

    public void showField() {
        System.out.println(String.format("Light rider playing field for bot %s", bot));
        int n = width*height;
        for (int i = 0; i < n; i++) {
            if (i > 0 && i % width == 0)
                System.out.print("\n");
            System.out.print(String.format("%s\t",field[i]));
        }
        System.out.println("\n");
    }

    public static Predicate<Supplier<Field>> onField = maybeF -> {
        Field f;
        try {
            f = maybeF.get();
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        Optional<Integer> bot = getBot(f);
        return
                bot.isPresent() &&
                        f.validPos.apply(bot.get()) &&
                        f.getField()[bot.get()] != '*';
    };

    public Supplier<Character[]> cloneField = () ->
        getField().clone();

    public static Function<Field,Stream<Move>> moveBot = (field) ->
        Arrays.asList(Move.UP, Move.LEFT, Move.DOWN, Move.RIGHT).stream()
            .map(f -> f.apply(field)).filter(m -> onField.test(m.getField()));

    public Function<Integer,Boolean> validPos = pos ->
            0 <= pos && pos < getWidth()*getHeight();

    public static Optional<Integer> getBot(Field f) {
        int i = 0;
        for (char c : f.field) {
            if (c == f.bot)
                return Optional.of(i);
            i++;
        }
        return Optional.empty();
    }

    public static Field of(int w, int h, char bot, Character[] field) {
        return new Field(w, h, bot, field);
    }

    public Function<Integer,Field> markXorCrash = (pos) -> {
        this.getField()[pos] = this.getField()[pos] == 'x' ? '*' : 'x';
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
    UP, RIGHT, DOWN, LEFT, NONE;
}

class Move {
    private Direction direction;
    private Supplier<Field> field;

    public Move(Direction d, Field f) {
        direction = d;
        this.field = () -> move.apply(f, direction);
    }

    public String showMove() {
        return String.format(" [ %s ] ", direction.name());
    }

    public void printMove() {
        System.out.print(this);
    }

    public String toString() {
        return showMove();
    }

    public Direction getDirection() {
        return direction;
    }

    public Supplier<Field> getField() {
        return field;
    }

    public static BiFunction<Field,Direction,Field> move = (field,d) -> {
        int start = Field.getBot(field).orElseThrow(IllegalArgumentException::new);
        field.markXorCrash.apply(start);
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
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), f.cloneField.get()));

    public static final Function<Field, Move> LEFT = f ->
            new Move(
                    Direction.LEFT,
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), f.cloneField.get()));

    public static final Function<Field, Move> DOWN = f ->
            new Move(
                    Direction.DOWN,
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), f.cloneField.get()));

    public static final Function<Field, Move> RIGHT = f ->
            new Move(
                    Direction.RIGHT,
                    Field.of(f.getWidth(), f.getHeight(), f.getBot(), f.cloneField.get()));
}