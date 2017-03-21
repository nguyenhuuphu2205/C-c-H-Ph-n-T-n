import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by nguyenhuuphu on 16-Mar-17.
 */
public class MySSHServer {
     private int clientNumber=0;
    public static void main(String[] args) throws IOException {
        ServerSocket listener=null;

        System.out.println("Server is waiting to user accept");
        int clientNumber=0;
        try{
            listener=new ServerSocket(8080);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        try{
            while(true){


                if(clientNumber>3){
                    listener.close();
                }else {
                    Socket soketOfServer=listener.accept();
                    System.out.println(clientNumber);
                    new ServiceThread(soketOfServer, clientNumber++).start();
                }
            }
        }finally {
            listener.close();
        }

    }
    public static void log(String message){
        System.out.println(message);
    }
    public static  class ServiceThread extends Thread{
        private Socket socket;
        private int numberSoket;
        public ServiceThread(Socket socket,int numberSocket){
            this.numberSoket=numberSocket;
            this.socket=socket;
            log("New connection with client:"+this.numberSoket+"at"+socket);
        }public void run(){
            try{
                BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter os=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                os.write("Chấp nhận kết nối");

                System.out.println("số kết nối hiện tại:"+(numberSoket+1));

                os.newLine();
                os.flush();
                while(true){
                    String line=null;
                    line=is.readLine();
                    if(line.equals("exit")){
                        numberSoket=numberSoket-1;
                    }

                   // System.out.println("Nhan duoc lenh:"+line);
                    if(line!=null){
                        Lenh lenh=ThucHienLenh.phanTichLenh(line);
                      if(lenh.numArg()==1) {
                          switch (lenh.getCommand()) {
                              case "showdir":
                                  os.write(ThucHienLenh.showDirectoryInDirectory());
                                  os.newLine();
                                  os.flush();
                                  break;
                              case "time":
                                  os.write(ThucHienLenh.showDateTime());
                                  os.newLine();
                                  os.flush();
                                  break;
                              case "show":
                                  os.write(ThucHienLenh.showDirectory());
                                  os.newLine();
                                  os.flush();
                                  break;
                              case "ls":
                                  os.write(ThucHienLenh.showFile().toString());
                                  os.newLine();
                                  os.flush();
                                  break;
                              case "exit":
                                  numberSoket--;
                                  os.write("OK");
                                  os.newLine();
                                  os.flush();
                                  break;
                              case "delete*":
                                  System.out.println(line);
                                  break;
                              default:
                                  os.write("Lệnh không đúng");
                                  os.newLine();
                                  os.flush();

                          }
                      }

                }else{
                        this.numberSoket--;
                    }
                }

            }catch(IOException e){
                System.out.println(e);
                e.printStackTrace();
            }
        }


    }
}