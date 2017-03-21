import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
            System.out.println("Current dir:" + current);
             currentDir = System.getProperty("user.dir");
            System.out.println("Current dir using System:" + currentDir);

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
                System.out.println("File:"+file.getName());
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
        System.out.println(Arrays.toString(directories));
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
        String []array=myFile.list();
        for(String s:array){
            File file=new File(myFile.getPath(),s);
            file.delete();
        }
        return myFile.delete();



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
        return true;
    }
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

}
