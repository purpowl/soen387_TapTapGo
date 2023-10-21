package src;
import java.util.HashMap;
import java.util.Map.Entry;

import src.Exceptions.InsufficientInventoryException;
import src.Exceptions.InvalidParameterException;
import src.Exceptions.ProductNotFoundException;

public class Customer extends User {
    protected enum customerTypes {Anonymous, Registered};
    protected HashMap<Product, Integer> cart;
    protected customerTypes customerType;

    public Customer(String username) {
        super(username);
        this.cart = new HashMap<>();
        this.customerType = customerTypes.Anonymous;
    }

    public Customer(String username, String password) {
        super(username, password);
        this.cart = new HashMap<>();
        this.customerType = customerTypes.Registered;
    }

    public HashMap<Product, Integer> getCart() {
        return this.cart;
    }

    // find the product and its quantity in cart by SKU
    public Product findCartProductEntryBySKU(String SKU) {
        for (Entry<Product, Integer> entry : cart.entrySet()) {
            Product productToFind = entry.getKey();
            if (productToFind != null && productToFind.getSKU().equals(SKU)) return productToFind;
        }
        return null;
    }

    public void addProductToCart(String SKU, int amount) throws InvalidParameterException, InsufficientInventoryException, ProductNotFoundException {
        // check if amount is postive
        if (amount <= 0) throw new InvalidParameterException("Product amount must be greater than 0.");
        
        // check that the product exists in the warehouse
        Product product = Warehouse.findProductBySKU(SKU);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        // if product exists, check that the amount to add does not exceed warehouse inventory
        else {
            Product productInCart = findCartProductEntryBySKU(SKU);
            Integer amountInWarehouse = Warehouse.findProductInventoryBySKU(SKU);
            if ((productInCart != null && cart.get(productInCart)+amount <= amountInWarehouse) || (productInCart == null && amount <= amountInWarehouse)) {
                // add to cart
                if (productInCart != null) cart.merge(productInCart, amount, (oldAmount, newAmount) -> oldAmount+newAmount);
                else cart.put(Warehouse.findProductBySKU(SKU), amount);
            }
            else throw new InsufficientInventoryException();

        }
    }

    public void removeProductFromCart(String SKU) throws ProductNotFoundException {
        // check if cart is empty
        if (!cart.isEmpty()) {
            Product productInCart = findCartProductEntryBySKU(SKU);
            // remove product if it's found in cart
            if (productInCart == null) throw new ProductNotFoundException("Product not found in cart.");
            else cart.remove(productInCart);
        }
    }

    public void modifyProductCountInCart(String SKU, int amount) throws InvalidParameterException, ProductNotFoundException, InsufficientInventoryException {
        // check if amount is positive
        if (amount <= 0) throw new InvalidParameterException("Product amount must be greater than 0.");
        
        // check if cart is empty
        if (!cart.isEmpty()) {
            Product productInCart = findCartProductEntryBySKU(SKU);

            // check if product exists in cart
            if (productInCart == null) throw new ProductNotFoundException("Product not found in cart.");
            else {
                // check if the new amount does not exceed warehouse inventory
                Integer amountInWarehouse = Warehouse.findProductInventoryBySKU(SKU);
                if (amount > amountInWarehouse) throw new InsufficientInventoryException();
                else cart.put(productInCart, amount);
            }
        }
    }

}