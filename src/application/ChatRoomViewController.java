package application;

import java.io.IOException;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.Client;
import model.Packet;

public class ChatRoomViewController {
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private Button refreshButton;
	@FXML private Button chatButton;
	@FXML private Button logoutButton;
	@FXML private ListView<String> listView;
	private Client client;
	private ArrayList<String> clients;	
	
	public void setClient(Client client) {
		this.client = client;	
	}
	
	// Check what client is connected
	public void refreshButtonPressed(ActionEvent event) throws IOException{	
		Packet packet = new Packet("client list");
		client.sendPacketToServer(packet);
		clients = client.getClients();
		listView.getItems().clear();
		listView.getItems().addAll(clients);	
	}
	
	// Change to chat room view
	public void chatButtonPressed(ActionEvent event) throws IOException{
		String recipient;
		recipient = listView.getSelectionModel().getSelectedItem();
		if(recipient!=null) {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatView.fxml"));
			root = loader.load();
			ChatViewController controller = loader.getController();
			controller.setClient(client);
			controller.setRecipient(recipient);
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);	
			stage.setScene(scene);
			stage.show();
		}				
	}
	
}
