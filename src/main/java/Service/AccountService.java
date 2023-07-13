package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    /**
     * Registers a new account with the given username and password.
     *
     * @param username the username for the account
     * @param password the password for the account
     * @return the registered Account object if successful, or null if not
     */
    public Account registerAccount(String username, String password) {
        if (username.isBlank() || password.length() < 4 || (accountDAO.getAccountByUsername(username) != null)) {
            return null;
        }

        Account registeredAccount = accountDAO.insertAccount(username, password);
        if (registeredAccount != null) {
            return registeredAccount;
        } else {
            return null;
        }
    }

    /**
     * Performs the login process for the specified username and password.
     *
     * @param username the username for the account
     * @param password the password for the account
     * @return the Account object if login is successful, or null if not
     */
    public Account login(String username, String password) {
        Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
        if (account != null) {
            return account;
        }
        return null;
    }
}
