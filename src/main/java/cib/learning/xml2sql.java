package cib.learning;

import cib.learning.Service.arguments;
import cib.learning.DBconnectors.JTpqsl;
import cib.learning.DBconnectors.pgsql;
import cib.learning.data.Persons;
import cib.learning.Service.xml;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class xml2sql {
    public static void main(String[] args) throws JAXBException, IOException, SQLException {
      /*  System.out.println("Start app");
        for(int i = 0; i<args.length; i++) {
            System.out.println("args[" + i + "]: " + args[i]);
        }*/
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
        Persons pers=new xml().getPerson(options.get("-xmlin"));
        //System.out.println(pers.toString());
        pgsql con =new pgsql();
        con.resource=resource;
        con.save(pers);
        //Boolean save = new xml().exportPersontoFile(con.getPersons(),options.get("-xmlout"));
        JTpqsl conjtemplate= new JTpqsl();
        conjtemplate.resource=resource;
        conjtemplate.save(pers);
        Boolean save = new xml().exportPersontoFile(conjtemplate.getPersons(),options.get("-xmlout"));
        //System.out.println(conjtemplate.getPersons());
        System.out.println("Stop  app");
    }
}