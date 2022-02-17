package controllers;

import communication.Communicator;
import communication.CommunicatorFactory;
import communication.IO;
import communication.Observer;
import communication.Subject;
import communication.*;
import communication.UDPChatCommunicator;
import communication.WebSocketCommunicator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 * FXML Controller class
 *
 * @author marre
 */
public class MainWindowController implements Initializable, Observer {

    @FXML
    private TextArea txtAreaChat;
    @FXML
    private TextField txtMessage;
    @FXML
    private TextField txtName;
    @FXML
    private ToggleGroup gg;

    private CommunicatorFactory comFactory = new CommunicatorFactory();
    private Communicator _communicator = comFactory.createCommunicator("UDP", this); //communicator set to UDP at startup
    IO io = new IO();
    
    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        _communicator.startListen();
        addRadioButtonListener();
        _communicator.registerObserver(this);
    }

    /**
     * Receive message from user.
     *
     * @param message The received message.
     */
    public void receiveMessage(String message) {
        txtAreaChat.setText(txtAreaChat.getText() + "\n" + message);
    }

    /**
     * Inform the user that an error has occurred and exit the application.
     *
     * @param e
     */
    public void error(Exception e) {
        showAlert("An error has occured and the application will close: \n" + e.getMessage(), "Error Error!");
        System.exit(1);
    }

    /**
     * Adds listener for radio buttons in the view. 
     */
    private void addRadioButtonListener() {
        gg.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) -> {
            RadioButton chk = (RadioButton) t1.getToggleGroup().getSelectedToggle(); // Cast object to radio button
            
            _communicator.removeObserver(this);
            _communicator = null;
            _communicator = comFactory.createCommunicator(chk.getText(), this);
            _communicator.startListen();
            _communicator.registerObserver(this);
            
        });
    }

    @FXML
    private void handleSendButton() {
        if (inputValid(txtName.getText(), txtMessage.getText())) {
            this.sendMessage(txtName.getText(), txtMessage.getText());
            txtMessage.setText("");
        }
    }

    private boolean inputValid(String name, String message) {
        if (name.length() == 0) {
            this.showAlert("Please write your name to use the chat", "Fail");
            return false;
        }
        if (message.length() == 0) {
            this.showAlert("Please write a real message.", "Fail");
            return false;
        }
        return true;
    }

    /**
     * Send current message to all users.
     */
    private void sendMessage(String name, String message) {
        try {
            _communicator.sendChat(name, message);
            //io.printMessage(name, message);
            
            //encrypts name 
            TextDecorator encryptedName = new NormalText(name);
            encryptedName = new Encrypt(encryptedName);
            //encrypts message
            TextDecorator encryptedMsg = new NormalText(message);
            encryptedMsg = new Encrypt(encryptedMsg);
            
            io.printMessage(encryptedName.getMessage(), encryptedMsg.getMessage());
            
        } catch (Exception e) {
            this.error(e);
        }
    }

    private void showAlert(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);

        alert.showAndWait();
    }
    
}
