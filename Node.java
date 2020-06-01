import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.*;

public class Node extends Thread {
  String coordHost;
  String id;
  DatagramSocket datagramSocket;

  public Node(String id, int port) throws SocketException {
    this.coordHost = "127.0.0.1";
    this.datagramSocket = new DatagramSocket(port);
    this.id = id;
  }

  public void run() {
    System.out.println("Initializing process " + id);
    while (true) {
      try {
        System.out.println("Process " + id + " try blocked");
        if (lock()) {
          System.out.println("Process " + id + " blocked");
          for (int i = 0; i < 50; i++) {

            Integer lastValue = read();
            write(lastValue + 100, id, i);

          }
          unlock();
          System.out.println("Process " + id + " unblocked");
        }
        Thread.sleep(1000);

      } catch (Exception e) {
        System.out.println(e);
      }
    }
  }

  private boolean lock() throws IOException {
    byte[] buffer = "lock".getBytes();
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(coordHost), 4500);
    datagramSocket.send(packet);
    buffer = new byte[8192];
    while (true) {
      try {
        packet = new DatagramPacket(buffer, buffer.length);
        datagramSocket.setSoTimeout(500);
        datagramSocket.receive(packet);
        return Boolean.parseBoolean(new String(packet.getData(), 0, packet.getLength()));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void unlock() throws IOException {
    byte[] buffer = "unlock".getBytes();
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(coordHost), 4500);
    datagramSocket.send(packet);
  }

  public static Integer read() throws IOException {
    String lastLine = "";
    String currentLine;
    BufferedReader bufferedReader = new BufferedReader(new FileReader("sharedFile.txt"));
    while ((currentLine = bufferedReader.readLine()) != null) {
      lastLine = currentLine;
    }
    bufferedReader.close();
    return Integer.parseInt(lastLine);
  }

  public static void write(final Integer value, final String idProcess, final Integer i) throws IOException {
    BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("sharedFile.txt", true));
    BufferedWriter bufferedWriterHistory = new BufferedWriter(new FileWriter("historico-acessos.txt", true));

    bufferedWriter.write("\n".concat(String.valueOf(value)));
    bufferedWriterHistory.write("\n".concat(String.valueOf(i + 1)).concat(" - id processo ")
        .concat(String.valueOf(idProcess)).concat(" - ").concat(String.valueOf(value)));
    bufferedWriter.close();
    bufferedWriterHistory.close();
  }
}