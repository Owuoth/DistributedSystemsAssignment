
package ToysSwing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketServer {
    Connection conn;
    Consumer<String> onReceive;
    Consumer<String> onConnect;
    
    public SocketServer() {
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
               ServerSocket serverSocket = new ServerSocket(0);
               
               String message = "Server is running on port " + serverSocket.getLocalPort();
               onConnect.accept(message);
               
               socket = serverSocket.accept();
               message = "Connected successfully";
               onConnect.accept(message);
               
               ObjectOutputStream out =new ObjectOutputStream(socket.getOutputStream());
               ObjectInputStream in =new ObjectInputStream(socket.getInputStream());
                this.out = out;
                
                send("Connected Successfully");
               
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
