package server;


import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChatServer {
    private ServerSocket serverSocket;
    public ConcurrentHashMap<String, MyRunnable> myRunnables = new ConcurrentHashMap();
    public CopyOnWriteArrayList onlineUsers;


    private void startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        // start a new servers, use argument of port number.
        System.out.println("Server started, listening on : " + port);
        while (true) {      // continue working, never ending loop
            Socket socket = serverSocket.accept();    // connect a client
            MyRunnable myRunnable = new MyRunnable(socket,this);   // make a new object of MyRunnable, 2 arguments is
            // socket and serverScoket
            Thread thread = new Thread(myRunnable);                //make a new thread for this myRunnable
            myRunnables.put(myRunnable.toString(),myRunnable);    // put this myRunnable and its name to the map.
            thread.start();                                         // start the thread.
        }
    }



    public String online(){
        String online= new String();
        return  String.join(",",onlineUsers);

   }



    public void sendMsgToAll(String msg){
        myRunnables.values().forEach(myRunnable -> myRunnable.msg(msg));

    }

    public void sendMsg(String sender,String[] receiver,String msg){
      String message="MESSAGE#"+sender+"#"+msg;
        //todo how to finish this method?
    }


    //Call server with arguments like this: 8088
    public static void main(String[] args) throws IOException {
        int port = 8088;
        
        try {
            if (args.length == 1) {
                port = Integer.parseInt(args[0]);
            }

        } catch (NumberFormatException ne) {
            System.out.println("Illegal inputs provided when starting the server!");
            return;
        }
        new ChatServer().startServer(6789);

    }



}
