package cib.learning.DBconnectors;

import cib.learning.adapter.hobbyMapper;
import cib.learning.adapter.personMapper;
import cib.learning.data.Person;
import cib.learning.data.Persons;
import cib.learning.data.hobby;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class JTpqsl {
    public ResourceBundle resource;
    private JdbcTemplate jdbcTemplate;
    private String insertPersonSQL= "INSERT INTO person (name,birthday) VALUES (?, date(?))";
    private String insertHobbySQL= "INSERT INTO hobby (idpers,complexity,hobby_name) VALUES(?,?,?)";
    private void connect(){
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("org.postgresql.Driver JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return ;
        }
        System.out.println("JdbcTemplate JDBC Driver successfully connected");
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(resource.getString("DB_URL"));
        dataSource.setUsername(resource.getString("USER"));
        dataSource.setPassword(resource.getString("PASS"));
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public boolean save(Persons pers) {
        connect();
        jdbcTemplate.execute(resource.getString("ct"));
        insertPerson(pers.getPersons());
        return true;
    }
    private int JTinsertPerson(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertPersonSQL,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, person.getName());
            ps.setString(2, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(person.getBirthday()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKeys().size() > 1) {
            return (int) keyHolder.getKeys().get("id");
        } else {
            return keyHolder.getKey().intValue();
        }
    }
    private void JTinsertHobby(hobby hob, int persid) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertHobbySQL);
            ps.setInt(1, persid);
            ps.setInt(2, hob.getComplexity());
            ps.setString(3, hob.getHobby_name());
           return ps;
        }, keyHolder);
    }
    private void insertPerson(List<Person> list) {
        SimpleJdbcInsert insertIntoUser;
        for (Person person : list) {
                long key = JTinsertPerson(person);
                if (key>0) {
                    // Retrieve the auto generated key(s).
                    //System.out.println(key);
                    insertHobby(person.getHobbies(), (int) key);
                }
        }
    }
    private void insertHobby(List<hobby> list, int persid) {
        for (hobby hob : list) {
            JTinsertHobby(hob,persid);
        }
    }
    private List<hobby> getHobby(int idpers) {
        List<hobby> hobbies =new ArrayList<>();
        Statement stmt = null;
        try {
            hobbies=jdbcTemplate.query(
                    resource.getString("getHobby"),
                    new PreparedStatementSetter() {
                        public void setValues(PreparedStatement preparedStatement) throws
                                SQLException {
                            preparedStatement.setInt(1, idpers);
                        }
                    }, new hobbyMapper());
        } catch (Exception e) {
            System.out.println("cant get Hobby for idpers="+idpers);
            e.printStackTrace();
            return hobbies;
        }
        //insertPerson(pers.getPersons());
        return hobbies;
    }
    private Persons getPersonswithoutHobby() {
        connect();
        //List<Person> Pers = new ArrayList<>();
        Persons P = new Persons();
        List<hobby> hobbies = new ArrayList<>();
        //Statement stmt = null;
        try {
            P.setPersons(jdbcTemplate.query(
                    resource.getString("getPersons"),
                    new personMapper()));
        } catch (Exception e) {
            System.out.println("cant get persons");
            e.printStackTrace();
            return P;
        }
        //insertPerson(pers.getPersons());
        return P;
    }
    public Persons getPersons(){
        Persons p = getPersonswithoutHobby();
        List<Person> pers = new ArrayList<>();
        for (Person per : p.getPersons() ) {
            per.setHobbies(getHobby(per.getId()));
            pers.add(per);
        }
        p.setPersons(pers);
        return p;
    }
}