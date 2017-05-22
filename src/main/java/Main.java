import com.scaryponens.Game.Field;

import java.util.*;
import java.util.stream.Collectors;


public class Main {

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
                    Field gameField = new Field(Integer.parseInt(field_width), Integer.parseInt(field_height), botId, field);
                    Optional<Integer> botPos = Field.getBot(gameField, botId);
                    List<Integer> neighbours = Field.validNeighbours.apply(gameField, botPos)
                        .collect(
                            Collectors.collectingAndThen(Collectors.toList(),
                                    collected -> {
                                        Collections.shuffle(collected);
                                        return collected;
                                    }));
                    String move = gameField.positionToCommand(Field.getBot(gameField, botId).get(), neighbours.get(0));
                    System.out.println(move);
                    System.out.flush();
                    break;
                default:
                    // error
            }
        }
    }

}


