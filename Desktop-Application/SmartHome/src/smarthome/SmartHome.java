/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smarthome;

import insidefx.undecorator.Undecorator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author danny
 */
public class SmartHome extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.setTitle("Smart Home - Login");
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        Undecorator undecorator = new Undecorator(stage, (Region) root);
        undecorator.getStylesheets().add("skin/undecorator.css");
        //undecorator.getStylesheets().add("@LoginCSS.css");
        Scene scene = new Scene(undecorator);
        stage.setResizable(false);
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    

   
}
