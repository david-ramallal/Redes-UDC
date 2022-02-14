package es.udc.redes.tutorial.tcp.server;
import java.io.IOException;
import java.net.*;

/** Multithread TCP echo server. */

public class TcpServer {

  public static void main(String argv[]) {
    if (argv.length != 1) {
      System.err.println("Format: es.udc.redes.tutorial.tcp.server.TcpServer <port>");
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
        // Create a ServerThread object, with the new connection as parameter
        ServerThread sThread = new ServerThread(newSocket);
        // Initiate thread using the start() method
        sThread.start();
      }
    // Uncomment next catch clause after implementing the logic
    } catch (SocketTimeoutException e) {
      System.err.println("Nothing received in 300 secs");
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
     } finally{
      //Close the socket
      try {
        servSocket.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
