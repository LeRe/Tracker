package ru.ijava.tracker.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by levchenko on 20.07.2017.
 */

public class ServerService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new SocketServer()).start();

        return START_STICKY;
    }

    private static class SocketServer implements Runnable {
        @Override
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(2222);
                while (true) {
                    Socket s = ss.accept();
                    new Thread(new SocketProcessor(s)).start();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static class SocketProcessor implements Runnable {

        private Socket s;
        private InputStream is;
        private OutputStream os;

        private SocketProcessor(Socket s) {
            this.s = s;
            try {
                this.is = s.getInputStream();
                this.os = s.getOutputStream();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                readInputHeaders();
                writeResponse("<html><body><h1>Hello WORLD!!!</h1></body></html>");
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
            System.err.println("Client processing finished");
        }

        private void writeResponse(String s) throws Throwable {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Server: YarServer/2009-09-09\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + s.length() + "\r\n" +
                    "Connection: close\r\n\r\n";
            String result = response + s;
            os.write(result.getBytes());
            os.flush();
        }

        private void readInputHeaders() throws Throwable {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            while(true) {
                String s = br.readLine();
                if(s == null || s.trim().length() == 0) {
                    break;
                }
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
