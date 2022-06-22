package cib.learning.data;

import cib.learning.adapter.DateAdapter;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "Person")
@XmlAccessorType (XmlAccessType.FIELD)
@Setter
@Getter
public class Person {
    private int id;
    @XmlElement
    private String name;
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date birthday;

    @XmlElementWrapper(name = "hobbies")
    @XmlElement(name="hobby")
    private List<hobby> Hobbies;
/*
    public List<hobby> getHobbies() {
        return Hobbies;
    }
    public void setHobbies(List<hobby> Hobbies) {
        this.Hobbies = Hobbies;
    }

 */
    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", birthday=" + birthday +
                ", hobbies=" + Hobbies +
                '}';
    }
}
