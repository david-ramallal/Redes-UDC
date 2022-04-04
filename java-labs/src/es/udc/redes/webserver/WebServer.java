package es.udc.redes.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class WebServer {
    
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Format: es.udc.redes.webserver.WebServer <port>");
            System.exit(-1);
        }

        ServerSocket serverSocket = null;
        int port = Integer.parseInt(args[0]);

        try{
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(300000);
            while(true){
                Socket clientSocket = serverSocket.accept();
                ServerThread sThread = new ServerThread(clientSocket);
                sThread.start();
            }
        }catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally{
            try {
                assert serverSocket != null;
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
