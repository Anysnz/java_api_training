package fr.lernejo.navy_battle;


import com.sun.net.httpserver.HttpExchange;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public class ParamServer {
    private final String id;
    private final String url;
    private final String message;

    public ParamServer(Boolean startGame) {
        super();
        this.id = UUID.randomUUID().toString();;
        this.url = "";
        this.message = "P";
    }

    public ParamServer(String id, String url, String message) {
        this.id = id;
        this.url = url;
        this.message = message;
    }
    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("url", url);
        obj.put("message", message);
        return obj;
    }

    public static ParamServer fromJSON(JSONObject object) throws JSONException {
        return new ParamServer(
            object.getString("id"),
            object.getString("url"),
            object.getString("message")
        );
    }

    public JSONObject toJSONDTO() {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("url", url);
        obj.put("message", message);
        return obj;
    }

    public ParamServer withURL(String url) {
        return new ParamServer(
            this.id,
            url,
            this.message
        );
    }
}
