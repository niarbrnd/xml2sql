package cib.learning.adapter;

import cib.learning.data.hobby;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class hobbyMapper implements RowMapper<hobby> {

    @Override
    public hobby mapRow(ResultSet rs, int rowNum) throws SQLException {

        hobby hob = new hobby();
        hob.setHobby_name(rs.getString("hobby_name"));
        hob.setComplexity(rs.getInt("complexity"));
        return hob;

    }
}
