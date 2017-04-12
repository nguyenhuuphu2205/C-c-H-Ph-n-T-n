import java.io.*;
import java.math.BigInteger;
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
                    new ServiceThread(soketOfServer, ++clientNumber,listener).start();

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
        private ServerSocket listener;
        private Socket socket;
        private int numberSoket;

        public ServiceThread(Socket socket,int numberSocket,ServerSocket listener){
            this.numberSoket=numberSocket;
            this.socket=socket;
            this.listener=listener;
            log("New connection with client:"+this.numberSoket+"at"+socket);
        }public void run(){
            MaHoa rsa=new MaHoa(1024);
            try{
                boolean dangnhap=false;
                BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter os=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                OutputStream os1 = socket.getOutputStream();
                InputStream is1=socket.getInputStream();


                os.write("Chấp nhận kết nối");

                System.out.println("số kết nối hiện tại:"+(numberSoket));

                os.newLine();
                os.flush();
                while(true){


                    String line=null;
                    line=is.readLine();
                   // System.out.println(line);
                    String lineGiaiMa=new String(rsa.decrypt(new BigInteger(line),1996).toByteArray());
                    System.out.println(lineGiaiMa);
                    line=lineGiaiMa;
                    if(line.equals("exit")){
                        numberSoket=numberSoket-1;
                    }

                   // System.out.println("Nhan duoc lenh:"+line);

                    if(line!=null){
                        Lenh lenh=ThucHienLenh.phanTichLenh(line);
                        if(lenh.getCommand().equals("login")){
                            //boolean temp=ThucHienLenh.login(lenh.getArg1(),lenh.getArg2());
                            boolean temp=ThucHienLenh.login(lenh.getArg1(),lenh.getArg2());
                            if(temp==true){
                                dangnhap=true;
                               // String reply=rsa.encrypt1("Đăng nhập thành công !!!");
                                os.write("Đăng nhập thành công!!!");
                                os.newLine();
                                os.flush();

                            }else{
                                dangnhap=false;
                                os.write("username hoặc password không đúng");
                                os.newLine();
                                os.flush();
                            }


                        }
                        if(dangnhap==true) {
                            if (lenh.numArg() == 1) {
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
                                        os.write((ThucHienLenh.showFile().toString()));
                                        os.newLine();
                                        os.flush();
                                        break;
                                    case "exit":
                                        numberSoket--;
                                        os.write(("OK"));
                                        os.newLine();
                                        os.flush();
                                        break;
                                    case "delete*":
                                        System.out.println(line);
                                        break;
                                    default:
                                        os.write(("Lệnh không đúng"));
                                        os.newLine();
                                        os.flush();

                                }
                            }
                            if (lenh.numArg() == 2) {
                                switch (lenh.getCommand()) {
                                    case "delete":
                                        boolean t1 = ThucHienLenh.deleteFile(lenh.getArg1());
                                        if (t1 == true) {
                                            os.write(("Xóa file thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        } else {
                                            os.write(("Không thành công!! File không tồn tại"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        }
                                    case "create":
                                        boolean t2 = ThucHienLenh.createFile(lenh.getArg1());
                                        if (t2 == true) {
                                            os.write(("Tạo file thành công!!!!"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        } else {
                                            os.write(("Tạo file không thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        }
                                    case "createdir":
                                        boolean t3 = ThucHienLenh.createDirectory(lenh.getArg1());
                                        if (t3 == true) {
                                            os.write(("Tạo thư mục thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        } else {
                                            os.write(("Tạo thư mục không thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        }
                                    case "deletedir":
                                        boolean t4 = ThucHienLenh.deleteDirectory(lenh.getArg1());
                                        if (t4 == true) {
                                            os.write(("Xóa thư mục thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        } else {
                                            os.write(("Xóa thư mục không thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        }
                                    default:
                                        os.write(("lệnh không đúng"));
                                        os.newLine();
                                        os.flush();

                                }
                            }
                            if (lenh.numArg() == 3) {

                                switch (lenh.getCommand()) {
                                    case "move":
                                        boolean t1 = ThucHienLenh.moveFile(lenh.getArg1(), lenh.getArg2());
                                        if (t1 == true) {
                                            os.write(("Di chuyển file thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        } else {
                                            os.write(("Di chuyển file không thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        }
                                    case "movedir":
                                        boolean t2 = ThucHienLenh.moveDirectory(new File(lenh.getArg1()), new File(lenh.getArg2()));
                                        if (t2 == false) {
                                            os.write(("Di chuyển thư mục thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        } else {
                                            os.write(("Di chuyển thư mục không thành công"));
                                            os.newLine();
                                            os.flush();
                                            break;
                                        }
                                    case "login":
                                        boolean t3 = ThucHienLenh.login(lenh.getArg1(), lenh.getArg2());
                                        break;
                                    case "download":

                                        File myFile = new File (lenh.getArg2());
                                        byte [] mybytearray  = new byte [(int)myFile.length()];
                                        FileInputStream fis = new FileInputStream(myFile);
                                        BufferedInputStream bis = new BufferedInputStream(fis);
                                        bis.read(mybytearray,0,mybytearray.length);
//                                        OutputStream os1 = socket.getOutputStream();
                                        System.out.println("Sending " + "(" + mybytearray.length + " bytes)");
                                        os1.write(mybytearray,0,mybytearray.length);
                                        os1.flush();
                                        os1.flush();
                                        System.out.println("Done.");
                                       bis.close();
                                       fis.close();


                                    os1.close();
//
                                        break;
                                    case "upload":
                                        int current1=0;
                                        byte [] mybytearray1  = new byte [10000000];
                                        FileOutputStream fos = new FileOutputStream(lenh.getArg2());
                                        BufferedOutputStream bos = new BufferedOutputStream(fos);

//                                        InputStream is1=socket.getInputStream();
                                        int bytesRead1 = is1.read(mybytearray1,0,mybytearray1.length);

                                        current1 = bytesRead1;
                                        do {
                                            bytesRead1 =
                                                    is1.read(mybytearray1, current1, (mybytearray1.length-current1));
                                            if(bytesRead1 >= 0) current1 += bytesRead1;
                                        } while(bytesRead1 > -1);


                                        bos.write(mybytearray1, 0 , current1);
                                        bos.flush();
                                        //delete(mybytearray);
                                        System.out.println("File " + lenh.getArg1()
                                                + " downloaded (" + current1 + " bytes read)");

                                        bos.close();
                                        fos.close();
                                        is1.close();
                                        break;



                                    default:
                                        os.write(("Lệnh không đúng"));
                                        os.newLine();
                                        os.flush();
                                }
                            }
                        }else{
                            if(lenh.getCommand().equals("exit")){
                                os.write(("OK"));
                                os.newLine();
                                os.flush();
                            }else {
                                os.write(("Đề nghị đăng nhập"));
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
