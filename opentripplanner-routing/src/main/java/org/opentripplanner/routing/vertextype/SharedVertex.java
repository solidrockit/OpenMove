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

package org.opentripplanner.routing.vertextype;

import java.util.Date;
import java.util.List;
import java.net.URLEncoder;

import org.onebusaway.gtfs.model.Stop;
import org.opentripplanner.model.TripPlan;
import org.opentripplanner.routing.core.Request;
import org.opentripplanner.routing.core.Response;
import org.opentripplanner.routing.core.RoutingRequest;
import org.opentripplanner.routing.core.TripRequest;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.Server;
import org.opentripplanner.routing.services.PathService;
import org.opentripplanner.routing.spt.GraphPath;
import org.simpleframework.xml.core.ElementException;

public class SharedVertex extends TransitStop {

    private String sharedId;
    private Server neighbour;
    private boolean localStop;
    
    public PathService pathService;
    
	public SharedVertex(Graph graph, Stop stop) {
		super(graph, stop);
	}

	public SharedVertex(Graph graph, Stop stop, String sharedId, Server neighbour) {
		super(graph, stop);
		this.sharedId = sharedId;
		this.neighbour = neighbour;
		this.localStop = false;
	}
    
	public String getSharedId() {
		return sharedId;
	}

	public void setSharedId(String sharedId) {
		this.sharedId = sharedId;
	}

	public Server getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(Server neighbour) {
		this.neighbour = neighbour;
	}
	
	public boolean isLocalStop() {
		return localStop;
	}

	public void setLocalStop(boolean localStop) {
		this.localStop = localStop;
	}

	public TripPlan sendRequestToNeighbour(RoutingRequest options)
	{
		/* Makes a new call to the API PLAN (recursive) with other parameters
		* in RoutingRequest. Specifically new 'from' or 'to' in RoutingContext.
		* The main routing algorithm (A* Star) will call here: return the resulting Path.
		*/
		
		TripRequest remoteTrip = new TripRequest();
		Request remoteRequest = new Request();
		Response respuesta = null;
		
		//fromPlace=42.835412,-2.670686
		//&toPlace=42.873857,-2.679784
		//&optimize=QUICK
		//&maxWalkDistance=840
		//&mode=TRANSIT,WALK
		
		remoteRequest.setFrom(options.from);
		remoteRequest.setTo(options.to);		
		remoteRequest.setDateTime(options.getDateTime());
		remoteRequest.setOptimize(options.optimize);
		remoteRequest.setMaxWalkDistance(840.0);
		remoteRequest.setModes(options.modes);
		
		remoteRequest.setNumItineraries(1);
		
		/*remoteRequest.setDateTime(DateFormat.format("MM/dd/yy", System.currentTimeMillis()).toString(), 
				DateFormat.format("hh:mmaa", System.currentTimeMillis()).toString());*/
	
		respuesta = remoteTrip.requestPlan(remoteRequest, neighbour);
		
		try {
			return respuesta.getPlan();
		} catch (NullPointerException e){
			// Can't find a  remote leg
			options.banSharedVertex(options.rctx.sharedVertex);
			return null;
		}
	}
    
}

