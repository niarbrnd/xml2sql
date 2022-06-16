package cib.learning.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "Persons")
@XmlAccessorType (XmlAccessType.FIELD)
public class Persons {
    @XmlElement (name = "Person")
    private List<Person> People =null;

    public List<Person> getPersons() {
        return People;
    }
    public void setPersons(List<Person> People) {
        this.People = People;
    }
}