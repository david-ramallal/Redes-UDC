package es.udc.redes.webserver;

import java.net.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ServerThread extends Thread {

    private final Socket socket;
    private Date date;
    private final String server = "WebServer_593";
    private long contentLength;
    private String contentType;
    private long lastModified;

    public ServerThread(Socket s) {
        // Store the socket s
        this.socket = s;
    }

    public void run() {
        try {
            // This code processes HTTP requests and generates
            // HTTP responses
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWr = new PrintWriter(socket.getOutputStream(), true);
            BufferedOutputStream output = new BufferedOutputStream(socket.getOutputStream());

            answer(input, printWr, output);

            output.close();
            input.close();
            // Uncomment next catch clause after implementing the logic
        } catch (SocketTimeoutException e) {
            System.err.println("Nothing received in 300 secs");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close the client socket
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void answer(BufferedReader input, PrintWriter printWr, BufferedOutputStream output) throws ParseException {
        String request, fileName, ifModString;
        File resource;
        Method requestType;
        Codes code;
        date = new Date();

        try {
            request = input.readLine();                // request, ex: GET /index.html HTTP/1.0
            do{
                ifModString = input.readLine();        // If-Modified-Since
            }while(ifModString != null && !"".equals(ifModString) && !ifModString.startsWith("If-Modified-Since: "));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        if(request == null || request.isEmpty())
            return;
        fileName = obtainNameFile(request);
        requestType = obtainRequestType(request);
        code = Codes.Ok;

        String[] requestWords = request.split("\\s+");  // to check how many words has the request line

        if(requestType == null || requestWords.length != 3){
            requestType = new GET();
            code = Codes.BadRequest;
            fileName = "/error400.html";
        }

        String path = "p1-files";
        resource = new File(path + fileName);

        if(!resource.exists()){
            code = Codes.NotFound;
            fileName = "/error404.html";
            resource = new File(path + fileName);
        }

        contentType = obtainContentType(resource);
        contentLength = resource.length();
        lastModified = resource.lastModified();


        if(ifModString != null && ifModString.startsWith("If-Modified-Since:")){
            if(obtainIfModified(ifModString).getTime() < lastModified){
                code = Codes.NotModified;
            }
        }

        if(code == Codes.NotModified)
            requestType = new HEAD();

        requestType.methodFunction(code, printWr, output,resource,this);

    }

    public static String makeHeader(Codes code, ServerThread serverThread){
        String printStr = "HTTP/1.0 ";
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        switch (code){
            case Ok : printStr = printStr + "200 OK\n";
            break;
            case NotModified : printStr = printStr + "304 Not Modified\n";
            break;
            case BadRequest : printStr = printStr + "400 Bad Request\n";
            break;
            case NotFound : printStr = printStr + "404 Not Found\n";
            break;
        }
        printStr = printStr + "Date: " + formatter.format(serverThread.date) + "\n";
        printStr = printStr + "Server: " + serverThread.server + "\n";
        printStr = printStr + "Content-Length: " + serverThread.contentLength + "\n";
        printStr = printStr + "Content-Type: " + serverThread.contentType + "\n";
        printStr = printStr + "Last-Modified: " + formatter.format(serverThread.lastModified) + "\n";
        return printStr;
    }

    public String obtainContentType(File file){
        String filename = file.getName().toLowerCase();

        if(filename.endsWith(".html"))
            return "text/html";
        else if(filename.endsWith(".txt"))
            return "text/plain";
        else if(filename.endsWith(".gif"))
            return "image/gif";
        else if(filename.endsWith(".png"))
            return "image/png";
        else
            return "application/octet-stream";
    }

    public String obtainNameFile(String request){
        String fileName;
        StringTokenizer tokenizer = new StringTokenizer(request);
        tokenizer.nextToken();
        fileName = tokenizer.nextToken();
        if(fileName.equals("/"))
            fileName = "/index.html";
        return fileName;
    }

    public Method obtainRequestType(String request){
        String reqType;
        Method type;
        StringTokenizer tokenizer = new StringTokenizer(request);
        reqType = tokenizer.nextToken().toUpperCase();

        if(reqType.equals("GET"))
            type = new GET();
        else if(reqType.equals("HEAD"))
            type = new HEAD();
        else type = null;

        return type;
    }

    public Date obtainIfModified(String line) throws ParseException {
        String strDate;
        Date rtnDate;
        strDate = line.substring("If-Modified-Since:".length());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        rtnDate = formatter.parse(strDate);

        return rtnDate;
    }
}
