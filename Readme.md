# Vertex:
### Project Info:
- author:   RiseGhost 👻
- language: Java ☕

Este projeto pretende implementar um servidor multithread em Java, capaz de lidar com vários clientes connectados em simultâneo e capaz de fazer broadcast da mensagens/ficheiros de um cliente para os restantes clientes.

Este projeto conta com uma versão com interface gráfica disponivél no seguinte link:

https://github.com/RiseGhost/VertexChat

### Implementation:

Para cada novo cliente connectado ao servidor é criado um novo thread de BroadCast responsável por receber os packages enviados por aquele cliente e enviar esse packages para todos os outros clientes.
```Java
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
```

Os packages são enviados e recebidos sobre a forma de array de bytes. Permitindo assim o envio de mensagens de ficheiros por partes dos clientes. __O envio de ficheiros só esta disponivél na versão com GUI.__

Para identificar o tipo de package que o clientes esta a enviar. Ficheiro ou string existe a classe FileType que ler os magic bytes do array bytes e derifica se é um string ou ficheiro.

Quando o package a ser enviado é somente uma string o servidor anexa no inicio da string os nomes dos clientes connectados no servidor, o conteudo da mensagem e no fim o nome do cliente que enviou aquela string. Por exemplo: <br> Estão conectado no servidor os clientes Ana e Alex e o Alex envia uma string para a Ana a dizer Olá. O que será enviado será:

[Ana, Alex, Olá, Alex]

O metodo de broadcast é syncronizado. E caso o cliente esteja a enviar um ficheiro, o ficheiro é enviado juntamente com um string com o conteudo de $file$. Para que o cliente consiga saber quem é que enviou aquele ficheiro.
Metodo run do BroadCast:

```Java
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
```

### CLI:

O servidor mostra uma simples interface em CLI sobre algumas informações:

- On/Off;
- Port;
- BroadCast thread create;
- Client disconnect;
- Type file send.

![](https://user-images.githubusercontent.com/91985039/252740171-3f7c8ad0-5757-4b17-bd94-325bd3f3206d.jpg)

### Falhas e Erros:

O servidor não apresenta alguma falhas. Como por exemplo:

- Se o cliente escrever uma "," na mensagem o conteudo irá aparecer errado no lado do outro cliente. Pois o servidor envia um string com os nomes do clientes sepadaros por virgulas.

