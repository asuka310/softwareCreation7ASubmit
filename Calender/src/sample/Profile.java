package sample;

import java.io.Serializable;
import java.util.ArrayList;

public class Profile implements Serializable {
    private ArrayList<String> able_list = new ArrayList<String>();
    private ArrayList<String> disable_list = new ArrayList<String>();
    private String nickname;
    private String gender;

    private String user_name;

    private boolean valid = false;

    public Profile(){}

    public Profile(String user_name, String nickname, String gender, ArrayList<String> able_list, ArrayList<String> disable_list){
        this.user_name = user_name;
        this.nickname = nickname;
        this.gender = gender;
        this.able_list = able_list;
        this.disable_list = disable_list;
        this.valid = true;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGender() {
        return gender;
    }

    public ArrayList<String> getAble_list() {
        return able_list;
    }

    public ArrayList<String> getDisable_list() {
        return disable_list;
    }

    public boolean isValid() {
        return valid;
    }

    @Override
    public String toString() {
        return "Profile\n\tusername : " + user_name + "\n\tnickname : " + nickname + "\n\tgender : " + gender + "\n\table_list : " + able_list + "\n\tdisable_list : " + disable_list;
    }
}
