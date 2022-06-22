package cib.learning.adapter;

import cib.learning.data.Person;
import cib.learning.data.hobby;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class personMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person per = new Person();
        per.setName(rs.getString("name"));
        per.setBirthday(rs.getDate("birthday"));
        per.setId(rs.getInt("id"));
        return per;
    }
}
