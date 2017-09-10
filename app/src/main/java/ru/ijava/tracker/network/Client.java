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
 *
 * TODO ^^^^^^^^^^^^^^^^^^^
 * TODO Network tasks...
 * TODO
 * TODO 1. Отправлять только что определенное местоположение для хранения на удаленный и локальный сервер
 * TODO
 * TODO 2. Регистрация клиентского устройства на удаленном сервере
 * TODO         В таблице device должна появляться запись об клиенте содержащая ID клиента и NickName
 * TODO     Регистрация вероятно может происходить при сохранении местоположения устройства
 * TODO
 * TODO 3. Нисходящая синхронизация баз
 * TODO     Клиент получает недостающии записи по устройствам
 * TODO     Клиент получает записи о местах положения этих устройств
 * TODO
 * TODO
 * TODO 4. Восходящая синхронизация баз
 * TODO     Клиент выгружает на сервер информацию по своим местоположениям и своему устройству
 * TODO     Проверяется вся ли информация есть на сервере, далее запрашиваем недостающии записи
 * TODO     Клиент отправляет их, сервер сохраняет
 * TODO
 * TODO
 * TODO Необходимо расщирить диагностику в программе
 * TODO Должны выводиться сведения
 * TODO     запушена ли система позиционирования
 * TODO     Запущен ли сервер
 * TODO     Сколько записей об устройсве
 * TODO         id, nickname, count_locations_records, datetime_last_location_records
 * TODO
 * TODO
 * TODO
 * TODO
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

            while (!closeConnection) {

                if(messagesQueue.size() > 0)
                {
                    Message message;
                    message = messagesQueue.get(0);
                    objectOutputStream.writeObject(message);
                    messagesQueue.remove(0);
                }

                //wait server response
                readServerRequest();

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
