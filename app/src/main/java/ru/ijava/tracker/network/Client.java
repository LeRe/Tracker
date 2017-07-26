package ru.ijava.tracker.network;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by rele on 7/25/17.
 */

public class Client implements Runnable, NetworkObject {

    public static final String HOST_NAME = "127.0.0.1";
    public static final int PORT = 2222;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private boolean closeConnection = false;

    public Client() {

    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(HOST_NAME, PORT);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //Write to server   send object, or send objects from queue
            Message message = new Message();
            message.action = Message.ACTION_LOG_I;
            message.content = "Hello, i client, this request for server!!!!! ";
            objectOutputStream.writeObject(message);

            while (!closeConnection) {
                //wait server response
                readServerRequest();

                //Здесь можно проверить не пустали очереь сообщений от клиента и отправить их серверу
                //Хотя лучше обрабатывать эту очередь в отдельном потоке...!!!...???

                //Отвалимся от сервера и сами выйдем
                message = new Message();
                message.action = Message.ACTION_CLOSE_CONECTION;
                objectOutputStream.writeObject(message);
                this.closeConection();
            }

        } catch (UnknownHostException e) {
            Log.i("RELE", "Client: UnknownHostException Don't know about host " + HOST_NAME);
        } catch (IOException e) {
            Log.i("RELE", "Client: IOException. Couldn't get I/O for the connection to " +
                    HOST_NAME);
        }

    }

    private void readServerRequest() {

        Message message = null;
        try {
            message = (Message) objectInputStream.readObject();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(message != null) {
            MessageHandler messageHandler = new MessageHandler(this);
            messageHandler.process(message);
        }
    }

    @Override
    public void closeConection() {
        this.closeConnection = true;
    }

}
