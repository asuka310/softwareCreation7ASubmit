package sample;

import com.sun.javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server_client.client.ClientSocketData;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;

public class CalenderGraphics {
    MyCalendar setting;
    CalendarCall[] button_day = new CalendarCall[42];

    MyDateInfo[] myDateInfo = new MyDateInfo[42];
    Label[] DayOfWeek=new Label[7];
    String[] weekday={"Sun","Mon","Tue","Wed","Thur","Fri","Sat"};

    GridPane calenderGrid;
    int year, month; // yearもmonthも現世のもの
    Calendar tableCalender;
    Controller ct;
    //CheckMyProfileController cmpct;

    Stage stage;

    //Label label1;

    public CalenderGraphics(Controller controller, Stage st){
        ct = controller;
        stage = st;
        //cmpct = cmpcontroller;
        calenderGrid = ct.getCalendar_grid();// = new GridPane();
        //calenderGrid.setStyle("-fx-background-color: white;");
        calenderGrid.setPrefWidth(840);

        tableCalender = Calendar.getInstance();

        this.year = tableCalender.get(Calendar.YEAR);
        this.month = tableCalender.get(Calendar.MONTH) + 1;

        System.out.println("month : " + month);

        buttonSetting(calenderGrid);
        weekdaySetting(calenderGrid);

        //ct.getYearselect().setValue(String.valueOf(tableCalender.get(Calendar.YEAR)));
        //ct.getMonthselect().setValue(tableCalender.get(Calendar.MONTH)+1);
        //System.out.println("show" + ct.getYearselect().getOnShown());
    }


    void buttonSetting(GridPane grid){
        for(int day=0;day<42;day++){
            button_day[day] = new CalendarCall();
            final int d = day;
            button_day[day].getDay_button().setOnMouseClicked(event -> dayCallClicked(event, d));
        }
        for(int day=0;day<42;day++){
            GridPane.setConstraints(button_day[day],day%7,1+day/7);
        }
        for(int day=0;day<42;day++){
            button_day[day].setPrefWidth(120);
            button_day[day].setPrefHeight(80);
            button_day[day].getDay_label().setFont(new Font(20));
            if(day%7==6) button_day[day].getColor_base().setFill(Paint.valueOf("#8EB8FF"));
            if(day%7==0) button_day[day].getColor_base().setFill(Paint.valueOf("#FF97C2"));
        }
        for(int day=0;day<42;day++)grid.getChildren().add(button_day[day]);

        ct.getWarptime().setOnMouseClicked(event -> warpTime());
        ct.getLogin_button().setOnMouseClicked(event -> mouse1Click(event));
        ct.getNext_month_button().setOnMouseClicked(event -> moveMonth(1));
        ct.getBack_month_button().setOnMouseClicked(event -> moveMonth(-1));
        ct.getSend_file_button().setOnMouseClicked(event -> sendFileToServer());
        ct.getReceive_button().setOnMouseClicked(event -> getFileFromServer());
        ct.getTimetable_button().setOnMouseClicked(event -> timetableClick());
        ct.getProfile_button().setOnMouseClicked(event -> profileClick());
        ct.getGoToTodayButton().setOnMouseClicked(event -> goToTodayButtonClick());

    }

    void weekdaySetting(GridPane grid){
        for(int day=0;day<7;day++)DayOfWeek[day]=new Label(weekday[day]);
        for(int day=0;day<7;day++){
            GridPane.setConstraints(DayOfWeek[day],day,0);
            DayOfWeek[day].setPrefWidth(100);
            DayOfWeek[day].setPrefHeight(80);
            DayOfWeek[day].setFont(new Font(20));
            DayOfWeek[day].setStyle("-fx-background-color: transparent");
            if(day%7==6) DayOfWeek[day].setTextFill(Color.BLUE);
            if(day%7==0) DayOfWeek[day].setTextFill(Color.RED);
            DayOfWeek[day].setAlignment(Pos.BOTTOM_CENTER);
            DayOfWeek[day].setPadding(new Insets(0, 0, 10, 0));
            grid.getChildren().add(DayOfWeek[day]);
        }
    }

    /**以下においてmyDateInfoに対してのデータの作成を行う*/
    void createDateInfo(int year, int month){
        //cal_year_month.setText(String.valueOf(year) + "年" + String.valueOf(month) + "月");

        for(int i = 0; i < 42; i++)
            myDateInfo[i] = new MyDateInfo(); // 初期化

        int calenderClassMonth = month - 1;

        Calendar cal = (Calendar) tableCalender.clone(); //カレンダーのインスタンスを取得
        int lastDate = cal.getActualMaximum(Calendar.DATE); //この月の日数を取得
        cal.set(year, calenderClassMonth, 1);//この月の初日に設定
        int offsetWeek = 1 - cal.get(Calendar.DAY_OF_WEEK);//この月の初日の曜日を取得し日曜日との差を得る
        cal.add(Calendar.DATE, offsetWeek);//カレンダーの左上に当たる日にちを取得
        int index = 0;
        for(int i = offsetWeek; i < 0; i++) { // 前の月分
            myDateInfo[index].day = (Calendar) cal.clone(); //クローン
            myDateInfo[index++].isExtra = true;
            cal.add(Calendar.DATE, 1); //一日加算
        }
        for(int i = 1; i <= lastDate; i++){ // 今月分
            myDateInfo[index++].day = (Calendar) cal.clone(); //クローン
            cal.add(Calendar.DATE, 1); //一日加算
        }
        while(index < 42){ // 来月分
            myDateInfo[index].day = (Calendar) cal.clone(); //クローン
            myDateInfo[index++].isExtra = true;
            cal.add(Calendar.DATE, 1); //一日加算
        }
    }

    public GridPane getGrid_BackOrNextOneMonth(int delta){
        tableCalender.add(Calendar.MONTH, delta); //delta分加減算
        System.out.println(tableCalender.getTime());
        System.out.println(year + " : " + month);

        updateGrid();
        //ct.getYearselect().getSelectionModel().select(year - 1); // (year - 1);
        /*ComboBoxListViewSkin<?> skin = (ComboBoxListViewSkin<?>)ct.getYearselect(). .getSkin();
        System.out.println(ct.getYearselect());
        System.out.println(skin);
        if(skin != null)
            ((ListView<?>)skin.getPopupContent()).scrollTo(year - 1);
        */

        return calenderGrid;
    }

    private void updateGrid(){
        this.year = tableCalender.get(Calendar.YEAR);
        this.month = tableCalender.get(Calendar.MONTH) + 1;

        createDateInfo(year, month);
        updateButton();
        ct.getYearselect().getSelectionModel().select(year - 1); // (year - 1);
        ct.getYearselect().setOnShown(event -> ((ComboBoxListViewSkin)ct.getYearselect().getSkin()).getListView().scrollTo(ct.getYearselect().getSelectionModel().getSelectedIndex()));
        ct.getMonthselect().setValue(month);
        ct.getMonthselect().setOnShown(event -> ((ComboBoxListViewSkin)ct.getMonthselect().getSkin()).getListView().scrollTo(ct.getMonthselect().getSelectionModel().getSelectedIndex()));
    }

    public void updateButton(){
        // カレンダーの情報をサーバから得る
        HashMap<String, ArrayList<MyEvents>> receiveEvents = new HashMap<String, ArrayList<MyEvents>>();
        if(ClientSocketData.getInstance().isFirstSetuped()) {
            try {
                ArrayList<String> l = new ArrayList<String>();
                l.add("getEventOfMonth");
                l.add(String.valueOf(this.year));
                l.add(String.valueOf(this.month));
                ClientSocketData.getInstance().sendObject(l);

                receiveEvents = (HashMap<String, ArrayList<MyEvents>>) ClientSocketData.getInstance().getObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        Calendar today = Calendar.getInstance();//本日の日付を得る
        for(int day=0;day<42;day++){
            button_day[day].getDay_label().setText(String.valueOf(myDateInfo[day].getDate()));

            button_day[day].getColor_base().setFill(Paint.valueOf("white"));//とりあえず白に


            if(myDateInfo[day].getDay().get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                    myDateInfo[day].getDay().get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                    myDateInfo[day].getDay().get(Calendar.DATE) == today.get(Calendar.DATE))
                button_day[day].getColor_base().setFill(Paint.valueOf("#FFFF88"));//本日の場所の色を変える

            if(myDateInfo[day].isExtra) { // 月外の日
                button_day[day].setDisable(true); // ボタンを押せなくする
                if(day%7==6) button_day[day].getColor_base().setFill(Paint.valueOf("#D9E5FF")); // 土曜の色を薄く
                if(day%7==0) button_day[day].getColor_base().setFill(Paint.valueOf("#FFD5EC")); // 日曜の色を薄く
            }
            else {
                button_day[day].setDisable(false);

                button_day[day].roadDayList(myDateInfo[day].getEvents()); // 予定リストの読み込み

                if(day%7==6) button_day[day].getColor_base().setFill(Paint.valueOf("#8EB8FF")); // 土曜の色を戻す
                if(day%7==0) button_day[day].getColor_base().setFill(Paint.valueOf("#FF97C2")); // 日曜の色を戻す
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String key = sdf.format(myDateInfo[day].getDay().getTime());
            if(receiveEvents.containsKey(key)){
                for(MyEvents e : receiveEvents.get(key))
                    myDateInfo[day].getEvents().add(e);
                button_day[day].roadDayList(myDateInfo[day].getEvents());
            }

        }
    }


    private void dayCallClicked(MouseEvent event, int gridnum){
        DayPlanView dpv = new DayPlanView(myDateInfo[gridnum]);
        dpv.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == true && newValue == false) {
                button_day[gridnum].roadDayList(myDateInfo[gridnum].getEvents()); // 予定リストの読み込み
                System.out.println("load day list");
            }
        });
        dpv.show();

        System.out.println("gridnum :: " + gridnum);
        System.out.println("myDataInfo :: " + myDateInfo[gridnum]);
        System.out.println("myDataInfo :: " + myDateInfo[gridnum].getDate());


    }

    private void moveMonth(int delta){
        calenderGrid = getGrid_BackOrNextOneMonth(delta);
    }

    private void sendFileToServer(){
        //単一ファイルを選択
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle( "ファイル選択" );
        File f = fileChooser.showOpenDialog( stage );
        try {
            byte[] data = Files.readAllBytes(f.toPath());
            ArrayList<String> l = new ArrayList<String>();
            l.add("registerFile");
            l.add(f.getName());
            l.add(String.valueOf(f.length()));
            ClientSocketData.getInstance().sendObject(l);

            ClientSocketData.getInstance().sendObject(data);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void getFileFromServer(){
        FileReceive fr = new FileReceive();
        fr.show();
    }

    private void mouse1Click(MouseEvent e){
        if(!ClientSocketData.getInstance().isLogin()) {
            NewStageTest2 nst2 = new NewStageTest2(stage, ct);
            nst2.showingProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue == true && newValue == false) {
                    if (ClientSocketData.getInstance().isLogin()) {
                        //TODO 表示の処理
                        Alert alrt = new Alert(Alert.AlertType.NONE, "ログインが完了しました。", ButtonType.OK); //アラートを作成
                        alrt.setHeaderText(null);
                        alrt.showAndWait(); //表示
                        ct.getLogin_button().setText("ログアウト");
                        return;
                    }
                }
            });
            nst2.show();
        }else{
            //TODO ログアウト処理
            Alert alrt = new Alert(Alert.AlertType.NONE, "ログアウトが完了しました。", ButtonType.OK); //アラートを作成
            alrt.setHeaderText(null);
            alrt.showAndWait(); //表示
            ct.getLogin_button().setText("ログイン");
            ct.getDisplayname().setText("ようこそ、 ゲスト さん！");
            String user = ClientSocketData.getInstance().getUserName();
            ClientSocketData.getInstance().setUserName(null);
            ClientSocketData.getInstance().setProfile(new Profile());
            ClientSocketData.getInstance().setLogin(false); // ログインしてない
            // TODO サーバにログアウトを送信
            try {
                ArrayList<String> l = new ArrayList<String>();
                l.add("logoutUser");
                l.add(InetAddress.getLocalHost().getHostName());
                l.add(user);
                ClientSocketData.getInstance().sendObject(l);
            }catch (UnknownHostException ex){
                ex.printStackTrace();
            }
        }
    }

    private void warpTime(){
        tableCalender.set(Calendar.YEAR ,Integer.parseInt(ct.getYearselect().getValue())); // 年の反映
        tableCalender.set(Calendar.MONTH, ct.getMonthselect().getValue() - 1); // 月の反映
        System.out.println("jump to :: " + tableCalender.getTime());
        updateGrid(); // カレンダーの更新
    }

    private void profileClick(){
        CheckMyProfile profileview=new CheckMyProfile(); //行先変更
        profileview.show();
    }

    private void timetableClick(){
        TimetableView timetableview =new TimetableView();
        timetableview.show();
    }

    private void goToTodayButtonClick(){
        Calendar today = Calendar.getInstance();
        tableCalender.set(Calendar.YEAR ,today.get(Calendar.YEAR)); // 年の反映
        tableCalender.set(Calendar.MONTH, today.get(Calendar.MONTH)); // 月の反映
        System.out.println("jump to :: " + tableCalender.getTime());
        updateGrid(); // カレンダーの更新
        ct.getYearselect().setValue(String.valueOf(year));
        ct.getMonthselect().setValue(month);
    }

}
