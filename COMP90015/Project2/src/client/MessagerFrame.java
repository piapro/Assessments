package client;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;

public class MessagerFrame extends JFrame {

    private String timeStamp;
    private static int PORT = 1234;
    private static String IP = "localhost";
    private DataOutputStream output;
    private DataInputStream input;
    private JTextArea display;
    private JTextArea inputArea;
    private JButton submitBtn;
    private JButton clearBtn;
    private JTextPane userDisplay;
    private JLabel statusBar;

    /**
     * Create the Instance Messager UI Framework.
     */

    public static void main(String[] args) {

        MessagerFrame mf = new MessagerFrame("Messager");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    mf.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mf.connect();
    }

    public void connect() {
        display.append("\nEstablishing Connection...");

        try (Socket socket = new Socket(IP, PORT)) {
            //Data Input Stream
            input = new DataInputStream(socket.getInputStream());
            //Data Output Stream
            output = new DataOutputStream(socket.getOutputStream());

            //Write to Server
            //Get messages from Server
            String message = input.readUTF();
            display.append("\n" + message);

            output.writeUTF("Connection Started");
            output.flush();
            while (true) {
                //Show Connection status
                System.out.println("Connecting...");
                TimeUnit.SECONDS.sleep(60);
            }
        } catch (Exception e) {
            display.setText("Connection Error");
        }

    }

    public MessagerFrame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 550, 580);
        getContentPane().setLayout(null);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                int n = JOptionPane.showConfirmDialog(null, "Do you want to Exit", "Exit",
                        JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    System.exit(0); //Shutdown Client
                } else if (n == JOptionPane.NO_OPTION) {

                }
            }
        });


        //Message Display Area:display
        JScrollPane displayScrollPane = new JScrollPane();
        displayScrollPane.setBounds(0, 0, 350, 350);
        getContentPane().add(displayScrollPane);
        display = new JTextArea();
        displayScrollPane.setViewportView(display);

        //Message input field: inputArea
        JScrollPane inputScrollPane = new JScrollPane();
        inputScrollPane.setBounds(0, 366, 350, 68);
        getContentPane().add(inputScrollPane);
        inputArea = new JTextArea();
        inputScrollPane.setViewportView(inputArea);

        //Status Bar:statusBar
        timeStamp = new SimpleDateFormat("MMMM d, yyyy").format(new java.util.Date());
        statusBar = new JLabel();
        statusBar.setBounds(0, 508, 532, 25);
        getContentPane().add(statusBar);
        statusBar.setText(timeStamp);


        //Submit button: submitBtn
        submitBtn = new JButton("Submit");
        submitBtn.setBounds(60, 465, 97, 25);
        getContentPane().add(submitBtn);

        //Clear display button: clearBtn
        clearBtn = new JButton("Clear");
        clearBtn.setBounds(200, 465, 97, 25);
        getContentPane().add(clearBtn);

        //User display and manage
        userDisplay = new JTextPane();
        userDisplay.setBounds(362, 0, 158, 434);
        getContentPane().add(userDisplay);

        //Button Action Listener
        submitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputArea.getText().isEmpty()) {
                    new JOptionPane().showMessageDialog(null,
                            "You cannot send an empty message ! ");
                } else {
                    String sendText = inputArea.getText();
                    try {
                        output.writeUTF(sendText);
                        output.flush();
                    } catch (IOException e1) {
                        display.append("\nCommunication Error\n");
                    }
                    //receive message
                }
            }
        });

        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                display.setText("");
            }
        });


    }

}
