/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smarthome;

import insidefx.undecorator.Undecorator;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author danny
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML private Label label;
    @FXML WebView webview2 ;
    @FXML WebView webview1;
    @FXML WebView ipCamView;
    @FXML private MediaView videoView;
    @FXML private LineChart<String,Integer> tempChart;
    @FXML private TextField ipTF;
    @FXML private TextField videoTF;
    @FXML private Label currentIP;
    @FXML private Label currentVL;
    @FXML private ColorPicker colorChooser1;
    @FXML private Circle currentColor;
    @FXML private CheckBox startupVoice;
    
    String ipaddress=null;
    String videoLink = null;
    String voiceFlag="false";
    @FXML
    private void pwdChangeButtonAction(ActionEvent event) {
        
        Stage stage = new Stage();
        //Fill stage with content
        stage.setTitle("New Window");
        
        
        stage.setTitle("Smart Home");
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("changeUserPasswd.fxml"));
            Undecorator undecorator = new Undecorator(stage, (Region) root);
            undecorator.getStylesheets().add("skin/undecorator.css");
            Scene scene = new Scene(undecorator);
            stage.setScene(scene);
            scene.setFill(Color.TRANSPARENT);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
   
        
        
    }
   
      
    @FXML
    private void updateColor(ActionEvent event) {
        Color newColor = colorChooser1.getValue();
        currentColor.setFill(newColor);
        NumberFormat formatter = new DecimalFormat("#0.00");  
        

        String red= formatter.format(newColor.getRed());
        String green= formatter.format(newColor.getGreen());
        String blue= formatter.format(newColor.getBlue());
        System.out.println("R= "+red+"  : G= "+green+"   : B= "+blue);
        
         new Thread(new Runnable() {
                @Override
                public void run() {
                    
                        httpPost("http://"+ipaddress+":8080/cgi-bin/setRGB.py?R="+red+"&G="+green+"&B="+blue);
                        System.out.println("Inside The Thread.");
                    
                }
            }).start();
         
      
    }
       
    public void httpPost(String url) {
        try {
            HttpClient Client = new DefaultHttpClient();
            try
            {
                String SetServerString = "";
                // Create Request to server and get response
                HttpGet httpget = new HttpGet(url);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                SetServerString = Client.execute(httpget, responseHandler);

        }
        catch(Exception ex)
        {

        }
    } 
    catch (Exception e) {


    }
    
    }
    
    @FXML
    private void settingButtonAction(ActionEvent event){
   
        setPreference(ipTF.getText(),videoTF.getText(),Boolean.toString(startupVoice.isSelected()));
        getPreference();
        updateTextField();
        System.out.println("Current Value :"+ipaddress);
        System.out.println(startupVoice.isSelected());
        //update the webviews with the new values
        final WebEngine eng1 = webview2.getEngine();
        eng1.load("http://"+ipaddress+":8000");
        //eng1.reload();
        

        final WebEngine eng2 = webview1.getEngine();
        eng2.load("http://"+ipaddress+":8000/room1.html");
        //eng1.reload();
        final WebEngine eng3 = ipCamView.getEngine();
        eng3.load(videoLink);
        //eng3.reload();
        ///////////////////////////////////////////
      
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
     getPreference();   
     
     final WebEngine eng1 = webview2.getEngine();
     eng1.load("http://"+ipaddress+":8000");

     final WebEngine eng2 = webview1.getEngine();
     eng2.load("http://"+ipaddress+":8000/room1.html");
     
     final WebEngine eng3 = ipCamView.getEngine();
     eng3.load(videoLink);
     
     initChart();
     System.out.println(Boolean.parseBoolean(voiceFlag));
     startupVoice.setSelected(Boolean.parseBoolean(voiceFlag));
     updateTextField();
     Color newColor = colorChooser1.getValue();
     currentColor.setFill(newColor);
    }    
    
    
    private void initPlayer (String uri) {
       

        Media media = new Media(uri);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        videoView.setMediaPlayer(mediaPlayer);
        mediaPlayer.play();
       /* mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
            }
        });*/
    }
    
    
    private void initChart(){
    
        tempChart.getXAxis().setAutoRanging(true);
        tempChart.getYAxis().setAutoRanging(true);
        
        XYChart.Series series1 = new XYChart.Series<>();
        series1.setName("Living Room");
        //series.getData().add(new XYChart.Data<>("12", 23));
        series1.getData().add(new XYChart.Data("1", 23));
        series1.getData().add(new XYChart.Data("2", 14));
        series1.getData().add(new XYChart.Data("3", 15));
        series1.getData().add(new XYChart.Data("4", 24));
        series1.getData().add(new XYChart.Data("5", 34));
        series1.getData().add(new XYChart.Data("6", 36));
        series1.getData().add(new XYChart.Data("7", 22));
        series1.getData().add(new XYChart.Data("8", 45));
        series1.getData().add(new XYChart.Data("9", 43));
        series1.getData().add(new XYChart.Data("10", 17));
        series1.getData().add(new XYChart.Data("11", 29));
        series1.getData().add(new XYChart.Data("12", 25));
        
        XYChart.Series series2 = new XYChart.Series<>();
        series2.setName("Study Room");
        series2.getData().add(new XYChart.Data("1", 13));
        series2.getData().add(new XYChart.Data("2", 34));
        series2.getData().add(new XYChart.Data("3", 15));
        series2.getData().add(new XYChart.Data("4", 34));
        series2.getData().add(new XYChart.Data("5", 54));
        series2.getData().add(new XYChart.Data("6", 66));
        series2.getData().add(new XYChart.Data("7", 32));
        series2.getData().add(new XYChart.Data("8", 15));
        series2.getData().add(new XYChart.Data("9", 33));
        series2.getData().add(new XYChart.Data("10", 47));
        series2.getData().add(new XYChart.Data("11", 19));
        series2.getData().add(new XYChart.Data("12", 55));
        
        tempChart.getData().add(series1);
        tempChart.getData().add(series2);
    
    
    }
    
    
    
 public void getPreference() {
        Preferences prefs = Preferences.userNodeForPackage(SmartHome.class);
        ipaddress = prefs.get("ipaddress", null);
        videoLink = prefs.get("videolink", null);
        voiceFlag=prefs.get("voiceflag", null);
    }
    
public void setPreference(String arg1,String arg2,String arg3) {
    Preferences prefs = Preferences.userNodeForPackage(SmartHome.class);
    prefs.put("ipaddress",arg1 );
    prefs.put("videolink",arg2 );
    prefs.put("voiceflag",arg3 );
}
    
    public void updateTextField(){
    
        currentIP.setText("Current Value :"+ipaddress);
        currentVL.setText("Current Value :"+videoLink);
        ipTF.setText(ipaddress);
        videoTF.setText(videoLink);
    }
    
    

}
