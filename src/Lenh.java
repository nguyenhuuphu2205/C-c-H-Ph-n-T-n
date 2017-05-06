/**
 * Created by trananh bacninh on 18-Mar-17.
 */
public class Lenh {
    private String command;
    private String arg1;
    private String arg2;
    public Lenh(String command, String arg1, String arg2) {
        this.command = command;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Lenh() {
        command=null;
        arg1=null;
        arg2=null;
    }

    public String getCommand() {

        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
    }
    public int numArg(){
        if(arg1==null&&arg2==null){
            return 1;
        }else{
            if(arg1!=null&&arg2==null){
                return 2;
            }else
                return 3;
        }
    }
}
