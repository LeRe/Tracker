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

public class Client implements Runnable {

    public static final String HOST_NAME = "127.0.0.1";
    public static final int PORT = 2222;

    public Client() {

    }

    @Override
    public void run() {
        try {
            Log.i("RELE", "Client: task run...");
            Socket socket = new Socket(HOST_NAME, PORT);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String fromServer;

//            BufferedReader stdIn =
//                    new BufferedReader(new InputStreamReader(System.in));
//            String fromUser;

            //Write to server
            out.println("HELLO!!!  111\r\n");
            out.println("\r\n");

            while ((fromServer = in.readLine()) != null) {
                Log.i("RELE", "Client: Server response - " + fromServer);

                //nikogda ne proizoidet
                if (fromServer.equals("Bye."))
                    break;

//                fromUser = stdIn.readLine();
//                if (fromUser != null) {
//                    System.out.println("Client: " + fromUser);
//                    out.println(fromUser);
//                }

                //Write to server
                out.println("HELLO!!!  222\r\n");
                out.println("\r\n");
            }

            //Write to server
            out.println("HELLO!!!  333\r\n");
            out.println("\r\n");

        } catch (UnknownHostException e) {
            Log.i("RELE", "Client: Don't know about host " + HOST_NAME);
        } catch (IOException e) {
            Log.i("RELE", "Client: Couldn't get I/O for the connection to " +
                    HOST_NAME);
        }

        Log.i("RELE", "Client: My task end!!!");
    }
}
