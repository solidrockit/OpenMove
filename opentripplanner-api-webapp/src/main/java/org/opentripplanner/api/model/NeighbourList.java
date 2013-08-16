
package org.opentripplanner.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
public class NeighbourList {
    @XmlElements(value = { @XmlElement(name="neighbour") })
    public List<NeighbourInfo> neighbours = new ArrayList<NeighbourInfo>();

}