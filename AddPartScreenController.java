
package jenncoleinventory.view_controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import jenncoleinventory.model.InHousePart;
import jenncoleinventory.model.Inventory;
import jenncoleinventory.model.OutSourcedPart;
import jenncoleinventory.model.Part;


public class AddPartScreenController implements Initializable {
    
    @FXML
    private Label partScreenLabel;
    @FXML
    private TextField partIDText;
    @FXML
    private TextField partNameText;
    @FXML
    private TextField partInvText;
    @FXML
    private TextField partPriceText;
    @FXML
    private TextField partMaxText;
    @FXML
    private TextField partMinText;
    @FXML
    private TextField partLastText;
    @FXML
    private Label partID;
    @FXML
    private Label partName;
    @FXML
    private Label partInv;
    @FXML
    private Label partPrice;
    @FXML
    private Label partMax;
    @FXML
    private Label partMin;
    @FXML
    private Label partLastLabel;
    @FXML
    private ToggleGroup inOut;
    @FXML
    private RadioButton inHouseRadio;
    @FXML
    private RadioButton outSourcedRadio;
    Part part;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setFields(Part part) {
        this.part = part;
        
        partIDText.setText(Integer.toString(part.getID()));
        partNameText.setText(part.getName());
        partInvText.setText(Integer.toString(part.getStock()));
        partPriceText.setText(Double.toString(part.getPrice()));
        partMaxText.setText(Integer.toString(part.getMax()));
        partMinText.setText(Integer.toString(part.getMin()));
        
        if (part instanceof InHousePart) {
            InHousePart partIn = (InHousePart) part;
            inHouseRadio.setSelected(true);
            partLastLabel.setText("Machine ID");
            partLastText.setText(Integer.toString(partIn.getMachineId()));
        }
        else {
            OutSourcedPart partOut = (OutSourcedPart) part;
            outSourcedRadio.setSelected(true);
            partLastLabel.setText("Company Name");
            partLastText.setText(partOut.getCompanyName());
        }
    }
    
    public void setScreenLabel(String name) {
        partScreenLabel.setText(name);
    }
    
    @FXML
    private void partSaveHandler(ActionEvent event) throws IOException {       
        int id = 0; 
        String name = partNameText.getText();
        int inv = Integer.parseInt(partInvText.getText());
        double price = Double.parseDouble(partPriceText.getText());
        int max = Integer.parseInt(partMaxText.getText());
        int min = Integer.parseInt(partMinText.getText());
        String s = partLastText.getText();
  
        boolean isNum = true;
        
        // can't figure out how to get this to read the other fields
        if (name.trim().isEmpty() || s.trim().isEmpty()) {
            Alert saved = new Alert(AlertType.INFORMATION);
            saved.setTitle("Fields Empty");
            saved.setHeaderText(null);
            saved.setContentText("One or more fields have been left blank!");

            saved.showAndWait();
        }
        else {
            if(inv >= min && inv <= max) {
                if(inHouseRadio.isSelected()) {
                    InHousePart newInPart = new InHousePart(id, name, price, inv, min, max);
                    int machine = 0;
                    
                    try{
                        machine = Integer.parseInt(s);
                    }
                    catch (NumberFormatException e) {
                        isNum = false;
                    }
                    
                    if(isNum) {
                        newInPart.setMachineId(machine);
                        
                        if(part != null) {
                        newInPart.setId(part.getID());
                        Inventory.deletePart(part);
                        }

                        Inventory.addPart(newInPart);
                        
                        Alert saved = new Alert(AlertType.INFORMATION);
                        saved.setTitle("Part Saved");
                        saved.setHeaderText(null);
                        saved.setContentText("Part Successfully Saved!");

                        saved.showAndWait();

                        Parent showMain = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                        Scene showMainScene = new Scene(showMain);

                        Stage mainScene = (Stage)((Node)event.getSource()).getScene().getWindow();
                        mainScene.setScene(showMainScene);
                        mainScene.show();
                    }
                    else {
                        Alert saved = new Alert(AlertType.INFORMATION);
                        saved.setTitle("Machine ID");
                        saved.setHeaderText(null);
                        saved.setContentText("Machine ID must be a number!");

                        saved.showAndWait();
                    }                    
                }
                else {
                    OutSourcedPart newOutPart = new OutSourcedPart(1, name, price, inv, min, max);
                    int company = 0;
                    
                    try{
                        company = Integer.parseInt(s);
                    }
                    catch (NumberFormatException e) {
                        isNum = false;
                    }
                    
                    if(!isNum) {
                        newOutPart.setCompanyName(s);
                        
                        if(part != null) {
                            newOutPart.setId(part.getID());
                            Inventory.deletePart(part);
                            }

                            Inventory.addPart(newOutPart);

                            Alert saved = new Alert(AlertType.INFORMATION);
                            saved.setTitle("Part Saved");
                            saved.setHeaderText(null);
                            saved.setContentText("Part Successfully Saved!");

                            saved.showAndWait();

                            Parent showMain = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                            Scene showMainScene = new Scene(showMain);

                            Stage mainScene = (Stage)((Node)event.getSource()).getScene().getWindow();
                            mainScene.setScene(showMainScene);
                            mainScene.show();
                        }
                    else {
                        Alert saved = new Alert(AlertType.INFORMATION);
                        saved.setTitle("Company Name");
                        saved.setHeaderText(null);
                        saved.setContentText("Company Name must be a name!");

                        saved.showAndWait();
                    }
                }
            } 
            else {
                Alert outOfBounds = new Alert(AlertType.INFORMATION);
                outOfBounds.setTitle("Inventory Error");
                outOfBounds.setHeaderText(null);
                outOfBounds.setContentText("Inventory levels are inaccurate!");
                outOfBounds.showAndWait();
            }
        }    
    }

    @FXML
    private void partCancelHandler(ActionEvent event) throws IOException {
       Alert alert = new Alert(AlertType.CONFIRMATION);
       alert.setTitle("Cancel and Return to Main Screen");
       alert.setHeaderText(null);
       alert.setContentText("Are you sure you want to cancel?");
       
       Optional<ButtonType> result = alert.showAndWait();
            if(result.get()== ButtonType.CANCEL){
                alert.close();
            }
            else {
                Parent showMain = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                Scene showMainScene = new Scene(showMain);
       
                Stage mainScene = (Stage)((Node)event.getSource()).getScene().getWindow();
                mainScene.setScene(showMainScene);
                mainScene.show();
            }
    }

    @FXML
    private void inHouseHandler(ActionEvent event) {
        partLastLabel.setText("Machine ID");        
    }

    @FXML
    private void outSourcedHandler(ActionEvent event) {
        partLastLabel.setText("Company Name");
    }
}