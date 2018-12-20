package edu.bupt.wangfu.module.topicMgr.ldap.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class SendTopicCommand {
    private String managerAddr;
    private HttpClient client;
    private static int counter = 0;

    public SendTopicCommand(String managerAddr) {
        this.managerAddr = managerAddr;
        client = new HttpClient();
    }


    /**
     * 查询主题
     *
     * @return
     */
    public String check(String topic, String newTopic,String receiveAddress) {
        String content = "";
        String returnValue = "";
        content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        content += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:org=\"http://edu.bupt.wangfu.module.topicMgr.ldap.user\">";
        content += "<soapenv:Header/>";
        content += "<soapenv:Body>";
        content += "<org:TopicProcess>";
        content  +=  EscapeSequenceGenerate(
                "<wsnt:check xmlns:wsnt=\"http://docs.oasis-open.org/topicMgr/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
        content += EscapeSequenceGenerate("<wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate(" <wsa:Address>");
        content += EscapeSequenceGenerate("<topic>" + topic + "</topic>");
        content += EscapeSequenceGenerate("<newTopic>" + newTopic + "</newTopic>");
        content += EscapeSequenceGenerate("<userAddress>" + receiveAddress + "</userAddress>");
        content += EscapeSequenceGenerate("</wsa:Address>");
        content += EscapeSequenceGenerate("</wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate("<wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">");
        content += EscapeSequenceGenerate("</wsnt:TopicExpression>");
        content += EscapeSequenceGenerate("</wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:check>");
        content += "</org:TopicProcess>";
        content += "</soapenv:Body>";
        content += "</soapenv:Envelope>";
        String[] responseValue = send(managerAddr + "/topicprocess/",new HashMap<String,String>(), "utf-8", true, content.trim());

//        if(responseValue[0].equals("200") && !responseValue[1].contains("failed")) {
//            returnValue="ok";
//            String message = responseValue[1];
//            int messageStart = message.indexOf("<ns2:Address>") + 13;
//            int messageEnd = message.indexOf("</ns2:Address>");
//            if( ( messageStart >= 0 ) && ( messageEnd >= 0 ) )
//            {
//                String address = message.substring(messageStart, messageEnd);
////                subscriptionAddr = address;
////                System.out.println("******************************subscribe Address " + subscriptionAddr);
//            }
//        } else {
//            returnValue="error";
//        }
        return responseValue.toString();
    }

    public String add(String topic, String newTopic,String receiveAddress) {
        String content = "";
        String returnValue = "";

        content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        content += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:org=\"http://edu.bupt.wangfu.module.topicMgr.ldap.user\">";
        content += "<soapenv:Header/>";
        content += "<soapenv:Body>";
        content += "<org:TopicProcess>";
        content  +=  EscapeSequenceGenerate(
                "<wsnt:add xmlns:wsnt=\"http://docs.oasis-open.org/topicMgr/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
        content += EscapeSequenceGenerate("<wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate(" <wsa:Address>");
        content += EscapeSequenceGenerate("<topic>" + topic + "</topic>");
        content += EscapeSequenceGenerate("<newTopic>" + newTopic + "</newTopic>");
        content += EscapeSequenceGenerate("<userAddress>" + receiveAddress + "</userAddress>");
        content += EscapeSequenceGenerate("</wsa:Address>");
        content += EscapeSequenceGenerate("</wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate("<wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">");
        content += EscapeSequenceGenerate(topic);
        content += EscapeSequenceGenerate("</wsnt:TopicExpression>");
        content += EscapeSequenceGenerate("</wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:add>");
        content += "</org:TopicProcess>";
        content += "</soapenv:Body>";
        content += "</soapenv:Envelope>";
        String[] responseValue = send(managerAddr + "/TopicProcess/",new HashMap<String,String>(), "utf-8", true, content.trim());

//        if(responseValue[0].equals("200") && !responseValue[1].contains("failed")) {
//            returnValue="ok";
//            SubscribeResponse response = new SubscribeResponse();
//            String message = responseValue[1];
//            int messageStart = message.indexOf("<ns2:Address>") + 13;
//            int messageEnd = message.indexOf("</ns2:Address>");
//            if( ( messageStart >= 0 ) && ( messageEnd >= 0 ) )
//            {
//                String address = message.substring(messageStart, messageEnd);
//                subscriptionAddr = address;
//                System.out.println("******************************subscribe Address " + subscriptionAddr);
//            }
//        } else {
//            returnValue="error";
//        }
        return returnValue;
    }

    public String delete(String topic, String newTopic,String receiveAddress) {
        String content = "";
        String returnValue = "";

        content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        content += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:org=\"http://edu.bupt.wangfu.module.topicMgr.ldap.user\">";
        content += "<soapenv:Header/>";
        content += "<soapenv:Body>";
        content += "<org:TopicProcess>";
        content  +=  EscapeSequenceGenerate(
                "<wsnt:delete xmlns:wsnt=\"http://docs.oasis-open.org/topicMgr/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
        content += EscapeSequenceGenerate("<wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate(" <wsa:Address>");
        content += EscapeSequenceGenerate("<topic>" + topic + "</topic>");
        content += EscapeSequenceGenerate("<newTopic>" + newTopic + "</newTopic>");
        content += EscapeSequenceGenerate("<userAddress>" + receiveAddress + "</userAddress>");
        content += EscapeSequenceGenerate("</wsa:Address>");
        content += EscapeSequenceGenerate("</wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate("<wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">");
        content += EscapeSequenceGenerate(topic);
        content += EscapeSequenceGenerate("</wsnt:TopicExpression>");
        content += EscapeSequenceGenerate("</wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:delete>");
        content += "</org:TopicProcess>";
        content += "</soapenv:Body>";
        content += "</soapenv:Envelope>";
        String[] responseValue = send(managerAddr + "/TopicProcess/",new HashMap<String,String>(), "utf-8", true, content.trim());

//        if(responseValue[0].equals("200") && !responseValue[1].contains("failed")) {
//            returnValue="ok";
//            SubscribeResponse response = new SubscribeResponse();
//            String message = responseValue[1];
//            int messageStart = message.indexOf("<ns2:Address>") + 13;
//            int messageEnd = message.indexOf("</ns2:Address>");
//            if( ( messageStart >= 0 ) && ( messageEnd >= 0 ) )
//            {
//                String address = message.substring(messageStart, messageEnd);
//                subscriptionAddr = address;
//                System.out.println("******************************subscribe Address " + subscriptionAddr);
//            }
//        } else {
//            returnValue="error";
//        }
        return returnValue;
    }

    public String modify(String topic, String newTopic,String receiveAddress) {
        String content = "";
        String returnValue = "";
        content += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        content += "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:org=\"http://edu.bupt.wangfu.module.topicMgr.ldap.user\">";
        content += "<soapenv:Header/>";
        content += "<soapenv:Body>";
        content += "<org:TopicProcess>";
        content  +=  EscapeSequenceGenerate(
                "<wsnt:modify xmlns:wsnt=\"http://docs.oasis-open.org/topicMgr/b-2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">");
        content += EscapeSequenceGenerate("<wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate(" <wsa:Address>");
        content += EscapeSequenceGenerate("<topic>" + topic + "</topic>");
        content += EscapeSequenceGenerate("<newTopic>" + newTopic + "</newTopic>");
        content += EscapeSequenceGenerate("<userAddress>" + receiveAddress + "</userAddress>");
        content += EscapeSequenceGenerate("</wsa:Address>");
        content += EscapeSequenceGenerate("</wsnt:ConsumerReference>");
        content += EscapeSequenceGenerate("<wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:TopicExpression Dialect=\"http://docs.oasis-open.org/wsn/t-1/TopicExpression/Simple\">");
        content += EscapeSequenceGenerate(topic);
        content += EscapeSequenceGenerate("</wsnt:TopicExpression>");
        content += EscapeSequenceGenerate("</wsnt:Filter>");
        content += EscapeSequenceGenerate("<wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:SubscriberAddress>");
        content += EscapeSequenceGenerate("</wsnt:modify>");
        content += "</org:TopicProcess>";
        content += "</soapenv:Body>";
        content += "</soapenv:Envelope>";
        String[] responseValue = send(managerAddr + "/TopicProcess/",new HashMap<String,String>(), "utf-8", true, content.trim());

//        if(responseValue[0].equals("200") && !responseValue[1].contains("failed")) {
//            returnValue="ok";
//            SubscribeResponse response = new SubscribeResponse();
//            String message = responseValue[1];
//            int messageStart = message.indexOf("<ns2:Address>") + 13;
//            int messageEnd = message.indexOf("</ns2:Address>");
//            if( ( messageStart >= 0 ) && ( messageEnd >= 0 ) )
//            {
//                String address = message.substring(messageStart, messageEnd);
//                subscriptionAddr = address;
//                System.out.println("******************************subscribe Address " + subscriptionAddr);
//            }
//        } else {
//            returnValue="error";
//        }
        return returnValue;
    }

    /**
     * 将<、>切换为xml格式
     *
     * @param string
     * @return
     */
    public static String EscapeSequenceGenerate(String string){
        string = string.replaceAll("<", "&lt;");
        string = string.replaceAll(">", "&gt;");
        return string;
    }

    protected String[] send(String url, Map<String, String> params, String charset, boolean pretty, String content) {
        StringBuffer response = new StringBuffer();
//        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        method.setQueryString("");
        try {
            content = new String(content.getBytes("UTF-8"),"ISO-8859-1");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
//		method.setRequestBody(content);
        System.out.println("content:   "+content);
        try {
            method.setRequestEntity(new StringRequestEntity(content, "text/xml", "utf-8"));
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        if (params != null) {
            HttpMethodParams p = new HttpMethodParams();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                p.setParameter(entry.getKey(), entry.getValue());
            }
            method.setParams(p);
        }
        try {
            int status = client.executeMethod(method);
            counter++;
            System.out.println("isresponsecounter:"+counter);
            System.out.println("----------5");
            BufferedReader reader = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), charset));
            String line;
            System.out.println("----------1");
            while ((line = reader.readLine()) != null) {
                System.out.println("----------2");
                if (pretty)
                    response.append(line).append(System.getProperty("line.separator"));
                else
                    response.append(line);
            }
            System.out.println("----------3");
            reader.close();
            method.releaseConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        System.out.println("----------4");
        String res = response.toString();
        res = res.replaceAll("&lt;", "<");
        res = res.replaceAll("&gt;", ">");
        res = res.replaceAll("&quot;", "\"\"");
        System.out.println("response:  "+res);
        return new String[] {String.valueOf(method.getStatusCode()),res};
    }
}
