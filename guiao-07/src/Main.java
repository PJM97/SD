import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ServerWorker implements Runnable{
    private Socket socket;
    private int id;

    public ServerWorker(Socket socket, int id){
        this.socket=socket;
        this.id=id;
    }

    private void parser(String str){
        String[] strL = str.split(" ");
        switch (strL[0]){
            case "1":
                System.out.println("fst case");
                break;
            case "2":
                System.out.println("snd case");
                break;
            default:
                System.out.println("default case");
                break;
        }
    }

    public void run(){
        try{
            System.out.println("Conexão Recebida");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String line;  //string para a leitura das mensagens do cliente
            while((line=in.readLine())!=null && !line.equals("exit")){
                parser(line);
                System.out.println("\nReceived message from client: "+line);
                //String str = in.readLine();
                out.println(line);
                out.flush();
                System.out.println("Worker-"+id+"Reply with: "+line);

            }
            System.out.println("\nWorker-"+id+" > Client Disconnected. Connection is closed\n");

            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        }catch(Throwable t){}
    }

}

class Servidor implements Runnable{
    private ServerSocket sSock;
    private Boolean k = true;
    private int swCount = 0;

    Servidor() throws Exception{
        this.sSock = new ServerSocket(12345);
    }

    public void run(){
        try{
            while(true){
                System.out.println("Servidor Ligado");
                Socket clSock = sSock.accept();

                Thread sw = new Thread( new ServerWorker(clSock,swCount++) );
                sw.start();


                //System.out.println("Servidor Desligado");
            }
        }
        catch(Throwable t){}
    }

}

class Cliente implements  Runnable{
    private Socket socket;

    Cliente() throws Exception{
        this.socket = new Socket("localhost",12345);
    }

    public void run(){
        try{
            while(true){
                System.out.println("Cliente Ligado");
                //Socket clSock = sSock.accept();
                System.out.println("Conexão Establecida");

                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader systemIn = new BufferedReader(new InputStreamReader(System.in));
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //mensagens do servidor


                String userInput; //String para ler o input do cliente
                String response;  //Stirng para ler a resposta do Servidor

                System.out.println("$ ");

                while((userInput=systemIn.readLine())!=null && !userInput.equals("exit")){
                    out.println(userInput);
                    out.flush();

                    response= in.readLine();
                    System.out.println("Resposta do Servidor: "+response);
                    System.out.print("\n$ ");
                }
                //System.out.println("Client Disconnected. Connection is closed");

                socket.shutdownOutput();
                socket.shutdownInput();
                socket.close();
                //System.out.println("Servidor Desligado");
            }
        }
        catch(Throwable t){}
    }

}

public class Main {

    public static void main(String args[]){
        try {
            Servidor server = new Servidor();
            server.run();
        }catch(Throwable t){}
    }

}
