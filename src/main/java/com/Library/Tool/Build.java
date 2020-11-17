package com.Library.Tool;

/**
 * This {@code Build} interface provides the
 * abstract methods to be implemented by the (App.class)
 * for the interface of the LMT project
 */
public interface Build {
    public void welPage();
    public void navCheck();
    public void showTrendBooks();
    public void search();
    /**
     * Inner interface (Account) build like (Build$Account.class) deals with the
     * account management sector
     */
    interface Account {
        public void signUpAccount();
        public void logInAccount();
        public void updateAccount();
        public void deleteAccount();
    }
}
