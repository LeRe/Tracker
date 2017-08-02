package ru.ijava.tracker.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by rele on 7/25/17.
 */

public class Server implements Runnable {
    private static MessageHandler messageHandler;

    public static final String LOCAL_HOST_NAME = "127.0.0.1";
    public static final int PORT = 2222;

    public Server(MessageHandler messageHandler)
    {
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(PORT);
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

    private static class SocketProcessor implements Runnable, NetworkDevice {
        private Socket s;
        private ObjectOutputStream objectOutputStream;
        private ObjectInputStream objectInputStream;

        private boolean closeConnection = false;

        private SocketProcessor(Socket s) {
            this.s = s;
            try {
                this.objectOutputStream = new ObjectOutputStream(s.getOutputStream());
                this.objectInputStream = new ObjectInputStream(s.getInputStream());
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (!closeConnection) {
                    readClientRequest();
                    writeResponse(generateResponse());
                }
            } catch (Throwable t) {
                /*do nothing*/
            } finally {
                try {
                    s.close();
                } catch (Throwable t) {
                    /*do nothing*/
                }
            }
        }

        private Message generateResponse() {
            Message message = null;//new Message();
            //message.action = Message.ACTION_LOG_I;
            //message.content = "Hello, it's responce from SERVER";
            return message;
        }

        private void writeResponse(Message message) throws Throwable {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        }

        private void readClientRequest() throws Throwable {
            Message message = (Message) objectInputStream.readObject();
            if(message != null) {
                messageHandler.process(message);
            }
        }

        @Override
        public void closeConection() {
            this.closeConnection = true;
        }
    }
}
