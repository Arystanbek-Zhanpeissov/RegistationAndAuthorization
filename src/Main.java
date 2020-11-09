import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Main {

    public static int newInput(){
        Scanner scan = new Scanner(System.in);
        int newAge;
        try {
            String age = scan.next();
            newAge = Integer.parseInt(age);
            return newAge;
        } catch (NumberFormatException e) {
            System.out.println("Please enter number");
            return newInput();
        }
    }

    public static void registation() throws SQLException {
        DBConnection db = new DBConnection();
        Scanner scan = new Scanner(System.in);
        int newAge;
        try {
            System.out.println("Enter your name");
            String name = scan.next();
            System.out.println("Enter your username");
            String username = scan.next();
            System.out.println("Enter your age");
            int age = newInput();
            System.out.println("Enter your email");
            String email = scan.next();
            System.out.println("Enter your password");
            String password = scan.next();
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
            String query = "Insert into \"InAndUpTable\" (name, username, email, password, age) values (?,?,?,?,?); ";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, email);
            stmt.setString(4, hashed);
            stmt.setInt(5, age);
            stmt.executeUpdate();
            System.out.println("Data Saved");
        } catch (SQLException e) {
            System.out.println("SQLException");
        } catch (InputMismatchException e) {
            System.out.println("Enter number please");
        }
    }

    public static void authorization() throws SQLException {
        DBConnection db = new DBConnection();
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("Enter your username");
            String username = scan.next();
            System.out.println("Enter your password");
            String password = scan.next();
            Statement st = db.getConnection().createStatement();
            String query = "SELECT username, password FROM \"InAndUpTable\";";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                if (BCrypt.checkpw(password, rs.getString("password")) && username.equals(rs.getString("username")))
                {System.out.println("It matches");
                    break;}
                else {
                    System.out.println("It does not match");
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException");
        }
    }
    public static void listOfUsers() throws SQLException {
        DBConnection db = new DBConnection();
        Scanner scan = new Scanner(System.in);
        try {
            Statement st = db.getConnection().createStatement();
            String query = "SELECT * FROM \"InAndUpTable\";";
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                System.out.print(rs.getInt("id")+ " ");
                System.out.print(rs.getString("name")+ " ");
                System.out.print(rs.getString("username")+ " ");
                System.out.print(rs.getString("email")+ " ");
                System.out.print(rs.getInt("age")+ " ");
                System.out.println(rs.getString("password"));
                System.out.println();
            }
        }catch (SQLException e) {
            System.out.println("SQLException");
        }
    }

    public static void deleteUser() throws SQLException {
        DBConnection db = new DBConnection();
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("Enter your id");
            int id = scan.nextInt();
            Statement st = db.getConnection().createStatement();
            String query = "Delete from \"InAndUpTable\" where id = ?; ";
            PreparedStatement stmt = db.getConnection().prepareStatement(query);
            stmt.setInt(1,id);
            stmt.executeUpdate();
            System.out.println("Data Saved");
        }catch (SQLException e) {
            System.out.println("SQLException");
        }
    }


    public static void main(String[] args) throws SQLException {
        Scanner scan = new Scanner(System.in);
        boolean check = true;
        while(check){
        System.out.println("Registration = 1, Authorization = 2, Break = 0");
        int x = scan.nextInt();
            switch (x) {
                case 1:
                    registation();
                    break;
                case 2:
                    authorization();
                    while (check){
                        System.out.println("List of Users = 1, Delete User = 2, Break = 0");
                        int a = scan.nextInt();
                        switch (a){
                            case 1:
                                listOfUsers();
                                break;
                            case 2:
                                deleteUser();
                                break;
                            case 0:
                                check = false;

                        }
                    }
                    break;
                case 0:
                    check = false;
            }
        }
    }
}