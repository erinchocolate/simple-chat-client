package application;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

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

public class SignupViewController {
	private Scene scene;
	private Stage stage;
	private Parent root; 
	@FXML private Button signupButton;
	@FXML private Button loginButton;
	@FXML private TextField usernameInput;
	@FXML private TextField passwordInput;
	@FXML private Label label;
	private MD5Encryption encryption;
	private String username;
	private String password;
	private String hashPassword;
	private Client client;
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setEncryption(MD5Encryption encryption) {
		this.encryption = encryption;
	}
	
	public void signupButtonPressed(ActionEvent event) throws Exception{
		username = usernameInput.getText();
		password = passwordInput.getText();	
		checkInput();
		hashPassword = encryption.encryptString(password);
		Packet packet = new Packet("signup");
		client.ASEinitIV();
		packet.setUsername(client.AESencrypt(username));
		packet.setPassword(client.AESencrypt(hashPassword));
		client.sendPacketToServer(packet);
		displaySignupInfo();
	}
	
	public void loginButtonPressed(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
		root = loader.load();
		LoginViewController controller = loader.getController();
		controller.setClient(client);		
		controller.setEncryption(encryption);
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);	
		stage.setScene(scene);
		stage.show();
	}
	
	public void displaySignupInfo() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if(client.isSignupSucceed()) {
					label.setText("Success");
				}else{
					label.setText("Username is taken");
				}
			}						
		});
	}
	
	public void checkInput() {
		if(username == null) {
			label.setText("Please enter your username!");
		}
		if(password == null) {
			label.setText("Please enter your password!");
		}
	}
	
}
