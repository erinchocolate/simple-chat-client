package application;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

import encryption.MD5Encryption;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;
import model.Packet;

public class LoginViewController {	
	private Scene scene;
	private Stage stage;
	private Parent root;
	@FXML private Button signupButton;
	@FXML private Button loginButton;
	@FXML private TextField usernameInput;
	@FXML private TextField passwordInput;
	@FXML private Label label;
	private MD5Encryption encryption;
	private Client client;
	private String username;
	private String password;
	private String hashPassword;
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setEncryption(MD5Encryption encryption) {
		this.encryption = encryption;
	}
	
	// Send login info to server
	public void loginButtonPressed(ActionEvent event) throws Exception{
		username = usernameInput.getText();
		password = passwordInput.getText();	
		hashPassword = encryption.encryptString(password);
		Packet packet = new Packet("login");
		client.ASEinitIV();
		packet.setUsername(client.AESencrypt(username));
		packet.setPassword(client.AESencrypt(hashPassword));
		client.sendPacketToServer(packet);
		if(client.isLoginSucceed()) {
			changeToChatRoomView(event);
		}
	}
	
	// Change to sign up view
	public void signupButtonPressed(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SignupView.fxml"));
		root = loader.load();
		SignupViewController controller = loader.getController();
		controller.setClient(client);		
		controller.setEncryption(encryption);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);	
		stage.setScene(scene);
		stage.show();
	}
	
	public void changeToChatRoomView(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatRoomView.fxml"));
		root = loader.load();
		ChatRoomViewController controller = loader.getController();
		controller.setClient(client);		
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);	
		stage.setScene(scene);
		stage.show();
	}

}
