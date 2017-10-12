package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;


public class Server {

    private ServerSocket ss;

    String[] userName = {"Admin", "User1", "User2"};
    InputStream in;
    String[] onlineNameList = {};//在线用户名数组
    private List<Socket> sockets;


    public void start() throws IOException {
        sockets = new ArrayList<Socket>();
        ss = new ServerSocket(8000);
        while (true) {
            System.out.println("绑定端口");

//          I/O阻塞
            Socket s = ss.accept();
            sockets.add(s);
            System.out.println("连接成功");
            new Service(s, sockets, userName).start();
        }
    }
}

class Service extends Thread {
    Socket s;
    List<Socket> sockets;
    private Scanner s2;
    String[][] userNamePwdList;
    int userPos;    //用户在用户数组中的位置

    public Service(Socket s, List<Socket> sockets, String[][] userNamePwdList) {
        this.s = s;
        this.sockets = sockets;
        this.userNamePwdList = userNamePwdList;
    }

    public Service(Socket s, List<Socket> sockets) {
        this.s = s;
        this.sockets = sockets;
    }

    public void run() {
        try {
            InputStream in = s.getInputStream();
            OutputStream out = s.getOutputStream();
            s2 = new Scanner(in);

            String userNamePwd = s2.nextLine();

            //验证用户名和密码
            if (checkUserNamePwd(userNamePwd) != -1) {

                for (int i = 0; i < sockets.size(); i++) {
                    //遍历用户组广播新用户加入
                    Socket s2 = (Socket) sockets.get(i);
                    OutputStream out1 = s2.getOutputStream();
                    out1.write(("                        " + this.userNamePwdList[this.userPos][0] + "已进入QQ聊天室...\n\n").getBytes());
                    out1.flush();
                }

                final SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");

                while (true) {

                    String str = s2.nextLine();
                    for (int i = 0; i < sockets.size(); i++) {
                        //遍历用户组广播消息
                        Socket s2 = (Socket) sockets.get(i);
                        OutputStream out1 = s2.getOutputStream();

                        //发送用户名 + 服务器时间  + 聊天内容
                        out1.write(("@" + userNamePwdList[this.userPos][0] + "     " + currentDate.format(new Date()) + "\n" + str + "\n\n").getBytes());
                        out1.flush();
                    }
                }
            } else {
                out.write("error".getBytes());
                System.out.println("entering");
                this.s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 用户名与验证码检查
     * 返回用户在数组中的位置，用户不存在则返回-1
     */
    private int checkUserNamePwd(final String userNamePwd) {

        for (int i = 0; i < userNamePwd.length(); i++) {
            if (userNamePwd.equals(this.userNamePwdList[i][0] + this.userNamePwdList[i][1])) {
                this.userPos = i;
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.start();
    }
}