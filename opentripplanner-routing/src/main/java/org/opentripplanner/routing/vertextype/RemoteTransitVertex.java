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

import org.onebusaway.gtfs.model.Stop;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.Server;

/** Abstract base class for vertices in the GTFS layer of the graph. */
public abstract class RemoteTransitVertex extends TransitVertex {

    private Server neighbour;

	public RemoteTransitVertex(Graph graph, String label, Stop stop,
			Server neighbour) {
		super(graph, label, stop);
		this.neighbour = neighbour;
	}

	public RemoteTransitVertex(Graph graph, String label, Stop stop) {
		super(graph, label, stop);
	}

	public Server getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(Server neighbour) {
		this.neighbour = neighbour;
	}

}
