package communication;

import controllers.MainWindowController;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The communicator handles the network traffic between all chat clients.
 * Messages are sent and received via the WebSocket.
 * 
 * @author marre
 */
public class WebSocketCommunicator extends Communicator implements Subject {

    private final String WEB_SOCKET_ADDRESS = "http://laboration5.herokuapp.com/";
    private Socket IOSocket;
    private MainWindowController _chat = null;
    private ArrayList<Observer> observers;
    communication.IO io = new communication.IO();

    public WebSocketCommunicator(MainWindowController chat) {
        this._chat = chat;
        observers = new ArrayList<Observer>();
    }

    /**
     * Send the chat message to all clients.
     *
     * @param sender Name of the sender.
     * @param message Text message to send.
     * @throws java.lang.Exception
     */
    @Override
    public void sendChat(String sender, String message) throws Exception {
        
        try {
            this.IOSocket.emit("message", sender + ": " + message);
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
        try {
            this.listenForMessages();
        } catch (Exception e) {
            io.printError(e.getMessage());
            _chat.error(e);
        }
    }
    
    public void stopListen() throws Exception {
        IOSocket.disconnect();
        IOSocket.close();
    }

    /**
     * Listen for messages from other clients.
     */
    public void listenForMessages() throws Exception {
        try {
            IOSocket = IO.socket(WEB_SOCKET_ADDRESS);
            IOSocket.on("message", (final Object... args) -> {
                notifyObservers(args[0].toString());
            });
            IOSocket.connect();
        } catch (URISyntaxException e) {
            io.printError(e.getMessage());
            throw e;
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
