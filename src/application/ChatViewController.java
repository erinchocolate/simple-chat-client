package application;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import encryption.MD5Encryption;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.Client;
import model.Packet;

public class ChatViewController implements Initializable{
	@FXML private Button sendButton;
	@FXML private TextField messageInput;
	@FXML private VBox messageBox;
	@FXML private ScrollPane scrollPane;
	private Client client;
	private String recipient;
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {			
		messageBox.heightProperty().addListener(new ChangeListener<Number>() {		
			public void changed(ObservableValue<?extends Number> obserable, Number oldValue, Number newValue) {
				scrollPane.setVvalue((Double) newValue);
			}	
		});
	}
	
	public void sendButtonPressed(ActionEvent event) throws Exception{
		String messageToSend = messageInput.getText();
		if(!messageToSend.isEmpty()) {
			Packet packet = new Packet("message");
			client.ASEinitIV();
			// use AES to encrypt message
			packet.setRecipient(client.AESencrypt(recipient));
			packet.setMessage(client.AESencrypt(messageToSend));
			packet.setSender(client.AESencrypt(client.getUsername()));
			client.sendPacketToServer(packet);
			
			HBox hBox = new HBox();
			hBox.setAlignment(Pos.CENTER_RIGHT);
			hBox.setPadding(new Insets(5, 5, 5, 10));
			Text text = new Text(messageToSend);
			TextFlow textFlow = new TextFlow(text);
			textFlow.setPadding(new Insets(5, 5, 5, 10));
			hBox.getChildren().add(textFlow);
			messageBox.getChildren().add(hBox);			
			messageInput.clear();
		}
	}
	
	public void getMessage() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				System.out.println(client.getMessage());
			}	
		});
	}
	
}
