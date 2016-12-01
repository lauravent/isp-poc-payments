package com.ibm.isp.poc.rest.server;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyRestServer extends ResourceConfig {

	public JerseyRestServer() {
		super();
		packages("com.rp.item.service;com.rp.customer.service;com.rp.cantieri.service;com.rp.todo.service");
	}
	
}