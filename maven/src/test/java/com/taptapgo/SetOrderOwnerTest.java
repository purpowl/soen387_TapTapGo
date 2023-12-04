package com.taptapgo;

import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.repository.OrderIdentityMap;
import com.taptapgo.repository.OrderRepository;
import com.taptapgo.repository.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SetOrderOwnerTest {

    static final String TEST_DB_NAME = "taptapgo_test.db";
    static Connection db_conn;
    static Order testOrderGuest;
    static Order testOrderRegistered;
    static Order testOrderRegistered2;

    static User testGuest;

    static User testRegistered;
    static User testRegistered2;

    @BeforeAll
    public static synchronized void setUp() throws InvalidParameterException, ClassNotFoundException, SQLException {
        // Set up any necessary test data or configurations
        testGuest = User.createGuestUser("123456");
        testRegistered = User.loadRegisteredUser("rc00001", "testS", "testS", "testS", "testS", true);
        testRegistered2 = User.loadRegisteredUser( "rc00002", "testC", "testC", "testC", "testC", false);

        HashMap<Product, Integer> testCart = new HashMap<>();
        testCart.put(new Product("KB005", "TGR Alice", "Description: Made of thick PBT and dye-sub legends, these corny keycaps will be a delicious addition to your keyboard setup.","TGR", "TGR_KB005", 463), 3);
        testOrderGuest = Order.createOrder("1234 Halley Street", "NU", "CA", "12345", "credit", 1234, "1234 Halley Street", "NU", "CA", "12345", testCart, "gc123456" );
        testOrderRegistered = Order.createOrder("1234 Halley Street", "NU", "CA", "12345", "credit", 1234, "1234 Halley Street", "NU", "CA", "12345", testCart, "rc00001" );
        testOrderRegistered2 = Order.createOrder("1234 Halley Street", "NU", "CA", "12345", "credit", 1234, "1234 Halley Street", "NU", "CA", "12345", testCart, "rc00002" );

        UserRepository.setDBName("taptapgo_test.db");
        OrderRepository.setDBName("taptapgo_test.db");
        UserRepository.clearUserTables();
        OrderRepository.clearOrderTables();

        UserRepository.createGuestUserInDatabase(testGuest);
        UserRepository.createRegisteredUserInDB(testRegistered, "pass123");
        UserRepository.createRegisteredUserInDB(testRegistered2, "pass234");
        OrderRepository.createOrder(testOrderGuest);
        OrderRepository.createOrder(testOrderRegistered);
        OrderRepository.createOrder(testOrderRegistered2);
    }

    @AfterAll
    public static synchronized void tearDown() throws SQLException, ClassNotFoundException {
        UserRepository.clearUserTables();
        OrderRepository.clearOrderTables();
        UserRepository.setDBName("taptapgo.db");
        OrderRepository.setDBName("taptapgo.db");
    }

    @Test
    public synchronized void testSetOrderOwner() throws InvalidParameterException {
        // case 1: invalid order ID format
        assertThrows(InvalidParameterException.class, () ->
                OrderIdentityMap.setOrderOwner(1, "gc123456"), "Invalid order ID format.");

        // case 2: blank customer ID
        assertThrows(InvalidParameterException.class, () ->
                OrderIdentityMap.setOrderOwner(10001, null), "Customer ID can't be blank.");

        // case 3: claiming registered user's order
        assertThrows(InvalidParameterException.class, () ->
                OrderIdentityMap.setOrderOwner(10003, "rc00001"), "Cannot claim an order from a registered user.");

        assertTrue(OrderIdentityMap.setOrderOwner(10001, "rc00001"));
    }
}
