package es.udc.redes.tutorial.udp.server;

import java.net.*;

/**
 * Implements a UDP echo server.
 */
public class UdpServer {

    public static void main(String argv[]) {
        if (argv.length != 1) {
            System.err.println("Format: es.udc.redes.tutorial.udp.server.UdpServer <port_number>");
            System.exit(-1);
        }

        DatagramSocket socketDatagram = null;
        int sPort = Integer.parseInt(argv[0]);

        try {
            // Create a server socket
            socketDatagram = new DatagramSocket(sPort);
            // Set maximum timeout to 300 secs
            socketDatagram.setSoTimeout(300000);
            byte[] packet = new byte[1024];
            while (true) {
                // Prepare datagram for reception
                DatagramPacket receptionDatagram = new DatagramPacket(packet, packet.length);
                // Receive the message
                socketDatagram.receive(receptionDatagram);
                System.out.println("SERVER: Received "
                        + new String(receptionDatagram.getData(), 0, receptionDatagram.getLength())
                        + " from " + receptionDatagram.getAddress().toString() + ":"
                        + receptionDatagram.getPort());
                // Prepare datagram to send response
                DatagramPacket responseDatagram = new DatagramPacket(packet, packet.length, receptionDatagram.getAddress(), receptionDatagram.getPort());
                // Send response
                socketDatagram.send(responseDatagram);
                System.out.println("SERVER: Sending "
                        + new String(responseDatagram.getData()) + " to "
                        + responseDatagram.getAddress().toString() + ":"
                        + responseDatagram.getPort());
            }
          
        // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("No requests received in 300 secs ");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
        // Close the socket
            socketDatagram.close();
        }
    }
}
