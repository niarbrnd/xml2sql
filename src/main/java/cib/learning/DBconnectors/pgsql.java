package cib.learning.DBconnectors;

import cib.learning.data.Person;
import cib.learning.data.Persons;
import cib.learning.data.hobby;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class pgsql {
    public ResourceBundle resource;
    private Connection conn;
    private boolean connect(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return false;
        }
        System.out.println("PostgreSQL JDBC Driver successfully connected");
        Statement stmt = null;
        try {
            conn = DriverManager
                    .getConnection(
                            resource.getString("DB_URL"),
                            resource.getString("USER"),
                            resource.getString("PASS"));
        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return false;
        }
        if (conn != null) {
            System.out.println("You successfully connected to database now");
            return true;
        } else {
            System.out.println("Failed to make connection to database");
        }
        return false;
    }
    public boolean save(Persons pers) {
        connect();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(resource.getString("ct"));
        } catch (SQLException e) {
            System.out.println("CREATE TABLE Failed");
            e.printStackTrace();
            return false;
        }
        insertPerson(pers.getPersons());
        return true;
    }
    private void insertPerson(List<Person> list) {
        String SQL = "INSERT INTO person (name,birthday) "
                + "VALUES(?,date(?))";
        try (
                PreparedStatement statement = conn.prepareStatement(SQL,Statement.RETURN_GENERATED_KEYS)) {
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
                    //System.out.println(key);
                    insertHobby(actor.getHobbies(),key);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    private void insertHobby(List<hobby> list,int persid) {
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
    private List<hobby> getHobby(int idpers) {
        List<hobby> hobbies =new ArrayList<>();
        Statement stmt = null;
        try {
            PreparedStatement statement = conn.prepareStatement(resource.getString("getHobby"));
            statement.setInt(1,idpers);
            ResultSet rs=statement.executeQuery();
            // Количество колонок в результирующем запросе
            int columns = rs.getMetaData().getColumnCount();
            // Перебор строк с данными
            while(rs.next()){
                /*for (int i = 1; i <= columns; i++){
                    //System.out.print(rs.getString(i) + "\t");
                }*/
                hobby h=new hobby();
                h.setComplexity(rs.getInt("complexity"));
                h.setHobby_name(rs.getString("hobby_name"));
                hobbies.add(h);
                //System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("CREATE TABLE Failed");
            e.printStackTrace();
            return hobbies;
        }
        //insertPerson(pers.getPersons());
        return hobbies;
    }
    public Persons getPersons() {
        List<Person> Pers =new ArrayList<>();
        Persons P = new Persons();
        Statement stmt = null;
        try {
            PreparedStatement statement = conn.prepareStatement(resource.getString("getPersons"));
            ResultSet rs=statement.executeQuery();
            // Количество колонок в результирующем запросе
            int columns = rs.getMetaData().getColumnCount();
            // Перебор строк с данными
            while(rs.next()){
                /*for (int i = 1; i <= columns; i++){
                    //System.out.print(rs.getString(i) + "\t");
                }*/
                Person p=new Person();
                p.setName(rs.getString("name"));
                p.setBirthday(rs.getDate("birthday"));
                p.setHobbies(getHobby(rs.getInt("id")));
                Pers.add(p);
                //System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("CREATE TABLE Failed");
            e.printStackTrace();
            return P;
        }
        //insertPerson(pers.getPersons());
        P.setPersons(Pers);
        return P;
    }
}
