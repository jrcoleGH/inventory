
package jenncoleinventory.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();;
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();;
    
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }
    
    // never used
    public static Part lookupPart(int partId) {
        return allParts.get(partId);
    }
    
    // never used
    public static Product lookupProduct(int productId) {
        return allProducts.get(productId);
    }
    
    // never used
    public static ObservableList<Part> lookupPart(String partName) {
        return allParts;
    }
    
    // never used
    public static ObservableList<Product> lookupProduct(String productName) {
        return allProducts;
    }
    
    // never used
    public static void updatePart(int index, Part selectedPart) {
    }
    
    // never used
    public static void updateProduct(int index, Product newProduct) {
    }
    
    public static boolean deletePart(Part selectedPart) {
            allParts.remove(selectedPart);
            return true;
    }
    
    public static boolean deleteProduct(Product selectedProduct) {
            allProducts.remove(selectedProduct);
            return true;
    }
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
