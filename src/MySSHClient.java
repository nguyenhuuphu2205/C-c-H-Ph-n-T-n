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
        //BufferedReader is=null;
        BufferedReader is=null;
        Scanner scanner = new Scanner(System.in);
        try {
            socketClient = new Socket("localhost", 8080);
            os = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
           is = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            System.out.println(is.readLine());
            System.out.println(ThucHienLenh.help());
        }catch (UnknownHostException e1){
            System.out.println("Kết nối thất bại. Not found 404!");

        }catch(IOException e){
            System.out.println("Kết nối thất bại. Not found 404!");
            System.exit(0);

        }
        try{
            String lenh=null;
            String response = "";
            InputStream is1=socketClient.getInputStream();
            OutputStream os1 = socketClient.getOutputStream();
            while(true) {
                    for(int i=0;i<1000;i++) {
                        System.out.println("Nhap lenh:");

                        lenh = scanner.nextLine();

                        String maHoaLenh=rsa.encrypt((new BigInteger(lenh.getBytes()))).toString();
                        os.write(maHoaLenh);
                        os.newLine();
                        os.flush();
                        Lenh lenhDownload=ThucHienLenh.phanTichLenh(lenh);
                        if(lenhDownload.getCommand().equals("download")){
                            int current=0;
                            byte [] mybytearray  = new byte [10000000];
                            FileOutputStream fos = new FileOutputStream(lenhDownload.getArg1());
                            BufferedOutputStream bos = new BufferedOutputStream(fos);

//                            InputStream is1=socketClient.getInputStream();
                            int bytesRead = is1.read(mybytearray,0,mybytearray.length);

                             current = bytesRead;
                            do {
                                bytesRead =
                                        is1.read(mybytearray, current, (mybytearray.length-current));
                                if(bytesRead >= 0) current += bytesRead;
                            } while(bytesRead > -1);


                            bos.write(mybytearray, 0 , current);
                            bos.flush();
                            //delete(mybytearray);
                            System.out.println("File " + lenhDownload.getArg1()
                                    + " downloaded (" + current + " bytes read)");

//                             bos.close();
//                            fos.close();

                        //is1.close();
                            //socketClient = new Socket("localhost", 8080);
                            //os = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
                            //is = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));

                           continue;




                        }
                        if(lenhDownload.getCommand().equals("upload")){
                            File myFile = new File (lenhDownload.getArg1());
                            byte [] mybytearray  = new byte [(int)myFile.length()];
                            FileInputStream fis = new FileInputStream(myFile);
                            BufferedInputStream bis = new BufferedInputStream(fis);
                            bis.read(mybytearray,0,mybytearray.length);
//                            OutputStream os1 = socketClient.getOutputStream();
                            System.out.println("Sending " + "(" + mybytearray.length + " bytes)");
                            os1.write(mybytearray,0,mybytearray.length);
                            os1.flush();
                            os1.flush();
                            System.out.println("Done.");
                            bis.close();
                            fis.close();
                            os1.close();
                            System.out.println();
                            continue;
                        }
//                       is = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
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



        }catch (IOException e2){
            e2.printStackTrace();
        }


    }
}
