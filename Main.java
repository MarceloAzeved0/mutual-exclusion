import java.net.SocketException;

public class Main {
  public static void main(String[] args) throws SocketException {
    Coordinator coordinator = new Coordinator();
    coordinator.start();

    Node Node1 = new Node("1", 4501);
    Node Node2 = new Node("2", 4502);
    Node Node3 = new Node("3", 4503);
    Node Node4 = new Node("4", 4504);
    Node Node5 = new Node("5", 4505);

    Node1.start();
    Node2.start();
    Node3.start();
    Node4.start();
    Node5.start();
  }
}