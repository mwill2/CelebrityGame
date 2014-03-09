import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class CelebrityServer {
  public static void main(String argv[]) throws Exception {
    ServerSocket welcomeSocket = new ServerSocket(6001);
    while (true) {
      Socket connectionSocket = null;
      try {
        connectionSocket = welcomeSocket.accept();
        Connection connection = new Connection(connectionSocket);
        Thread t = new Thread(connection);
        t.start();
      }
      catch(SocketException e) {
        System.err.println(e);
        e.printStackTrace();
        continue;
      }
    }
  }
}
