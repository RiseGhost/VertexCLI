import java.net.Socket;

public class User {
    private Socket socket;
    private String Name;
    private int ID;

    public User(Socket socket, int ID){
        this.socket = socket;
        this.ID = ID;
        this.Name = "Undefined";
    }

    public void setName(String Name){this.Name = Name;}

    public Socket getSocket(){return this.socket;}
    public String getName(){return this.Name;}
    public int getID(){return this.ID;}
}
