package agh.ics.oop;

import agh.ics.oop.presenter.MenuPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SimulationApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("menu.fxml"));
        GridPane viewRoot = loader.load();
        MenuPresenter presenter = loader.getController();

        String[] vals = new String[16];
        vals[0] = "3";
        vals[1] = "300";
        vals[2] = "50";
        vals[3] = "50";
        vals[4] = "50";
        vals[5] = "false";
        vals[6] = "true";
        vals[7] = "200";
        vals[8] = "50";
        vals[9] = "0";
        vals[10] = "2";
        vals[11] = "15";
        vals[12] = "25";
        vals[13] = "5";
        vals[14] = "1";
        vals[15] = "300";
        presenter.setFields(vals);

        stage.setTitle("Darwin World - Menu");
        stage.setMaximized(true);
        stage.setScene(new Scene(viewRoot));
        stage.show();
    }

}