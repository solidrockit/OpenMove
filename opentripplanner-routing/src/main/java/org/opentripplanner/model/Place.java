/* This program is free software: you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License
as published by the Free Software Foundation, either version 3 of
the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>. */

package org.opentripplanner.model;

import java.util.Calendar;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.opentripplanner.model.json_serialization.GeoJSONSerializer;
import org.opentripplanner.routing.util.Constants;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
* A Place is where a journey starts or ends, or a transit stop along the way.
*/
public class Place {

    /**
* For transit stops, the name of the stop. For points of interest, the name of the POI.
*/
	@Element(required=false)
    public String name = null;

    /**
* The ID of the stop. This is often something that users don't care about.
*/
	@Element(required=false)
    public AgencyAndId stopId = null;

    /**
* The "code" of the stop. Depending on the transit agency, this is often
* something that users care about.
*/
	@Element(required=false)
    public String stopCode = null;

    /**
* The longitude of the place.
*/
	@Element(required=false)
    public Double lon = null;
    
    /**
* The latitude of the place.
*/
	@Element(required=false)
    public Double lat = null;

    /**
* The time the rider will arrive at the place.
*/
	@Element(required=false)
    public String arrival = null;

    /**
* The time the rider will depart the place.
*/
	@Element(required=false)
    public String departure = null;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String orig;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String zoneId = "";

    /**
* For transit trips, the stop index (numbered from zero from the start of the trip
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public Integer stopIndex;

    /**
* Returns the geometry in GeoJSON format
* @return
*/
    //TODO remove or fix!

    String getGeometry() {
        return Constants.GEO_JSON_POINT + lon + "," + lat + Constants.GEO_JSON_TAIL;
    }
    
    @XmlElement
    @JsonSerialize
    @Element(required=false)
    public String geometry = this.getGeometry();

    public Place() {
    }

    public Place(Double lon, Double lat, String name) {
        this.lon = lon;
        this.lat = lat;
        this.name = name;
    }

    public Place(Double lon, Double lat, String name, Calendar timeAtState) {
        this(lon, lat, name);
        this.arrival = departure = timeAtState.toString();
    }
}