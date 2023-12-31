package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    /**
     * Retrieve all accounts from the account table.
     *
     * @return all accounts
     */
    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();

        try {
            String sql = "SELECT * FROM account";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getInt("Account_id"), rs.getString("user_name"),
                        rs.getString("password"));
                accounts.add(account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    /**
     * Retrieve a specific Account using its Account ID.
     *
     * @param id a Account ID.
     * @return the Account object with the specified ID, or null if not found
     */
    public Account getAccountById(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE Account_ID = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Account account = new Account(rs.getInt("Account_id"), rs.getString("user_name"),
                        rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Add a new Account record into the database.
     *
     * @param account an object representing an Account. The Account object does not
     *                contain an Account ID.
     * @return the newly inserted Account object with the generated Account ID, or
     *         null if not successful
     */
    public Account insertAccount(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedAccountId = generatedKeys.getInt(1);
                return new Account(generatedAccountId, username, password);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Update the Account identified by the Account ID with the values contained in
     * the Account object.
     *
     * @param id      a Account ID.
     * @param account a Account object. The Account object does not contain an
     *                Account ID.
     */
    public void updateAccount(int id, Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE account SET user_name = ?, password = ? WHERE Account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
            preparedStatement.setInt(3, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Delete the Account identified by the Account ID.
     *
     * @param id a Account ID.
     */
    public void deleteAccount(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM account WHERE Account_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Retrieve a specific Account using its username and password.
     *
     * @param username the username of the Account.
     * @param password the password of the Account.
     * @return the Account object with the specified username and password, or null
     *         if not found.
     */
    public Account getAccountByUsernameAndPassword(String username, String password) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"),
                        rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Retrieve an Account using its username.
     *
     * @param username the username of the Account.
     * @return the Account object with the specified username, or null if not found.
     */
    public Account getAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE user_name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Account account = new Account(rs.getInt("account_id"), rs.getString("user_name"),
                        rs.getString("password"));
                return account;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Check if an account with the given username already exists.
     *
     * @param username the username to check.
     * @return true if the account exists, false otherwise.
     */
    public boolean doesAccountExistUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE user_name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Check if an account with the given account ID already exists.
     *
     * @param account_id the account ID to check.
     * @return true if the account exists, false otherwise.
     */
    public boolean doesAccountExistAccountID(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM account WHERE account_id  = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id);

            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}