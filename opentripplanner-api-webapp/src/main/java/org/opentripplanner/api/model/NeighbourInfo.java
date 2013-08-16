/* This program is free software: you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public License
 as published by the Free Software Foundation, either version 3 of
 the License, or (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package org.opentripplanner.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.opentripplanner.routing.graph.Server;

import lombok.Getter;


@XmlRootElement(name = "neighbour")
public class NeighbourInfo {
    @XmlElement @Getter
    public String Id = "";
    
    @XmlElement @Getter
    public String GlobalId = "";
    
    @XmlElement @Getter
    private String Name = "unknown";
    
    public NeighbourInfo() {

    }
    
    public NeighbourInfo(Server server) {
        this.Id = server.getId();
        this.GlobalId = server.getGlobalId();
        this.Name = server.getName();
        
    }
    
    public NeighbourInfo(String id, String globalId, String name) {
        this.Id = id;
        this.GlobalId = globalId;
        this.Name = name;
        
    }
    
    
}
