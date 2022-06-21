package cib.learning.libs;

import cib.learning.data.Persons;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.SQLException;

public class xml {
    public Persons getPerson(String pathtoxml) throws JAXBException, FileNotFoundException {
        Persons pers = new Persons();
        FileReader filexml;
        try {
            filexml = new FileReader(pathtoxml);
        } catch (FileNotFoundException e) {
            System.out.println("file xml "+pathtoxml+" not found");
            e.printStackTrace();
            return pers;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(Persons.class);
            pers = (Persons) context.createUnmarshaller()
                    .unmarshal(filexml);
        }
            catch (JAXBException e) {
                System.out.println("Connection Failed");
                e.printStackTrace();
        }
        return pers;
    }
}
