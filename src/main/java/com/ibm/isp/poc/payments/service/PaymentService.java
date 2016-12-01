package com.ibm.isp.poc.payments.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.ibm.isp.poc.configuration.VcapConfiguration;
import com.ibm.isp.poc.push.service.PushService;

@Path("/payments")
public class PaymentService {
	private PushService pushService = new PushService();
	private String iibURL = "";
	
	@POST
	@Path("/pay")
	@Produces(MediaType.APPLICATION_JSON)
	public PaymentTransaction insert(InputStream incomingData) {
		PaymentTransaction paymentTransaction = new PaymentTransaction();
		StringBuilder sBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				sBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + sBuilder.toString());
		JSONParser jsonParser = new JSONParser();
		JSONObject obj = null;
		try {
			String fromIIB = callIIB();
			String message = "Call IIB on " + getIibURL() + " --> " + fromIIB;
			// {"ordinante":"Roberto Pozzi","beneficiario":"alex.guerrera89@gmail.com","method":"paypal","amount":"150.00"}
			obj = (JSONObject) jsonParser.parse(sBuilder.toString());
			String ordinante = (String) obj.get("ordinante");
			String beneficiario = (String) obj.get("beneficiario");
			String method = (String) obj.get("method");
			Double amount = new Double((String) obj.get("amount"));
			paymentTransaction.setOrdinante(ordinante);
			paymentTransaction.setBeneficiario(beneficiario);
			paymentTransaction.setMethod(method);
			paymentTransaction.setAmount(amount);
			paymentTransaction.setMessage(message);
			if (paymentTransaction.getStatus().equalsIgnoreCase(PaymentTransaction.STATUS_OK) && getMobilePushFromEnv()) {
				pushService.push("Ti sono stati accreditati " + paymentTransaction.getAmount() + " € da " + paymentTransaction.getOrdinante());
			}
			System.out.println("PaymentTransaction --> Status : " + paymentTransaction.getStatus() + " - ID : " + paymentTransaction.getId() + " - Method : " + paymentTransaction.getMethod() + " - Ordinante : " + paymentTransaction.getOrdinante() + " - Beneficiario : " + paymentTransaction.getBeneficiario() + " - Amount : " + paymentTransaction.getAmount());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return paymentTransaction;
	}
	
	private String callIIB() {
		setIibURL(getIIBUrlFromEnv());
		// create the rest client instance
		Client client = ClientBuilder.newClient();
		// create the Builder instance to interact with
		WebTarget webTarget = client.target(getIibURL());
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		// perform a GET on the resource. The resource will be returned as plain text
		Response response = invocationBuilder.get();
		System.out.println(response.getStatus());
		String xmlStr = response.readEntity(String.class);
		System.out.println(xmlStr);
		return xmlStr;
	}
	
	private String getIIBUrlFromEnv() {
		String iibUrl = VcapConfiguration.getInstance().get(VcapConfiguration.ENV_IIB_URL);
		String iibEndpoint = "http://" + iibUrl + "/payment/v1/payment";
		return iibEndpoint;
	}
	
	private boolean getMobilePushFromEnv() {
		return new Boolean(VcapConfiguration.getInstance().get(VcapConfiguration.ENV_MOBILE_PUSH));
	}

	public void setPushService(PushService pushService) {
		this.pushService = pushService;
	}

	public String getIibURL() {
		return iibURL;
	}

	public void setIibURL(String iibURL) {
		this.iibURL = iibURL;
	}
	
}