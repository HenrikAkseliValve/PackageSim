/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim;

import java.util.Random;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/** @author Henrik Valve */
public class SimMain extends Application{
    
    /* Tells should all the threads be closing */
    public static boolean closing = false;
    
    /* Tells the label that tells users for errors */
    public static Label help = null;
    
    /* Random object for the progrma */
    public final static Random RANDOM = new Random();
    
    @Override
    public void start(Stage stage) throws Exception{
        // Load main FXML screen
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("FXMLMain.fxml"))));
        
        // Set closing event
        stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
            
            @Override
            public void handle(WindowEvent event){
                System.exit(0);
            }
        });
        
        
        // Show the main stage
        stage.show();
    }
    
    
    /** @param args the command line arguments */
    public static void main(String[] args){
        launch(args);
    }
    
}
