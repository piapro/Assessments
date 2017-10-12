import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import sun.net.ConnectionResetException;

import javax.net.ServerSocketFactory;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class DictionaryServer extends Thread {
    //private static int PORT = 3939;//Internal test only
    private static DataInputStream input;
    private static DataOutputStream output;
    private static int counter = 0;
    private static String fileName = "Dictionary.txt";
    private static FileWriter fw;
    private static BufferedReader br;
    private static Hashtable<String,String> dictTable = new Hashtable<>();
    static Set<String> set = dictTable.keySet();
    static Iterator it = set.iterator();

    /**
     * Read Hash Map from file
     *
     * @param fileName
     */
    public static Hashtable readFile(String fileName) {
        try {
            File dictionary = new File(fileName);
            if (!dictionary.exists()) {
                System.out.println("Cannot find the dictionary file.");
                dictionary.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(dictionary));
            String line = null;

            while ((line = br.readLine()) != null) {
                String[] temp = line.split("@");
                dictTable.put(temp[0], temp[1]);
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading the file");
        }
        return dictTable;

    }

    /**
     * Write Hash Map into file
     * @param fileName
     * @param dictTable
     * @param
     * @param
     */
    public static void writeFile(String fileName, Hashtable dictTable) {
        FileWriter fr = null;
        Set<String> set = dictTable.keySet();
        Iterator it = set.iterator();
        try {
            fw = new FileWriter(new File(fileName), false);
            //fw.write("<Word>@<Definitions>\n Data_Source_by_©_2017_Merriam-Webster_Incorporated");
            while (it.hasNext()) {
                String word = (String) it.next();
                String def = (String) dictTable.get(word);
                fw.write(word + "@" + def + "\n");
                fw.flush();
            }
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Find a specific word from Hash Map
     *
     * @param word
     * @param dictTable
     */
    public static String findWord(String word, Hashtable dictTable) {
        //Set<String> set = dictTable.keySet();
        //Iterator it = set.iterator();
        String result = null;

        if (dictTable.containsKey(word)) {
            result = (String) dictTable.get(word);

        } else {
            result = "No such word founded in the dictionary";
        }
        return result;
    }

    /**
     * Add a word into Hash Map
     *
     * @param word
     * @param def
     * @param dictTable
     */
    public static String addWord(String word, String def, Hashtable dictTable) {
        //Set<String> set = dictTable.keySet();
        //Iterator it = set.iterator();
        String result = null;

        System.out.println(dictTable.containsKey(word));

        if (dictTable.containsKey(word)) {
            result = "The word already exists in the Dictionary.";
        } else {
            dictTable.put(word, def);
            result = "The word has been added to the Dictionary";
        }
        return result;


    }

    /**
     * Delete a specific word from Hash Map
     *
     * @param word
     * @param dictTable
     */
    public static String removeWord(String word, Hashtable dictTable) {
        //Set<String> set = dictTable.keySet();
        //Iterator it = set.iterator();
        String result = null;
        if (!dictTable.containsKey(word)) {
            result = "The word did not exists in the Dictionary.";
        } else {
            dictTable.remove(word);
            result = "The word has been deleted from the Dictionary";
        }

        return result;
    }

    /**
     * Socket communication
     *
     * @param client
     */
    private static void serverClient(Socket client) {
        try (Socket clientSocket = client) {
            // Data Input Stream:
            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            // Data Output Stream
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            // Send welcome message to client
            output.writeUTF("\n[SERVER]:Welcome [Client-"+counter+"]"+"\n");
            output.flush();
            //Show connection status in server command line
            System.out.println("[Client-"+counter+"] " + input.readUTF());


            while (true) {

                String post = input.readUTF();// read from client textField
                System.out.println(post);//Show request from client textField

                String[] text = post.split(":");
                String command = text[0];
                String vocabulary = text[1];
                String[] wordWithDef = vocabulary.split("@");

                String word = wordWithDef[0];
                //System.out.println(word);
                String def = wordWithDef[1];
                //System.out.println(def);

                if (command.equalsIgnoreCase("SEARCH")) {
                    System.out.println("Client " + counter + " Applying for query the word[" + word + "]");
                    output.writeUTF(findWord(word, dictTable));
                    output.flush();

                }

                if (command.equalsIgnoreCase("ADD")) {
                    System.out.println("Client " + counter +
                            " Applying for add a word[" + word + "] with definition \"" + def + " \"");
                    output.writeUTF(addWord(word, def, dictTable));
                    output.flush();

                }

                if (command.equalsIgnoreCase("REMOVE")) {
                    System.out.println("Client " + counter + " Applying for Remove the word[" + word + "]");
                    output.writeUTF(removeWord(word, dictTable));
                    output.flush();
                }

                if (command.equalsIgnoreCase("DISCONNECT")) {
                    System.out.println("Client " + counter + " Disconnected!");
                    writeFile(fileName, dictTable);
                    break;

                }

            }


        } catch (UnknownHostException e) {
            System.out.println("ERROR: UnknownHost");
            //e.printStackTrace();
        } catch (SocketTimeoutException e) {
            System.out.println("ERROR: Socket Timeout");
            //e.printStackTrace();
        } catch (ConnectionResetException e) {
            System.out.println("ERROR: Connection Reset");
            //e.printStackTrace();
        } catch (IOException e) {
            System.out.println("ERROR: Socket Communication Error.\nClient offline!");
            //e.printStackTrace();
            writeFile(fileName, dictTable);
        }
    }

    /**
     * Server bootSequence just for fun @_@
     */
    private static void bootSequence() {
        try {
            System.out.println("Starting up: COMP90015 Distributed System Server, Please wait...");
            TimeUnit.SECONDS.sleep(1);
            System.out.println();
            System.out.println("Memory for crash kernel (0x0 to 0x0) not within permissible range" +
                    "\nRed Hat nash version 7.8.7 starting");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Welcome to Red Hat Enterprise Linux Server");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Setting clock: " +
                    new SimpleDateFormat("MMMM d, yyyy HH:MM:SS").format(new java.util.Date()));
            System.out.println("Checking file systems");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("[OK]");
            System.out.println("Mounting local file systems");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("[OK]");
            System.out.println("Checking network configurations");
            TimeUnit.SECONDS.sleep(1);
            System.out.println("[OK]");
            System.out.println("Starting communication...");
            System.out.println("Service Status Console : \n");
        } catch (InterruptedException e) {
            System.out.println("System Error..");
        }
    }

    public static void main(String args[]) {

        //Object that will store the parsed command line arguments
        CmdLineArgs argsBean = new CmdLineArgs();

        //Parser provided by args4j
        CmdLineParser parser = new CmdLineParser(argsBean);
        try {

            //Parse the arguments
            parser.parseArgument(args);

            //After parsing, the fields in argsBean have been updated with the given
            //command line arguments
            System.out.println("Host: " + argsBean.getHost());
            System.out.println("Port: " + argsBean.getPort());

        } catch (CmdLineException e) {

            System.err.println(e.getMessage());

            //Print the usage to help the user understand the arguments expected
            //by the program
            parser.printUsage(System.err);
        }

        bootSequence();
        readFile(fileName);
        //Assign new Socket if current Socket was not available
        ServerSocketFactory factory = ServerSocketFactory.getDefault();
        //try(Model)will close model object when program is closed ,instead of using object .close()
        try (ServerSocket server = factory.createServerSocket(argsBean.getPort())) {//argsBean.getPort())
            System.out.println("Waiting for Clients Connection Requests...");
            // Wait for connections.
            while (true) {
                Socket client = server.accept();
                counter++;
                System.out.println("[Client-"+counter+"] Applying for connect!");
                // Start a new thread for a connection
                Thread t = new Thread(() -> serverClient(client));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("IP Address already in use");
        } finally {
            writeFile(fileName, dictTable);
            System.exit(0);
        }

    }

}
