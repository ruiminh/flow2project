package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

        Socket socket;
        PrintWriter pw;
        Scanner scanner;

        public void connect(String address, int  port) throws IOException {
            socket=new Socket(address,port);
            pw=new PrintWriter(socket.getOutputStream(),true);
            scanner=new Scanner(socket.getInputStream());
            System.out.println(scanner.nextLine());
            Scanner keyboard = new Scanner(System.in);
            boolean keepRunning=true;
            while(keepRunning){
                String msgToSend= keyboard.nextLine();
                pw.println(msgToSend);
                if(scanner.hasNextLine()){
                    System.out.println(scanner.nextLine());
                }


                if(msgToSend.equals("CLOSE#")){
                    keepRunning=false;
                }


            }
            socket.close();


        }
        public static void main(String[] args) throws IOException {
            new ChatClient().connect("localhost",6789);

        }

}
