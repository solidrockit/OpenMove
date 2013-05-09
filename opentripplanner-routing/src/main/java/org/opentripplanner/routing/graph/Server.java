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

package org.opentripplanner.routing.graph;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import org.onebusaway.gtfs.impl.calendar.CalendarServiceImpl;
import org.onebusaway.gtfs.model.Agency;
import org.onebusaway.gtfs.model.AgencyAndId;
import org.onebusaway.gtfs.model.calendar.CalendarServiceData;
import org.onebusaway.gtfs.model.calendar.ServiceDate;
import org.onebusaway.gtfs.services.calendar.CalendarService;
import org.opentripplanner.common.MavenVersion;
import org.opentripplanner.gbannotation.GraphBuilderAnnotation;
import org.opentripplanner.gbannotation.NoFutureDates;
import org.opentripplanner.model.GraphBundle;
import org.opentripplanner.routing.core.MortonVertexComparatorFactory;
import org.opentripplanner.routing.core.TransferTable;
import org.opentripplanner.routing.edgetype.TimetableSnapshotSource;
import org.opentripplanner.routing.impl.StreetVertexIndexServiceImpl;
import org.opentripplanner.routing.services.StreetVertexIndexService;
import org.opentripplanner.routing.vertextype.SharedVertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.vividsolutions.jts.geom.Envelope;

/**
 * A graph is really just one or more indexes into a set of vertexes. It used to keep edgelists for
 * each vertex, but those are in the vertex now.
 */
public class Server implements Serializable {

    private long id;
    private String globalId;
    private String name;
    private String serviceUrl;
    private String url;
    private Map<String, SharedVertex> sharedVertexList;
    private boolean inMis;
    
	public Server() {
		super();
	}

	public Server(long id, String globalId, String name, String serviceUrl,
			String url, Map<String, SharedVertex> sharedVertexList,
			boolean inMis) {
		super();
		this.id = id;
		this.globalId = globalId;
		this.name = name;
		this.serviceUrl = serviceUrl;
		this.url = url;
		this.sharedVertexList = sharedVertexList;
		this.inMis = inMis;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getGlobalId() {
		return globalId;
	}

	public void setGlobalId(String globalId) {
		this.globalId = globalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, SharedVertex> getSharedVertexList() {
		return sharedVertexList;
	}

	public void setSharedVertexList(Map<String, SharedVertex> sharedVertexList) {
		this.sharedVertexList = sharedVertexList;
	}

	public boolean isInMis() {
		return inMis;
	}

	public void setInMis(boolean inMis) {
		this.inMis = inMis;
	}

	public boolean isNeighbour() {
		//si la lista de nodos compartidos contiene elementos -> es servidor vecino
		return sharedVertexList.size() > 0;
	}
}