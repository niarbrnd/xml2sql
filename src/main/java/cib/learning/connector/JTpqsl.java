package cib.learning.connector;

import cib.learning.data.Person;
import cib.learning.data.Persons;
import cib.learning.data.hobby;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

public class JTpqsl {
    public ResourceBundle resource;
    private JdbcTemplate jdbcTemplate;
    public boolean save(Persons pers) {
        try {
            Class.forName("JdbcTemplate");
        } catch (ClassNotFoundException e) {
            System.out.println("JdbcTemplate JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return false;
        }
        System.out.println("JdbcTemplate JDBC Driver successfully connected");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(resource.getString("DB_URL"));
        dataSource.setUsername(resource.getString("USER"));
        dataSource.setPassword(resource.getString("PASS"));
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcTemplate.execute(resource.getString("ct"));
        insertPerson(pers.getPersons());
        return true;
    }
    public void insertPerson(List<Person> list) {
        SimpleJdbcInsert insertIntoUser;
        for (Person actor : list) {
            try {

                insertIntoUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("user").usingGeneratedKeyColumns("id_user");
                insertIntoUser.
                jdbcTemplate.update(
                        "INSERT INTO person (name,birthday) VALUES (?, date(?))",
                        actor.getName(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actor.getBirthday()));
                ResultSet keyset = statement.getGeneratedKeys();
                if (keyset.next()) {
                    // Retrieve the auto generated key(s).
                    int key = keyset.getInt(1);
                    System.out.println(key);
                    insertHobby(actor.getHobbies(), key);
                }
                //count++;
                // execute every 100 rows or less
                //if (count % 100 == 0 || count == list.size()) {
                //    statement.executeBatch();
                //}
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    public void insertHobby(List<hobby> list, int persid) {
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
