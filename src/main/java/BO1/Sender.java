package BO1;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.*;
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Sender {

    private static final String EXCHANGE_NAME  = "direct_productsales" ;
    private static String path = "C:\\Users\\emnad\\Desktop\\TP2\\src\\main\\java\\BO1\\Actions.txt" ;

    static void clearFile()
    {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path)))
        {
            writer.write("");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception
    {
        ConnectionFactory factory = new ConnectionFactory() ;
        factory.setHost("localhost");

        try(   Connection queueConnection = factory.newConnection() ;
                Channel queueChannel = queueConnection.createChannel() ;

        ) {
            queueChannel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            queueChannel.queueDeclare("product_sales", false, false, false, null);
            queueChannel.queueBind("product_sales", EXCHANGE_NAME, "product_sales");
            String line = null;
            try (BufferedReader reader = new BufferedReader(new FileReader(path))){
                while((line = reader.readLine()) != null)
                {
                    queueChannel.basicPublish(EXCHANGE_NAME , "product_sales" , null , line.getBytes());
                    System.out.println(" [x] Sent '" + line + "'");
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            clearFile();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}