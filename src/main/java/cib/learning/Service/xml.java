package cib.learning.Service;

import cib.learning.data.Persons;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;

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
    public boolean exportPersontoFile(Persons pers, String pathexport) throws IOException {
        FileWriter filexml;
        try {
            filexml = new FileWriter(pathexport);
        } catch (FileNotFoundException e) {
            System.out.println("file xml "+pathexport+" not write");
            e.printStackTrace();
            return false;
        }
        try {
            JAXBContext context = JAXBContext.newInstance(Persons.class);
            //Create Marshaller
            Marshaller jaxbMarshaller = context.createMarshaller();
            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.marshal(pers,filexml);
            System.out.println("Export XML export to "+pathexport);
        }
        catch (JAXBException e) {
            System.out.println("Export XML Failed");
            e.printStackTrace();
        }
        return false;
    }
}
