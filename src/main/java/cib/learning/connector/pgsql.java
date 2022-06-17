package cib.learning.connector;

import cib.learning.data.Person;
import cib.learning.data.Persons;
import cib.learning.data.hobby;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class pgsql {
    public ResourceBundle resource;
    public boolean save(Persons pers) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return false;
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Connection connection = null;
        Statement stmt = null;
        try {
            connection = DriverManager
                    .getConnection(
                            resource.getString("DB_URL"),
                            resource.getString("USER"),
                            resource.getString("PASS"));
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return false;
        }
        if (connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(resource.getString("ct"));
        } catch (SQLException e) {
            System.out.println("CREATE TABLE Failed");
            e.printStackTrace();
            return false;
        }
        insertPerson(pers.getPersons());
        return true;
    }
    public void insertPerson(List<Person> list) {
        String SQL = "INSERT INTO person (name,birthday) "
                + "VALUES(?,date(?))";
        try (
                Connection connection = null;
                Connection conn = DriverManager
                        .getConnection(
                                resource.getString("DB_URL"),
                                resource.getString("USER"),
                                resource.getString("PASS"));
                PreparedStatement statement = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS);) {
            int count = 0;

            for (Person actor : list) {
                statement.setString(1, actor.getName());
                statement.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actor.getBirthday()));
                statement.addBatch();
                statement.executeBatch();
                ResultSet keyset = statement.getGeneratedKeys();
                if ( keyset.next() ) {
                    // Retrieve the auto generated key(s).
                    int key = keyset.getInt(1);
                    System.out.println(key);
                    insertHobby(actor.getHobbies(),key);
                }
                //count++;
                // execute every 100 rows or less
                //if (count % 100 == 0 || count == list.size()) {
                //    statement.executeBatch();
                //}
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public void insertHobby(List<hobby> list,int persid) {
        String SQL = "INSERT INTO hobby (idpers,complexity,hobby_name) "
                + "VALUES(?,?,?)";
        try (Connection connection = null;
                Connection conn = DriverManager
                        .getConnection(
                                resource.getString("DB_URL"),
                                resource.getString("USER"),
                                resource.getString("PASS"));
                PreparedStatement statement = conn.prepareStatement(SQL);) {
            int count = 0;

            for (hobby actor : list) {
                statement.setInt(1, persid);
                statement.setInt(2, actor.getComplexity());
                statement.setString(3, actor.getHobby_name());

                statement.addBatch();
                count++;
                // execute every 100 rows or less
                if (count % 100 == 0 || count == list.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
