/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smarthome;

import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import com.sun.speech.freetts.*;




/**
 * FXML Controller class
 *
 * @author danny
 */

 



public class LoginWindowController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Text allertTextBox;

    String user=null;
    static String passwd=null;
    String startupVoiceFlag="false";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        getPreference();
        
        EventHandler<ActionEvent> goAction = new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent event) {
                   loginOnClick(event);
                }
            };
        username.setText(user);
        password.setOnAction(goAction);
        if(startupVoiceFlag.equals("true")){
            welcomeGreeting();
        }
        
    }    
    
     @FXML
    private void loginOnClick(ActionEvent event) {
        String Usr=username.getText();
        String pswd=password.getText();
        
        
         
        if(Usr.equals(user) && pswd.equals(passwd) ){
         
                Stage stage = new Stage();
                stage.setTitle("Smart Home");
        Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Undecorator undecorator = new Undecorator(stage, (Region) root);
                undecorator.getStylesheets().add("skin/undecorator.css");
                //undecorator.getStylesheets().add("@LoginCSS.css");
                Scene scene = new Scene(undecorator);
                //stage.setResizable(false);
                stage.setScene(scene);
                scene.setFill(Color.TRANSPARENT);
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        ((Node)(event.getSource())).getScene().getWindow().hide();
        }
        else{
        
            allertTextBox.setText("Invalid User Name or Passowrd...");
        }
    }
       
public void getPreference() {
   Preferences prefs = Preferences.userNodeForPackage(SmartHome.class);
   user = prefs.get("username", null);
   passwd = prefs.get("password", null);
   startupVoiceFlag = prefs.get("voiceflag","false");
   if(user ==null || passwd==null){
       user="danny";
       passwd="leslielp";
   
   }
}
    private static final String voiceName="kevin";
    
    public void welcomeGreeting(){
        Voice voice;
        VoiceManager vm= VoiceManager.getInstance();
        voice=vm.getVoice(voiceName);
        
        voice.allocate();
        try{
            voice.speak("Welcome");
        }
        catch(Exception e){
        
        
        }
    
    
    }
}
