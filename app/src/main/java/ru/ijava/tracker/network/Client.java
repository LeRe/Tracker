package ru.ijava.tracker.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by rele on 7/25/17.
 */

public class Client {

    public static final String HOST_NAME = "127.0.0.1";
    public static final int PORT = 2222;

    public Client() {
        try {

            Socket kkSocket = new Socket(HOST_NAME, PORT);
            PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(kkSocket.getInputStream()));


            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        } catch (UnknownHostException e) {
            Log.i("RELE", "Don't know about host " + HOST_NAME);
        } catch (IOException e) {
            Log.i("RELE", "Couldn't get I/O for the connection to " +
                    HOST_NAME);
        }
    }
}
