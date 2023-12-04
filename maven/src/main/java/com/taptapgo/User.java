package com.taptapgo;

import com.taptapgo.exceptions.InsufficientInventoryException;
import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.exceptions.ProductNotFoundException;
import com.taptapgo.repository.UserIdentityMap;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private static AtomicInteger registeredIDGen = new AtomicInteger(0);
    protected String userID;
    protected String firstName;
    protected String lastName;
    protected String phone;
    protected String email;
    protected HashMap<Product, Integer> cart;
    protected boolean isStaff;
    protected boolean isRegisteredUser;

    /**
     * private User constructor to create guest user with only session ID
     * @param sessionID string
     * @throws InvalidParameterException from Customer constructor if unable to create guest customer
     */
    private User(String sessionID) throws InvalidParameterException {
        this.userID = "gc" + sessionID;
        this.firstName = null;
        this.lastName = null;
        this.phone = null;
        this.email = null;
        this.isStaff = false;
        this.isRegisteredUser = false;
    }

    /**
     * private User constructor to create guest user with session ID and personal info
     * @param sessionID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @throws InvalidParameterException from Customer constructor if unable to create guest customer
     */
    private User(String sessionID, String firstName, String lastName, String phone, String email) throws InvalidParameterException {
        this.userID = "gc" + sessionID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        // guest cannot be staff
        this.isStaff = false;
        this.isRegisteredUser = false;
    }

    /**
     * private User constructor to create registered user with no ID yet
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @throws InvalidParameterException from Customer constructor if unable to create registered customer
     */
    private User(String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        registeredIDGen.incrementAndGet();
        this.userID = "rc" + String.format("%05d", registeredIDGen.get());
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.isStaff = isStaff;
        this.isRegisteredUser = true;
    }

    /**
     * private User constructor to create registered user with existing ID
     * @param userID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @throws InvalidParameterException from Customer constructor if unable to create registered customer
     */
    private User(String userID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.isStaff = isStaff;
        this.isRegisteredUser = true;
    }

    /**
     * public method to create registered user with existing ID
     * @param userID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create registered customer
     */
    public static User loadRegisteredUser(String userID, String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        return new User(userID, firstName, lastName, phone, email, isStaff);
    }

    /**
     * public method to create registered user with new ID
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @param isStaff boolean
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create registered customer
     */
    public static User createRegisteredUser(String firstName, String lastName, String phone, String email, boolean isStaff) throws InvalidParameterException {
        return new User(firstName, lastName, phone, email, isStaff);
    }

    /**
     * public method to create guest user with only session ID
     * @param sessionID string
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create guest customer
     */
    public static User createGuestUser(String sessionID) throws InvalidParameterException {
        return new User(sessionID);
    }

    /**
     * public method to create guest user with session ID and personal info
     * @param sessionID string
     * @param firstName string
     * @param lastName string
     * @param phone string
     * @param email string
     * @return User object
     * @throws InvalidParameterException from private constructor if unable to create guest customer
     */
    public static User createGuestUserWithInfo(String sessionID, String firstName, String lastName, String phone, String email) throws InvalidParameterException {
        return new User(sessionID, firstName, lastName, phone, email);
    }

    /**
     * check if user is a registered user
     * @return true if registered, false if not
     */
    public boolean isRegisteredUser() {
        return this.isRegisteredUser;
    }

    /**
     * check if user is staff
     * @return true if staff, false if not
     */
    public boolean isStaff() {
        return this.isStaff;
    }

    /**
     * add this user (guest or registered) to the database
     * @param passcode the passcode of the new user
     * @return true if successfully set, false if not
     */
    public boolean addUserToDB(String passcode) throws InvalidParameterException {
        if (this.isRegisteredUser()) {
            return UserIdentityMap.addRegisteredUserToDB(this, passcode);
        }
        else {
            return UserIdentityMap.addGuestUserToDB(this);
        }
    }

    /**
     * Set the passcode for an existing registered user
     * @param newPasscode the passcode we want to set
     * @return true if successfully set, false if not
     * @throws InvalidParameterException if user is not registered, or unable to set new passcode
     */
    public boolean setPasscode(String newPasscode) throws InvalidParameterException {
        // only registered customer can change passcode, guest customer must go through sign up
        if (!this.isRegisteredUser()) {
            throw new InvalidParameterException("User must be registered to change passcode");
        }
        else if (newPasscode.isEmpty()) {
            throw new InvalidParameterException("New passcode cannot be blank");
        }
        else if (newPasscode.length() < 4) {
            throw new InvalidParameterException("New passcode must be 4 characters or more");
        }
        else if (!(StringUtils.isAlphanumeric(newPasscode))) {
            throw new InvalidParameterException("New passcode must be alphanumeric");
        }

        // change the user passcode
        return UserIdentityMap.changeUserPasscode(this.userID, newPasscode);
    }

    /**
     * Overloaded setPasscode with the testDBName for unit testing purposes
     * @param newPasscode the passcode we want to set
     * @param testDBName name of the test SQLite database file
     * @return true if successfully set, false if not
     * @throws InvalidParameterException if user is not registered, or unable to set new passcode
     */
    public boolean setPasscode(String newPasscode, String testDBName) throws InvalidParameterException {
        // only registered customer can change passcode, guest customer must go through sign up
        if (!this.isRegisteredUser()) {
            throw new InvalidParameterException("User must be registered to change passcode");
        }
        else if (newPasscode.isEmpty()) {
            throw new InvalidParameterException("New passcode cannot be blank");
        }
        else if (newPasscode.length() < 4) {
            throw new InvalidParameterException("New passcode must be 4 characters or more");
        }
        else if (!(StringUtils.isAlphanumeric(newPasscode))) {
            throw new InvalidParameterException("New passcode must be alphanumeric");
        }

        // change the user passcode
        return UserIdentityMap.changeUserPasscode(this.userID, newPasscode, testDBName);
    }



    public String getUserID() {
        return this.userID;
    }

    public static void updateRegisteredIDGen(int maxID) {
        registeredIDGen = new AtomicInteger(maxID);
    }

    public HashMap<Product, Integer> getCart() {
        return this.cart;
    }

    public void loadCart(HashMap<Product, Integer> cart) {
        this.cart = cart;
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
        Product product = Warehouse.getInstance().findProductBySKU(SKU);
        if (product == null) {
            throw new ProductNotFoundException();
        }
        // if product exists, check that the amount to add does not exceed warehouse inventory
        else {
            Product productInCart = findCartProductEntryBySKU(SKU);
            Integer amountInWarehouse = Warehouse.getInstance().getProductInventoryBySKU(SKU);
            if ((productInCart != null && cart.get(productInCart)+amount <= amountInWarehouse) || (productInCart == null && amount <= amountInWarehouse)) {
                // add to cart
                if (productInCart != null) cart.merge(productInCart, amount, (oldAmount, newAmount) -> oldAmount + newAmount);
                else cart.put(Warehouse.getInstance().findProductBySKU(SKU), amount);
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
                Integer amountInWarehouse = Warehouse.getInstance().getProductInventoryBySKU(SKU);
                if (amount > amountInWarehouse) throw new InsufficientInventoryException();
                else cart.put(productInCart, amount);
            }
        }
    }

    public void clearCart() {
        this.cart = new HashMap<>();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setStaff(boolean isStaff) {
        this.isStaff = isStaff;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null){
            return false;
        }
        if (!(object.getClass().equals(this.getClass()))){
            return false;
        }

        User otherUser = (User) object;

        return otherUser.userID.equals(this.userID) && otherUser.firstName.equals(this.firstName) && otherUser.lastName.equals(this.lastName);
    }
 }