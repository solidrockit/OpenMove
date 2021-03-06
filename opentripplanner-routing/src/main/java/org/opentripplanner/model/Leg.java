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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.opentripplanner.util.DateUtils;
import org.opentripplanner.util.model.EncodedPolylineBean;
import org.opentripplanner.routing.core.TraverseMode;
import org.opentripplanner.routing.patch.Alert;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

 /**
* One leg of a trip -- that is, a temporally continuous piece of the journey that takes place on a
* particular vehicle (or on foot).
*/

public class Leg {

    /**
* The date and time this leg begins.
*/
	@Element(required=false)
    public String startTime = null;
    
    /**
* The date and time this leg ends.
*/
	@Element(required=false)
    public String endTime = null;
	
    /**
* The distance traveled while traversing the leg in meters.
*/
	@Element(required=false)
    public Double distance = null;

    /**
* The mode (e.g., <code>Walk</code>) used when traversing this leg.
*/
    @XmlAttribute
    @JsonSerialize
    //public String mode = TraverseMode.WALK.toString();
	@Attribute
	public String mode = "";

    /**
* For transit legs, the route of the bus or train being used. For non-transit legs, the name of
* the street being traversed.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute
    public String route = "";

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String agencyName;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String agencyUrl;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public int agencyTimeZoneOffset;
    

    /**
* For transit leg, the route's (background) color (if one exists). For non-transit legs, null.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String routeColor = null;

    /**
* For transit legs, the type of the route. Non transit -1
* When 0-7: 0 Tram, 1 Subway, 2 Train, 3 Bus, 4 Ferry, 5 Cable Car, 6 Gondola, 7 Funicular
* When equal or highter than 100, it is coded using the Hierarchical Vehicle Type (HVT) codes from the European TPEG standard
* Also see http://groups.google.com/group/gtfs-changes/msg/ed917a69cf8c5bef
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public Integer routeType = null;
    
    /**
* For transit legs, the ID of the route.
* For non-transit legs, null.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String routeId = null;

    /**
* For transit leg, the route's text color (if one exists). For non-transit legs, null.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String routeTextColor = null;

    /**
* For transit legs, if the rider should stay on the vehicle as it changes route names.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public Boolean interlineWithPreviousLeg;

    
    /**
* For transit leg, the trip's short name (if one exists). For non-transit legs, null.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String tripShortName = null;

    /**
* For transit legs, the headsign of the bus or train being used. For non-transit legs, null.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String headsign = null;

    /**
* For transit legs, the ID of the transit agency that operates the service used for this leg.
* For non-transit legs, null.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String agencyId = null;
    
    /**
* For transit legs, the ID of the trip.
* For non-transit legs, null.
*/
    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String tripId = null;
    
    /**
* The Place where the leg originates.
*/
    @Element(required=false)
    public Place from = null;
    
    /**
* The Place where the leg begins.
*/
    @Element(required=false)
    public Place to = null;

    /**
* For transit legs, intermediate stops between the Place where the leg originates and the Place where the leg ends.
* For non-transit legs, null.
* This field is optional i.e. it is always null unless "showIntermediateStops" parameter is set to "true" in the planner request.
*/
    @XmlElementWrapper(name = "intermediateStops")
    @JsonProperty(value="intermediateStops")
    @ElementList(name="intermediateStops",required=false)
    public List<Place> stop;

    /**
* The leg's geometry.
*/
    @Element(required=false)
    public EncodedPolylineBean legGeometry;

    /**
* A series of turn by turn instructions used for walking, biking and driving.
*/
    @XmlElementWrapper(name = "steps")
    @JsonProperty(value="steps")
    @ElementList(name="steps",required=false)
    public List<WalkStep> walkSteps;

    /**
* Deprecated field formerly used for notes -- will be removed. See
* alerts
*/
    @XmlElement
    @JsonSerialize
    @ElementList(name="notes",required=false)
    private List<Note> notes;

    @XmlElement
    @JsonSerialize
    @ElementList(name="alerts",required=false)
    private List<Alert> alerts;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String routeShortName;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String routeLongName;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String boardRule;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public String alightRule;

    @XmlAttribute
    @JsonSerialize
    @Attribute(required=false)
    public Boolean rentedBike;

    /**
* bogus walk/bike/car legs are those that have 0.0 distance,
* and just one instruction
*
* @return boolean true if the leg is bogus
*/
    public boolean isBogusNonTransitLeg() {
        boolean retVal = false;
        if( (TraverseMode.WALK.toString().equals(this.mode) ||
             TraverseMode.CAR.toString().equals(this.mode) ||
             TraverseMode.BICYCLE.toString().equals(this.mode)) &&
            (this.walkSteps == null || this.walkSteps.size() <= 1) &&
            this.distance == 0) {
            retVal = true;
        }
        return retVal;
    }
    
    /**
* The leg's duration in milliseconds
*/
    @XmlElement
    @JsonSerialize
    @Element
    /*public long getDuration() {
        return new Date(endTime).getTime() - new Date(startTime).getTime();
    }*/
    public long duration;

    public void addAlert(Alert alert) {
        if (notes == null) {
            notes = new ArrayList<Note>();
        }
        if (alerts == null) {
            alerts = new ArrayList<Alert>();
        }
        String text = alert.alertHeaderText.getSomeTranslation();
        if (text == null) {
            text = alert.alertDescriptionText.getSomeTranslation();
        }
        if (text == null) {
            text = alert.alertUrl.getSomeTranslation();
        }
        Note note = new Note(text);
        if (!notes.contains(note)) {
            notes.add(note);
        }
        if (!alerts.contains(alert)) {
            alerts.add(alert);
        }
    }

    public void setTimeZone(TimeZone timeZone) {
    	
        Calendar calendar = Calendar.getInstance(timeZone);
        try {
			calendar.setTime(DateUtils.dateFormat.parse(startTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        startTime = DateUtils.dateFormat.format(calendar.getTime());
        
        calendar = Calendar.getInstance(timeZone);
        try {
			calendar.setTime(DateUtils.dateFormat.parse(endTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        endTime = DateUtils.dateFormat.format(calendar.getTime());
        
        agencyTimeZoneOffset = timeZone.getOffset(calendar.getTime().getTime());
    }
}