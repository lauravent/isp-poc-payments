package com.ibm.isp.poc.configuration;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VcapConfiguration {
	private static VcapConfiguration _theInstance = null;
	private static HashMap<String, String> _map;
	public static final String ENV_IIB_URL = "IIB_URL";
	public static final String ENV_MOBILE_PUSH = "MOBILE_PUSH";

	private VcapConfiguration() {
		super();
	}

	public static VcapConfiguration getInstance() {
		if (_theInstance == null) {
			_theInstance = new VcapConfiguration();
			_theInstance.init();
		}
		return _theInstance;
	}

	private void init() {
		_map = new HashMap<>();
		// USER_DEFINED environment variables
		String iibUrl = System.getenv(ENV_IIB_URL);
		Boolean mobilePush = new Boolean(System.getenv(ENV_MOBILE_PUSH));
		System.out.println("IIB_URL = " + iibUrl);
		System.out.println("MOBILE_PUSH = " + mobilePush);
		if (iibUrl == null) {
			// Set url to Bluemix Eu Docker container endpoint
			iibUrl = "134.168.39.105:7800";
			// Set url to Secure gateway endpoint
			//iibUrl = "cap-sg-prd-3.integration.ibmcloud.com:16886";
			System.out.println(ENV_IIB_URL + " environment variable not found, falling back to default : " + iibUrl);
		}
		_map.put(ENV_IIB_URL, iibUrl);
		_map.put(ENV_MOBILE_PUSH, mobilePush.toString());
		// 'VCAP_SERVICES' contains all the credentials of services bound to this application.
		String vcapServices = System.getenv("VCAP_SERVICES");
		if (vcapServices == null) {
			System.out.println("VCAP_SERVICES not found");
			return;
		}
		System.out.println("VCAP_SERVICES = " + vcapServices);
		JSONParser json = new JSONParser();
		JSONObject obj = null;
		try {
			obj = (JSONObject) json.parse(vcapServices);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public String get(String key) {
		return _map.get(key);
	}

}