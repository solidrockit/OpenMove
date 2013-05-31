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

import java.util.List;

import org.onebusaway.gtfs.model.Stop;
import org.opentripplanner.routing.core.RoutingRequest;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.Server;
import org.opentripplanner.routing.services.PathService;
import org.opentripplanner.routing.spt.GraphPath;

public class SharedVertex extends TransitStop {

    private String sharedId;
    private Server neighbour;
    
    public PathService pathService;
    
	public SharedVertex(Graph graph, Stop stop) {
		super(graph, stop);
	}

	public SharedVertex(Graph graph, Stop stop, String sharedId,
			Server neighbour) {
		super(graph, stop);
		this.sharedId = sharedId;
		this.neighbour = neighbour;
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

	public GraphPath sendRequestToNeighbour(RoutingRequest options)
	{
		/*  Realiza una nueva llamada al método PLAN del API (recursivo) con otros parametros
		 *  en RoutingRequest. Concretamente nuevos 'from' o 'to' en RoutingContext.
		 *  Se llamará aqui desde el algoritmo principal de rutado: devuelvo el nuevo Path.
		*/
		
		//TripPlan plan = planGenerator.generate(options);
		List<GraphPath> paths = pathService.getPaths(options); //implementado en routing.impl.SimplifiedPathServiceImpl por ejemplo
		//Devuelvo solo la primera opción de rutado de cara a juntarla luego con el resto de la ruta local
		return paths.get(0);
	}
    
}
