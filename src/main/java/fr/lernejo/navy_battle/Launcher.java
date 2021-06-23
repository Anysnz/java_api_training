package fr.lernejo.navy_battle;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Launcher {
    public static void main(String[] args) {
	    int port = Integer.parseInt(args[0]);
            new StartServer().Start(port);
    }
}
