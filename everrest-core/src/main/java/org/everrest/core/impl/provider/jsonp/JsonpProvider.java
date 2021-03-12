package org.everrest.core.impl.provider.jsonp;

import org.everrest.core.provider.EntityProvider;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public class JsonpProvider implements EntityProvider<JsonValue> {

    private final JsonWriterFactory writerFactory = Json.createWriterFactory(null);
    private final JsonReaderFactory readerFactory = Json.createReaderFactory(null);

    @Override
    public long getSize(JsonValue jsonValue, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    private boolean isSupported(Class<?> aClass, MediaType mediaType) {
        return JsonValue.class.isAssignableFrom(aClass) && mediaType.equals(MediaType.APPLICATION_JSON_TYPE);
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return isSupported(aClass, mediaType);
    }

    @Override
    public boolean isWriteable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return isSupported(aClass, mediaType);
    }

    @Override
    public JsonValue readFrom(Class<JsonValue> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        try (JsonReader reader = readerFactory.createReader(inputStream)) {
            return reader.readValue();
        }
    }

    @Override
    public void writeTo(JsonValue jsonValue, Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream outputStream) throws IOException, WebApplicationException {
        try (JsonWriter writer = writerFactory.createWriter(outputStream)) {
            writer.write(jsonValue);
        }
    }
}
