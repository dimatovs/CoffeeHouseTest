package Database;

import Events.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.*;
import java.util.LinkedList;

public class DBConnector {

    private static final String dbUrl = "jdbc:mysql://localhost:3306/CoffeeHouse";
    private static final String login = "root";
    private static final String password = "1234";
    private static Connection connection;
    private String sql;

    public boolean isRegistered(OrderEvent event) {
        ResultSet resultCount;
        try {
            sql = "SELECT COUNT(*) FROM Events WHERE order_id=? AND type=?";
            connection = DriverManager.getConnection(dbUrl,login, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,event.getOrderId());
            statement.setString(2, "registered");
            resultCount = statement.executeQuery();
            return resultCount.next() && resultCount.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean isCanceledOrIssued(OrderEvent event) {
        ResultSet resultCount;
        try {
            sql = "SELECT COUNT(*) FROM Events WHERE order_id=? AND (type=? OR type=?)";
            connection = DriverManager.getConnection(dbUrl,login, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,event.getOrderId());
            statement.setString(2, "issued");
            statement.setString(3,"canceled");
            resultCount = statement.executeQuery();
            return resultCount.next() && resultCount.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void publishEvent(OrderEvent event) {
        ObjectMapper mapper = new ObjectMapper();
        StringWriter str = new StringWriter();
        try {
            mapper.writeValue(str, event);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            connection = DriverManager.getConnection(dbUrl, login, password);
            sql = "INSERT INTO Events (id,order_id, type, payload) VALUES(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, event.getId());
            statement.setInt(2, event.getOrderId());
            statement.setString(3, event.getType());
            statement.setString(4, str.toString());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public LinkedList<OrderEvent> getAllEventsByOrderId(int id) {
        ObjectMapper mapper = new ObjectMapper();
        LinkedList<OrderEvent> listOfEvents = new LinkedList<>();
        sql = "SELECT * FROM Events WHERE order_id=?";
        try {
            connection = DriverManager.getConnection(dbUrl,login, password);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1,id);
            ResultSet resultSetEvents = statement.executeQuery();

            while(resultSetEvents.next()) {
                StringReader payload = new StringReader(resultSetEvents.getString(4));
              if (resultSetEvents.getString(3).equals("registered")) {
                  OrderRegistered registered = mapper.readValue(payload, OrderRegistered.class);
                  deserialize(resultSetEvents,registered);
                  listOfEvents.add(registered);
              } else if (resultSetEvents.getString(3).equals("canceled")) {
                  OrderCanceled canceled = mapper.readValue(payload, OrderCanceled.class);
                  canceled.setOrderId(id);
                  canceled.setType("canceled");
                  canceled.setId(resultSetEvents.getInt(1));
                  listOfEvents.add(canceled);
              } else if (resultSetEvents.getString(3).equals("issued")) {
                  OrderIssued issued = mapper.readValue(payload, OrderIssued.class);
                  deserialize(resultSetEvents,issued);
                  listOfEvents.add(issued);
              } else if (resultSetEvents.getString(3).equals("inProgress")) {
                  OrderInProgress inProgress = mapper.readValue(payload, OrderInProgress.class);
                  deserialize(resultSetEvents,inProgress);
                  listOfEvents.add(inProgress);
              } else if (resultSetEvents.getString(3).equals("ready")) {
                  OrderIsReady ready = mapper.readValue(payload, OrderIsReady.class);
                  deserialize(resultSetEvents,ready);
                  listOfEvents.add(ready);
                }
            }
        } catch(SQLException | IOException ex) {
            throw new RuntimeException(ex);
        }
        return listOfEvents;
    }

    private <T extends OrderEvent> OrderEvent deserialize(ResultSet resultSet, T event) throws SQLException{
        StringReader payload = new StringReader(resultSet.getString(4));
        event.setId(resultSet.getInt(1));
        event.setOrderId(resultSet.getInt(2));
        event.setType((resultSet.getString(3)));
        return event;
    }

}
