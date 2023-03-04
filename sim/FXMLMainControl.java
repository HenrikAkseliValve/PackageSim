package sim;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import static sim.SimMain.help;

/** @author Henrik Valve */
public class FXMLMainControl implements Initializable{
    
    /** Shows and controls the index.html */
    @FXML
    private WebView webview;
    
    /** List of SmartPosts to choose where road goes. Used creation and deletion */
    @FXML
    private ChoiceBox<String> pathTo;
    
    /** List of SmartPosts to choose where road comes. Used creation and deletion */
    @FXML
    private ChoiceBox<String> fromTo;
    
    /** Smart post's name on creation */
    @FXML
    private TextField postName;
    
    /** Smart post's address on creation */
    @FXML
    private TextField postAddress;
    
    /** Smart post's postal code on creation */
    @FXML
    private TextField postPostalcode;
    
    /** Smart post's Region on creation */
    @FXML
    private TextField postRegion;
    
    /** URL on loading SmartPosts*/
    @FXML
    private TextField smartPostURL;
    
    /** Label that displays information telling user about errors */
    @FXML
    private Label userhelp;
    
    /** Simulate button so that we can change attributes for it */
    @FXML
    private Button buttonSimulate;
    
    /** */
    private static final Background B_RED   = new Background(new BackgroundFill(Color.RED,CornerRadii.EMPTY,Insets.EMPTY));
    private static final Background B_GREEN = new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY));
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        this.webview.getEngine().load(this.getClass().getResource("index.html").toExternalForm());
      
         // Create example items
        SmartPostManager.getManager().addItem("Teddy",  20, 20,20,false);
        SmartPostManager.getManager().addItem("Bottle", 20, 50,20,true);
        SmartPostManager.getManager().addItem("Cat",    60, 30,20,false);
        SmartPostManager.getManager().addItem("Doge",   120,60,40,false);
      
         // Start the new thread that handles packages moving
        Thread th = new Thread(SmartPostManager.getManager());
        th.setDaemon(true);
        th.start();
      
        // Set the user helping string  
        ObjectCourseEnd.help = this.userhelp;
        
        // Change "Simulate" button's back ground color
        this.buttonSimulate.setBackground(B_RED);
    }

    /**
     * Button event to open item window
     * @param event 
     */
    @FXML
    private void openItemsWin(ActionEvent event){
        // Tell user that this feature isn't implimented
        help.setText("This feature is not implimented here go to packages!");
        help.setTextFill(Color.PURPLE);
    }

    /**
     * Button event to open packages window
     * @param event 
     */
    @FXML
    private void openPackagesWin(ActionEvent event){
        try{
          // Set the webengine so that SmartPostManager can use it  
          SmartPostManager.setWebEngine(this.webview.getEngine());
          // Stage of the window
          Stage win = new Stage();
          // Load items screen                          
          win.setScene(new Scene(FXMLLoader.load(new File("src\\objectcourseend\\FXMLPackages.fxml").toURI().toURL())));
          //Show the window
          win.show();                        
        } 
        catch(IOException ex){
          Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Button event to open list window
     * @param event 
     */
    @FXML
    private void openListWin(ActionEvent event){
        // Tell user that this feature isn't implimented
        help.setText("This feature is not implimented!");
        help.setTextFill(Color.PURPLE);
    }

    /**
     * Button event to log item window
     * @param event 
     */
    @FXML
    private void openLogWin(ActionEvent event){
        // Tell user that this feature isn't implimented
        help.setText("This feature is not implimented!");
        help.setTextFill(Color.PURPLE);
    }

    /**
     * Button event to create path
     * @param event 
     */
    @FXML
    private void createPath(ActionEvent event){
        // Check that there is input
        if(!this.fromTo.getValue().isEmpty()&&!this.pathTo.getValue().isEmpty()){
            
            // Check is road already drawn
            if(SmartPostManager.getManager().isRoadNotDrawn(this.fromTo.getValue(),this.pathTo.getValue())){
                ArrayList p=new ArrayList();
                double p1[] = SmartPostManager.getManager().getGeoPoint(this.fromTo.getValue());
                double p2[]= SmartPostManager.getManager().getGeoPoint(this.pathTo.getValue());
            
                p.add(p1[0]);
                p.add(p1[1]);
                p.add(p2[0]);
                p.add(p2[1]);
                Integer dist = ((Double)this.webview.getEngine().executeScript("document.createPath("+ p + ", 'red', 1)")).intValue();
                SmartPostManager.getManager().addDrawnRoad(this.fromTo.getValue(),this.pathTo.getValue(),dist);
            }
        }
        else{
            // Tell user that name isn't unique
            help.setText("Least one field doesn't have input!");
            help.setTextFill(Color.RED);
        }
    }
    /**
     * Button event to delete all the paths
     * @param event 
     */
    @FXML
    private void deletePath(ActionEvent event){
        webview.getEngine().executeScript("document.deletePaths()");
        SmartPostManager.getManager().clearPaths();
    }
    
    /**
     * Button event to create SmartPost
     * @param event 
     */
    @FXML
    private void createSmartPost(ActionEvent event){
        
        // Check that user did input things
        if(!this.postName.getText().isEmpty()
           &&!this.postAddress.getText().isEmpty()
           &&!this.postPostalcode.getText().isEmpty()
           &&!this.postRegion.getText().isEmpty()){
            
            // Ask if the name is unique
            if(SmartPostManager.getManager().isUniqueName(this.postName.getText())){
            
                // Tell user that this feature isn't implimented
                help.setText("This feature is not implimented!");
                help.setTextFill(Color.PURPLE);
            }
            else{
                // Tell user that name isn't unique
                help.setText("Name given isn't unique!");
                help.setTextFill(Color.RED);
            }
        }
        else{
            // Tell user that least one of the fields doesn't have input
            help.setText("Least one field missing input!");
            help.setTextFill(Color.RED);
        }
    }

    /**
     * Button event to load SmartPost from XML
     * @param event 
     */
    @FXML
    private void loadSmartPosts(ActionEvent event){
        
        if(!this.smartPostURL.getText().isEmpty()){
            SmartPost ite[] = SmartPostManager.getManager().loadSmartPosts(this.smartPostURL.getText());
            for(SmartPost sp : ite){
                // Draw marker
                webview.getEngine().executeScript("document.goToLocation('" + sp.getFullAddress() + "', 'SmartPost', 'blue')");
                // Add name to lists
                this.fromTo.getItems().add(sp.getName());
                this.pathTo.getItems().add(sp.getName());
            }
        }
        
        // Sort both fromTo and pathTo items
        Collections.sort(this.fromTo.getItems());
        Collections.sort(this.pathTo.getItems());
    }

    @FXML
    private void toggleSimulate(ActionEvent event){
        if(this.buttonSimulate.getBackground()==FXMLMainControl.B_GREEN){
            this.buttonSimulate.setBackground(B_RED);
            
            // Tell user that this feature isn't implimented
            help.setText("It just change color!");
            help.setTextFill(Color.PURPLE);
        }
        else{
            this.buttonSimulate.setBackground(B_GREEN);
        }
        
    }
      
    
    
}
