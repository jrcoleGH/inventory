
package jenncoleinventory.view_controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import static javax.swing.UIManager.get;
import jenncoleinventory.model.Inventory;
import jenncoleinventory.model.Part;
import jenncoleinventory.model.Product;


public class AddProdScreenController implements Initializable {

    @FXML
    private Label prodScreenLabel;
    @FXML
    private Label prodID;
    @FXML
    private Label prodName;
    @FXML
    private Label prodInv;
    @FXML
    private Label prodPrice;
    @FXML
    private Label prodMax;
    @FXML
    private Label prodMin;
    @FXML
    private TextField prodIDText;
    @FXML
    private TextField prodNameText;
    @FXML
    private TextField prodInvText;
    @FXML
    private TextField prodPriceText;
    @FXML
    private TextField prodMaxText;
    @FXML
    private TextField prodMinText;
    @FXML
    private TextField prodPartSearchText;
    @FXML
    private TableView<Part> addPartTable;
    @FXML
    private TableView<Part> deletePartTable;
    @FXML
    private TableColumn<Part, Integer> partTableID2;
    @FXML
    private TableColumn<Part, String> partTableName2;
    @FXML
    private TableColumn<Part, Integer> partTableInv2;
    @FXML
    private TableColumn<Part, Double> partTablePrice2;
    @FXML
    private TableColumn<Part, Integer> partTableID3;
    @FXML
    private TableColumn<Part, String> partTableName3;
    @FXML
    private TableColumn<Part, Integer> partTableInv3;
    @FXML
    private TableColumn<Part, Double> partTablePrice3;
    
    Product product;
    ObservableList<Part> chosenPart = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        partTableID2.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partTableName2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partTableInv2.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partTablePrice2.setCellValueFactory(new PropertyValueFactory<>("price"));
        addPartTable.setItems(Inventory.getAllParts());
               
        partTableID3.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partTableName3.setCellValueFactory(new PropertyValueFactory<>("name"));
        partTableInv3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partTablePrice3.setCellValueFactory(new PropertyValueFactory<>("price"));
    }  
    
    public void setScreenLabel(String name) {
        prodScreenLabel.setText(name);
    }

    @FXML
    private void prodPartSearchHandler(ActionEvent event) {
        String partSearched = prodPartSearchText.getText();
        for(Part p:Inventory.getAllParts()) {
            if(p.getName().equalsIgnoreCase(partSearched) || p.getName().toLowerCase().contains(partSearched.toLowerCase()) 
                    || Integer.toString(p.getID()).equals(partSearched)) {
                addPartTable.getSelectionModel().select(p);
            }
        }
    }

    @FXML
    private void prodPartAddHandler(ActionEvent event) {
        Part selectedPart = addPartTable.getSelectionModel().getSelectedItem();
        
        if(selectedPart != null) {
            chosenPart.add(selectedPart);
            deletePartTable.setItems(chosenPart);
        }
        else {
            Alert select = new Alert(AlertType.INFORMATION);
            select.setTitle("No Part Selected");
            select.setHeaderText(null);
            select.setContentText("Please select a part in the table.");

            select.showAndWait(); 
        }  
        
        addPartTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void prodDeleteHandler(ActionEvent event) {
        Part selectedPart = deletePartTable.getSelectionModel().getSelectedItem();
        
        if(selectedPart != null) {
            chosenPart.remove(selectedPart);
            deletePartTable.setItems(chosenPart);
        }
        else {
            Alert select = new Alert(AlertType.INFORMATION);
            select.setTitle("No Part Selected");
            select.setHeaderText(null);
            select.setContentText("Please select a part in the table.");

            select.showAndWait(); 
        }  
        
        deletePartTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void prodSaveHandler(ActionEvent event) throws IOException {
        int id = 0;
        String name = prodNameText.getText();
        int inv = Integer.parseInt(prodInvText.getText());
        double price = Double.parseDouble(prodPriceText.getText());
        int max = Integer.parseInt(prodMaxText.getText());
        int min = Integer.parseInt(prodMinText.getText());
        boolean empty = chosenPart.isEmpty();
        
        // can't figure out how to get this to read the other fields
        if (name.trim().isEmpty()) {
            Alert saved = new Alert(AlertType.INFORMATION);
            saved.setTitle("No Product Name");
            saved.setHeaderText(null);
            saved.setContentText("Product must have a name!");

            saved.showAndWait();
        }
        else {
            if(inv >= min && inv <= max && !empty) {
                Product newProduct = new Product(id, name, price, inv, min, max);
                double total = 0;

                if(product != null) {
                    newProduct.setId(product.getID());                   
                    Inventory.deleteProduct(product);
                }

                for(Part p : chosenPart) {
                    newProduct.addAssociatedPart(p);
                    total += p.getPrice();
                }

                if(total > price) {
                    Alert saved = new Alert(AlertType.INFORMATION);
                    saved.setTitle("Product Price");
                    saved.setHeaderText(null);
                    saved.setContentText("Product price is less than the total price of parts. "
                        + "Please update the product price.");

                    saved.showAndWait(); 
                }
                else {  
                    Inventory.addProduct(newProduct);

                    Alert saved = new Alert(AlertType.INFORMATION);
                    saved.setTitle("Product Saved");
                    saved.setHeaderText(null);
                    saved.setContentText("Product Successfully Saved!");

                    saved.showAndWait();

                    Parent showMain = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                    Scene showMainScene = new Scene(showMain);

                    Stage mainScene = (Stage)((Node)event.getSource()).getScene().getWindow();
                    mainScene.setScene(showMainScene);
                    mainScene.show();
                }
            }
            else {
                if(empty) {
                    Alert saved = new Alert(AlertType.INFORMATION);
                    saved.setTitle("Product Parts");
                    saved.setHeaderText(null);
                    saved.setContentText("Product has no associated parts. Please add at least one part.");

                    saved.showAndWait();
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
    }

    @FXML
    private void prodCancelHandler(ActionEvent event) throws IOException {
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
    
    public void setFields(Product product) {
        this.product = product;
        
        prodIDText.setText(Integer.toString(product.getID()));
        prodNameText.setText(product.getName());
        prodInvText.setText(Integer.toString(product.getStock()));
        prodPriceText.setText(Double.toString(product.getPrice()));
        prodMaxText.setText(Integer.toString(product.getMax()));
        prodMinText.setText(Integer.toString(product.getMin()));
        
        addPartTable.setItems(Inventory.getAllParts());
        deletePartTable.setItems(product.getAllAssociatedParts());
        
        for(Part p:product.getAllAssociatedParts()) {
            chosenPart.add(p);
        } 
    }    
}
