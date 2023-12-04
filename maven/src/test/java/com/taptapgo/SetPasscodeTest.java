package com.taptapgo;

import com.taptapgo.exceptions.InvalidParameterException;
import com.taptapgo.repository.UserRepository;
import com.taptapgo.repository.WarehouseRepository;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SetPasscodeTest {

    static final String TEST_DB_NAME = "taptapgo_test.db";
    static final String[] TABLES = {"orderitem", "order", "registereduser", "guestuser"};
    static Connection db_conn;
    static User testGuest;
    static User testRegisteredStaff;
    // 2nd user solely for the purpose of testing uniqueness of new passcode
    static User testRegisteredNonStaff;

    @BeforeAll
    public static synchronized void setUp() throws InvalidParameterException, SQLException, ClassNotFoundException {
        // Set up any necessary test data or configurations
        testGuest = User.createGuestUser("123456");
        testRegisteredStaff = User.loadRegisteredUser("rc00001", "testS", "testS", "testS", "testS", true);
        testRegisteredNonStaff = User.loadRegisteredUser( "rc00002", "testC", "testC", "testC", "testC", false);

        clearTables();

        UserRepository.createGuestUserInDatabase(testGuest, TEST_DB_NAME);
        UserRepository.createRegisteredUserInDB(testRegisteredStaff, "staff", TEST_DB_NAME);
        UserRepository.createRegisteredUserInDB(testRegisteredNonStaff, "nonstaff", TEST_DB_NAME);
    }

    @AfterAll
    public static synchronized void tearDown() throws SQLException, ClassNotFoundException {
        clearTables();
    }

    @Test
    public synchronized void testSetPasscode() throws InvalidParameterException {
        // case 1: guest user
        assertThrows(InvalidParameterException.class, () ->
                testGuest.setPasscode("random", TEST_DB_NAME), "Guest user cannot change passcode");

        // case 2: registered user and new passcode is blank
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode(null, TEST_DB_NAME), "New passcode cannot be blank");

        // case 3: registered user and new passcode is <4 characters
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("one", TEST_DB_NAME), "New passcode must be longer than 4 characters");

        // case 4: registered user and new passcode is non-alphanumeric
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("!@#$%^&*()", TEST_DB_NAME), "New passcode must be alphanumeric");

        // case 5: registered user and new passcode is the same as old passcode
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("staff", TEST_DB_NAME), "New passcode must be different from the old passcode");

        // case 6: registered user and new passcode is the same as another user's passcode
        assertThrows(InvalidParameterException.class, () ->
                testRegisteredStaff.setPasscode("nonstaff", TEST_DB_NAME), "New passcode must be different from the old passcode");

        // case 7: registered user and valid new passcode
        assertTrue(testRegisteredStaff.setPasscode("newpasscode123", TEST_DB_NAME));
    }

    private static synchronized void clearTables() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        URL dbUrl = WarehouseRepository.class.getClassLoader().getResource(TEST_DB_NAME);
        db_conn = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);

        for (int i = 0; i < TABLES.length; i++) {
            PreparedStatement pstmt = db_conn.prepareStatement("DELETE FROM " + TABLES[i]);
            pstmt.executeUpdate();
        }

        db_conn.close();
    }
}