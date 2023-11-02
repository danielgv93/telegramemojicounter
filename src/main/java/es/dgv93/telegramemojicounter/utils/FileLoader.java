package es.dgv93.telegramemojicounter.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.InputStream;

public class FileLoader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    public static <T> T load(String file, CheckedFunction<InputStream, T> mapper) {
        try (var is = FileLoader.class.getResourceAsStream(file)) {
            if (is == null) {
                return null;
            }
            return mapper.apply(is);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T loadJson(String path, Class<T> clazz) {
        return FileLoader.load(path, (is) -> OBJECT_MAPPER.readValue(is, clazz));
    }
    @FunctionalInterface
    public interface CheckedFunction<T, R> {
        R apply(T t) throws Exception;
    }
}
