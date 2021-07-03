/*
package fr.lernejo.navy_battle;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.OutputStream;
public class CustomHandler implements HttpHandler {
    private final HttpExchange exchange;
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String body = "Hello";
        exchange.sendResponseHeaders(200, body.length());
        try (OutputStream os = exchange.getResponseBody()) { // (1)
            os.write(body.getBytes());
        }
    }
}
*/

