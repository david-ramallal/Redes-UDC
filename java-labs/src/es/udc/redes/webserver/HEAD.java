package es.udc.redes.webserver;

import java.io.*;

public class HEAD extends Method{
    @Override
    public void methodFunction(Codes code, PrintWriter printWr, BufferedOutputStream output, File resource, ServerThread serverThread) {
        printWr.println(ServerThread.makeHeader(code, serverThread));
    }
}
