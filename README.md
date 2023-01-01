# Client

This is a simple chat client I built to study basic networking, socket programming, concurrency, cryptography(AES, RSA and Hashing).

### How it works

- Create a new socket and client object
- When the client connects to the server, create a new thread to listen for packet object sent from the server
- When user login or sign up, MD5 class can encrypt the password using **java.math and java.security**
- Use **JavaFX** to build the view and controller for signup, login, chat room and chat page