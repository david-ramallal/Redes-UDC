package es.udc.redes.webserver;

import java.io.*;

public abstract class Method {
    public abstract void methodFunction(Codes code, PrintWriter printWr, BufferedOutputStream output, File resource, ServerThread serverThread);
}
