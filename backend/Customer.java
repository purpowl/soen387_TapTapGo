import Exceptions.ProductNotFoundException;
import Exceptions.InsufficientInventoryException;
import Exceptions.InvalidParameterException;
import java.util.HashMap;
import java.util.Map.Entry;

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
            if (productToFind.getSKU().equals(SKU)) {
                return productToFind;
            }
        }
        return null;
    }

    public void addProductToCart(String SKU, int amount) throws InvalidParameterException, InsufficientInventoryException, ProductNotFoundException {
        // check if amount is postive
        if (amount < 0) throw new InvalidParameterException("Product amount cannot be less than 0.");
        
        // check that the product exists in the warehouse
        Product product = Warehouse.findProductBySKU(SKU);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        // if product exists, check that the amount to add does not exceed warehouse inventory
        else {
            Integer amountInWarehouse = Warehouse.findProductInventoryBySKU(SKU);
            Product productInCart = findCartProductEntryBySKU(SKU);
            if (cart.get(productInCart)+amount > amountInWarehouse) {
                throw new InsufficientInventoryException();
            }
            else {
                // add the amount to cart
                cart.merge(productInCart, amount, (oldAmount, newAmount) -> oldAmount+newAmount);
            }
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
        if (amount < 0) throw new InvalidParameterException("Product amount cannot be less than 0.");
        
        // check if cart is empty
        if (!cart.isEmpty()) {
            Product productInCart = findCartProductEntryBySKU(SKU);

            // check if product exists in cart
            if (productInCart == null) {
                throw new ProductNotFoundException("Product not found in cart.");
            }
            else {
                // check if the new amount does not exceed warehouse inventory
                Integer amountInWarehouse = Warehouse.findProductInventoryBySKU(SKU);
                if (amount <= amountInWarehouse) throw new InsufficientInventoryException();
                else cart.put(productInCart, amount);
            }
        }
    }

}