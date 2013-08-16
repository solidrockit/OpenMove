package org.opentripplanner.api.model;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "response")
public class NumNeighbours {

    private int number;
    
    public NumNeighbours() {
        number = 0;
    }
    public NumNeighbours(int num) {
        number = num;
    }
    @XmlElement(name="number")
    public int getNombre() {
        return number;
    }
}
