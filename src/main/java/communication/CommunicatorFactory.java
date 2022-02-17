package communication;

import controllers.MainWindowController;

/**
 *
 * @author Jonas
 */
public class CommunicatorFactory { //simple Factory responsible for creating the correct communicator 

    public Communicator createCommunicator(String type, MainWindowController chat) {
        Communicator communicator = null;
        
        if (type.equals("WebSocket")) {
            communicator = new WebSocketCommunicator(chat);
        } else if (type.equals("UDP")) {
            communicator = new UDPChatCommunicator(chat);
        }

        return communicator;
    }
}
