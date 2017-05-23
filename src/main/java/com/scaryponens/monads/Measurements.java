package com.scaryponens.monads;

import com.scaryponens.Game.Field;

import java.util.Optional;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

/**
 * Created by Reuben on 5/22/2017.
 */
public class Measurements {
    public static BiFunction<Field,Integer,Pair<String,Integer>> distanceToCrashUP = (f,pos) -> {
        int w = f.getWidth(), h = f.getHeight();
        int n = pos / w;
        int c = pos % w;
        char[] stack = new char[n + 1];
        stack[n] = '.';
        int d = IntStream.range(0, n).map(i -> {
            int j = (n - 1) - i;
            int r = j * w;
            if (f.getField()[r + c] == '.' && stack[n - i] == '.') {
                stack[j] = '.';
                return 1;
            } else {
                stack[j] = '*'; // crash
                return 0;
            }
        }).sum();
        return Pair.of("up", d);
    };

    public static BiFunction<Field,Integer,Pair<String,Integer>> distanceToCrashDOWN = (f,pos) -> {
        int w = f.getWidth(), h = f.getHeight();
        int n = (pos / w);
        int c = pos % w;
        char[] stack = new char[h - n];
        stack[0] = '.';
        int d = IntStream.range(n+1, w).map(i -> {
            int r = i * w + c;
            if (f.getField()[r + c] == '.' && stack[i - n - 1] == '.') {
                stack[i - n] = '.';
                return 1;
            } else {
                stack[i - n] = '*'; // crash
                return 0;
            }
        }).sum();
        return Pair.of("down", d);
    };

}
