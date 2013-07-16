/*
 * Copyright 2011 Marcy Gordon
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.opentripplanner.routing.core;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.opentripplanner.model.Leg;
import org.opentripplanner.routing.core.Request;
import org.opentripplanner.routing.core.Response;
import org.opentripplanner.routing.graph.Server;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class TripRequest {
	private Response response;

	public TripRequest() {
	}

	public Response requestPlan(Request requestParams) {
		Server servidor = new Server();
		servidor.setServiceUrl("http://130.206.138.15:8080/opentripplanner-api-webapp/ws/plan");
		return requestPlan(requestParams, servidor);
	}
	
	public Response requestPlan(Request requestParams, Server neighbour) {
		HashMap<String, String> tmp = requestParams.getParameters();

		Collection c = tmp.entrySet();
		Iterator itr = c.iterator();

		String params = "";
		boolean first = true;
		while(itr.hasNext()){
			if(first) {
				params += "?" + itr.next();
				first = false;
			} else {
				params += "&" + itr.next();						
			}
		}
		
		//fromPlace=42.835412,-2.670686
		//&toPlace=42.873857,-2.679784
		//&optimize=QUICK
		//&maxWalkDistance=840
		//&mode=TRANSIT,WALK
		
		//String u = "http://130.206.138.15:8080/opentripplanner-api-webapp/ws/plan?mode=TRANSIT,WALK&optimize=QUICK&maxWalkDistance=840&toPlace=42.873857,-2.679784&fromPlace=42.835412,-2.670686";
		//String u = "http://go.cutr.usf.edu:8083/opentripplanner-api-webapp/ws/plan?fromPlace=28.066192823902,-82.416927819827&toPlace=28.064072155861,-82.41109133301&arr=Depart&min=QUICK&maxWalkDistance=7600&itinID=1&submit&date=06/07/2011&time=11:34%20am";
		
		String u = neighbour.getServiceUrl() + params;
		
		//Below fixes a bug where the New York OTP server will whine
		//if doesn't get the parameter for intermediate places
		/*if(server.getRegion().equalsIgnoreCase("New York")) {
			u += "&intermediatePlaces=";
		}*/
		
		//Log.d(TAG, "URL: " + u);
		
		HttpClient client = new DefaultHttpClient();
		String result = "";
		try {
			result = Http.get(u).use(client).header("Accept", "application/xml").header("Keep-Alive","timeout=60, max=100").charset("UTF-8").followRedirects(true).asString();
			//Log.d(TAG, "Result: " + result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		Serializer serializer = new Persister();

		Response plan = null;
		try {
			plan = serializer.read(Response.class, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		//TODO - handle errors and error responses
		if(plan == null) {
			//Log.d(TAG, "No response?");
			return null;
		}
		return plan;
	}
}
