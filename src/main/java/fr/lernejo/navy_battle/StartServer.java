package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;
import java.util.UUID;
import java.util.concurrent.Executors;

public class StartServer {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Storage<ParamServer> localServer = new Storage<>();

    public final void Start(int port, String url) throws IOException {
        localServer.set(new ParamServer(
            UUID.randomUUID().toString(),
            "http://localhost:" + port,
            "OK"
        ));
        HttpServer serverHttp = HttpServer.create(new InetSocketAddress(port), 0);
        serverHttp.setExecutor(Executors.newSingleThreadExecutor());
        serverHttp.createContext("/ping", this::handlePing);
        serverHttp.start();
    }
        private void handlePing(HttpExchange exchange) throws IOException {
            String body = "OK";
            exchange.sendResponseHeaders(200, body.length());
            try (OutputStream os = exchange.getResponseBody()) { // (1)
                os.write(body.getBytes());
            }
        }

    }
