package dev.http418.sgmmssimulator.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    private static final Gson gson = new Gson();

    private static final Path dataFolder = Path.of("src/main/resources/dev/http418/sgmmssimulator/data");


    public static ArrayList<ArrayList<String>> loadMapData() {
        Path filePath = dataFolder.resolve("map.json");
        try {
            String json = Files.readString(filePath); // Java 11+
            Type type = new TypeToken<List<List<String>>>() {
            }.getType();
            List<List<String>> list = gson.fromJson(json, type);

            ArrayList<ArrayList<String>> result = new ArrayList<>();
            for (List<String> innerList : list) {
                ArrayList<String> innerArrayList = new ArrayList<>(innerList);
                result.add(innerArrayList);
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace(); // Cambiar a un alert
            throw new RuntimeException("Error al leer el archivo del mapa: " + filePath);
        }
    }
}