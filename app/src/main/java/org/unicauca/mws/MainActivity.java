package org.unicauca.mws;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import org.unicauca.server.MyServer;

import java.io.IOException;

public class MainActivity extends Activity {
    private String TAG = "MainActivity";
    private MyServer server;
    private int port = 8080;
    private RelativeLayout optionsRelative;
    private RelativeLayout footerRelative;
    private int GET_INDEX = 1000;
    TextView info;
    private String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideLayouts();
    }

    //Oculta Elementos de la pantalla
    private void hideLayouts() {
        optionsRelative = (RelativeLayout) findViewById(R.id.optionsRelativeL);
        optionsRelative.setVisibility(View.INVISIBLE);
        footerRelative = (RelativeLayout) findViewById(R.id.footer);
        footerRelative.setVisibility(View.INVISIBLE);
    }

    // Este método se ejecuta cuando se presiona el boton + y permite
    // agregar la referencia al indice de la página web cliente
    public void selectIndex(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        startActivityForResult(intent, GET_INDEX);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GET_INDEX == 1000 && resultCode == RESULT_OK) {
            // Mostramos por pantalla la ruta del archivo seleccionado.
            route = data.getData().getPath();
            info = (TextView) findViewById(R.id.DirInfo);
            info.setText("El archivo Seleccionado es: \n " + route);
            route = route.split("index.html")[0];
            server.setRoute(route);

        }
    }

    //obtiene la dirección ip del servidor web en pantalla
    @SuppressLint("DefaultLocale")
    public String getLocalIpAddress() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        int ip = wm.getConnectionInfo().getIpAddress();
        String ipStr = String.format("%d.%d.%d.%d", (ip & 0xff),
                (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));

        return ipStr;
    }

    // Activa/Desactiva el servidor web
    public void startWebServer(View v) {
        // Is the toggle on?
        boolean on = ((ToggleButton) v).isChecked();
        TextView tv = (TextView) findViewById(R.id.ip);
        TextView tvUrl = (TextView) findViewById(R.id.urlInfo);
        if (on) {
            server = new MyServer(port, MainActivity.this);
            try {
                tv.setText(getString(R.string.startServer));
                tvUrl.setText("http://" + getLocalIpAddress() + ":" + port
                        + "/index.html");
                optionsRelative.setVisibility(View.VISIBLE);
                footerRelative.setVisibility(View.VISIBLE);
                server.start();
            } catch (IOException ioe) {
                Log.i("Httpd", "The server could not start." +ioe);
            }
            Log.i("Httpd", "Web server initialized.");
        } else {
            optionsRelative.setVisibility(View.INVISIBLE);
            footerRelative.setVisibility(View.INVISIBLE);
            if (server != null) {
                info.setText("Url");
                server.stop();
                tv.setText(getString(R.string.stopServer));
            }
        }

    }
}
