package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import java.awt.*;
import java.lang.String;

public class TimetableViewController {
   public GridPane timetable;
   public GridPane timetable_period;
   private String[] dayofweek={"月","火","水","木","金","土"};
   public Label[] daylabel=new Label[6];
   public Label[] dayperiod =new Label[6];
   public Button[] button_timetable=new Button[36];
   public ListView[] listview_timetable=new ListView[36];

   @FXML
   void initialize(){
      dayofweekSet();
      dayperiodSet();
      buttonSet();
      //listsetting();
      System.out.println("カス");
   }

   void dayofweekSet(){
      for(int day=0;day < 6;day++) {
         daylabel[day] = new Label(dayofweek[day]);
         GridPane.setConstraints(daylabel[day], day, 0);
         timetable.getChildren().add(daylabel[day]);
      }
   }

   void dayperiodSet(){
      for(int day=0;day<6;day++){
         dayperiod[day]=new Label(String.valueOf(day+1));
         GridPane.setConstraints(dayperiod[day],0,day);
         timetable_period.getChildren().add(dayperiod[day]);
      }
   }

   void buttonSet(){
      for(int period =0;period<36;period++){
         button_timetable[period]=new Button();
         button_timetable[period].setPrefHeight(50);
         button_timetable[period].setPrefWidth(70);
         button_timetable[period].setOpacity(0.5);
         GridPane.setConstraints(button_timetable[period],period%6,period/6+1);
         button_timetable[period].setOnMouseClicked(event->tablebuttonClicked());
         timetable.getChildren().add(button_timetable[period]);
      }
   }

   void listsetting(){
      for(int period =0;period<36;period++){
         listview_timetable[period]=new ListView<>();
         listview_timetable[period].setPrefHeight(50);
         listview_timetable[period].setPrefWidth(70);
         listview_timetable[period].setOpacity(0);
         GridPane.setConstraints( listview_timetable[period],period%6,period/6+1);
         listview_timetable[period].setOnMouseClicked(event->tablebuttonClicked());
         timetable.getChildren().add( listview_timetable[period]);
      }
   }

   void tablebuttonClicked(){
      RegisterSubjectView rsv = new RegisterSubjectView();
      System.out.println("ok");
      rsv.show();
   }


}
