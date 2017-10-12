/**
 * @author Aoheng MA
 * @verison 1.00
 */

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class VisualDictionaryClient extends JFrame {

    //Declare Port and IP address
    private static int PORT;//default:3939
    private static String IP; //default:"localhost"
    private DataOutputStream output;
    private DataInputStream input;
    private Container container;
    private JTextArea display;
    private JTextField textField = new JTextField("Enter your query HERE", 50);

    /**
     * Construct Client GUI
     * @param title
     */
    public VisualDictionaryClient(String title) {

        super(title);
        container = this.getContentPane();
        container.setBackground(Color.LIGHT_GRAY);

        final JPanel test = new JPanel();
        test.setBorder(new BevelBorder(BevelBorder.RAISED));
        getContentPane().add(test, BorderLayout.CENTER);


        //Header Layout
        display = new JTextArea("Welcome to COMP90015 Project 1\n" +
                "\nAuthor:\nAoheng Ma 888788" +
                "\nThe University of Melbourne\n", 20, 50);
        container.add(display, "North");


        //Center Layout
        textField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setText("");
            }

            public void focusLost(FocusEvent e) {
            }

        });
        container.add(textField, "Center");

        //Button Layout
        // Search
        JButton searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial", Font.PLAIN, 15));

        //Add new word
        JButton addBtn = new JButton("Add");
        addBtn.setFont(new Font("Arial", Font.PLAIN, 15));

        //Delete word
        JButton removeBtn = new JButton("Remove");
        removeBtn.setFont(new Font("Arial", Font.PLAIN, 15));

        //Button Layout
        JPanel btnLayoutCtrl = new JPanel();
        btnLayoutCtrl.add(searchBtn, BorderLayout.WEST);
        btnLayoutCtrl.add(addBtn, BorderLayout.CENTER);
        btnLayoutCtrl.add(removeBtn, BorderLayout.EAST);
        container.add(btnLayoutCtrl, "South");

        try {
            searchBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    search();
                }
            });

            addBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addWord();
                }
            });

            removeBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeWord();
                }
            });
        } catch (Exception e) {
            //e.printStackTrace();
            display.append("\nError please check your input");
        }
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    output.writeUTF("DISCONNECT: CLIENT Disconnected @ \n");
                    output.flush();
                } catch (IOException exit) {
                    display.append("Communication Reset");
                    //exit.printStackTrace();
                } finally {
                    System.exit(0);
                }
            }
        });


    }

    /**
     * connect to the server
     */
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
        } catch (UnknownHostException e) {
            //e.printStackTrace();
            display.append("\nUnknown Host");
        } catch (ConnectException e) {
            //e.printStackTrace();
            display.append("\nConnect Exception! \nCould not reach the server");
        } catch (SocketTimeoutException e) {
            //e.printStackTrace();
            display.append("\nCommunication Timeout");
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (IOException e) {
            //e.printStackTrace();
            display.append("\nCan not access destination host\nPlease double check network connection");
        }

    }

    /**
     * Send the search-word command to the server
     */
    public void search() {

        display.setText("");
        display.append("Connecting...\n");
        String search = textField.getText();
        String result = null;
        if (textField.getText().isEmpty()) {
            new JOptionPane().showMessageDialog(null, "Please enter a word to search");
        } else if (search.equalsIgnoreCase("Enter your query HERE")) {
            new JOptionPane().showMessageDialog(null, "Please double check your input");
        } else {
            String searchPost = "SEARCH:" + search + "@\n";
            try {
                output.writeUTF(searchPost);
                output.flush();
                result = input.readUTF();
            } catch (IOException e) {
                display.append("\nCommunication Error\n");
                //e.printStackTrace();
            }
            textField.setText("Enter your query HERE");
            display.append("Search " + search + " :\n");
            display.append("Result:\n "+result);
        }
    }

    /**
     * Send add-word command to the server
     */
    public void addWord() {
        display.setText("");
        display.append("Connecting...\n");
        String add = textField.getText();
        String result = null;
        if (textField.getText().isEmpty()) {
            new JOptionPane().showMessageDialog(null,
                    "Please enter a word with relative definitions" +
                            "\nMake sure use \"@\" as delimiter");
        } else if (add.indexOf("@") == -1) {
            new JOptionPane().showMessageDialog(null,
                    "Please enter a word with relative definitions" +
                            "\nMake sure use \"@\" as delimiter" +
                            "\nFor multiple meaning,you can use \";\"as delimiter");
        } else {
            String addPost = "ADD:" + add;
            try {
                output.writeUTF(addPost);
                output.flush();
                result = input.readUTF();
            } catch (IOException e) {
                display.append("\nCommunication Error\n");
                e.printStackTrace();
            }
            textField.setText("Enter your query HERE");
            display.append("Search " + add + " :\n");
            display.append("Result:\n "+result);
        }
    }

    /**
     * Send remove-word command to the server
     */
    public void removeWord() {
        display.setText("");
        display.append("Connecting...\n");
        String remove = textField.getText();
        String result = null;
        if (remove.isEmpty()) {
            new JOptionPane().showMessageDialog(null, "Please enter a word to delete");
        } else if (remove.equalsIgnoreCase("Enter your query HERE")) {
            new JOptionPane().showMessageDialog(null, "Please double check your input");
        } else {
            String removePost = "REMOVE:" + remove + "@" + "\n";
            try {
                output.writeUTF(removePost);
                output.flush();
                result = input.readUTF();
            } catch (IOException e) {
                display.append("\nCommunication Error\n");
                e.printStackTrace();
            }
            textField.setText("Enter your query HERE");
            display.append("Search " + remove + " :\n");
            display.append("Result:\n "+result);
        }

    }

    /**
     * Main method including read IP Address and Port# from command line
     * @param args
     */
    public static void main(String args[]) {
        CmdLineArgs argsBean = new CmdLineArgs();
        CmdLineParser parser = new CmdLineParser(argsBean);
        try {
            parser.parseArgument(args);
            System.out.println("Host: " + argsBean.getHost());
            System.out.println("Port: " + argsBean.getPort());

        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            //Print the usage to help the user understand the arguments expected
            //by the program
            parser.printUsage(System.err);
        }

        PORT = argsBean.getPort();
        IP = argsBean.getHost();

        VisualDictionaryClient vc = new VisualDictionaryClient("E-Dictionary");
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    vc.setSize(350, 500);
                    vc.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        vc.connect();

    }
}