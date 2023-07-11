import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    private Socket socket;

    public Client(String ip, int port){
        try{
            socket = new Socket(ip, port);
            new ReadMSG(socket);
            System.out.println("Indique seu nome:");
            Scanner name = new Scanner(System.in);
            ObjectOutputStream NameOS = new ObjectOutputStream(socket.getOutputStream());
            NameOS.writeObject(name.nextLine());
            NameOS.flush();

            while (true){
                Scanner scanner = new Scanner(System.in);
                String msg = scanner.nextLine();
                if (msg.equals("exit")){
                    socket.close();
                    break;
                }
                else{
                    DataOutputStream os = new DataOutputStream(socket.getOutputStream());
                    os.writeInt(msg.getBytes(StandardCharsets.UTF_8).length);
                    os.write(msg.getBytes(StandardCharsets.UTF_8));
                    os.flush();
                }
            }
        }   catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args){
        Client client = new Client("192.168.1.246", 8000);
    }
}
