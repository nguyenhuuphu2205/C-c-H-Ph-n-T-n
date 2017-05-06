import java.io.*;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by nguyenhuuphu on 16-Mar-17.
 */
public class MySSHServer {
     private int clientNumberConnected=0;
    public  static void main(String[] args) throws IOException {

        ServerSocket listener = null;

        System.out.println("Server is waiting to user accept");

        ArrayList<Thread> arrayThread = new ArrayList<Thread>();
        try {
            listener = new ServerSocket(8080);          //Lắng nghe trên cổng 8080
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {

            while (true) {

                /*
                    Kiểm tra số kết nối hiện tại nếu kết nối quá 4 thì không cho kết nối
                 */
                if (MySSHServer.countThread(arrayThread) > 3) {

                } else {
                    Socket soketOfServer = listener.accept();
                    ServiceThread serviceThread = new ServiceThread(soketOfServer, MySSHServer.countThread(arrayThread) + 1, listener);
                    arrayThread.add(serviceThread);
                    serviceThread.start();
                }
            }
        }catch(Exception e){
            System.out.println("Đã kết thúc 1 kết nối");
    }finally {
            listener.close();
        }

    }
    /*
        Hàm ghi log ra màn hình
     */
    public static void log(String message){
        System.out.println(message);
    }
    /*
        Định nghĩa 1 Thread chính để làm việc giữa client và server
     */
    public static  class ServiceThread extends Thread{
        private ServerSocket listener;
        private Socket socket;
        private int numberSoket;

        public ServiceThread(Socket socket,int numberSocket,ServerSocket listener){
            this.numberSoket=numberSocket;
            this.socket=socket;
            this.listener=listener;
            log("New connection with client:"+this.numberSoket+" at"+socket);
        }public void run(){
            MaHoa rsa=new MaHoa(1024);
            try{
                boolean dangnhap=false;
                BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter os=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                os.write("Chấp nhận kết nối");
                System.out.println("số kết nối hiện tại:"+(numberSoket));
                os.newLine();
                os.flush();
                while(true){
                    String line=null;
                    line=is.readLine();
                    String lineGiaiMa=new String(rsa.decrypt(new BigInteger(line),1996).toByteArray());     //Giải mã lệnh
                    line=lineGiaiMa;
                    if(line.equals("exit")){
                        numberSoket=numberSoket-1;
                    }

                    if(line!=null){
                        Lenh lenh=ThucHienLenh.phanTichLenh(line);          //Phân tích lệnh
                        if(lenh.getCommand().equals("login")){
                            boolean temp=ThucHienLenh.login(lenh.getArg1(),lenh.getArg2());
                            if(temp==true){
                                dangnhap=true;
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
                        if(dangnhap==true) {                //Kiểm tra đăng nhập

                            /*
                                Xử lý lệnh 1 tham số
                             */
                            if (lenh.numArg() == 1) {
                                switch (lenh.getCommand()) {
                                    /*
                                        Hiển thị thư mục hiện tại
                                     */
                                    case "showdir":
                                        os.write(ThucHienLenh.showDirectoryInDirectory());
                                        os.newLine();
                                        os.flush();
                                        break;
                                    /*
                                        Hiển thị thời gian của hệ thống
                                     */
                                    case "time":
                                        os.write(ThucHienLenh.showDateTime());
                                        os.newLine();
                                        os.flush();
                                        break;
                                    /*
                                        Hiển thị thư mục hiện tại
                                     */
                                    case "show":
                                        os.write(ThucHienLenh.showDirectory());
                                        os.newLine();
                                        os.flush();
                                        break;
                                    /*
                                        Hiển thị danh sách các file trong thư mục hiện tại
                                     */
                                    case "ls":
                                        os.write((ThucHienLenh.showFile().toString()));
                                        os.newLine();
                                        os.flush();
                                        break;
                                    /*
                                        Thoát chương trình
                                     */
                                    case "exit":
                                        numberSoket--;
                                        os.write(("OK"));
                                        os.newLine();
                                        os.flush();
                                        break;
                                    default:
                                        os.write(("Lệnh không đúng"));
                                        os.newLine();
                                        os.flush();

                                }
                            }

                            /*
                                Xử lý lệnh 2 tham số
                             */
                            if (lenh.numArg() == 2) {
                                switch (lenh.getCommand()) {
                                    /*
                                        Xóa file
                                     */
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
                                     /*
                                            Tạo File
                                         */
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
                                    /*
                                        Tạo thư mục
                                         */
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
                                    /*
                                        Xóa thư mục
                                         */
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

                            /*
                                Xử lý lệnh 3 tham số
                             */
                            if (lenh.numArg() == 3) {

                                switch (lenh.getCommand()) {
                                    /*
                                        Di chuyển file
                                     */
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
                                    /*
                                        Di chuyển thư mục
                                         */
                                    case "movedir":
                                        boolean t2 = ThucHienLenh.moveDirectory(new File(lenh.getArg1()), new File(lenh.getArg2()));
                                        if (t2 == true) {
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
                                        /*
                                            login
                                         */
                                    case "login":
                                        boolean t3 = ThucHienLenh.login(lenh.getArg1(), lenh.getArg2());
                                        break;
                                    /*
                                        Download File từ server về client
                                     */
                                    case "download":

                                        File file=new File(lenh.getArg2());
                                        if(!file.exists()){                     //Kiểm tra file có tồn tại không
                                            os.write("FileKhongTonTai");
                                            os.newLine();
                                            os.flush();
                                        }
                                        if(file.exists()&&file.isFile()) {
                                            /*
                                            Kiểm tra file có thích lớn, nếu file có kích thước lớn hơn 10KB thì cắt file làm 3 phần và download từng phần riêng biệt về
                                             */
                                            if (file.length() / 1024 > 10) {
                                                os.write("FileMax");
                                                os.newLine();
                                                os.flush();
//
                                        /*
                                            Thực hiện cắt file làm 3 phần trước khi download
                                         */
                                                ThucHienLenh.splitFile(lenh.getArg2(), "split", 3);
                                        /*
                                            Gửi từng file  nhỏ dần dần trên đường truyền thông qua 3 Thread
                                         */
                                                for (int temp = 0; temp < 3; temp++) {
                                                    new SendFileThread("split." + temp, temp + 1).start();
                                                }

                                                Thread.sleep(5000);
                                        /*
                                        Xóa các file đã cắt khi hoàn thành việc download
                                         */
                                                for (int temp = 0; temp < 3; temp++) {
                                                    ThucHienLenh.deleteFile("split." + temp);
                                                }
                                            }else{
                                                os.write("FileMin");
                                                os.newLine();
                                                os.flush();
                                                new SendFileThread(lenh.getArg2(),1).start();
                                            }

                                        }

                                        break;
                                        /*
                                            Upload file lên server
                                         */
                                        case "upload":
                                                os.write("Yes");
                                                os.newLine();
                                                os.flush();
                                                if(is.readLine().equals("Yes")) {
                                                    ThucHienLenh.nhanFile(lenh.getArg2(), 1);
                                                    break;
                                                }else{
                                                    break;
                                                }
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
                System.out.println("Đã kết thúc 1 kết nối:"+socket);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /*
        Hàm đếm số lượng client đăng nhập
     */
    public static int countThread(ArrayList<Thread> arr){
        int count=0;
        for(int i=0;i<arr.size();i++){
            if(arr.get(i).isAlive()){
                count++;
            }

        }
        return count;
    }
    /*
        Định nghĩa 1 class  Thread để gửi File qua Soket
     */
    public  static class SendFileThread extends Thread{
        private int port;
        private String filename;
        public SendFileThread(String filename,int port)
        {
            this.port=port;
            this.filename=filename;
        }
        public void run(){
            try {
                ThucHienLenh.guiFile(this.filename,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
        Định nghĩa 1 class Thread để nhận File qua Soket
     */
    public static class ReceiveThread extends Thread{
        private int port;
        private String filename;
        public ReceiveThread(String filename,int port)
        {
            this.port=port;
            this.filename=filename;
        }
        public void run(){
            ThucHienLenh.nhanFile(this.filename,port);
            ReceiveThread.interrupted();

        }
    }
}
