package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MyRunnable implements Runnable{
    private Socket socket;
    private ChatServer chatserver;
    private PrintWriter pw;

    public static ArrayList<String> users= new ArrayList<>();

    static{
        users.add("Ruimin");
        users.add("User1");
        users.add("User2");
        users.add("User3");
    }


    public MyRunnable(Socket socket, ChatServer chatserver) {
        this.socket = socket;
        this.chatserver = chatserver;
    }

    @Override
    public void run() {
        try {
            handleClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean handleCommand(String msg, PrintWriter pw, Scanner scanner) throws IOException{
        String[] parts = msg. split("#");
        if(parts.length==1){
            if(parts[0].equals("CLOSE")) {

                System.out.println("Client disconnected");
                return false;
            }
            pw.println("CLOSE#1");
            socket.close();
            chatserver.myRunnables.remove(this.toString());
            chatserver.onlineUsers.remove(Thread.currentThread().getName());
            chatserver.sendMsgToAll("ONLINE#"+chatserver.online());

        }else if(parts.length==3){
            String[] receivers=parts[1].split(",");
            if(parts[0].equals("SEND")){
                chatserver.sendMsg(Thread.currentThread().getName(),receivers,parts[2]);
            }
        }
        return true;

    }


    private void handleClient() throws IOException {
        pw = new PrintWriter(socket.getOutputStream(),true);
        Scanner scanner = new Scanner(socket.getInputStream());
        System.out.println("New client connected, running on: "+Thread.currentThread().getName());
        pw.println("You are now connected, you are running on: "+Thread.currentThread().getName());


        try {

            String message= scanner.nextLine();
            String[] strs = message.split("#");
            System.out.println(strs);
            // todo Why it most of time don't work well?

            boolean keepRunning = true;

            if((strs[0].equals("CONNECT")) && (strs.length==2)) {

                if(users.contains(strs[1])){
                    System.out.println(strs[1]+" is online");
                    pw.println(strs[1]+" your are online");
                    Thread.currentThread().setName(strs[1]);
                    pw.println("now the thread is called " + strs[1]);
                    System.out.println(chatserver.onlineUsers);
                    chatserver.onlineUsers.add(strs[1]);// todo everytime give me NullPointerException, could
                                                       // because of split don't work problem.

                    chatserver.sendMsgToAll("ONLINE#"+chatserver.online());



                    while (keepRunning) {
                        message = scanner.nextLine();  //Blocking call
                        keepRunning = handleCommand(message, pw, scanner);  // handleCommand() return a boolean. This is
                        // about to close or not.

                    }
                    pw.println("CLOSE#0");
                    chatserver.onlineUsers.remove(strs[1]);
                    chatserver.sendMsgToAll("ONLINE#"+chatserver.online());
                    pw.println("Connection is closing");
                    socket.close();
                    chatserver.myRunnables.remove(this.toString());

                }
                pw.println("CLOSE#2");
                socket.close();
                chatserver.myRunnables.remove(this.toString());

            }
            pw.println("CLOSE#1");
            socket.close();
            chatserver.myRunnables.remove(this.toString());

        } catch (Exception e){           //here sort client hard stop problem
            System.out.println("Client disconnected");
            e.printStackTrace();
        }
    }


    public void msg(String msg){
        pw.println(msg);
    }


}
