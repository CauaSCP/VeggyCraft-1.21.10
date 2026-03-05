package net.klayil;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class MapToJson<S extends String, T> {
    @Override
    public String toString() {
        return value;
    }

    private final String value;

    public MapToJson(Map<S, ? extends T> mapToParse, String... outputFilePath) throws IOException {
        Gson gson = new Gson();

        if (outputFilePath.length > 0 && outputFilePath[0] != null) {
            FileWriter writer = new FileWriter(outputFilePath[0]);

            gson.toJson(mapToParse, writer);

            writer.close();

            value = Files.readString(Path.of(outputFilePath[0]));

            return;
        }

        value = new Gson().toJson(mapToParse);
    }
}
