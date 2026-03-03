package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/** This class is used to manage an inventory of parts and products.*/
public class Inventory {

    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /** This method adds a part. The part is added to the allParts list.
     * @param newPart the part to add*/
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /** This method searches the parts list. It searches by ID.
     * @param partId the ID to search
     * @return the part found
     * @return null if no part found*/
    public static Part lookUpPart(int partId) {

        for(int i = 0; i < allParts.size(); i++) {
            Part pt = allParts.get(i);
            if (pt.getId() == (partId)) {
                return pt;
            }
        }
        return null;
    }

    /** This method searches the parts list. It searches by part name.
     * @param partName the name to search
     * @return the part found*/
    public static ObservableList<Part> lookUpPart(String partName) {
        ObservableList<Part> searchList = FXCollections.observableArrayList();

        for(int i = 0; i < allParts.size(); i++) {
            Part pt = allParts.get(i);
                if (pt.getName().toLowerCase().contains(partName.toLowerCase())) {
                    searchList.add(pt);
                }
        }
        return searchList;
    }

    /** This method updates a part. It replaces and existing part with a new one.
     * @param index the location of the part to replace in the list
     * @param selectedPart the new part */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);

    }

    /** This method deletes a part. Permanently deletes part from parts list
     * @param selectedPart the part to delete
     * @return the list with the part deleted*/
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

     /** @return the parts list*/
     public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /** This method adds a product. The product is added to the all products list.
     * @param newProduct the product to add*/
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /** This method searches the products list. It searches by ID.
     * @param productId the ID to search
     * @return the product found
     * @return null if no product found*/
    public static Product lookUpProduct(int productId) {

         for(int i = 0; i < allProducts.size(); i++) {
             Product pd = allProducts.get(i);
                if (pd.getId() == (productId)) {
                 return pd;
             }
         }
         return null;
    }

    /** This method searches the products list. It searches by product name.
     * @param productName the name to search
     * @return the product found*/
    public static ObservableList<Product> lookUpProduct(String productName) {
        ObservableList<Product> searchList = FXCollections.observableArrayList();

        for(int i = 0; i < allProducts.size(); i++) {
            Product prod = allProducts.get(i);
                if (prod.getName().toLowerCase().contains(productName.toLowerCase())) {
                    searchList.add(prod);
                }
        }
        return searchList;
    }

    /** This method updates a product. It replaces and existing product with a new one.
     * @param index the location of the product to replace in the list
     * @param newProduct the new product */
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }

    /** This method deletes a product. Permanently deletes product from products list
     * @param selectedProduct the product to delete
     * @return the list with the product deleted*/
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /** @return the products list*/
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}
