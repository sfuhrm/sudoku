package de.sfuhrm.sudoku;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GsonRiddleSerializerTest {

    @ParameterizedTest
    @MethodSource("de.sfuhrm.sudoku.GameSchemas#getSupportedGameSchemas")
    public void testSerializeDeserialize(GameSchema gameSchema) {
        Random r = new Random();
        RiddleImpl riddle = new RiddleImpl(gameSchema);
        riddle.set(0, 0, (byte) 1);
        riddle.setWritable(0, 0, false);
        riddle.set(1, 1, (byte) 2);
        riddle.setWritable(1, 1, true);

        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Riddle.class, new GsonRiddleSerializer())
                .create();

        String json = gson.toJson(riddle);
        assertNotNull(json);

        Riddle deserialized = gson.fromJson(json, Riddle.class);
        assertNotNull(deserialized);
        assertEquals(riddle.getSchema(), deserialized.getSchema());
        assertEquals(riddle.get(0, 0), deserialized.get(0, 0));
        assertEquals(riddle.getWritable(0, 0), deserialized.getWritable(0, 0));
        assertEquals(riddle.get(1, 1), deserialized.get(1, 1));
        assertEquals(riddle.getWritable(1, 1), deserialized.getWritable(1, 1));
        
        // Check dimensions
        assertEquals(riddle.getSchema().getWidth(), deserialized.getSchema().getWidth());
    }
}
