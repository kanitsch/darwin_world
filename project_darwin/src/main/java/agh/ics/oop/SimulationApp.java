package agh.ics.oop;

import agh.ics.oop.presenter.AnimalsList;
import agh.ics.oop.presenter.MenuPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SimulationApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("menu.fxml"));
        GridPane viewRoot = loader.load();
        MenuPresenter presenter = loader.getController();

        stage.setTitle("Darwin World - Menu");
        stage.setMaximized(true);
        stage.setScene(new Scene(viewRoot));
        stage.show();
    }

    private void configureStage(Stage primaryStage, BorderPane viewRoot) {
        var scene = new Scene(viewRoot);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simulation app");
        primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
        primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
    }

    public Stage startAnimalsList(AnimalsList presenter) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Stage newWindow = new Stage();
        newWindow.setTitle("Choose animal");
        loader.setController(presenter);
        loader.setLocation(getClass().getClassLoader().getResource("animalsWindow.fxml"));
        newWindow.setScene(new Scene(loader.load()));
        newWindow.show();

        return newWindow;
    }
}