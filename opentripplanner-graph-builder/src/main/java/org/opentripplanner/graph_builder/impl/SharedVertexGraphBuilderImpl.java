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

package org.opentripplanner.graph_builder.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import lombok.Setter;

import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.Stop;
import org.opentripplanner.graph_builder.services.GraphBuilder;
import org.opentripplanner.gtfs.GtfsLibrary;
import org.opentripplanner.routing.edgetype.loader.NetworkLinker;
import org.opentripplanner.routing.graph.Edge;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.Server;
import org.opentripplanner.routing.graph.Vertex;
import org.opentripplanner.routing.graph.VertexComparatorFactory;
import org.opentripplanner.routing.vertextype.RemoteTransitVertex;
import org.opentripplanner.routing.vertextype.SharedVertex;
import org.opentripplanner.routing.vertextype.TransitStop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Builds a street graph from OpenStreetMap data.
 * 
 */
public class SharedVertexGraphBuilderImpl implements GraphBuilder {

	private static final Logger LOG = LoggerFactory.getLogger(SharedVertexGraphBuilderImpl.class);
	
	@Setter
	private File sharedVertexFile;
	
	public class SharedVertexException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}
	
	@Override
	public void buildGraph(Graph graph, HashMap<Class<?>, Object> extra) {
		// 1) Obtener lista de servidores desde el documento XML. El path se encuentra en el bean “sharedVertexFile”
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		
		try {
			db = dbf.newDocumentBuilder();
			
			Document document = db.parse(sharedVertexFile.getAbsolutePath());
			NodeList nodeList = document.getElementsByTagName("server");	
			HashMap<String, Server> serverList = new HashMap();
			// 2) Iterar la lista de servidores y crear un objeto de tipo Server por cada uno
			if (nodeList != null && nodeList.getLength() > 0) {
				for (int i = 0; i < nodeList.getLength(); i++) {
					Element element = (Element) nodeList.item(i);
					
					Server server = new Server();
					server.setGlobalId(element.getAttribute("globalId"));
					server.setId(element.getAttribute("id"));
					server.setName(element.getAttribute("name"));
					server.setServiceUrl(element.getAttribute("serviceUrl"));
					server.setUrl(element.getAttribute("url"));	
					serverList.put(server.getGlobalId(), server);
					
					LOG.info("neighbour id: " + server.getId());
					
					NodeList nodeList2 = element.getElementsByTagName("node");
					// 3) Iterar la lista de nodos compartidos de cada vecino
					if (nodeList2 != null && nodeList2.getLength() > 0) {
						for (int j = 0; j < nodeList2.getLength(); j++) {
							Element element2 = (Element) nodeList2.item(j);
							
							// 4) Crear nuevo objeto SharedVertex por cada uno y añadirlo al grafo.
							Stop stop = new Stop();
							stop.setCode(element2.getAttribute("code"));
							//Server ID + Node ID //revisar
							stop.setId(AgencyAndId.convertFromString(server.getGlobalId() + GtfsLibrary.ID_SEPARATOR + element2.getAttribute("id")));
							stop.setName(element2.getAttribute("name"));
							if (!element2.getAttribute("lat").isEmpty())
								stop.setLat(Double.parseDouble(element2.getAttribute("lat")));
							if (!element2.getAttribute("lon").isEmpty())
								stop.setLon(Double.parseDouble(element2.getAttribute("lon")));
							String sharedVertexId = element2.getAttribute("id");
							
							SharedVertex sharedVertex = null;
							
							
							
							LOG.info("sharedVertex id: " + sharedVertexId);
							
							if (sharedVertexId != null && !sharedVertexId.isEmpty()) {
								//Comprobar si el vertice ya existia en el grafo
								Collection<Vertex> vertices = graph.getVertices();
								Vertex exists = null;
								for (Vertex v : vertices) {
									if (v instanceof TransitStop && ((TransitStop)v).getLat() == stop.getLat() && ((TransitStop)v).getLon() == stop.getLon()) {
										exists = v;
									} 
								}
								if (exists instanceof TransitStop){
									// This transitStop (v) is going to be replaced by sharedVertex
									Collection<Edge> outs= exists.getOutgoing();
									Collection<Edge> ins = exists.getIncoming();
									sharedVertex = new SharedVertex(graph, new Stop(((TransitStop)exists).getStop()) , sharedVertexId, server);
									for (Edge edge : ins){
										// Add incoming edges to the new sharedVertex
										sharedVertex.addIncoming(edge);
									}
									for (Edge edge : outs){
										// Add outgoing edges to the new sharedVertex
										sharedVertex.addOutgoing(edge);
									}
									/* Check if it  is localStop.
									 *    If so, the sharedVertex must be connected with the nearby streets with NetworkLinker.
									 *    If not, the sharedVertex nearby osm nodes must not be downloaded.
									 */
									if (element2.getAttribute("localStop").equals("localStop"))
										sharedVertex.setLocalStop(true);
								} else {
									// This sharedVertex is a new addition to the graph
									sharedVertex = new SharedVertex(graph, stop , sharedVertexId, server);
									if (element2.getAttribute("localStop").equals("localStop"))
										sharedVertex.setLocalStop(true);
									else
										sharedVertex.setLocalStop(false);
									graph.addVertex(sharedVertex);
								}	
							} 
						}
					}					
				}
				graph.setServerList(serverList);
			}
		} catch (ParserConfigurationException e) {
			throw new SharedVertexException();
		} catch (SAXException e) {
			throw new SharedVertexException();
		} catch (IOException e) {
			throw new SharedVertexException();
		} 
	}
	
	

    @Override
    public List<String> provides() {
        return Arrays.asList("fares");
    }

    @Override
    public List<String> getPrerequisites() {
        return Collections.emptyList();
    }

    @Override
    public void checkInputs() {
        // nothing to do
    }
}