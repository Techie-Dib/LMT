package com.Library.Tool;

/**
 * This {@code UserData} class is mainly used for account management services
 * which includes account creation,updating,logging in and deletion.
 * It provides certain methods for the above mentioned tasks
 */
import java.sql.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Uses the Build.Account inner interface for
 * implementing the abstract methods
 */
public class UserData implements Build.Account {
    /**
     * This method deals with the signing in or creation of an account.
     * The account details are preserved securely in a SQL DB table which
     * can only be accessed by the admins under some privacy TOS
     * @throws ClassNotFoundException (If that particular jdbc class is not found)
     *         SQLException (If caused by SQL injection issues)
     *         InterruptedException (If two threads collide in the sleeping or waiting process)
     */
    @Override
    public void signUpAccount() {
        //The DB path with SOD
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        Random ran = new Random();
        @SuppressWarnings("resource")
        Scanner scan1 = new Scanner(System.in);
        @SuppressWarnings("resource")
        Scanner scan2 = new Scanner(System.in);
        System.out.println("--------------------------------------------------------");
        System.out.println("|                  SIGNUP ACCOUNT                      |");
        System.out.println("|                  --------------                      |");
        System.out.println("--------------------------------------------------------");
        System.out.println("Enter your FirstName : ");
        String Fname = scan1.nextLine();
        System.out.println("Enter your LastName : ");
        String Lname = scan1.nextLine();
        System.out.println("Enter your Email Address : ");
        String email = scan1.nextLine();
        System.out.println("Enter your Password (min 8 digit length) : ");
        String pass = scan1.nextLine();
        System.out.println("Enter your Mobile Number : ");
        long num = scan2.nextLong();
        try{
            int ID = ran.nextInt(100000000);
            String query = String.format("insert into USER_DATA values(1,'%s','%s','%s','%s',%d,%d)",Fname,Lname,email,pass,num,ID);
            Class.forName("oracle.jdbc.driver.OracleDriver");
            /*"*" is used as an account safety measure for admin account*/
            Connection con = DriverManager.getConnection(url,"Techie","2357");
            /*Executing the SQL query*/
            Statement st = con.createStatement();
            ResultSet rs = null;
            if(pass.length() >= 8 && email.contains("@")) {
                rs = st.executeQuery(query);
                Thread.sleep(1000);
                System.out.println("Account successfully created");
            }else {
                Thread.sleep(1000);
                System.out.println("<< Something wrong with your email or password, kindly try again >>");
                signUpAccount();
            }
            rs.close();
        }catch(ClassNotFoundException | SQLException | InterruptedException e) {
            e.printStackTrace();
        }
        new App().search();
    }
    /**
     * This method deals with the logging process of
     * the user's account
     * @throws ClassNotFoundException (If that particular jdbc class is not found)
     *         SQLException (If caused by SQL injection issues)
     *         InterruptedException (If two threads collide in the sleeping or waiting process)
     */
    @Override
    public void logInAccount() {
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String choice;
        String con_pass = "";
        String query = "";
        String email = "";
        int num;
        @SuppressWarnings("resource")
        Scanner scan1 = new Scanner(System.in);
        @SuppressWarnings("resource")
        Scanner scan2 = new Scanner(System.in);
        System.out.println("--------------------------------------------------------");
        System.out.println("|                  LOGIN ACCOUNT                       |");
        System.out.println("|                  --------------                      |");
        System.out.println("--------------------------------------------------------");
        System.out.println("Want to login through email address or phone number ? (Type either 'email' or 'number')");
        choice = scan1.nextLine();
        if(choice.equalsIgnoreCase("email")) {
            System.out.println("Enter your EmailAddress : ");
            email = scan1.nextLine();
            query = String.format("select Password from USER_DATA where Email = '%s'",email);
        }else{
            System.out.println("Enter your Mobile Number : ");
            num = scan2.nextInt();
            query = String.format("select Password from USER_DATA where MobileNo = %d",num);
        }
        System.out.println("Kindly enter your password : ");
        String pass = scan1.nextLine();
        try{
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url,"Techie","2357");
            Statement st = con.createStatement();
            ResultSet rs = null;
            if(pass.length() >= 8 && email.contains("@")) {
                rs = st.executeQuery(query);
            }else {
                Thread.sleep(1000);
                System.out.println("<< Something wrong with your email or password, kindly try again >>");
                logInAccount();
            }
            //Checks the password for every username
            while(rs.next()){
                con_pass = rs.getString(1);
            }
            if(con_pass.equals(pass)) {
                Thread.sleep(1000);
                System.out.println("Account successfully logged in");
            }else {
                Thread.sleep(1000);
                System.out.println("Invalid password!!,try again");
                logInAccount();
            }
        }catch(SQLException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
        new App().search();
    }
    /**
     * This method helps the user to update their account details
     * but only Names
     * @throws ClassNotFoundException (If that particular jdbc class is not found)
     *         SQLException (If caused by SQL injection issues)
     *         InterruptedException (If two threads collide in the sleeping or waiting process)
     */
    @Override
    public void updateAccount() {
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String query = "";
        String field;
        String username;
        @SuppressWarnings("resource")
        Scanner scan3 = new Scanner(System.in);
        System.out.println("--------------------------------------------------------");
        System.out.println("|                  UPDATE ACCOUNT                      |");
        System.out.println("|                  --------------                      |");
        System.out.println("--------------------------------------------------------");
        System.out.println("Kindly enter your username : ");
        username = scan3.nextLine();
        System.out.println("Kindly enter the field that you want to update (Only FirstName and LastName) : ");
        field = scan3.nextLine().toLowerCase();
        switch (field) {
            case "firstname":
                System.out.println("Enter your new first name : ");
                String f_name = scan3.nextLine();
                query = String.format("update USER_DATA set FirstName = '%s' where concat(FirstName,LastName) = '%s'", f_name, username);
                break;
            case "lastname":
                System.out.println("Enter your new last name : ");
                String l_name = scan3.nextLine();
                query = String.format("update USER_DATA set FirstName = '%s' where concat(FirstName,LastName) = '%s'", l_name, username);
                break;
        }
        try {
            String temp_query = "select concat(Firstname,Lastname) from Employees";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url, "Techie", "2357");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(temp_query);
            //Checks for all the usernames in the USER_DATA table
            while (rs.next()) {
                if (rs.getString(1).equalsIgnoreCase(username)) {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection con2 = DriverManager.getConnection(url, "Techie", "2357");
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st.executeQuery(query);
                    Thread.sleep(1000);
                    System.out.println("Account successfully updated");
                    rs.close();
                } else {
                    Thread.sleep(1000);
                    System.out.println("<< Not a valid username >>");
                }
            }
        } catch (ClassNotFoundException | SQLException | InterruptedException e) {
            e.printStackTrace();
        }
        new App().search();
    }
    /**
     * This methods enables the user to delete all the details from their account
     * @throws ClassNotFoundException (If that particular jdbc class is not found)
     *         SQLException (If caused by SQL injection issues)
     *         InterruptedException (If two threads collide in the sleeping or waiting process)
     */
    @Override
    public void deleteAccount() {
        String url = "jdbc:oracle:thin:@localhost:1521:ORCL";
        String name;
        @SuppressWarnings("resource")
        Scanner scan1 = new Scanner(System.in);
        System.out.println("--------------------------------------------------------");
        System.out.println("|                  Delete ACCOUNT                       |");
        System.out.println("|                  --------------                      |");
        System.out.println("--------------------------------------------------------");
        System.out.println("Enter your username : ");
        name = scan1.nextLine();
        String query = String.format("delete from USER_DATA where concat(FirstName,LastName) = '%s'",name);
        try {
            String temp_query = "select concat(Firstname,Lastname) from Employees";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url,"Techie","2357");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(temp_query);
            while(rs.next()) {
                if(rs.getString(1).equalsIgnoreCase(name)) {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    Connection con2 = DriverManager.getConnection(url,"Techie","2357");
                    Statement st2 = con.createStatement();
                    ResultSet rs2 = st.executeQuery(query);
                    Thread.sleep(1000);
                    System.out.println("Account successfully deleted");
                    rs.close();
                }else {
                    Thread.sleep(1000);
                    System.out.println("<< Not a valid username >>");
                }
            }
        }catch(ClassNotFoundException | SQLException | InterruptedException e) {
            e.printStackTrace();
        }
        new App().search();
    }
}
