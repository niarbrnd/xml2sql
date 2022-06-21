package cib.learning;

import cib.learning.libs.arguments;
import cib.learning.connector.JTpqsl;
import cib.learning.connector.pgsql;
import cib.learning.data.Persons;
import cib.learning.libs.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class xml2sql {
    public static void main(String[] args) throws JAXBException, IOException, SQLException {
        System.out.println("Start app");
        for(int i = 0; i<args.length; i++) {
            System.out.println("args[" + i + "]: " + args[i]);
        }
        Map<String, String> options = new arguments().get(args);
        /*ResourceBundle a = ResourceBundle.getBundle("config");
        /*System.out.println(options.toString());
        for ( String key : options.keySet() ) {
            System.out.println( key );
            System.out.println(options.get(key));
        }
        System.out.println(options.get("-сonfig"));
        System.out.println(options.keySet().toArray()[0]);
         */
        ResourceBundle resource = new  PropertyResourceBundle(Files.newInputStream(Paths.get(options.get("-config"))));
        Persons pers=new xml().getPerson(options.get("-xml"));
        //System.out.println(pers.toString());
        pgsql con =new pgsql();
        con.resource=resource;
        con.save(pers);

        JTpqsl conjtemplate= new JTpqsl();
        conjtemplate.resource=resource;
        conjtemplate.save(pers);
        System.out.println("Stop  app");
    }
}