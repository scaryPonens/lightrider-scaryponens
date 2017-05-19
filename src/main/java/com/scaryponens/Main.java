package com.scaryponens;

import java.util.Scanner;

public class Main {


    private Scanner scan = new Scanner(System.in);


    public void run() {
        while (scan.hasNextLine()) {
            String line = scan.nextLine();

            if (line.length() == 0) continue;

            String[] parts = line.split(" ");
            switch (parts[0]) {
                case "settings":
                    // store game settings
                    break;
                case "update":
                    // store game updates
                    break;
                case "action":
                    System.out.println("up");
                    System.out.flush();
                    break;
                default:
                    // error
            }
        }
    }
}
