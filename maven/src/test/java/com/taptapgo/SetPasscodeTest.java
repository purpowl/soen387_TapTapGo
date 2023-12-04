package com.taptapgo;

import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.repository.OrderRepository;
import com.taptapgo.repository.UserRepository;
import com.taptapgo.repository.WarehouseRepository;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetPasscodeTest {

    static final String TEST_DB_NAME = "taptapgo_test.db";
    static final String REG_DB_NAME = "taptapgo.db";

    static User testGuest;
    static User testRegisteredStaff;
    // 2nd user solely for the purpose of testing uniqueness of new passcode
    static User testRegisteredNonStaff;

    @BeforeAll
    public static synchronized void setUp() throws InvalidParameterException, SQLException, ClassNotFoundException {
        // Set up any necessary test data or configurations
        UserRepository.setDBName(TEST_DB_NAME);
        OrderRepository.setDBName(TEST_DB_NAME);
        WarehouseRepository.setDBName(TEST_DB_NAME);
        UserRepository.clearUserTables();
        OrderRepository.clearOrderTables();
        WarehouseRepository.clearWarehouse();

        testGuest = User.createGuestUser("123456");
        testRegisteredStaff = User.loadRegisteredUser("rc00001", "testS", "testS", "testS", "testS", true);
        testRegisteredNonStaff = User.loadRegisteredUser( "rc00002", "testC", "testC", "testC", "testC", false);

        UserRepository.createGuestUserInDatabase(testGuest);
        UserRepository.createRegisteredUserInDB(testRegisteredStaff, "staff");
        UserRepository.createRegisteredUserInDB(testRegisteredNonStaff, "nonstaff");
    }

    @AfterAll
    public static synchronized void tearDown() throws SQLException, ClassNotFoundException {
        UserRepository.clearUserTables();
        OrderRepository.clearOrderTables();
        WarehouseRepository.clearWarehouse();

        UserRepository.setDBName(REG_DB_NAME);
        OrderRepository.setDBName(REG_DB_NAME);
        WarehouseRepository.setDBName(TEST_DB_NAME);
    }

    @Test
    public synchronized void testSetPasscode() throws InvalidParameterException {
        // case 1: guest user
        assertThrows(InvalidParameterException.class, () ->
                testGuest.setPasscode("random"), "Guest user cannot change passcode");

        // case 2: registered user and new passcode is blank
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode(""), "New passcode cannot be blank");

        // case 3: registered user and new passcode is blank
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode(null), "New passcode cannot be null");

        // case 4: registered user and new passcode is <4 characters
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("one"), "New passcode must be longer than 4 characters");

        // case 5: registered user and new passcode is non-alphanumeric
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("!@#$%^&*()"), "New passcode must be alphanumeric");

        // case 6: registered user and new passcode is the same as old passcode
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("staff"), "New passcode must be different from the old passcode");

        // case 7: registered user and new passcode is the same as another user's passcode
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("nonstaff"), "New passcode must be different from the old passcode");

        // case 8: registered user and valid new passcode
        assertTrue(testRegisteredStaff.setPasscode("newpasscode123"));
    }
}