package cib.learning.data;


import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hobby")
@XmlAccessorType (XmlAccessType.FIELD)
@Setter
@Getter
public class hobby {
    @XmlElement
    private int complexity;
    @XmlElement
    private String hobby_name;
    @Override
    public String toString() {
        return "hobby{" +
                "complexity=" + complexity +
                ", hobby_name='" + hobby_name + '\'' +
                '}';
    }
}
