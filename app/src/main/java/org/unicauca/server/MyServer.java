package org.unicauca.server;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyServer extends NanoHTTPD {
    Context c;
    private boolean session;
    private String route;
    /**
     * Hashtable mapping (String)FILENAME_EXTENSION -> (String)MIME_TYPE
     */
    private static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {
        {
            put("css", "text/css");
            put("htm", "text/html");
            put("html", "text/html");
            put("xml", "text/xml");
            put("java", "text/x-java-source, text/java");
            put("md", "text/plain");
            put("txt", "text/plain");
            put("asc", "text/plain");
            put("gif", "image/gif");
            put("jpg", "image/jpeg");
            put("jpeg", "image/jpeg");
            put("png", "image/png");
            put("mp3", "audio/mpeg");
            put("m3u", "audio/mpeg-url");
            put("mp4", "video/mp4");
            put("ogv", "video/ogg");
            put("flv", "video/x-flv");
            put("mov", "video/quicktime");
            put("swf", "application/x-shockwave-flash");
            put("js", "application/javascript");
            put("pdf", "application/pdf");
            put("doc", "application/msword");
            put("ogg", "application/x-ogg");
            put("zip", "application/octet-stream");
            put("exe", "application/octet-stream");
            put("class", "application/octet-stream");
        }
    };

    public MyServer(int port, Context c) {
        super(port);
        this.c = c;
    }

    // Procesa las respuestas del servidor
    @Override
    public Response serve(String uri, Method method,
                          Map<String, String> header, Map<String, String> parameters,
                          Map<String, String> files) {
        Log.i("MyServer", "Url = "+uri);
        return responseByUrl(uri);
    }

    // Procesa las respuestas del servidor
    private Response responseByUrl(String url) {
        if (url.equalsIgnoreCase("/index.html")) {
            return responseforHTML("index.html");
        } else {
            String[] mimeType = url.split("\\.");
            return responseForMime(mimeType[1], url);
        }
    }

    // Respuesta para archivos HTML
    private Response responseforHTML(String url) {
        String answer = "";
        FileReader indexFile;
        try {

            indexFile = new FileReader(getRoute()+url);
            Log.i("MyServer", "responseforHTML "+getRoute()+url);
            BufferedReader reader = new BufferedReader(indexFile);
            String line = "";
            int count = 0;
            // create a new output stream
            while ((line = reader.readLine()) != null) {
                answer += line;
            }
            reader.close();
        } catch (IOException ioe) {
            Log.w("Httpd", ioe.toString());
        }

        return new Response(Response.Status.ACCEPTED, MIME_TYPES.get("html"),
                answer);
    }

    // Respuesta para otro tipo de archivos
    private Response responseForMime(String mime, String url) {
        File file = file = new File(getRoute()+url);
        InputStream fileStream;
        try {
            fileStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return new Response("Error 404");
        }
        return new Response(Response.Status.OK, MIME_TYPES.get(mime), fileStream);
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }
}
