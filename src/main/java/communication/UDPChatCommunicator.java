package communication;

import controllers.MainWindowController;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The communicator handles the network traffic between all chat clients.
 * Messages are sent and received via the UDP protocol which may lead to
 * messages being lost.
 *
 * @author Thomas Ejnefjäll
 */
public class UDPChatCommunicator extends Communicator implements Runnable, Subject {

    private final int DATAGRAM_LENGTH = 100;
    private final int PORT = 6789;
    private final String MULTICAST_ADDRESS = "228.28.28.28";
    private MainWindowController _chat = null;
    private MulticastSocket _socket = null;
    private ArrayList<Observer> observers;
    IO io = new IO();

    /**
     * Create a chat communicator that communicates over UDP.
     *
     * @param chat The UI that want to receive incoming messages.
     */
    public UDPChatCommunicator(MainWindowController chat) {
        _chat = chat;
        observers = new ArrayList<Observer>();
        /*
         * force java to use IPv4 so we do not get a problem when using IPv4 multicast
         * address
         */
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    /**
     * Send the chat message to all clients.
     *
     * @param sender  Name of the sender.
     * @param message Text message to send.
     * @throws IOException If there is an IO error.
     */
    @Override
    public void sendChat(String sender, String message) throws Exception {

        try (DatagramSocket socket = new DatagramSocket()) {
            String toSend = sender + ": " + message;
            byte[] b = toSend.getBytes();

            DatagramPacket datagram = new DatagramPacket(b, b.length, InetAddress.getByName(MULTICAST_ADDRESS), PORT);

            socket.send(datagram);
            socket.disconnect();
            socket.close();
        } catch (Exception e) {
            io.printError(e.getMessage());
            throw e;
        }
         
    }

    /**
     * Start to listen for messages from other clients.
     */
    @Override
    public void startListen() {
        new Thread(this).start();
    }

    /**
     * Listen for messages from other clients.
     *
     * @throws Exception If there is an IO error.
     */
    private void listenForMessages() throws Exception {
        byte[] b = new byte[DATAGRAM_LENGTH];
        DatagramPacket datagram = new DatagramPacket(b, b.length);
        _socket = new MulticastSocket(PORT);
        _socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
        while (true) {
            _socket.receive(datagram);
            String message = new String(datagram.getData());
            message = message.substring(0, datagram.getLength());
            //_chat.receiveMessage(message);
            notifyObservers(message);
            datagram.setLength(b.length);
}
    }

    public void stopListen() throws Exception {
        _socket.leaveGroup(InetAddress.getByName(MULTICAST_ADDRESS));
        _socket.disconnect();
        _socket.disconnect();
    }

    @Override
    public void run() {
        try {
            this.listenForMessages();
        } catch (Exception e) {
            io.printError(e.getMessage());
            _chat.error(e);
        }
    }
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >= 0) {
            observers.remove(i);
        }
    }
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.receiveMessage(message);
        }
    }
}
