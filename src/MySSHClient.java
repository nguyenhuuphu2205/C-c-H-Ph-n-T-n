import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by nguyenhuuphu on 16-Mar-17.
 */
public class MySSHClient {
    public static void main(String[] args) {
        Socket socketClient=null;
        BufferedWriter os=null;
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
            e.printStackTrace();
        }
        try{
            String lenh=null;
            String response = "";
            while(true) {
                    for(int i=0;i<1000;i++) {
                        System.out.println("Nhap lenh:");

                        lenh = scanner.nextLine();

                        os.write(lenh);
                        os.newLine();
                        os.flush();
                       System.out.println(is.readLine());
                        if(lenh.equals("exit")){
                            os.write("exit");
                            os.newLine();
                            os.flush();

                            is.close();
                            os.close();

                            System.exit(1);

                        }

                    }

                    while ((response = is.readLine()) != null) {
                        System.out.println("Server response:" + response);
                        if(lenh.equals("EXIT")){
                            response = is.readLine();
                            System.out.println("Server response:" + response);
                            System.exit(1);
                        }

                        if (response.indexOf("OK") != -1) {
                            System.exit(1);
                            os.close();
                            is.close();

                        }

                    }



            }



        }catch (IOException e2){
            e2.printStackTrace();
        }


    }
}
