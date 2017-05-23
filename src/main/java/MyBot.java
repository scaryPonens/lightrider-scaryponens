import com.scaryponens.Game.Field;
import com.scaryponens.monads.GameState;
import com.scaryponens.monads.Pair;
import com.scaryponens.monads.Tuple3;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class MyBot {

    private Scanner scan = new Scanner(System.in);
    private char botId;
    private String field_width;
    private String field_height;
    private String round;
    private Character[] field;

    public void run() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.length() == 0) continue;

            String[] parts = line.split(" ");

            switch (parts[0]) {
                case "settings":
                    // store game settings
                    switch (parts[1]) {
                        case "your_botid":
                            botId = parts[2].charAt(0);
                            break;
                        case "field_width":
                            field_width = parts[2];
                            break;
                        case "field_height":
                            field_height = parts[2];
                            break;
                    }
                    break;
                case "update":
                    switch (parts[1]) {
                        case "game":
                            if ("round".equals(parts[2]))
                                round = parts[3];
                            else
                                field = parts[3].chars().filter(c -> c != (int)',').mapToObj(i -> (char)i).toArray(Character[]::new);
                            break;
                    }
                    break;
                case "action":
                    Field gameField = new Field(Integer.parseInt(field_width), Integer.parseInt(field_height), field);
                    String move = chooseMove(gameField, botId);
                    System.out.println(move);
                    System.out.flush();
                    break;
                default:
                    // error
            }
        }
    }

    public static String chooseMove(final Field field, char bot) {
        List<Tuple3<Integer,GameState,Stack<Field>>> games = IntStream.range(0, 100).parallel().mapToObj(i -> {
            GameState gs = moveBots(field.otherBot.apply(bot), bot).apply(field);
            Field _field = gs.runState(field)._3;
            Stack<Field> fields = playGame(gs, _field, new Stack<>());
            return Tuple3.of(gs.getGameRound(), gs, fields);
        }).sorted((p1, p2) -> Integer.compare(p1._1, p2._1))
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.reverse(collected);
                    return collected;
                }));

        GameState gs = games.get(0)._2;
        return gs.getMoves().peek();
    }

    public static Function<Field,GameState> moveBots(char bot1, char bot2) {
        return field ->
                GameState.of(f -> {
                    Optional<Integer> botPos = Field.getBot(f,bot1);
                    Pair<Integer,Optional<Field>> next = f.takeRandomMove(bot1);
                    String move = botPos.map(pos -> f.positionToCommand(pos, next._1)).orElse("up");
                    return Tuple3.of(move, next._2, field);
                }).flatMap(f -> {
                    Optional<Integer> botPos = Field.getBot(f,bot2);
                    Pair<Integer,Optional<Field>> next = f.takeRandomMove(bot2);
                    String move = botPos.map(pos -> f.positionToCommand(pos, next._1)).orElse("up");
                    return Tuple3.of(move, next._2, field);
                });
    }

    public static Stack<Field> playGame(GameState gs, Field f, Stack<Field> fields) {
        if (gs.isOver())
            return fields;
        else {
            Field next = gs.runState(f)._3;
            fields.push(next);
            return playGame(gs, next, fields);
        }
    }

    public static void main(String[] args) {
        (new MyBot()).run();
    }

}


