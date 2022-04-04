package es.udc.redes.webserver;

import java.io.*;

public class GET extends Method{
    @Override
    public void methodFunction(Codes code, PrintWriter printWr, BufferedOutputStream output, File resource, ServerThread serverThread) {
        FileInputStream inputStream;
        int c;

        printWr.println(ServerThread.makeHeader(code, serverThread));

        try{
            inputStream = new FileInputStream(resource);
            while ((c = inputStream.read()) != -1) {
                output.write(c);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
