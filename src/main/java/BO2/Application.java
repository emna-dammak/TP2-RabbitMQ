package BO2;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Application {
    public static void main(String[] args)
    {
        String url = "jdbc:mysql://localhost:3306/product_salesdb2?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false" ;
        String user = "root" ;
        String password = "3662" ;
        Scanner scanner = new Scanner(System.in) ;
        String path = "C:\\Users\\emnad\\Desktop\\TP2\\src\\main\\java\\BO2\\Actions.txt" ;
        BufferedWriter writer = null ;
        try {
            writer = new BufferedWriter(new FileWriter(path, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        java.sql.Connection dbConnection = null ;
        try{
            dbConnection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        while(true)
        {
            String query ;
            query = scanner.nextLine() ;
            String command = query.split(" ")[0] ;
            switch (command)
            {
                case "insert": {
                    String[] data = query.split(" ")[1].split(",");
                    int id = Integer.parseInt(data[0]);
                    int qty = Integer.parseInt(data[4]);
                    float cost = Float.parseFloat(data[5]);
                    float amt = Float.parseFloat(data[6]);
                    float tax = Float.parseFloat(data[7]);
                    float total = Float.parseFloat(data[8]);

                    String insertQuery = "INSERT INTO product_sales VALUES (" + id + ",'" + data[1] + "','" + data[2] + "','" + data[3] + "'," + qty + "," + cost + "," + amt + "," + tax + "," + total + ")";
                    try {
                        //java.sql.Connection dbConnection = DriverManager.getConnection(url, user, password);
                        PreparedStatement pst = dbConnection.prepareStatement(insertQuery);
                        pst.executeUpdate();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.write(insertQuery);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "select": {
                    String selectQuery = "SELECT * FROM product_sales";
                    try {
                        //java.sql.Connection dbConnection = DriverManager.getConnection(url, user, password);

                        PreparedStatement pst = dbConnection.prepareStatement(selectQuery);
                        java.sql.ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                            System.out.println(rs.getInt(1) + " : " + rs.getString(2) + " : " + rs.getString(3) + " : " + rs.getString(4) + " : " + rs.getInt(5) + " : " + rs.getFloat(6) + " : " + rs.getFloat(7) + " : " + rs.getFloat(8) + " : " + rs.getFloat(9));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "delete":
                {
                    String deleteQuery = "DELETE FROM product_sales WHERE id = " + query.split(" ")[1];
                    try {
                        //java.sql.Connection dbConnection = DriverManager.getConnection(url, user, password);
                        PreparedStatement pst = dbConnection.prepareStatement(deleteQuery);
                        pst.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.write(deleteQuery);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "update":
                {
                    String[] data = query.split(" ")[1].split(",");
                    int id = Integer.parseInt(data[0]);
                    int qty = Integer.parseInt(data[4]);
                    float cost = Float.parseFloat(data[5]);
                    float amt = Float.parseFloat(data[6]);
                    float tax = Float.parseFloat(data[7]);
                    float total = Float.parseFloat(data[8]);

                    String updateQuery = "UPDATE product_sales SET dat = '" + data[1] + "', region = '" + data[2] + "', product = '" + data[3] + "', qty = " + qty + ", cost = " + cost + ", amt = " + amt + ", tax = " + tax + ", total = " + total + " WHERE id = " + id;
                    try {
                        //java.sql.Connection dbConnection = DriverManager.getConnection(url, user, password);
                        PreparedStatement pst = dbConnection.prepareStatement(updateQuery);
                        pst.executeUpdate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.write(updateQuery);
                        writer.newLine();
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "exit":
                {
                    try {
                        dbConnection.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
                default:
                {
                    System.out.println("");
                }
            }
        }
    }
}
