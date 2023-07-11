import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private Socket socket;
    private ArrayList<User> Users = new ArrayList<>();
    private ArrayList<Socket> Clients  = new ArrayList<>();
    private ServerSocket ssocket;

    public Server(int serverport){
        try{
            int ID = 0;
            this.ssocket = new ServerSocket(serverport);
            System.out.println("🚪 port -> \033[0;33m" + serverport + "\033[0m");
            System.out.println("Server ON ✅");
            while (true){
                socket = ssocket.accept();
                Clients.add(socket);
                User u = new User(socket, ID);
                Users.add(u);
                ID++;
                new BroadCastMSG(u, Users);      //Thread responsável por ler as mensagens daquele cliente e fazer broadcast para os outros clientes.
            }
        }   catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Server(8000);
    }
}