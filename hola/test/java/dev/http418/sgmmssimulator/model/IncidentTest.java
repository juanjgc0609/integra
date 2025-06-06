package dev.http418.sgmmssimulator.model;

import dev.http418.sgmmssimulator.model.enums.IncidentState;
import dev.http418.sgmmssimulator.model.enums.IncidentType;
import dev.http418.sgmmssimulator.model.enums.TileType;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IncidentTest {

    private Incident incident;
    private Tile testTile;

    @BeforeEach
    void setUp() {
        testTile = new Tile(5, 10, TileType.RESIDENTIAL);
        incident = new Incident(testTile, IncidentType.THEFT);
    }

    @Test
    void testConstructor() {
        assertEquals(testTile, incident.getSource());
        assertEquals(IncidentType.THEFT, incident.getType());
        assertEquals(IncidentState.ACTIVE, incident.getState());
        assertTrue(incident.getTimestamp() > 0);
    }

    @Test
    void testStateTransitions() {
        // Test complete state flow
        assertEquals(IncidentState.ACTIVE, incident.getState());

        incident.switchState();
        assertEquals(IncidentState.ASSIGNED, incident.getState());

        incident.switchState();
        assertEquals(IncidentState.PROCESSING, incident.getState());

        incident.switchState();
        assertEquals(IncidentState.RESOLVED, incident.getState());

        // Should stay resolved
        incident.switchState();
        assertEquals(IncidentState.RESOLVED, incident.getState());
    }

    @Test
    void testCompareTo() {
        Tile tile1 = new Tile(1, 1, TileType.RESIDENTIAL);
        Tile tile2 = new Tile(2, 2, TileType.INDUSTRIAL);

        // Test priority comparison
        Incident theftIncident = new Incident(tile1, IncidentType.THEFT);     // Priority 1
        Incident fireIncident = new Incident(tile2, IncidentType.FIRE);       // Priority 2
        Incident accidentIncident = new Incident(tile1, IncidentType.ACCIDENT); // Priority 3

        assertTrue(theftIncident.compareTo(fireIncident) < 0);
        assertTrue(fireIncident.compareTo(accidentIncident) < 0);

        // Test timestamp comparison with same priority
        Incident theft1 = new Incident(tile1, IncidentType.THEFT);
        Incident theft2 = new Incident(tile2, IncidentType.THEFT);
        theft1.setTimestamp(1000L);
        theft2.setTimestamp(2000L);

        assertTrue(theft1.compareTo(theft2) < 0); // Earlier timestamp first
    }

    @Test
    void testContains() {
        // Test coordinates within tile bounds
        double insideX = 5 * Tile.TILE_SIZE + 20;
        double insideY = 10 * Tile.TILE_SIZE + 30;
        assertTrue(incident.contains(insideX, insideY));

        // Test coordinates outside tile bounds
        double outsideX = 3 * Tile.TILE_SIZE + 20;
        double outsideY = 8 * Tile.TILE_SIZE + 30;
        assertFalse(incident.contains(outsideX, outsideY));

        // Test edge cases
        double edgeX = 5 * Tile.TILE_SIZE;
        double edgeY = 10 * Tile.TILE_SIZE;
        assertTrue(incident.contains(edgeX, edgeY));
    }

    @Test
    void testGettersAndSetters() {
        // Test source
        Tile newTile = new Tile(3, 7, TileType.COMMERCIAL);
        incident.setSource(newTile);
        assertEquals(newTile, incident.getSource());

        // Test type
        incident.setType(IncidentType.FIRE);
        assertEquals(IncidentType.FIRE, incident.getType());

        // Test timestamp
        long newTimestamp = 12345L;
        incident.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, incident.getTimestamp());
    }

    @Test
    void testIncidentTypeProperties() {
        assertEquals(1, IncidentType.THEFT.getPriority());
        assertEquals(2, IncidentType.FIRE.getPriority());
        assertEquals(3, IncidentType.ACCIDENT.getPriority());

        assertEquals(Color.DARKSLATEBLUE, IncidentType.THEFT.getColor());
        assertEquals(Color.ORANGERED, IncidentType.FIRE.getColor());
        assertEquals(Color.GOLD, IncidentType.ACCIDENT.getColor());
    }
}