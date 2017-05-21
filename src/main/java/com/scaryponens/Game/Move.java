package com.scaryponens.Game;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Move {
    public static BiFunction<Field,Integer,Boolean> isLeftEdge= (field, pos)->
            pos % field.getWidth() == 0;
    public static BiFunction<Field,Integer,Boolean> isRightEdge= (field, pos) ->
            pos == field.getWidth() - 1;
    public static BiFunction<Field,Integer,Boolean> isTopEdge= (field, pos) ->
            pos < field.getWidth();
    public static BiFunction<Field,Integer,Boolean> isBottomEdge= (field, pos) ->
            field.getWidth() * (field.getHeight() - 1) <= pos;

    public static final Function<Field, Optional<Integer>> UP(Optional<Integer> bot) {
        return field -> {
            if (!bot.isPresent()) return Optional.empty();
            int start = bot.get();
            if (isTopEdge.apply(field, start))
                return Optional.empty();

            return Optional.of(start - field.getWidth());
        };
    }

    public static final Function<Field, Optional<Integer>> LEFT(Optional<Integer> bot) {
        return field -> {
            if (!bot.isPresent()) return Optional.empty();
            int start = bot.get();
            if (isLeftEdge.apply(field, start))
                return Optional.empty();

            return Optional.of(start - 1);
        };
    }

    public static final Function<Field, Optional<Integer>> DOWN(Optional<Integer> bot) {
        return field -> {
            if (!bot.isPresent()) return Optional.empty();
            int start = bot.get();
            if (isBottomEdge.apply(field, start))
                return Optional.empty();

            return Optional.of(start + field.getWidth());
        };
    }

    public static final Function<Field, Optional<Integer>> RIGHT(Optional<Integer> bot) {
        return field -> {
            if (!bot.isPresent()) return Optional.empty();
            int start = bot.get();
            if (isRightEdge.apply(field, start))
                return Optional.empty();

            return Optional.of(start + 1);
        };
    }
}