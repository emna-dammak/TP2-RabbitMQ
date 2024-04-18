package HO;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Receiver {

    private static final String EXCHANGE_NAME = "direct_productsales";
    private static String queueName = "product_sales";

    public static void main(String[] args) throws Exception {
        String url = "jdbc:mysql://localhost:3306/product_salesdbho?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false" ;
        String user = "root" ;
        String password = "3662" ;
        java.sql.Connection dbConnection = null ;
        try{
            dbConnection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "product_sales");
        java.sql.Connection finalDbConnection = dbConnection;
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String query = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Commant received: " + query);
            try {
                PreparedStatement pst = finalDbConnection.prepareStatement(query);
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        System.out.println(" [*] Waiting for messages. To exit press Ctrl+C");

    }
}