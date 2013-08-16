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

package org.opentripplanner.api.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

import org.opentripplanner.common.MavenVersion;
import org.opentripplanner.common.model.NamedPlace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * A trip planning request. Some parameters may not be honored by the trip planner for some or all
 * itineraries. For example, maxWalkDistance may be relaxed if the alternative is to not provide a
 * route.
 * 
 * NOTE this is the result of merging what used to be called a REQUEST and a TRAVERSEOPTIONS
 */
@Getter @Setter
public class VertexRequest implements Cloneable, Serializable {
    
    private static final long serialVersionUID = MavenVersion.VERSION.getUID();
    private static final Logger LOG = LoggerFactory.getLogger(VertexRequest.class);

    /* FIELDS UNIQUELY IDENTIFYING AN SPT REQUEST */

    /* TODO no defaults should be set here, they should all be handled in one place (searchresource) */ 
    /** The complete list of incoming query parameters. */
    public final HashMap<String, String> parameters = new HashMap<String, String>();
    /** The router ID -- internal ID to switch between router implementation (or graphs) */
    public String routerId = "";
    /** The location -- either a Vertex name or latitude, longitude in degrees */
    // TODO change this to Doubles and a Vertex
    public String location = ""; // allow missing 'location' for batch requests
    /** The location's user-visible name */
    public String locationName;
    /** The location's coordinates */
    public Coordinate locationCoord;
    
    private Locale locale = new Locale("en", "US");
    
    /* CONSTRUCTORS */
    
    /** Constructor */
    public VertexRequest() {

    }

    /* ACCESSOR METHODS */

      // TODO factor out splitting code which appears in 3 places
    public void setLocation(String location) {
        if (location.contains("::")) {
            String[] parts = location.split("::");
            this.locationName = parts[0];
            this.location = parts[1];
        } else {
            this.location = location;
        }
    }

    public NamedPlace getLocationPlace() {
        return new NamedPlace(locationName, location);
    }

    public String toHtmlString() {
        return toString("<br/>");
    }

    public String toString() {
        return toString(" ");
    }

    public String toString(String sep) {
        return getLocation(); 
    }
    /** Tear down any routing context (remove temporary edges from edge lists) */
    public void cleanup() {
 
    }

}
