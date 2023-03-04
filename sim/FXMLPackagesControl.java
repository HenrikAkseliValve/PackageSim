/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sim;

import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import static sim.SimMain.help;

/**
 * FXML Controller class
 *
 * @author Henrik Valve
 */
public class FXMLPackagesControl implements Initializable{
    /* Tells to be created items width */
    @FXML
    private TextField itemHeight;
    /* Tells to be created items height */
    @FXML
    private TextField itemWidth;
    /* Tells to be created items name */
    @FXML
    private TextField itemName;
    /* Tells to be created items fractioinality */
    @FXML
    private CheckBox itemFract;
    /* Tells to be created items detph */
    @FXML
    private TextField itemDept;
    /* Tells to be created packages's item */
    @FXML
    private ChoiceBox<String> packageItem;
    /* Tells to be created packages's class */
    @FXML
    private ChoiceBox<String> packageClass;
    /* Tells to be created packages's destination */
    @FXML
    private ChoiceBox<String> packageTo;
    /* Tells to be created packages's sender */
    @FXML
    private ChoiceBox<String> packageFrom;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize the choicebox's
        this.packageItem.getItems().addAll(SmartPostManager.getManager().getItems());
        this.packageTo.getItems().addAll(SmartPostManager.getManager().getPostNames());
        this.packageFrom.getItems().addAll(SmartPostManager.getManager().getPostNames());
        this.packageClass.getItems().addAll("Class 1","Class 2","Class 3");
        
        // Sort the packageTo and packageFrom
        Collections.sort(this.packageTo.getItems());
        Collections.sort(this.packageFrom.getItems());
    }    

    /**
     * Creates new item if fields aren't empty
     * 
     */
    @FXML
    private void createItem(ActionEvent event){
        // Check are inputs alright
        if(!this.itemName.getText().isEmpty()
           &&!this.itemWidth.getText().isEmpty()
           &&!this.itemHeight.getText().isEmpty()
           &&!this.itemDept.getText().isEmpty()){
            
            // Check that these number are over one
            int width  = Integer.valueOf(this.itemWidth.getText());
            int height = Integer.valueOf(this.itemHeight.getText());
            int depth  = Integer.valueOf(this.itemDept.getText());
            if(width>1&&height>1&&depth>1){
                SmartPostManager.getManager().addItem(this.itemName.getText(),
                    width,
                    height,
                    depth,
                    this.itemFract.isSelected());
                this.packageItem.getItems().add(this.itemName.getText());
            }
            else{
                // Tell user about interesting input
                help.setText("Interesting input for one the over one numbers!");
                help.setTextFill(Color.RED);
            }
        }
        else{
            // Tell user that input is missing
            help.setText("Input missing!");
            help.setTextFill(Color.RED);
        }
    }
    
     /**
     * Creates new package if fields aren't empty
     * 
     */
    @FXML
    private void createPackage(ActionEvent event){
        //Check that there is input
        if(!this.packageClass.getValue().isEmpty()
           &&!this.packageItem.getValue().isEmpty()
           &&!this.packageTo.getValue().isEmpty()
           &&!this.packageFrom.getValue().isEmpty()){
            
            SmartPostManager.getManager().addPackage(this.packageTo.getValue(),this.packageFrom.getValue(),
                                                      this.packageItem.getValue(),packageClass.getValue());
        }
        else{
            // Tell user that input is missing
            help.setText("Input missing!");
            help.setTextFill(Color.RED);
        }
    }
}
