import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ReadMSG extends Thread{
    private Socket socket;

    public ReadMSG(Socket socket){
        this.socket = socket;
        start();
    }

    public void run(){
        try{
            while (true){
                DataInputStream is = new DataInputStream(socket.getInputStream());
                byte[] File = new byte[is.readInt()];
                is.readFully(File);
                if (File.length < 4) System.out.println("WTF IS IT?");
                else if (File[0] == (byte) 0xFF && File[1] == (byte) 0xD8 && File[2] == (byte) 0xFF && File[3] == (byte) 0xE0){
                    System.out.println("Isto é um jpeg");
                    OutputStream os = new BufferedOutputStream(new FileOutputStream("download.jpg"));
                    os.write(File, 0, File.length);
                    os.flush();
                }
                else{
                    String[] msg = new String(File, StandardCharsets.UTF_8).split(",");
                    String author = msg[msg.length - 1].replace("]", "").substring(1);
                    System.out.println(author + " :" + msg[msg.length - 2]);
                }
            }
        }   catch (IOException e){
            System.out.println(e.getMessage());
            System.out.println("Parrou o ReaDMSG");
        }
    }
}
