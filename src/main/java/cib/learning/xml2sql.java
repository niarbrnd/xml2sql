package cib.learning;

import cib.learning.connector.pgsql;
import cib.learning.data.Person;
import cib.learning.data.Persons;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

public class xml2sql {
    public static void main(String[] args) throws JAXBException, IOException, SQLException {
        System.out.println("Start app");
        JAXBContext context = JAXBContext.newInstance(Persons.class);
        Persons b = new Persons();
        b = (Persons) context.createUnmarshaller()
                .unmarshal(new FileReader("c:/Users/dra/Downloads/persons.xml"));
        System.out.println(b.toString());
        pgsql con =new pgsql();
        con.save(b);
        System.out.println("Stop  app");
    }
}