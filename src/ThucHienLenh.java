import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created by nguyenhuuphu on 16-Mar-17.
 */
public class ThucHienLenh {


    /*
    Hiển thị thư mục hiện thời
     */
    public static String showDirectory() {
        String currentDir=null;
        try {
            String current = new java.io.File(".").getCanonicalPath();
             currentDir = System.getProperty("user.dir");

        }catch(IOException e){
            e.printStackTrace();
        }
        return currentDir;
    }
    /*
    Hiện thị danh dách file trong thư mục hiện thời
     */
    public static ArrayList<String> showFile(){
        ArrayList<String>danhSachFile=new ArrayList<String>();
        File dir = new File(".");
        File[] filesList = dir.listFiles();
        for (File file : filesList) {
            if (file.isFile()) {
                danhSachFile.add(file.getName());
            }
        }
        return danhSachFile;
    }


     /*
     Hiển thị danh sách các thư mục trong thư mục hiện hành.
      */
    public static String showDirectoryInDirectory(){
        File file = new File(".");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        return Arrays.toString(directories);
    }




    /*
    Hiển thị thời gian hệ thống
     */
    public static String showDateTime(){
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return sdf.format(date);
    }


    /*
    Xóa file nếu tồn tại ở thư mục hiện tại
     */
    public static boolean deleteFile(String filename){
        boolean t;
        File file=new File(filename);
        t=file.delete();
        return t;
    }



    /*
    Tạo 1 file ở thư mục hiện tại
     */
    public static boolean createFile(String filename){
        try {
            File file = new File(filename);
            file.createNewFile();
        }catch(IOException e){
            e.printStackTrace();
        }
        return true;
    }


    /*
    Di chuyển file đến 1 thư mục khác
     */
    public static boolean moveFile(String filename,String directory){
        File file=new File(filename);
        boolean t =file.renameTo(new File(directory+"\\"+file.getName()));
        return t;

    }
    /*
    Tạo thư mục
     */
    public static  boolean createDirectory(String namedict){
        boolean t;
        File directory=new File(namedict);
        t=directory.mkdir();
        return t;
    }
    /*
    Xóa toàn bộ nội dung thư mục hiện thời
     */
    public static boolean deleteDirectory(String directory){
        File myFile=new File(directory);
        if(myFile.exists()&&myFile.isDirectory()) {
            String[] array = myFile.list();
            for (String s : array) {
                File file = new File(myFile.getPath(), s);
                file.delete();
            }
            return myFile.delete();
        }else{
            return false;
        }


    }
    /*
    Di chuyển thư mục
     */
    public static boolean moveDirectory(File sourceFile, File destFile)
    {
        if (sourceFile.isDirectory())
        {
            for (File file : sourceFile.listFiles())
            {
                moveDirectory(file, new File(destFile.getPath(),file.getName()));
            }
            return true;
        }
        else
        {
            try {
                Files.move(Paths.get(sourceFile.getPath()), Paths.get(destFile.getPath()), StandardCopyOption.REPLACE_EXISTING);
                return true;
            } catch (IOException e) {
                return false;
            }
        }

    }
    /*
            Hiển thị các lệnh mà hệ thống hỗ trợ
     */
    public static String help(){
        String help="login -username -password:\t\t\t\tĐăng nhập\nshow : \t\t\t\tHiển thị thư mục hiện hành.\n" +
                "ls: \t\t\t\tHiển thị danh sách tệp trong thư mục hiện tại\n" +
                "showdir: \t\t\tHiện thị danh sách thư mục trong thư mục hiện hành.\n" +
                "time: \t\t\t\tHiển thị thời gian hiện tại của hệ thống.\n" +
                "delete -filename : \t\tXóa file\n" +
                "create -filename : \t\tTạo file\n" +
                "move -filename -directory : \tDi chuyển 1 file đến thư mục khác.\n" +
                "createdir -directory: \t\tTạo thư mục mới\n" +
                "deletedir -directory: \t\tXóa thư mục.\n" +
                "movedir -source -dest: \t\tDi chuyển toàn bộ file sang thư mục khác.\n" +
                "download --dest --source:\t\tDownload 1 file source và lưu với tên file dest\n"+
                "upload --source --dest:\t\tUpload 1 file từ source lên server và lưu với tên file dest\n"+
                "help:\t\t\t\tHiển thị các lệnh.";
        return help;
    }
    /*
                Kiểm tra đăng nhập
     */
    public static boolean login(String username,String password){
            ArrayList<String>listuser=new ArrayList<String>();
            ArrayList<String>listpass=new ArrayList<String>();
        try {

            FileInputStream fis = new FileInputStream("login.txt");
            DataInputStream dis = new DataInputStream(fis);
            String line=null;
            while((line=dis.readLine())!=null) {
                listuser.add(line);
                listpass.add(dis.readLine());
            }
            fis.close();
            dis.close();



        } catch (IOException ex) {
            ex.printStackTrace();
        }
       String temp=username+password;
        for(int i=0;i<listuser.size();i++){
            if(temp.equals(listuser.get(i)+listpass.get(i))){
                return true;
            }
        }
        return false;
    }
    /*
                Phân tích lệnh từ 1 chuỗi đầu vào
     */
    public static Lenh phanTichLenh(String t){
        Lenh lenh=new Lenh();
        int i,j;
        for(i=0;i<t.length();i++){
            if(t.charAt(i)==' '||i==t.length()-1){
                lenh.setCommand(t.substring(0,i));

                if(i==t.length()-1){
                    lenh.setCommand(t);
                    lenh.setArg1(null);
                    lenh.setArg2(null);
                    return lenh;
                }
                break;
            }
        }
        for(j=i+1;j<t.length();j++){
            if(t.charAt(j)==' '||j==t.length()-1){
                lenh.setArg1(t.substring(i+1,j));
                if(j==t.length()-1){
                    lenh.setArg1(t.substring(i+1,j+1));
                    lenh.setArg2(null);
                    return lenh;
                }
                lenh.setArg2(t.substring(j+1,t.length()));
                break;
            }
        }
        return lenh;
    }
    /*
            Cắt 1 File thành nhiều File nhỏ
     */
    public static boolean splitFile(String filenameSource,String destFile,int numberSplit){

            File file=new File(filenameSource);
            if(file.exists()&&file.isFile()){
                try {
                    InputStream is = new FileInputStream(file);
                    long sizefile = file.length();
                    long sizefileSplit = sizefile / numberSplit;
                    byte[] arr = new byte[1024];
                    for (int i = 0; i < numberSplit; i++) {
                        OutputStream os = new FileOutputStream(destFile+'.' + i);
                        int j = 0;
                        int a = 0;
                        while ((j = is.read(arr)) != -1) {
                            os.write(arr, 0, j);
                            a += j;
                            if (a > sizefileSplit) {
                                break;
                            }
                        }
                        os.flush();
                        os.close();
                    }
                    is.close();
                    return true;
                }catch (IOException e){
                    e.printStackTrace();
                    return false;
                }
            }else{

                return false;

            }




    }
    /*
        Ghép các File nhỏ thành 1 file
     */
    public static boolean merrgeFile(String filenameSource) {
        try {


            OutputStream os = new FileOutputStream(filenameSource);
            int count=0;
            byte [] arr=new byte[1024];
            while(true){
                File file=new File(filenameSource+"."+count);
                if(file.exists()&&file.isFile()){
                    InputStream is=new FileInputStream(file);
                    int j=0;
                    while( (j=is.read(arr))!=-1){
                        os.write(arr,0,j);
                    }
                    os.flush();
                    is.close();
                    ThucHienLenh.deleteFile(file.getName());
                    count++;
                }else{
                    break;
                }
            }
            os.close();
            return false;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }

    }
    /*
            Gửi 1 file thông qua 1 Socket
     */
    public static void guiFile(String filename ,int port) throws IOException {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            servsock = new ServerSocket(port+3000);
            while (true) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);
                    // send file
                    File myFile = new File (filename);
                    byte [] mybytearray  = new byte [(int)myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray,0,mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray,0,mybytearray.length);
                    os.flush();
                    System.out.println("Done.");
                    break;
                }
                finally {
                    if(fis !=null) fis.close();
                    if (bis != null) bis.close();
                    if (os != null) os.close();
                    if (sock!=null) sock.close();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (servsock != null) servsock.close();
        }
    }
    /*
        Nhận 1 File thông qua Socket
     */
    public static void nhanFile(String filename,int port){
        int bytesRead;
        int current = 0;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket("localhost", port+3000);
            System.out.println("Connecting...");

            // receive file
            byte [] mybytearray  = new byte [1000000];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(filename);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(mybytearray, current, (mybytearray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println("File " + filename
                    + " downloaded (" + current + " bytes read)");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bos != null) try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sock != null) try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}
