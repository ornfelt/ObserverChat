package communication;

/**
 *
 * @author jonas
 */
public abstract class Communicator {
    

/**
 * The communicator handles the network traffic between all chat clients.
 * Messages are sent and received via the UDP protocol which may lead to
 * messages being lost.
 *
 * @author Jonas
 */

    /**
     * Send the chat message to all clients.
     *
     * @param sender  Name of the sender.
     * @param message Text message to send.
     * @throws IOException If there is an IO error.
     */
    public void sendChat(String sender, String message) throws Exception {

    }

    /**
     * Start to listen for messages from other clients.
     */
    public void startListen() {
    }

    /**
     * Listen for messages from other clients.
     *
     * @throws Exception If there is an IO error.
     */
    private void listenForMessages() throws Exception {

    }

    public void stopListen() throws Exception {
    }

    public void run() {
    }
    
    public void registerObserver (Observer o){
        
    }
    
    public void removeObserver(Observer o){
        
    }
    
}
