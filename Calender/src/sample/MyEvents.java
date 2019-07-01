package sample;


import java.io.Serializable;
import java.util.Calendar;

/**予定を管理するクラス
 * 開始時刻と終了時刻、 予定の内容、
 * 場所、 カテゴリなど、の情報を持たせると良きと思われ
 *
 * 予定を追加する際にこのクラスのコンストラクタを作って(あとで作る)作成して
 * 管理やサーバーに送るなどしたら良さそうかなぁ...
 * */

public class MyEvents implements Serializable {
    String title;

    Calendar s_time;
    Calendar e_time;

    String place = null;
    String detail = null;

    public MyEvents(){}

    public MyEvents(String t, Calendar s, Calendar e, String p, String d){
        this.title = t;
        this.s_time = s;
        this.e_time = e;
        this.place = p;
        this.detail = d;
    }

    @Override
    public String toString() {
        return "\tタイトル : " + title + "\n\t開始時間 : " + s_time.getTime() + "\n\t終了時間 : " + e_time.getTime() + "\n\t場所 : " + place + "\n\t詳細 : " + detail;
    }

    public String getTitle() {
        return title;
    }

    public Calendar getS_time() {
        return s_time;
    }

    public Calendar getE_time() {
        return e_time;
    }

    public String getPlace() {
        return place;
    }

    public String getDetail() {
        return detail;
    }

}
