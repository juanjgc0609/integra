package dev.http418.sgmmssimulator.util;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileManagerTest {

    private static final Path dataFolder = Path.of("src/main/resources/dev/http418/sgmmssimulator/data");
    private static final Path mapFile    = dataFolder.resolve("map.json");

    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(dataFolder);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(mapFile)) {
            Files.delete(mapFile);
        }
    }

    @Test
    void testLoadMapDataValid() throws IOException {
        String jsonContent = "[[\"A\",\"B\",\"C\"],[\"D\",\"E\"]]";
        Files.writeString(mapFile, jsonContent);

        ArrayList<ArrayList<String>> result = FileManager.loadMapData();

        assertNotNull(result);
        assertEquals(2, result.size());

        ArrayList<String> firstList = result.get(0);
        assertEquals(3, firstList.size());
        assertEquals("A", firstList.get(0));
        assertEquals("B", firstList.get(1));
        assertEquals("C", firstList.get(2));

        ArrayList<String> secondList = result.get(1);
        assertEquals(2, secondList.size());
        assertEquals("D", secondList.get(0));
        assertEquals("E", secondList.get(1));
    }

    @Test
    void testLoadMapDataFileNotFound() {
        // 1) Asegurarnos de que map.json NO exista
        if (Files.exists(mapFile)) {
            try { Files.delete(mapFile); }
            catch (IOException ignore) {}
        }

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            FileManager.loadMapData();
        });

        String expectedMessagePart = "Error al leer el archivo del mapa";
        assertTrue(exception.getMessage().contains(expectedMessagePart));
    }
}
