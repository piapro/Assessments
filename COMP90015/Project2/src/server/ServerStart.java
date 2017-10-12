package server;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/*这个类是服务器端的等待客户端连接*/

public class ServerStart extends Thread {

    ServerSocket serverSocket;
    BufferedReader reader;
    PrintWriter writer;
    static List<Socket> clientsList;// 保存连接到服务器的客户端
    private final int PORT = 1234;
    private static DataInputStream input;
    private static DataOutputStream output;

    public static void main(String[] args) {
        ServerStart server = new ServerStart();
    }

    public ServerStart() {
        this.start();// Thread.start
    }

    public void run() {
        try {

            serverSocket = new ServerSocket(PORT);
            clientsList = new ArrayList<Socket>();
            System.out.println("Starting Connection on Port" + PORT);

            while (true) {
                System.out.println("Waiting for client");
                Socket client = serverSocket.accept();
                clientsList.add(client);
                System.out.println("Success" + client.toString());
                new ClientListener(client);

            }
        } catch (IOException e) {
            System.out.println("Can not establish connection");
            e.printStackTrace();
        }

    }

    public synchronized void sendMsg(String msg) {
        try {
            for (int i = 0; i < clientsList.size(); i++) {
                Socket client = clientsList.get(i);
                output = new DataOutputStream(client.getOutputStream());

                output.writeUTF(msg);
                output.flush();
            }

        } catch (Exception e) {
            println(e.toString());
        }
    }

    public void println(String s) {
        if (s != null) {
            // this.ui.taShow.setText(this.ui.taShow.getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }

    public void closeServer() {
        try {
            if (serverSocket != null)
                serverSocket.close();
            if (reader != null)
                reader.close();
            if (writer != null)
                writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}

class ClientListener extends Thread {
    BufferedReader reader;
    PrintWriter writer;
    Socket client;
    private static DataInputStream input;
    private static DataOutputStream output;

    public ClientListener(Socket client) {
        this.client = client;
        this.start();
    }

    // 为每一个客户端创建线程等待接收信息，然后把信息广播出去
    public void run() {
        String msg = "";
        while (true) {
            try {

                input = new DataInputStream(client.getInputStream());
                output = new DataOutputStream(client.getOutputStream());
                msg = input.readUTF();
                sendMsg(msg);

            } catch (IOException e) {
                println(e.toString());
                // e.printStackTrace();
                break;
            }
            if (msg != null && msg.trim() != "") {
                println(">>" + msg);
            }
        }
    }

    // 把信息广播到所有用户
    public synchronized void sendMsg(String msg) {
        try {
            for (int i = 0; i < Server.clientsList.size(); i++) {
                Socket client = Server.clientsList.get(i);
                output = new DataOutputStream(client.getOutputStream());

                output.writeUTF(msg);
                output.flush();
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void println(String s) {
        if (s != null) {
            // this.ui.taShow.setText(this.ui.taShow.getText() + s + "\n");
            System.out.println(s + "\n");
        }
    }
}