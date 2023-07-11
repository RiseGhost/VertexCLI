import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BroadCastMSG extends Thread{
    private Socket socket;
    private User User;
    private ArrayList<User> Users;

    public BroadCastMSG(User User, ArrayList<User> Users){
        socket = User.getSocket();
        this.User = User;
        this.Users = Users;
        System.out.println("BradCast create");
        start();
    }

    private ArrayList<String> getAllUsersName(){
        ArrayList<String> UserNames = new ArrayList<>();
        for(User u : Users)     UserNames.add(u.getName());
        return UserNames;
    }

    synchronized public void run(){
        try{
            ObjectInputStream name = new ObjectInputStream(socket.getInputStream());
            String ClientName = name.readObject().toString();
            User.setName(ClientName);

            while (true){
                DataInputStream is = new DataInputStream(socket.getInputStream());
                byte[] input = new byte[is.readInt()];
                is.readFully(input);
                String msg = new String(input, StandardCharsets.UTF_8);
                for (User u : Users){
                    if (!socket.equals(u.getSocket())){
                        DataOutputStream os = new DataOutputStream(u.getSocket().getOutputStream());
                        ArrayList<String> list = new ArrayList<>(getAllUsersName());
                        String FileType = new FileType().getType(input);
                        if (!FileType.equals("")){
                            System.out.println(new FileType().getType(input));
                            os.writeInt(input.length);
                            os.write(input);
                            os.flush();
                            msg = "$file$";
                        }
                        list.add(msg);
                        list.add(ClientName);
                        os.writeInt(list.toString().getBytes(StandardCharsets.UTF_8).length);
                        os.write(list.toString().getBytes(StandardCharsets.UTF_8));
                        os.flush();
                    }
                }
            }
        }   catch (IOException | ClassNotFoundException e){
            System.out.println("ID -> " + User.getID() + "  Name -> " + User.getName() + " . Was disconnected.");
            Users.remove(User);
        }
    }
}
