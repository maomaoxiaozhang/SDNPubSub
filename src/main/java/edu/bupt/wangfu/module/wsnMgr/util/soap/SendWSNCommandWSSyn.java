package edu.bupt.wangfu.module.wsnMgr.util.soap;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class SendWSNCommandWSSyn {
	private String wsProxyAddress;
	private String endpointAddr;
	private String localServiceAddr;
	private String subscriptionAddr;
	private String url;
	private static int statusCode = 0;
	private HttpClient client = null;
	private static int pCounter = 0;
	
	public SendWSNCommandWSSyn(String localServiceAddr,String wsProxyAddress){
		this.wsProxyAddress = wsProxyAddress;
		this.localServiceAddr = localServiceAddr;
		this.endpointAddr = null;
		url = wsProxyAddress;
		client = new HttpClient();
	}

	protected void send(Map<String, String> params, String charset, boolean pretty,String content) {
   		PostMethod method = new PostMethod(url); 
		try {
			method.setRequestEntity(new StringRequestEntity(content, "text/xml", "utf-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
   		method.setRequestHeader("Content-type" , "text/xml");
		 if (params != null) {
			 HttpMethodParams p = new HttpMethodParams();
			 for (Map.Entry<String, String> entry : params.entrySet()) {
				 p.setParameter(entry.getKey(), entry.getValue());
			 }
			 method.setParams(p);
		 }
		 try {
			 statusCode = client.executeMethod(method);
			 BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
			 String line;
			 while ((line = reader.readLine()) != null) {}
			 reader.close();
			 if( method.getStatusCode()==500)
			 {
				 if( client.getHttpConnectionManager().getConnection(method.getHostConfiguration())!=null)
					 client.getHttpConnectionManager().getConnection(method.getHostConfiguration()).close();
				 else
					 System.out.println("##########################connection is empty!!!!");
			 }
		 } catch (Exception e) {

		 } finally {

		 }
    }
	
    public int publish(String id, String topic, String msg){
		 String content = "";
		 content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		 content += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:org=\"http://edu.bupt.wangfu.module.wsnMgr.util.soap\">";
		 content += "<soapenv:Header/>";
		 content += "<soapenv:Body>";
		 content += "<org:WsnProcess>";
		 content  +=  EscapeSequenceGenerate(
				 "<wsnt:Publish xmlns:wsnt=\"http://docs.oasis-open.org/wsnMgr/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
		 content += EscapeSequenceGenerate("<wsnt:ConsumerReference>");
		 content += EscapeSequenceGenerate(" <wsa:Address>");
		 content += EscapeSequenceGenerate("<id>" + id + "</id>");
		 content += EscapeSequenceGenerate("<topic>" + topic + "</topic>");
		 content += EscapeSequenceGenerate("<message>" + msg + "</message>");
		 content += EscapeSequenceGenerate("</wsa:Address>");
		 content += EscapeSequenceGenerate("</wsnt:ConsumerReference>");
		 content += EscapeSequenceGenerate("<wsnt:Filter>");
		 content += EscapeSequenceGenerate("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">");
		 content += EscapeSequenceGenerate(topic);
		 content += EscapeSequenceGenerate("</wsnt:TopicExpression>");
		 content += EscapeSequenceGenerate("</wsnt:Filter>");
		 content += EscapeSequenceGenerate("<wsnt:SubscriberAddress>");
		 content += EscapeSequenceGenerate("</wsnt:SubscriberAddress>");
		 content += EscapeSequenceGenerate("</wsnt:Publish>");
		 content += "</org:WsnProcess>";
		 content += "</soapenv:Body>";
		 content += "</soapenv:Envelope>";

		send(new HashMap<>(), "utf-8", true, content.trim());
		return statusCode;
	}

	public int register(String id, String topic, String sendAddress){
		String content = "";
		content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		content += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:org=\"http://edu.bupt.wangfu.module.wsnMgr.util.soap\">";
		content += "<soapenv:Header/>";
		content += "<soapenv:Body>";
		content += "<org:WsnProcess>";
		content  +=  EscapeSequenceGenerate(
				"<wsnt:Register xmlns:wsnt=\"http://docs.oasis-open.org/wsnMgr/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
		content += EscapeSequenceGenerate("<wsnt:ConsumerReference>");
		content += EscapeSequenceGenerate(" <wsa:Address>");
		content += EscapeSequenceGenerate("<id>" + id + "</id>");
		content += EscapeSequenceGenerate("<topic>" + topic + "</topic>");
		content += EscapeSequenceGenerate("<userAddress>" + sendAddress + "</userAddress>");
		content += EscapeSequenceGenerate("</wsa:Address>");
		content += EscapeSequenceGenerate("</wsnt:ConsumerReference>");
		content += EscapeSequenceGenerate("<wsnt:Filter>");
		content += EscapeSequenceGenerate("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">");
		content += EscapeSequenceGenerate(topic);
		content += EscapeSequenceGenerate("</wsnt:TopicExpression>");
		content += EscapeSequenceGenerate("</wsnt:Filter>");
		content += EscapeSequenceGenerate("<wsnt:SubscriberAddress>");
		content += EscapeSequenceGenerate("</wsnt:SubscriberAddress>");
		content += EscapeSequenceGenerate("</wsnt:Register>");
		content += "</org:WsnProcess>";
		content += "</soapenv:Body>";
		content += "</soapenv:Envelope>";

		send(new HashMap<>(), "utf-8", true, content.trim());
		return statusCode;
	}

	public int notify(String topic, String msg){
		String content = "";
		content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		content += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:org=\"http://edu.bupt.wangfu.module.wsnMgr.util.soap\">";
		content += "<soapenv:Header/>";
		content += "<soapenv:Body>";
		content += "<org:WsnProcess>";
		content  +=  EscapeSequenceGenerate(
				"<wsnt:Notify xmlns:wsnt=\"http://docs.oasis-open.org/wsnMgr/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
		content += EscapeSequenceGenerate("<wsnt:ConsumerReference>");
		content += EscapeSequenceGenerate(" <wsa:Address>");
		content += EscapeSequenceGenerate("<topic>" + topic + "</topic>");
		content += EscapeSequenceGenerate("<message>" + msg + "</message>");
		content += EscapeSequenceGenerate("</wsa:Address>");
		content += EscapeSequenceGenerate("</wsnt:ConsumerReference>");
		content += EscapeSequenceGenerate("<wsnt:Filter>");
		content += EscapeSequenceGenerate("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">");
		content += EscapeSequenceGenerate("</wsnt:TopicExpression>");
		content += EscapeSequenceGenerate("</wsnt:Filter>");
		content += EscapeSequenceGenerate("<wsnt:SubscriberAddress>");
		content += EscapeSequenceGenerate("</wsnt:SubscriberAddress>");
		content += EscapeSequenceGenerate("</wsnt:Notify>");
		content += "</org:WsnProcess>";
		content += "</soapenv:Body>";
		content += "</soapenv:Envelope>";

		send(new HashMap<>(), "utf-8", true, content.trim());
		return statusCode;
	}

	public static String EscapeSequenceGenerate(String string){
		string = string.replaceAll("<", "&lt;");
		string = string.replaceAll(">", "&gt;");
		return string;
	}
}

	