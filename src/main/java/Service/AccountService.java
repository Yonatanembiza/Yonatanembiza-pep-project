package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private final AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public Account registerAccount(String username, String password) {
        if (username.isBlank() || password.length() < 4) {
            return null;
        }

        if (accountDAO.getAccountByUsername(username) != null) {
            return null;
        }

        Account registeredAccount = accountDAO.insertAccount(username, password);
        if (registeredAccount != null) {
            return registeredAccount;
        } else {
            return null;
        }
    }

    public Account login(String username, String password) {
        Account account = accountDAO.getAccountByUsernameAndPassword(username, password);
        if (account != null) {
            return account;
        }
        return null;
    }
}
