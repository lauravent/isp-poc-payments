package com.ibm.isp.poc.push.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class PushService {
	
	public void push(String message) {
		String appSecret = "7a54375e-0ee5-4fd7-bb0b-008c6efa761f";
		String applicationId = "432e12fe-19a0-405f-bff8-e4752babd1f6";
		String pushServiceUrl = "http://imfpush.ng.bluemix.net/imfpush/v1/apps/" + applicationId + "/messages";
		String appSecretHeaderName = "appSecret";
		String contentTypeHeaderName = "Content-Type";
		String contentTypeHeaderValue = "application/json";
		String jsonMsg = "{\"message\": {\"alert\": \"" + message + "\"}}";
		
		// create the rest client instance
		Client client = ClientBuilder.newClient();
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(pushServiceUrl);
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
		invocationBuilder.header(appSecretHeaderName, appSecret);
		// perform a POST on the resource. The resource will be returned as plain text
		//Response response = invocationBuilder.get();
		Response response = invocationBuilder.post(Entity.entity(jsonMsg, MediaType.APPLICATION_JSON));
		System.out.println(response.getStatus());
		String jsonStr = response.readEntity(String.class);
		System.out.println(jsonStr);
	}
	
}