package de.sfuhrm.sudoku;

/**
 * Serialization of a {@linkplain Riddle} to and from a String.
 */
public final class JsonSerializer {

    /** Gson instance with the {@linkplain GsonRiddleSerializer}
     * registered.
     * */
    private final com.google.gson.Gson gson;

    /**
     * Creates a new instance with a default Gson configuration.
     */
    public JsonSerializer() {
        this.gson = new com.google.gson.GsonBuilder()
                .registerTypeHierarchyAdapter(
                        Riddle.class,
                        new GsonRiddleSerializer())
                .create();
    }

    /**
     * Serializes a {@link Riddle} to a JSON string.
     * @param riddle the riddle to serialize.
     * @return the JSON representation of the riddle.
     */
    public String serialize(final Riddle riddle) {
        return gson.toJson(riddle);
    }

    /**
     * Deserializes a {@link Riddle} from a JSON string.
     * @param json the JSON string to deserialize.
     * @return the deserialized riddle instance.
     */
    public Riddle deserialize(final String json) {
        return gson.fromJson(json, Riddle.class);
    }

}
