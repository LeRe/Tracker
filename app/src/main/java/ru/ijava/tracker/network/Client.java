package ru.ijava.tracker.network;

import android.location.Location;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import ru.ijava.tracker.model.Device;

/**
 * Created by rele on 7/25/17.
 */

public class Client implements Runnable, NetworkDevice {
    private MessageHandler messageHandler;

    private final String HOST_NAME;
    private final int PORT;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private boolean closeConnection = false;

    ArrayList<Message> messagesQueue;

    public Client(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
        messagesQueue = new ArrayList<Message>();

        this.HOST_NAME = Server.LOCAL_HOST_NAME;
        this.PORT = Server.PORT;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(HOST_NAME, PORT);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //Write to server   send object, or send objects from queue
//            Message message = new Message();
//            message.action = Message.ACTION_LOG_I;
//            message.content = "Hello, i client, this request for server!!!!! ";
//            objectOutputStream.writeObject(message);

            while (!closeConnection) {

                //Здесь можно проверить не пустали очереь сообщений от клиента и отправить их серверу
                //Хотя лучше обрабатывать эту очередь в отдельном потоке...!!!...???
                if(messagesQueue.size() > 0)
                {
                    Message message;
                    message = messagesQueue.get(0);
                    objectOutputStream.writeObject(message);
                    messagesQueue.remove(0);
                }

                //wait server response
                readServerRequest();

                //Отвалимся от сервера и сами выйдем
//                message = new Message();
//                message.action = Message.ACTION_CLOSE_CONECTION;
//                objectOutputStream.writeObject(message);
//
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
            messageHandler.process(message);
        }
    }

    @Override
    public void closeConection() {
        this.closeConnection = true;
    }

    public void saveLocation(Device device, Location location) {
        if(device != null && location != null){
            Message message = new Message(Message.Action.SAVE_LOCATION, device, location);
            messagesQueue.add(message);
        }
    }
}