package com.pluralsight;

import java.awt.color.ICC_ColorSpace;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try(Scanner scanner = new Scanner(System.in)){
            if (args.length != 2) {
                System.out.println("Application needs two arguments to run: " +
                        "java com.pluralsight.Main <username> <password>");
                System.exit(1);
            }

            String username = args[0];
            String password = args[1];

            while (true) {
                System.out.println("What do you want to do?");
                System.out.println("1) Display all products");
                System.out.println("2) Display all customers");
                System.out.println("3) Display all Categories");
                System.out.println("0) Exit");
                System.out.print("Select an option: ");

                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        displayAllProducts(username, password);
                        break;
                    case 2:
                        displayAllCustomers(username, password);
                        break;
                    case 3:
                        displayAllCategories(username, password, scanner);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }
        }
    }

    private static void displayAllCategories(String username, String password, Scanner scanner){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind", username, password);
            ){
                String query = "SELECT CategoryID, CategoryName FROM northwind.categories;";

                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet results = statement.executeQuery();){

                    while (results.next()) {
                        int categoryID = results.getInt("CategoryID");
                        String categoryName = results.getString("CategoryName");

                        System.out.println("Category ID: " + categoryID);
                        System.out.println("Category Name: " + categoryName);
                        System.out.println("-----------------------------------------");
                    }
                }
                System.out.print("Select a category ID, to display all the products in that Category: ");
                int userCategory = scanner.nextInt();

                String CategoryQuery = """
                            SELECT ProductID, ProductName, UnitPrice, UnitsInStock
                            FROM products p
                            join categories c on p.CategoryID = c.CategoryID
                            where c.CategoryID = ?
                            """;
                try (PreparedStatement preparedStatement = connection.prepareStatement(CategoryQuery)){
                    preparedStatement.setInt(1, userCategory);

                    try(ResultSet results2 = preparedStatement.executeQuery();){
                        while (results2.next()) {
                            int productId = results2.getInt("ProductID");
                            String productName = results2.getString("ProductName");
                            double unitPrice = results2.getDouble("UnitPrice");
                            int unitsInStock = results2.getInt("UnitsInStock");

                            System.out.println("Product ID: " + productId);
                            System.out.println("Product Name: " + productName);
                            System.out.println("Unit Price: " + unitPrice);
                            System.out.println("Units In Stock: " + unitsInStock);
                            System.out.println("-----------------------------------------");
                        }
                    }

                }

            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllProducts(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind", username, password);
            ){
                String query = "SELECT ProductID, ProductName, UnitPrice, UnitsInStock FROM products";

                try (PreparedStatement statement = connection.prepareStatement(query);
                     ResultSet results = statement.executeQuery();){

                    while (results.next()) {
                        int productId = results.getInt("ProductID");
                        String productName = results.getString("ProductName");
                        double unitPrice = results.getDouble("UnitPrice");
                        int unitsInStock = results.getInt("UnitsInStock");

                        System.out.println("Product ID: " + productId);
                        System.out.println("Product Name: " + productName);
                        System.out.println("Unit Price: " + unitPrice);
                        System.out.println("Units In Stock: " + unitsInStock);
                        System.out.println("-----------------------------------------");
                    }
                }
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void displayAllCustomers(String username, String password) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/northwind", username, password)
            ){
                String query = "SELECT ContactName, CompanyName, City, Country, Phone FROM Customers ORDER BY Country";

                try (PreparedStatement statement = connection.prepareStatement(query);
                ResultSet results = statement.executeQuery();
                ){

                    while (results.next()) {
                        String contactName = results.getString("ContactName");
                        String companyName = results.getString("CompanyName");
                        String city = results.getString("City");
                        String country = results.getString("Country");
                        String phone = results.getString("Phone");

                        System.out.println("Contact Name: " + contactName);
                        System.out.println("Company Name: " + companyName);
                        System.out.println("City: " + city);
                        System.out.println("Country: " + country);
                        System.out.println("Phone: " + phone);
                        System.out.println("-----------------------------------------");
                    }
                }

            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

}