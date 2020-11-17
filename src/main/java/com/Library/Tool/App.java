package com.Library.Tool;

/**
 * The {@code App} is the implementation of an online library management tool
 * that can be used for gaining efficient knowledge about how a Book site works or provide
 * productive information to the customers and share different trends(best selling books) and links of various books.
 * Also,this service provides you an interactive way to share or sell your books with the help
 * of sites like Amazon.
 */

/**
 * @author Techie
 * @since  30/10/2020
 * @version jdk-8
 * @see org.json.simple
 * @see javax.swing
 * @see java.awt
 * @see java.io
 * @see java.net
 */
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class App extends UserData implements Build {
    /**
     * The method simply deploys the welcome page for the {@code App}
     * where it provides an interface to the user for further works relevant to the project.
     */
    @Override
    public void welPage() {
        String dec;
        @SuppressWarnings("resource")
        Scanner sc = new Scanner(System.in);
        System.out.println("~~~~~~~~~~~~~~~~~~~");
        System.out.println("| Welcome User!!  |");
        System.out.println("~~~~~~~~~~~~~~~~~~~");
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        System.out.println("|  Home  |  About  |  SignUp  |  SellBooks  |  Forums  |  Trending Books  |  UpdateAccount  |  Delete Account  |");
        System.out.println("----------------------------------------------------------------------------------------------------------------");
        System.out.println("Want to check any of them ? (Type 'yes' or 'no' in the next line)");
        dec = sc.nextLine();
        if(dec.equalsIgnoreCase("yes")) {
            /*A callback for the the method (.navCheck()) for checking the navigation bar pages */
            navCheck();
        }else {
            System.out.println("Okay, you can check other stuffs");
            /*A callback for the method (.search()) where user can have a look on the searching interface*/
            search();
        }
    }
    /**
     * This method allows the user to select their chosen pages and also invokes some methods
     * with conditional options and exit ways
     */
    @Override
    public void navCheck() {
        String choose;
        @SuppressWarnings("resource")
        Scanner scan = new Scanner(System.in);
        System.out.println("Kindly enter the page name (without any space) : ");
        choose = scan.nextLine().toLowerCase();
        //Instantiated an object (user) which allows a way to use 'UserData' class methods
        UserData user = new UserData();
        switch(choose) {
            case "home":
                welPage();
                break;
            case "about":
                aboutSec();
                break;
            case "signup":
                user.signUpAccount();
                break;
            case "sellbooks":
                System.out.println("You can check this link -> https://services.amazon.in/services/sell-on-amazon/benefits.html?ld=inrbinginkenshoo_502X692865_e_c_76278759274033_asret_");
                search();
                break;
            case "forums":
                forumsSec();
                break;
            case "trendingbooks":
                showTrendBooks();
                break;
            case "updateaccount":
                user.updateAccount();
            case "deleteaccount":
                user.deleteAccount();
            default:
                System.out.println("Oops!! something went wrong,try again");
                navCheck();
        }
    }
    /**
     * The (.search()) method is an integral part of {@code App} that allows
     * the user to search for various books or categories depending upon their choices
     * and sets the path for other necessary methods*/
    @Override
    public void search() {
        String book_name;
        String dec;
        String choose;
        String category;
        @SuppressWarnings("resource")
        Scanner sc1 = new Scanner(System.in);
        System.out.println("--------------");
        System.out.println("| SEARCH BAR |");
        System.out.println("--------------");
        System.out.println("Want to search any book or a category of books ? (Type 'yes' or 'no' in the next line) ");
        dec = sc1.nextLine();
        if(dec.equalsIgnoreCase("yes")) {
            System.out.println("Want to search a category or a specific book ? (Type either 'category' or 'books') ");
            choose = sc1.nextLine();
            if(choose.equalsIgnoreCase("category")) {
                System.out.println("Kindly enter the category name (Spellings sensitive) : ");
                category = sc1.nextLine();
                /*Invokes the (.displayCategory()) method after user enters the category name*/
                displayCategory(category);
            }
            System.out.println("Kindly enter the book name (Spellings sensitive) : ");
            book_name = sc1.nextLine();
            /*Calls the (.displayBook()) method after user enters the book name*/
            displayBook(book_name);
        }else { navChoose();}
    }
    /**
     * This method searches the internet for the book details and returns
     * the details like book name,author,description,links and with images too
     * @param  book
     *         that is to be added in the (.search()) method's user inputs
     * @throws FileNotFoundException (If that particular file is not found)
     *         MalformedURLException (If the URL is not a valid one)
     *         NoSuchBookException (If no such books of that particular name are found)
     *         IOException (If I/O - Input/Output issue with the code)
     *         ParseException (If parsing errors with the JSON code)
     */
    private void displayBook(String book) {
        Account();
        String key = null;
        File file = new File("G:/Important/APIKey.txt");
        try(Scanner sc2 = new Scanner(file)) {
            while(sc2.hasNextLine()) {
                key = sc2.nextLine();
            }
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        String raw = "https://www.googleapis.com/books/v1/volumes?q="+book+"+inauthor:keyes&key="+key;
        try{
            /*Creates a connection with the url and retrieves the information*/
            URL url = new URL(raw);
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer str = new StringBuffer();
            String line;
            while((line = br.readLine()) != null) {str.append(line);}
            JSONParser parse = new JSONParser();
            //Parsing it to JSONObject objects
            JSONObject json_obj = (JSONObject)parse.parse(str.toString());
            JSONArray json_array = (JSONArray)json_obj.get("items");
            String img = "";
            for(int i = 0; i < json_array.size(); i++) {
                JSONObject json1 = (JSONObject)json_array.get(i);
                JSONObject json2 = (JSONObject) json1.get("volumeInfo");
                JSONObject json3 = (JSONObject)json2.get("imageLinks");
                if(json3 != null && json3.containsKey("thumbnail")) {
                    img = (String)json3.get("thumbnail");
                }
                String title = (String)json2.get("title");
                System.out.println("-----------------------------------------------------------------------------");
                System.out.println("Title : "+title);
                JSONArray j_arr = (JSONArray)json2.get("authors");
                for(Object j : j_arr) {
                    System.out.println("Authors : "+j);
                }
                String pubdate = (String)json2.get("publishedDate");
                String des = (String)json2.get("description");
                String link = (String)json1.get("selfLink");

                System.out.println("Published Date : "+pubdate);
                System.out.println("Description : "+des);
                System.out.println("Book link : "+link);
                System.out.println("-----------------------------------------------------------------------------");
                /*Displaying the thumbnail image with JFrame and JLabel with some enhancement in the size*/
                URL url2 = new URL(img);
                BufferedImage image = ImageIO.read(url2);
                ImageIcon icon = new ImageIcon(image);
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.setSize(400,400);
                JLabel label = new JLabel();
                label.setIcon(icon);
                frame.add(label);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        }catch(MalformedURLException e) {
            try {
                throw new NoSuchBookException("<< Wrong Book name >>");
            } catch (NoSuchBookException e2) {
                e2.printStackTrace();
            }
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        navChoose();
        exit();
    }
    /**
     * This method displays a category selected by the user with images
     * @param category
     *        Added in the (.search()) method as the category name
     * @throws FileNotFoundException (If that particular file is not found)
     *         MalformedURLException (If the URL is not a valid one)
     *         IOException (If I/O - Input/Output issue with the code)
     *         ParseException (If parsing errors with the JSON code)*/
    private void displayCategory(String category) {
        Account();
        String key = null;
        //An API key necessary to unlock the pages(hidden)
        File file = new File("G:/Important/APIKey.txt");
        try(Scanner sc2 = new Scanner(file)) {
            while(sc2.hasNextLine()) {
                key = sc2.nextLine();
            }
        }catch(FileNotFoundException exp) {
            exp.printStackTrace();
        }
        String raw = "https://www.googleapis.com/books/v1/volumes?q=subject:"+category;
        try{
            URL url = new URL(raw);
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuffer str = new StringBuffer();
            String line;
            while((line = br.readLine()) != null) {str.append(line);}
            JSONParser parse = new JSONParser();
            JSONObject json_obj = (JSONObject)parse.parse(str.toString());
            JSONArray json_array = (JSONArray)json_obj.get("items");
            String img = "";
            for(int i = 0; i < json_array.size(); i++) {
                JSONObject json1 = (JSONObject) json_array.get(i);
                JSONObject json2 = (JSONObject) json1.get("volumeInfo");
                JSONObject json3 = (JSONObject) json2.get("imageLinks");
                if (json3 != null && json3.containsKey("thumbnail")) {
                    img = (String) json3.get("thumbnail");
                }
                String title = (String) json2.get("title");
                System.out.println("-----------------------------------------------------------------------------");
                System.out.println("Title : " + title);
                JSONArray j_arr = (JSONArray) json2.get("authors");
                for (Object j : j_arr) {
                    System.out.println("Authors : " + j);
                }
                String pubdate = (String) json2.get("publishedDate");
                String des = (String) json2.get("description");
                String link = (String) json1.get("selfLink");

                System.out.println("Published Date : " + pubdate);
                System.out.println("Description : " + des);
                System.out.println("Book link : " + link);
                System.out.println("-----------------------------------------------------------------------------");
                URL url2 = new URL(img);
                BufferedImage image = ImageIO.read(url2);
                ImageIcon icon = new ImageIcon(image);
                JFrame frame = new JFrame();
                frame.setLayout(new FlowLayout());
                frame.setSize(400,400);
                JLabel label = new JLabel();
                label.setIcon(icon);
                frame.add(label);
                frame.setVisible(true);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        } catch(IOException | ParseException e) {
            e.printStackTrace();
        }
        navChoose();
        exit();
    }
    /**
     * A method that displays the trending books for the {@code App} with the help of the default browser
     * of the Amazon best seller site
     * @throws  IOException (If there is a problem in the I/O process)
     */
    @Override
    public void showTrendBooks() {
        String link = "https://www.amazon.in/gp/bestsellers/books";
        try {
            //Opens the url with the help of the default browser
            Desktop.getDesktop().browse(new URI(link));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        search();
    }
    /**
     * This method deploys the about section of the
     * {@code App} with an image of the topic of this tool
     * @throws  IOException (If there is a problem in the I/O process)
     */
    private void aboutSec() {
        try{
            BufferedImage img = ImageIO.read(new File("G:/LibraryManagementTool/Image/library.jpg"));
            ImageIcon icon = new ImageIcon(img);
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(400,400);
            JLabel label = new JLabel();
            label.setIcon(icon);
            frame.add(label);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }catch(IOException e) {e.printStackTrace();}
        System.out.println("-----------------------------------------------------------------------------------------------------");
        System.out.println("It's a library management tool that helps us in searching, fetching and publishing different kinds of");
        System.out.println("books and also get the details of the trending books and get a chance to discover various new books");
        System.out.println("via the category corner.( In Brief Details)");
        System.out.println("-----------------------------------------------------------------------------------------------------");
        search();
    }
    /**
     * The (.forumsSec()) method deals with the forum section
     * of the project
     * @throws  IOException (If there is a problem in the I/O process)
     */
    public void forumsSec() {
        String forum;
        @SuppressWarnings("resource")
        Scanner sc3 = new Scanner(System.in);
        System.out.println("Kindly enter your suggestion or new ideas that might help us : ");
        forum = sc3.nextLine();
        try(FileWriter file_write = new FileWriter("G:/LibraryManagementTool/forums.txt",true)) {
            file_write.write(forum);
            System.out.println("<< Thanks for your suggestion or ideas >>");
        }catch(IOException e) {
            e.printStackTrace();
        }
        //Recalls the (.search()) method
        search();
    }
    /**
     * A method for replicating the navigation section part if
     * condition is true
     */
    private void navChoose() {
        String choose;
        Scanner sc4 = new Scanner(System.in);
        System.out.println("Want to check the navigation pages ? (Type 'yes' or 'no' in the next line)");
        choose = sc4.nextLine();
        if(choose.equalsIgnoreCase("yes")) {
            navCheck();
        }else{
            exit();
        }
    }
    /**
     * Method that deals with the account creation,update,deletion,etc.
     * for the {@code App}
     */
    private void Account() {
        UserData user = new UserData();
        @SuppressWarnings("resource")
        Scanner sc5 = new Scanner(System.in);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("Want to sign up or log in to your LMT account ? (Type 'yes' or 'no' in the next line)");
        String choice = sc5.nextLine();
        if(choice.equalsIgnoreCase("yes")) {
            System.out.println("Want to sign up or log in? (Type either 'sign' or 'log')");
            String dec = sc5.nextLine();
            if(dec.equalsIgnoreCase("sign")) {
                user.signUpAccount();
            }else {
                user.logInAccount();
            }
        }else{
            System.out.println("Okay,continue what you were doing");
        }
    }
    /**
     * This method exits the program
     * only if the condition is true
     */
    private void exit() {
        String choice;
        Scanner sc6 = new Scanner(System.in);
        System.out.println("Want to exit the program ? (Type 'yes' or 'no' in the next line)");
        choice = sc6.nextLine();
        if(choice.equalsIgnoreCase("yes")) {
            System.out.println("App shutting down.....");
            System.exit(0);
        }else {
            System.out.println("Okay continue your tasks");
            welPage();
        }
    }
}
/**
 * (Class) that throws an Exception if that
 * particular condition is not valid
 * @exception NoSuchBookException
 */
class NoSuchBookException extends Exception {
    public NoSuchBookException(String msg) {
        super(msg);
    }
}
