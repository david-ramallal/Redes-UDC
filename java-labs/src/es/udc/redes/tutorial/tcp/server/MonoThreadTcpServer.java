package es.udc.redes.tutorial.tcp.server;

import java.net.*;
import java.io.*;

/**
 * MonoThread TCP echo server.
 */
public class MonoThreadTcpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.tcp.server.MonoThreadTcpServer <port>");
            System.exit(-1);
        }

        ServerSocket servSocket = null;
        int port = Integer.parseInt(argv[0]);

        try {
            // Create a server socket
            servSocket = new ServerSocket(port);
            // Set a timeout of 300 secs
            servSocket.setSoTimeout(300000);
            while (true) {
                // Wait for connections
                Socket newSocket = servSocket.accept();
                // Set the input channel
                BufferedReader buffRead = new BufferedReader(new InputStreamReader(
                        newSocket.getInputStream()));
                // Set the output channel
                PrintWriter printWr = new PrintWriter(newSocket.getOutputStream(), true);
                // Receive the client message
                String message = buffRead.readLine();
                System.out.println("SERVER: Received " + message
                        + " from " + newSocket.getInetAddress().toString()
                        + ":" + newSocket.getPort());
                // Send response to the client
                printWr.println(message);
                System.out.println("SERVER: Sending " + message +
                        " to " + newSocket.getInetAddress().toString() +
                        ":" + newSocket.getPort());
                // Close the streams
                buffRead.close();
                printWr.close();
                newSocket.close();
            }
        // Uncomment next catch clause after implementing the logic            
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
	        //Close the socket
            try {
                servSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
