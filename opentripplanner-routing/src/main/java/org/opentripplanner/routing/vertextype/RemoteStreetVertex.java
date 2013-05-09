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

import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.Server;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Abstract base class for vertices in the street layer of the graph.
 * This includes both vertices representing intersections or points (IntersectionVertices) 
 * and Elevator*Vertices.
 */
public abstract class RemoteStreetVertex extends StreetVertex {

    private Server neighbour;

	public RemoteStreetVertex(Graph g, String label, Coordinate coord,
			String streetName, Server neighbour) {
		super(g, label, coord, streetName);
		this.neighbour = neighbour;
	}

	public RemoteStreetVertex(Graph g, String label, Coordinate coord,
			String streetName) {
		super(g, label, coord, streetName);
	}

	public Server getNeighbour() {
		return neighbour;
	}

	public void setNeighbour(Server neighbour) {
		this.neighbour = neighbour;
	}
    
}
