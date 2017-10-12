import client.MessagerFrame;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;

public class ClientThread extends Thread {
    private static int PORT;//default:3939
    private static String IP; //default:"localhost"
    private DataOutputStream output;
    private DataInputStream input;
    MessagerFrame mf;
    BufferedReader reader;
    PrintWriter writer;
    String timeStamp;

    public void ClientThread(MessagerFrame mf) {
        this.mf = mf;
        timeStamp = new SimpleDateFormat("MMMM d, yyyy HH:MM:SS").format(new java.util.Date());

        try (Socket socket = new Socket(IP, PORT)) {
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printer("Connedted with Server:+"+IP+" Port: "+PORT);
            //Data Input Stream
            input = new DataInputStream(socket.getInputStream());
            //Data Output Stream
            output = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            printer("Connection Error on Port " + PORT);
            printer(e.toString());
            e.printStackTrace();
        }
        this.start();
    }

    public void run() {
        String message = "";
        while (true) {
            try {
                message = reader.readLine();
            } catch (IOException e) {
                printer("Server Disconnected...");
                break;
            }
            if (message != null && message.trim() != "") {
                printer("User# >>> " + message);
            }
        }
    }
    public void sendMessage(String message) {
        try {
            writer.println(message);
        } catch (Exception e) {
            printer(e.toString());
        }
    }

    public void printer(String text) {
        if (text != null) {
            //this.mf.messageDisplay.setText(this.mf.messageDisplay.getText() + text + "\n"+ timeStamp);
            System.out.println(text + "\n");
        }
    }



}
