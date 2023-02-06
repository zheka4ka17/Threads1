package com.company;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class InventoryGenerator {
    public static void main(String[] args) throws IOException {
        Random random = new Random();
        Path fileName = Path.of("src/com/company/database.sql");
        for(int i=0; i<1000; i++){
            int character = random.nextInt(35)+1;
            int item = random.nextInt(92)+1;

        String query
                = String.format("Insert into inventory(character_id, item_id) values (%d, %d);%n", character, item);
            Files.writeString(fileName,query, StandardOpenOption.APPEND);

    }
    }
}
