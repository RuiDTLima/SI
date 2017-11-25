package pt.isel.si;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Utils {
    public static String load(String path) {
        try {
            return Files.readAllLines(Paths.get(path))
                    .stream()
                    .collect(Collectors.joining());
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}