/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToysSwing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class SocketClient {
    Connection conn;
    Consumer<String> onReceive;
    Consumer<String> onConnect;
    
    String ipaddress = "";
    int port = 0;

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public void setPort(int port) {
        this.port = port;
    }
        
    public SocketClient() {
        conn = new Connection();
    }

    public void setOnConnect(Consumer<String> onConnect) {
        this.onConnect = onConnect;
    }
    
    public void setOnReceive(Consumer<String> onReceive) {
        this.onReceive = onReceive;
    }
      
    public void startConnection() {
        conn.start();
    }
    
    public void send(String message) {
        try {
            conn.out.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void stopConnection() {
        try {
            conn.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public class Connection extends Thread {
        private ObjectOutputStream out;
        private Socket socket;
       @Override
       public void run() {
           try {
               
               socket = new Socket(ipaddress, port);
               
               
               ObjectOutputStream out =new ObjectOutputStream(socket.getOutputStream());
               ObjectInputStream in =new ObjectInputStream(socket.getInputStream());
                this.out = out;
               out.writeObject("Connected successfully");
                while(true){
                   String received = in.readObject().toString();
                   onReceive.accept(received);
           } 
           }catch (IOException ex) {
               Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
           } catch (ClassNotFoundException ex) {
               Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
           }
          
        }
        
    }
    
}
