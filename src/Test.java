import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by nguyenhuuphu on 16-Mar-17.
 */
public class Test {

    public static void main(String[] args) {
        try{
        String current = new java.io.File( "." ).getCanonicalPath();
        System.out.println("Current dir:"+current);
        String currentDir = System.getProperty("user.dir");
        System.out.println("Current dir using System:" +currentDir);
            File dir = new File(".");
            File[] filesList = dir.listFiles();
            for (File file : filesList) {
                if (file.isFile()) {
                    System.out.println("File:"+file.getName());
                }
            }
    }catch(IOException e){
            e.printStackTrace();
        }


        String time=ThucHienLenh.showDateTime();
        System.out.println(time);
        File file = new File(".");
        String[] directories = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        System.out.println(Arrays.toString(directories));
        String root=ThucHienLenh.showDirectory();
        try {
            //Bước 1: Tạo đối tượng luồng và liên kết nguồn dữ liệu
            FileInputStream fis = new FileInputStream("login.txt");
            DataInputStream dis = new DataInputStream(fis);

            //Bước 2: Đọc dữ liệu
            String n = dis.readLine();
            String m=dis.readLine();

            //Bước 3: Đóng luồng
            fis.close();
            dis.close();

            //Hiển thị nội dung đọc từ file
            System.out.println("Số nguyên: " + n);
            System.out.println("Số thực: " + m);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        ThucHienLenh.login("fd","fd");
        System.out.println(ThucHienLenh.login("test2","123"));
        String t="login";
        Lenh lenh=ThucHienLenh.phanTichLenh(t);
//        System.out.println(lenh.getCommand()+"#"+lenh.getCommand().length());
//        System.out.println(lenh.getArg1()+"#"+lenh.getArg1().length());
//        System.out.println(lenh.getArg2()+"#"+lenh.getArg2().length());
        System.out.println("sos tham số:"+lenh.numArg());
        boolean t1 =ThucHienLenh.moveFile("login.txt","test");
        System.out.println(t1);




    }

}
