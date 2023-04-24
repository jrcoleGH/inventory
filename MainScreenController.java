
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jenncoleinventory.model.InHousePart;
import jenncoleinventory.model.Inventory;
import jenncoleinventory.model.OutSourcedPart;
import jenncoleinventory.model.Part;
import jenncoleinventory.model.Product;


public class MainScreenController implements Initializable {

    @FXML
    private TextField partSearchText;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partTableID;
    @FXML
    private TableColumn<Part, String> partTableName;
    @FXML
    private TableColumn<Part, Integer> partTableInv;
    @FXML
    private TableColumn<Part, Double> partTablePrice;
    @FXML
    private TextField prodSearchText;
    @FXML
    private TableView<Product> prodTable;
    @FXML
    private TableColumn<Product, Integer> prodTableID;
    @FXML
    private TableColumn<Product, String> prodTableName;
    @FXML
    private TableColumn<Product, Integer> prodTableInv;
    @FXML
    private TableColumn<Product, Double> prodTablePrice;
    @FXML
    private Button exitButton;
    static boolean sampleData;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      
        if(!sampleData) {
            InHousePart sampleOne = new InHousePart(1, "Strawberry", 0.25, 360, 100, 900);
            OutSourcedPart sampleTwo = new OutSourcedPart(2, "Carrot", 0.30, 124, 100, 800);
            InHousePart sampleThree = new InHousePart(3, "Apple", 1.25, 25, 20, 300);
            InHousePart sampleFour = new InHousePart(4, "Blackberry", 0.80, 162, 90, 350);
            OutSourcedPart sampleFive = new OutSourcedPart(5, "Tomato", 1.25, 25, 10, 75);
            sampleOne.setMachineId(1);
            sampleTwo.setCompanyName("Test Data Company");
            sampleThree.setMachineId(1);
            sampleFour.setMachineId(1);
            sampleFive.setCompanyName("Test Data Company");
            Inventory.addPart(sampleOne);
            Inventory.addPart(sampleTwo);
            Inventory.addPart(sampleThree);
            Inventory.addPart(sampleFour);
            Inventory.addPart(sampleFive);
            
            Product sampleSix = new Product(101, "Berry Basket", 15.99, 7, 5, 20);
            Product sampleSeven = new Product(102, "Assorted Fruit Basket", 9.99, 12, 8, 30);
            Product sampleEight = new Product(103, "Veggie Tray", 11.99, 25, 10, 30);
            Inventory.addProduct(sampleSix);
            Inventory.addProduct(sampleSeven);
            Inventory.addProduct(sampleEight);
            sampleSix.addAssociatedPart(sampleOne);
            sampleSix.addAssociatedPart(sampleFour);
            sampleSeven.addAssociatedPart(sampleOne);
            sampleSeven.addAssociatedPart(sampleThree);
            sampleSeven.addAssociatedPart(sampleFour);
            sampleEight.addAssociatedPart(sampleTwo);
            sampleEight.addAssociatedPart(sampleFive);
      
            sampleData = true;
            }
        
        partTableID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partTableInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTable.setItems(Inventory.getAllParts());
        
        prodTableID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        prodTableName.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodTableInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodTablePrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        prodTable.setItems(Inventory.getAllProducts());
        
    }    

    @FXML
    private void partSearchHandler(ActionEvent event) {
        String partSearched = partSearchText.getText();
  
        for(Part p:Inventory.getAllParts()) {
            if(p.getName().equalsIgnoreCase(partSearched) || p.getName().toLowerCase().contains(partSearched.toLowerCase()) 
                    || Integer.toString(p.getID()).equals(partSearched)) {
                partTable.getSelectionModel().select(p);
            }
        } 
    }

    @FXML
    private void partAddHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPartScreen.fxml"));
       Parent showAddPart = loader.load();
       Scene showPartScene = new Scene(showAddPart);
       AddPartScreenController controller = loader.getController();
       controller.setScreenLabel("Add Part");
       
       Stage partScene = (Stage)((Node)event.getSource()).getScene().getWindow();
       partScene.setScene(showPartScene);
       partScene.show();
    }

    @FXML
    private void partModifyHandler(ActionEvent event) throws IOException {
        Part part = partTable.getSelectionModel().getSelectedItem();
        
        if (part != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPartScreen.fxml"));
            Parent showModPart = loader.load();
            Scene showPartScene = new Scene(showModPart);
            AddPartScreenController controller = loader.getController();
       
            Stage partScene = (Stage)((Node)event.getSource()).getScene().getWindow();
            partScene.setScene(showPartScene);
            partScene.show();
       
            controller.setFields(part);
            controller.setScreenLabel("Modify Part");
        }
        
        else {
            Alert select = new Alert(AlertType.INFORMATION);
            select.setTitle("No Part Selected");
            select.setHeaderText(null);
            select.setContentText("Please select a part in the table.");

            select.showAndWait();
        }
    }

    @FXML
    private void partDeleteHandler(ActionEvent event) {
        Part part = partTable.getSelectionModel().getSelectedItem();
        if (part != null) {
            
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Part Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this part?");
       
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()== ButtonType.CANCEL){
                alert.close();
                partTable.getSelectionModel().clearSelection();
            }
            else {
                Inventory.deletePart(part);
                partTable.getSelectionModel().clearSelection();
            }
        }
        else {
            Alert select = new Alert(AlertType.INFORMATION);
            select.setTitle("No Part Selected");
            select.setHeaderText(null);
            select.setContentText("Please select a part in the table.");

            select.showAndWait();
        }
    }

    @FXML
    private void prodSearchHandler(ActionEvent event) {
        String prodSearched = prodSearchText.getText();
            for(Product p:Inventory.getAllProducts()) {
                if(p.getName().equalsIgnoreCase(prodSearched) || p.getName().toLowerCase().contains(prodSearched.toLowerCase()) 
                        || Integer.toString(p.getID()).equals(prodSearched)) {
                    prodTable.getSelectionModel().select(p);
                }
            }
    }

    @FXML
    private void prodAddHandler(ActionEvent event) throws IOException {
       FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProdScreen.fxml"));
       Parent showAddProd = loader.load();
       Scene showProdScene = new Scene(showAddProd);
       AddProdScreenController add = loader.getController();
       add.setScreenLabel("Add Product");
       
       Stage prodScene = (Stage)((Node)event.getSource()).getScene().getWindow();
       prodScene.setScene(showProdScene);
       prodScene.show();
    }

    @FXML
    private void prodModifyHandler(ActionEvent event) throws IOException {
        Product product = prodTable.getSelectionModel().getSelectedItem();
        if (product != null) {
           FXMLLoader loader = new FXMLLoader(getClass().getResource("AddProdScreen.fxml"));
           Parent showModProd = loader.load();
           Scene showProdScene = new Scene(showModProd);
           AddProdScreenController controller = loader.getController();
           controller.setScreenLabel("Modify Product");
       
           Stage prodScene = (Stage)((Node)event.getSource()).getScene().getWindow();
           prodScene.setScene(showProdScene);
           prodScene.show();
       
           controller.setFields(product);
        }
        
        else {
            Alert select = new Alert(AlertType.INFORMATION);
            select.setTitle("No Product Selected");
            select.setHeaderText(null);
            select.setContentText("Please select a product in the table.");

            select.showAndWait();
        }  
    }

    @FXML
    private void prodDeleteHandler(ActionEvent event) {
        Product product = prodTable.getSelectionModel().getSelectedItem();
        if (product != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Product Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this product?");
       
            Optional<ButtonType> result = alert.showAndWait();
            if(result.get()== ButtonType.CANCEL){
                alert.close();
                prodTable.getSelectionModel().clearSelection();
            }
            else {
                Inventory.deleteProduct(product);
                prodTable.getSelectionModel().clearSelection();
            }
        }
        else {
            Alert select = new Alert(AlertType.INFORMATION);
            select.setTitle("No Product Selected");
            select.setHeaderText(null);
            select.setContentText("Please select a product in the table.");

            select.showAndWait();
        }
    }

    @FXML
    private void exitHandler(ActionEvent event) {
        Stage mainScreen = (Stage) exitButton.getScene().getWindow();
        mainScreen.close();
    }
    
}