package model;

import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import application.ChatViewController;
import encryption.AESEncryption;
import encryption.RSAEncryption;

public class Client {
	private Socket clientSocket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean signupState;	
	private boolean loginState;
	private ArrayList<String> clientList = new ArrayList<>();
	private String message;
	private String username;
	private RSAEncryption rsa;
	private AESEncryption aes;
	
	public Client(Socket clientSocket) {
		try {
			this.clientSocket = clientSocket;
			rsa = new RSAEncryption();
			aes = new AESEncryption();
			aes.init();
			out = new ObjectOutputStream(clientSocket.getOutputStream());
			in = new ObjectInputStream(clientSocket.getInputStream());
			listenMessage();			
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}
	
	// Generate a random initialization vector for AES encryption
	public void ASEinitIV() {
		aes.initIV();
	}
	
	public String RSAencrypt(String message) throws Exception {
		return rsa.encrypt(message);
	}
	
	public String AESencrypt(String message) throws Exception {
		return aes.encrypt(message);
	}
	
	public String AESdecrypt(String message) throws Exception {
		return aes.decrypt(message);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void sendPacketToServer(Packet packet) {
		try {
			//Use public key that matches server's private key to encrypt keys
			//for AES encryption and decryption
			packet.setKey(rsa.encrypt(aes.getKey()));
	        packet.setIV(rsa.encrypt(aes.getIV()));
			out.writeObject(packet);
		} catch (Exception e) {
			e.printStackTrace();
			close();
		}
	}
	
	public void listenMessage() {
		new Thread(new Runnable() {
			@Override
			public void run() {			
				while(clientSocket.isConnected()) {
					try {
						// Call different methods depends on packet header
						Packet packet = (Packet) in.readObject();
						String header = packet.getHeader();
						if(header.equals("login")) {
							loginResponse(packet);
						}else if(header.equals("signup")) {
							signupResponse(packet);
						}else if (header.equals("client list")) {
							clientListResponse(packet);
						}else {
							messageResponse(packet);
						}
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						close();
					} catch (IOException e) {
						e.printStackTrace();
						close();
					}
				}
			}
		}).start();
	}
	
	public ArrayList<String> getClients(){
		return clientList;
	}
	
	public void loginResponse(Packet packet) {
		if(packet.getMessage().equals("success")) {
			loginState = true;
			// Set username to client
			username = packet.getRecipient();
		}else {
			loginState = false;
		}
	}
	
	public void signupResponse(Packet packet) {
		if(packet.getMessage().equals("success")) {
			signupState = true;
		}else {
			signupState = false;
		}
	}
	
	public void clientListResponse(Packet packet) {
		clientList = packet.getClientList();
	}
	
	public void messageResponse(Packet packet) {
		message = packet.getMessage();
		System.out.println("sender: " + packet.getSender());
		System.out.println("recipent: " + packet.getRecipient());
		System.out.println("message: " + packet.getMessage());
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isLoginSucceed() {
		return loginState;
	}
	
	public boolean isSignupSucceed() {
		return signupState;
	}
	
	public void close() {
		try {
			in.close();
			out.close();
			if(!clientSocket.isClosed()) {
				clientSocket.close();
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}