package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class StartServer {
    public final void Start(int port) {
        try {
            // Start server
            InetSocketAddress soc = new InetSocketAddress(port);
            HttpServer server = HttpServer.create(soc, 0);

            server.setExecutor(Executors.newSingleThreadExecutor());

            server.createContext("/ping", new CustomHandler());
            server.start();
        }
        catch (java.io.IOException e) {
            System.err.println("Unable to launch the server: java.io.IOException " + e.getMessage());
        }
        catch (IllegalArgumentException e) {
            System.err.println("Invalid argument : IllegalArgument " + e.getMessage());
        }
        catch (NullPointerException e) {
            System.err.println("Null parameter are given: NullPointerException " + e.getMessage());
        }

    }
}
