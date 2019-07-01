package sample;

import javafx.scene.input.MouseEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**それぞれの日付ごとの情報を管理するクラス
 * 日にち、その日の予定、現在表示中の月に含まれているかなどを管理
 * ボタンを押してその日の詳細な予定を表示するときなどはリストeventsを渡して
 * 表示を行うと良きと思われ...*/

public class MyDateInfo implements Serializable {
    List<MyEvents> events = new ArrayList<MyEvents>();
    Calendar day;
    boolean isExtra = false;

    public MyDateInfo(){}   //コンストラクタ

    public MyDateInfo(Calendar c){  //コンストラクタ(カレンダークラス必須)
        this.day = c;
    }

    public int getDate(){   //日付を得るメソッド
        return day.get(Calendar.DATE);
    }

    public Calendar getDay() {
        return day;
    }

    public List<MyEvents> getEvents() {
        return events;
    }

    public void deleteEvents(MyEvents me){
        events.remove(events.indexOf(me));
    }
}
