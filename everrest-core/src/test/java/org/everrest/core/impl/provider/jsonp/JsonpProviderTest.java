package org.everrest.core.impl.provider.jsonp;

import org.everrest.core.impl.MultivaluedMapImpl;
import org.everrest.core.impl.provider.json.tst.Book;
import org.junit.Before;
import org.junit.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.core.MultivaluedHashMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.everrest.core.impl.provider.json.tst.Book.createJunitBook;
import static org.junit.Assert.assertEquals;

public class JsonpProviderTest {
    private JsonpProvider jsonpProvider;

    @Before
    public void setUp() throws Exception {
        jsonpProvider = new JsonpProvider();
    }

    @Test
    public void readsContentOfEntityStreamAsJsonValue() throws Exception {
        String content = "{\"title\": \"JUnit in Action\", \"pages\": 386, \"isdn\": 93011099534534, \"price\": 19.37, \"author\": \"Vincent Massol\"}";
        JsonValue result = jsonpProvider.readFrom(JsonValue.class, null, null, APPLICATION_JSON_TYPE, new MultivaluedMapImpl(),
                new ByteArrayInputStream(content.getBytes()));
        assertEquals("Vincent Massol", result.asJsonObject().getString("author"));
        assertEquals("JUnit in Action", result.asJsonObject().getString("title"));
        assertEquals(386, result.asJsonObject().getJsonNumber("pages").intValue());
        assertEquals(19.37, result.asJsonObject().getJsonNumber("price").doubleValue(), 0.0);
        assertEquals(93011099534534L, result.asJsonObject().getJsonNumber("isdn").longValue());
    }

    @Test
    public void writesJsonValueToOutputStream() throws Exception {
        JsonObject jsonBook = createJsonBook(createJunitBook());
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        jsonpProvider.writeTo(jsonBook, Book.class, null, null, APPLICATION_JSON_TYPE, new MultivaluedHashMap<>(), out);

        assertEquals("{\"author\":\"Vincent Massol\",\"title\":\"JUnit in Action\",\"pages\":386,\"price\":19.37,\"isdn\":93011099534534}", out.toString());
    }

    private JsonObject createJsonBook(Book book) {
        return Json.createObjectBuilder()
        .add("author", book.getAuthor())
        .add("title", book.getTitle())
        .add("pages", book.getPages())
        .add("price", book.getPrice())
        .add("isdn", book.getIsdn())
        .build();
    }
}
