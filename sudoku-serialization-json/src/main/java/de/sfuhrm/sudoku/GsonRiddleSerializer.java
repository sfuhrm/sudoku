package de.sfuhrm.sudoku;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.List;

/**
 * GSON Serializer and Deserializer implementation for {@link Riddle}
 * for direct Gson serialization.
 * Can be registered to Gson using:
 * <code>
 *             Gson gson = new GsonBuilder()
 *                 .registerTypeHierarchyAdapter(Riddle.class, new GsonRiddleSerializer())
 *                 .create();
 * </code>
 */
public class GsonRiddleSerializer implements JsonSerializer<Riddle>, JsonDeserializer<Riddle> {

    private static final String GAMESCHEMA_WIDTH = "width";
    private static final String GAMESCHEMA_BLOCK_WIDTH = "blockWidth";
    private static final String ROOT_CELL_DATA = "data";
    private static final String ROOT_WRITEABLE = "writeable";

    @Override
    public JsonElement serialize(Riddle src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        GameSchema schema = src.getSchema();
        jsonObject.addProperty(GAMESCHEMA_WIDTH, schema.getWidth());
        jsonObject.addProperty(GAMESCHEMA_BLOCK_WIDTH, schema.getBlockWidth());

        JsonArray dataArray = new JsonArray();
        byte[][] array = src.getArray();
        for (byte[] row : array) {
            JsonArray rowArray = new JsonArray();
            for (byte cell : row) {
                rowArray.add(cell);
            }
            dataArray.add(rowArray);
        }
        jsonObject.add(ROOT_CELL_DATA, dataArray);

        JsonArray writableArray = new JsonArray();
        for (int i = 0; i < schema.getWidth(); i++) {
            JsonArray rowArray = new JsonArray();
            for (int j = 0; j < schema.getWidth(); j++) {
                rowArray.add(src.getWritable(i, j));
            }
            writableArray.add(rowArray);
        }
        jsonObject.add(ROOT_WRITEABLE, writableArray);

        return jsonObject;
    }

    @Override
    public Riddle deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        int width = jsonObject.get(GAMESCHEMA_WIDTH).getAsInt();
        int blockWidth = jsonObject.get(GAMESCHEMA_BLOCK_WIDTH).getAsInt();

        GameSchema schema = findSchema(width, blockWidth);
        if (schema == null) {
            throw new JsonParseException("Unsupported schema: " + width + "x" + width + " with block width " + blockWidth);
        }

        Riddle riddle = new RiddleImpl(schema);
        
        JsonArray dataArray = jsonObject.getAsJsonArray(ROOT_CELL_DATA);
        for (int i = 0; i < width; i++) {
            JsonArray rowArray = dataArray.get(i).getAsJsonArray();
            for (int j = 0; j < width; j++) {
                riddle.set(i, j, rowArray.get(j).getAsByte());
            }
        }

        JsonArray writableArray = jsonObject.getAsJsonArray(ROOT_WRITEABLE);
        for (int i = 0; i < width; i++) {
            JsonArray rowArray = writableArray.get(i).getAsJsonArray();
            for (int j = 0; j < width; j++) {
                riddle.setWritable(i, j, rowArray.get(j).getAsBoolean());
            }
        }

        return riddle;
    }

    private GameSchema findSchema(int width, int blockWidth) {
        List<GameSchema> schemas = GameSchemas.getSupportedGameSchemas();
        for (GameSchema schema : schemas) {
            if (schema.getWidth() == width && schema.getBlockWidth() == blockWidth) {
                return schema;
            }
        }
        return null;
    }
}
