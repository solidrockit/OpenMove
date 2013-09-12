package org.opentripplanner.api.ws;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONException;

//import org.opentripplanner.api.common.ParameterException;
import org.opentripplanner.api.common.SearchResource;
import org.opentripplanner.api.core.ResponseVertex;
import org.opentripplanner.api.core.VertexRequest;
import org.opentripplanner.api.model.NeighbourInfo;
import org.opentripplanner.api.model.NeighbourList;
import org.opentripplanner.api.model.NumNeighbours;
//import org.opentripplanner.api.model.TripPlan;
//import org.opentripplanner.api.model.error.PlannerError;
import org.opentripplanner.common.ParameterException;
import org.opentripplanner.model.TripPlan;
import org.opentripplanner.model.error.PlannerError;
import org.opentripplanner.routing.graph.Graph;
import org.opentripplanner.routing.graph.Server;
import org.opentripplanner.routing.graph.Vertex;
import org.opentripplanner.routing.impl.StreetVertexIndexServiceImpl;
import org.opentripplanner.routing.services.GraphService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.spring.Autowire;

@Path("") //NOTE - /ws/dsearch is the full path -- see web.xml
@XmlRootElement
@Autowire
public class DistributedSearch  extends SearchResource {

    private static final Logger LOG = LoggerFactory.getLogger(Planner.class);
    @Autowired public GraphService grapService;
    @Context protected HttpServletRequest httpServletRequest;

    interface OneArgFunc<T,U> {
        public T call(U arg);
    }
        
    @GET
    @Path("/getVertexForPlace")
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML})
    public ResponseVertex getVertexForPlace() throws JSONException {
         
        VertexRequest request = null; 
        Vertex vertex = null;

        try {
            // fill in request from query parameters via shared superclass method
            request = super.buildRequest();
            Graph graph = graphService.getGraph(request.getRouterId());
            //vertex = graph.streetIndex.getVertexForPlace(request.getLocationPlace(), request);
            vertex = graph.streetIndex.getClosestVertex(request.locationCoord, request.locationName, null, false);
        }catch (Exception e) {
            PlannerError error = new PlannerError(e);
            e.printStackTrace();
        } finally {
            if (request != null) request.cleanup();
        }
        if (vertex!=null) {
            return new ResponseVertex(vertex);
        }
        else {
            return new ResponseVertex();
        }
    }
    
    @GET
    @Path("/getNeighbours")
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    public NeighbourList getNeighbours() throws JSONException {
        NeighbourList response= new NeighbourList();
        Server remoteServer;
       
        // fill in request from query parameters via shared superclass method
        try {
               Graph graph = graphService.getGraph();
               Map<String, Server> serverList = graph.getServerList();
               if (serverList!= null && !serverList.isEmpty()) {
                   for (Map.Entry<String, Server> server : serverList.entrySet()) {
                       remoteServer = (Server)server.getValue();
                       if (remoteServer.isNeighbour()) {
                           response.neighbours.add(new NeighbourInfo(remoteServer));
                       }      
                   }
               }
         } catch (Exception e) {
               e.printStackTrace();
         }

           return response;
    }
    
    @GET
    @Path("/getNumNeighbours")
    @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_XML })
    public NumNeighbours getNumNeighbours() throws JSONException {
          Response response = new Response(httpServletRequest);
          Server remoteServer;
          int i = 0;
          try {
          Graph graph = graphService.getGraph();
          Map<String, Server> serverList = graph.getServerList();
          if (serverList!= null && !serverList.isEmpty()) {
              for (Map.Entry<String, Server> server : serverList.entrySet()) {
                  remoteServer = (Server)server.getValue();
                  if (remoteServer.isNeighbour()) {
                      i++;
                  }      
              }
          }
          }catch (Exception e) {
              PlannerError error = new PlannerError(e);
              e.printStackTrace();
              response.setError(error);
          } 
          return new NumNeighbours(i);
    }
    
}
