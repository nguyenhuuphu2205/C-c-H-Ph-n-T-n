import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by nguyenhuuphu on 16-Mar-17.
 */
public class MySSHClient {

    public static void main(String[] args) {
        MaHoa rsa=new MaHoa(1024);
        Socket socketClient=null;
        BufferedWriter os=null;
        BufferedReader is=null;
        Scanner scanner = new Scanner(System.in);       //Nhận lệnh từ bàn phím
        try {
            socketClient = new Socket("localhost", 8080);
            os = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            is = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            System.out.println(is.readLine());
            System.out.println(ThucHienLenh.help());
            Thread.sleep(1000);
            if(!socketClient.isBound()){
                System.exit(0);
                System.out.println();
            }
        }catch (UnknownHostException e1){
            System.out.println("Kết nối thất bại. Not found 404!");

        }catch(IOException e){
            System.out.println("Kết nối thất bại. Not found 404!");
            System.exit(0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try{
            String lenh=null;
            String response = "";
            while(true) {
                    for(int i=0;i<1000;i++) {
                        System.out.println("Nhap lenh:");
                        lenh = scanner.nextLine();
                        String maHoaLenh=rsa.encrypt((new BigInteger(lenh.getBytes()))).toString(); //Mã hóa lệnh trước khi gửi đi
                        os.write(maHoaLenh);
                        os.newLine();
                        os.flush();
                        Lenh lenhDownload=ThucHienLenh.phanTichLenh(lenh);
                        /*
                            Client xử lý khi lệnh download được nhập từ bàn phím
                         */
                        if(lenhDownload.getCommand().equals("download")) {
                            String dinhDang = is.readLine();
                            if(dinhDang.equals("Đề nghị đăng nhập")){
                                System.out.println("Bạn chưa đăng nhâp");
                                continue;
                            }
                            if (dinhDang.equals("FileKhongTonTai")) {
                                System.out.println("File không tồn tại");
                                continue;
                            }
                            if (dinhDang.equals("FileMax")) {
                                for (int tempp = 0; tempp < 3; tempp++) {
                                    new MySSHServer.ReceiveThread(lenhDownload.getArg1() + "." + tempp, tempp + 1).start();
                                }
                                Thread.sleep(5000);
                                ThucHienLenh.merrgeFile(lenhDownload.getArg1());
                                continue;
                            }
                            if(dinhDang.equals("FileMin")){
                                new MySSHServer.ReceiveThread(lenhDownload.getArg1(),1).start();
                                Thread.sleep(2000);
                                continue;
                            }
                        }
                        /*
                                Client thực hiện 1 số xử lý khi lệnh upload được gõ từ bàn phím
                         */
                        if(lenhDownload.getCommand().equals("upload")) {
                            String dinhDang = is.readLine();
                            if (dinhDang.equals("Đề nghị đăng nhập")) {
                                System.out.println("Bạn chưa đăng nhâp");
                                continue;
                            } else {
                                if (dinhDang.equals("Yes")) {
                                    File fileUpload = new File(lenhDownload.getArg1());
                                    /*
                                    Kiểm tra sự tồn tại của file trước khi upload lên server
                                     */
                                    if (fileUpload.exists() && fileUpload.isFile()) {
                                        os.write("Yes");
                                        os.newLine();
                                        os.flush();
                                        try {
                                            ThucHienLenh.guiFile(lenhDownload.getArg1(), 1); //thực hiên upload
                                        } catch (Exception e) {
                                            System.out.println("Có lỗi xảy ra");
                                            continue;
                                        }
                                        continue;
                                    }else{
                                        System.out.println("File không tồn tại");
                                        os.write("No");
                                        os.newLine();
                                        os.flush();
                                        continue;
                                    }
                                }
                            }
                        }
                      System.out.println(is.readLine());
                        if(lenh.equals("exit")){
                            os.write(rsa.encrypt1("exit"));
                            os.newLine();
                            os.flush();
                            is.close();
                            os.close();
                            System.exit(1);
                        }
                    }
            }
        }catch (IOException e2) {
            e2.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
