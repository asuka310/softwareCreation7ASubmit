package sample;
import java.util.*;

/*
*カレンダーを使う上で必要なのは曜日と日にちの対応
* そのためのクラス
*/
public class MyCalendar {
    int calendar_year;
    int calendar_month;
    Calendar cal_default =Calendar.getInstance();
    Calendar rest_label=Calendar.getInstance();
    Integer[] calendar_day_set=new Integer[42];

    //現在の日時を取得しカレンダーのデータをセットするコンストラクタ
    //ラベル表示のためyear,monthの設定をしている
    MyCalendar(int i){
        calendar_year=cal_default.get(Calendar.YEAR);
        calendar_month=cal_default.get(Calendar.MONTH)+1+i;
        //cal_default.set(Calendar.YEAR,)
        cal_default.set(Calendar.MONTH,calendar_month-1);
        cal_default.set(Calendar.DATE,1);
        //rest_label.set(Calendar.MONTH,cal_default.get(Calendar.MONTH)-1);
        rest_label.set(Calendar.MONTH,calendar_month-2);
        for(int day=0;day<42;day++)calendar_day_set[day]=0;
        CalendarDataSet();
    }

    /*
    コンストラクタでカレンダーをその月の最初に設定
    その月の一日の曜日を取得
    0が日曜日で6が金曜日としている。
     */
    int Month_First_Day(){
        return cal_default.get(Calendar.DAY_OF_WEEK);
    }

    /*
     *
     */

    /*
    *0～41個の曜日に割り当てる
    * Calendarモジュールでは日曜日を1と割り当て
    * 配列なので0-indexである。そのためすべて-1して考える
    * 以上によりMonth_First_Dayで取得した日にちをoffsetとして
    * カレンダーのデザイン配置を決めるデータを取得する。
    */
    void CalendarDataSet(){
        int MonthFirst=Month_First_Day();
        calendar_day_set[MonthFirst-1]=1;
        //一番上の行の前の月の日にちを設定
        //真ん中は月の日にちの設定
        //最後の行は次の月の日にち
        for(int day=MonthFirst-2;day>=0;day--)
            calendar_day_set[day]=rest_label.getActualMaximum(Calendar.DAY_OF_MONTH)-(MonthFirst-2-day);
        for(int day=2;day<=cal_default.getActualMaximum(Calendar.DAY_OF_MONTH);day++)
            calendar_day_set[MonthFirst-1+(day-1)]=day;
        for(int day=cal_default.getActualMaximum(Calendar.DAY_OF_MONTH)+1;MonthFirst-1+(day-1)<42;day++)
            calendar_day_set[MonthFirst-1+(day-1)]=day-cal_default.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
