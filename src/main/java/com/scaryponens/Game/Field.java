package com.scaryponens.Game;

import com.scaryponens.monads.Pair;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Reuben on 5/19/2017.
 */
public class Field {
    private int width;
    private int height;
    private char bot1 = '0';
    private char bot2 = '1';
    private Character[] field;

    public Field() {}

    public Field(int width, int height, char bot1, char bot2, Character[] field) {
        this.width = width;
        this.height = height;
        this.bot1 = bot1;
        this.bot2 = bot2;
        this.field = field;
    }

    public Field(int width, int height, Character[] field) {
        this.width = width;
        this.height = height;
        this.bot2 = otherBot.apply(bot1);
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

    public char getBot1Id() {
        return bot1;
    }

    public char getBot2Id() {
        return bot2;
    }

    public Character[] getField() {
        return field;
    }

    public void showField() {
        System.out.println(String.format("Light rider playing field for bot1 %s", bot1));
        int n = width*height;
        for (int i = 0; i < n; i++) {
            if (i > 0 && i % width == 0)
                System.out.print("\n");
            System.out.print(String.format("%s\t",field[i]));
        }
        System.out.println("\n");
    }

    public Supplier<Character[]> cloneField = () -> getField().clone();

    public Field clone() {
        return Field.of(this.getWidth(), this.getHeight(), this.getBot1Id(), this.getBot2Id(), this.cloneField.get());
    }

    public Predicate<Integer> botCrash =
            pos -> this.getField()[pos] == getBot1Id() || this.getField()[pos] == getBot2Id();


    public Function<Character,Character> otherBot = thisBot -> (thisBot == getBot1Id()) ?
            getBot2Id() : getBot1Id();

    public Predicate<Integer> wallCrash =
            pos -> getField()[pos] == 'x';

    public Predicate<Optional<Integer>> validMove = maybePos ->
            maybePos.isPresent() &&
                    !(wallCrash.test(maybePos.get()) || botCrash.test(maybePos.get()));

    public static BiFunction<Field,Optional<Integer>,Stream<Integer>> validNeighbours =
        (field,bot) ->
                Arrays.asList(Move.UP(bot), Move.LEFT(bot), Move.DOWN(bot), Move.RIGHT(bot)).stream()
                        .map(f -> f.apply(field))
                        .filter(field.validMove)
                        .map(Optional::get);

    public String positionToCommand(int start, int end) {
        int delta = start - end;
        switch(delta) {
            case 1:
                return "left";
            case -1:
                return "right";
            case 16:
                return "up";
            default:
                return "down";
        }
    }

    public Function<Integer,Boolean> validPos = pos ->
            0 <= pos && pos < getWidth()*getHeight();

    public Supplier<Optional<Integer>> getBot1 = () -> getBot(this, this.bot1);

    public Supplier<Optional<Integer>> getBot2 = () -> getBot(this, this.bot2);

    public Pair<Integer,Optional<Field>> takeRandomMove(char bot) {
        Optional<Integer> botPos = getBot(this, bot);
        if (!botPos.isPresent())
            return Pair.of(null, Optional.empty());
        List<Integer> validMoves = validNeighbours.apply(this, botPos)
            .collect(
                Collectors.collectingAndThen(Collectors.toList(),
                collected -> {
                    Collections.shuffle(collected);
                    return collected;
                }));
        if (validMoves.isEmpty()) {
            // GAME OVER I HOPE I DRAW
            markX.apply(botPos.get());
            return Pair.of(botPos.get(), Optional.empty());
        } // else
        markX.apply(botPos.get());
        return Pair.of(validMoves.get(0), Optional.of(markBotOrCrash.apply(bot, validMoves.get(0))));
    }

    public static Optional<Integer> getBot(Field f, char bot) {
        int i = 0;
        for (char c : f.field) {
            if (c == bot)
                return Optional.of(i);
            i++;
        }
        return Optional.empty();
    }

    public static Field of(int w, int h, char bot, char bot2, Character[] field) {
        return new Field(w, h, bot, bot2, field);
    }

    public Function<Integer,Field> markX = (pos) -> {
        this.getField()[pos] = 'x';
        return this;
    };

    public BiFunction<Character, Integer,Field> markBotOrCrash = (bot, pos) -> {
        this.getField()[pos] = botCrash.test(pos) ||
                wallCrash.test(pos) ? '*' : bot;
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



