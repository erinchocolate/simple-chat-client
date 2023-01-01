package application;
	
import java.net.Socket;

import encryption.MD5Encryption;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.Client;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));     
			Parent root = (Parent)fxmlLoader.load();
			LoginViewController controller = fxmlLoader.<LoginViewController>getController();
			Client client = new Client(new Socket("localhost", 8888));
			MD5Encryption encryption = new MD5Encryption();
			controller.setEncryption(encryption);
			controller.setClient(client);
			Scene scene = new Scene(root);	
			primaryStage.setTitle("Chat");
			primaryStage.setScene(scene);
			primaryStage.show();		
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
