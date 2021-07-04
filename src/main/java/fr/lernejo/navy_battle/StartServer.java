package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import fr.lernejo.navy_battle.*;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.Executors;

public class StartServer {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Storage<BoardGame> localMap = new Storage<>();
    private final Storage<BoardGame> remoteMap = new Storage<>();
    private final Storage<ParamServer> localServer = new Storage<>();
    private final Storage<ParamServer> remoteServer = new Storage<>();

    public final void Start(int port, String url) throws IOException {
        localServer.set(new ParamServer(UUID.randomUUID().toString(),"http://localhost:" + port, "OK"));
        if (url != null)
            new Thread(() -> this.remoteStart(url)).start();
        HttpServer serverHttp = HttpServer.create(new InetSocketAddress(port), 0);
        serverHttp.setExecutor(Executors.newSingleThreadExecutor());
        serverHttp.createContext("/ping", this::handlePing);
        serverHttp.createContext("/api/game/start", e -> StartGame(new JsonHandler(e)));
        serverHttp.createContext("/api/game/fire", e -> JsonhandleFire(new JsonHandler(e)));
        serverHttp.start();
    }
    private void handlePing(HttpExchange exchange) throws IOException {
            String body = "OK";
            exchange.sendResponseHeaders(200, body.length());
            try (OutputStream os = exchange.getResponseBody()) { // (1)
                os.write(body.getBytes());
            }
    }
    public void remoteStart(String url) {
        try {
            var response = sendPOSTRequest(url + "/api/game/start", this.localServer.get().toJSON());
            this.remoteServer.set(ParamServer.fromJSON(response).withURL(url));

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to start game!");
        }
    }
    public void StartGame(JsonHandler jsonService) throws IOException {
        try {
            remoteServer.set(ParamServer.fromJSON(jsonService.getJSONObject()));
            jsonService.sendJSON(202, localServer.get().toJSON());
            System.out.println("Game Start");
        } catch (Exception e) { e.printStackTrace();jsonService.sendString(400, e.getMessage()); }
    }
    public void JsonhandleFire(JsonHandler jsonService) throws IOException {
        try { String cell = jsonService.getQueryParameter("cell");
            var pos = new Hook(cell);
            var res = localMap.get().hit(pos);var response = new JSONObject();
            response.put("consequence", res.toAPI());response.put("shipLeft", localMap.get().hasShipLeft());
            jsonService.sendJSON(200, response);
            Jsonfire();
        } catch (Exception e) { e.printStackTrace();jsonService.sendString(400, e.getMessage()); }
    }
    public void Jsonfire() throws IOException, InterruptedException { Hook coordinates = remoteMap.get().getNextPlaceToHit();
        var response = sendGETRequest(remoteServer.get().getUrl() + "/api/game/fire?cell=" + coordinates.toString());
        if (!response.getBoolean("shipLeft")) { System.out.println("I win");return; }
        var result = SetFire.fromAPI(response.getString("consequence"));
        if (result == SetFire.MISS)
            remoteMap.get().setCell(coordinates, GridCell.MISSED_FIRE);
        else
            remoteMap.get().setCell(coordinates, GridCell.SUCCESSFUL_FIRE);
    }

    public JSONObject sendPOSTRequest(String url, JSONObject obj) throws IOException, InterruptedException {
        HttpRequest requetePost = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .setHeader("Accept", "application/json")
            .setHeader("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(obj.toString()))
            .build();

        var response = client.send(requetePost, HttpResponse.BodyHandlers.ofString());return new JSONObject(response.body());
    }

    public JSONObject sendGETRequest(String url) throws IOException, InterruptedException {
        HttpRequest requeteGET = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .setHeader("Accept", "application/json")
            .GET()
            .build();

        var response = client.send(requeteGET, HttpResponse.BodyHandlers.ofString());
        return new JSONObject(response.body());
    }
}
