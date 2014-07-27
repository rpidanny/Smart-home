/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smarthome;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author danny
 */
public class ChangeUserPasswdController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML private PasswordField oldPW;
    @FXML private PasswordField newPW;
    @FXML private Text status;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    private void changePWOnClick(ActionEvent event) {
        
        //status.setText("Password is :"+LoginWindowController.passwd);
        String temp=oldPW.getText();
        String temp1=LoginWindowController.passwd;
        if(temp.equals(temp1)){
            setPreference("danny",newPW.getText());
            LoginWindowController.passwd=newPW.getText();
            status.setText("Password Changed!!");
        }
        else{
            status.setText("Password does not match..");
       
        }
    }
    
    
    public void setPreference(String arg1,String arg2) {
    Preferences prefs = Preferences.userNodeForPackage(SmartHome.class);
    prefs.put("username",arg1 );
    prefs.put("password",arg2 );
}
}
