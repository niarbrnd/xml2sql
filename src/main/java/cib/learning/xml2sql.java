package cib.learning;

import cib.learning.connector.pgsql;
import cib.learning.data.Person;
import cib.learning.data.Persons;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

public class xml2sql {
    public static void main(String[] args) throws JAXBException, IOException, SQLException {
        System.out.println("Start app");
        for(int i = 0; i<args.length; i++) {
            System.out.println("args[" + i + "]: " + args[i]);
        }
        Map<String, String> options = new HashMap<>();
        for (int i=0; i < args.length; i++) {
            switch (args[i].charAt(0)) {
                case '-':
                        System.out.println("Found dash with command " +
                                args[i] + " and value " + args[i+1] );
                        options.put( args[i], args[i+1]);
                        i= i+1;
                    break;
                default:
                    System.out.println("Parametr is bad "+ args[i] );
                    break;
            }
        }
        /*ResourceBundle a = ResourceBundle.getBundle("config");
        /*System.out.println(options.toString());
        for ( String key : options.keySet() ) {
            System.out.println( key );
            System.out.println(options.get(key));
        }
        System.out.println(options.get("-сonfig"));
        System.out.println(options.keySet().toArray()[0]);
         */
        ResourceBundle resource = ResourceBundle.getBundle(options.get("-config"));
        //System.out.println(a.getKeys().nextElement().toString());
        //System.out.println("ResourceBundle.getBundle");
        JAXBContext context = JAXBContext.newInstance(Persons.class);
        Persons b = new Persons();
        b = (Persons) context.createUnmarshaller()
                .unmarshal(new FileReader(options.get("-xml")));
        //System.out.println(b.toString());
        pgsql con =new pgsql();
        con.resource=resource;
        con.save(b);
        System.out.println("Stop  app");
    }
}