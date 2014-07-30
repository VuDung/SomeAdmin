package com.some.admin.parser;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.some.admin.model.Bound;
import com.some.admin.model.Online;
import com.some.admin.model.Player;
import com.some.admin.model.Register;
import com.some.admin.model.Total;


public class DataParser {

	public boolean parserLogin(String inputString){
		
		try {
			Document doc = getDomElement(inputString);
			NodeList nodeList = doc.getElementsByTagName("result");
			String result = nodeList.item(0).getFirstChild().getNodeValue().trim();
			if(result.equalsIgnoreCase("1")){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public List<Online> parserOnline(String inputString){
		
		try {
			List<Online> listOnline = new ArrayList<Online>();
			Document doc = getDomElement(inputString);
			NodeList nodeList = doc.getElementsByTagName("user");
			for(int i = 0; i < nodeList.getLength(); i++){
				Element e = (Element)nodeList.item(i);
				Online item = new Online();
				item.setNamePlayer(getValue(e, "uname"));
				item.setMoneyPlayer(getValue(e, "money"));
				listOnline.add(item);
			}
			return listOnline;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Bound> parserBound(String inputString){
		try {
			List<Bound> listBound = new ArrayList<Bound>();
			Document doc = getDomElement(inputString);
			NodeList nl = doc.getElementsByTagName("transaction");
			for(int i = 0; i < nl.getLength(); i++){
				Element e = (Element)nl.item(i);
				Bound item = new Bound();
				item.setMoney(getValue(e, "money"));
				item.setUname(getValue(e, "uname"));
				item.setCtime(getValue(e, "ctime"));
				listBound.add(item);
			}
			return listBound;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Register> parserRegister(String inputString){
		try {
			List<Register> listRegister = new ArrayList<Register>();
			Document doc = getDomElement(inputString);
			NodeList nl = doc.getElementsByTagName("user");
			for(int i = 0; i < nl.getLength(); i++){
				Element e = (Element)nl.item(i);
				Register item = new Register();
				item.setUserRegister(getValue(e, "uname"));
				item.setCtime(getValue(e, "ctime"));
				listRegister.add(item);
			}
			return listRegister;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> parserPlayer(String inputString){
		try {
			List<Player> listPlayer = new ArrayList<Player>();
			Document doc = getDomElement(inputString);
			NodeList nl = doc.getElementsByTagName("result");
			for(int i = 0; i < nl.getLength(); i++){
				Element e = (Element)nl.item(i);
				Player item = new Player();
				item.setGameType(getValue(e, "gameType"));
				item.setBetplayer(getValue(e, "betplayer"));
				item.setBetbanker(getValue(e, "betbanker"));
				item.setBettie(getValue(e, "bettie"));
				item.setBetbankerpair(getValue(e, "betbankerpair"));
				item.setBetplayerpair(getValue(e, "betplayerpair"));
				listPlayer.add(item);
			}
			return listPlayer;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Total> parserTotal(String inputString){
		try {
			List<Total> listTotal = new ArrayList<Total>();
			Document doc = getDomElement(inputString);
			NodeList nl = doc.getElementsByTagName("item");
			for(int i = 0; i < nl.getLength(); i++){
				Element e = (Element)nl.item(i);
				Total item = new Total();
				
				item.setBetplayer(getValue(e, "betplayer"));
				item.setBetbanker(getValue(e, "betbanker"));
				item.setBettie(getValue(e, "bettie"));
				item.setBetbankerpair(getValue(e, "betbankerpair"));
				item.setBetplayerpair(getValue(e, "betplayerpair"));
				listTotal.add(item);
			}
			return listTotal;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	public String getXmlFromUrl(String url) {
        String xml = null;
 
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return XML
        return xml;
    }
	
	public Document getDomElement(String xml) {

		Document doc = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));
			if (is != null)
				doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			System.out.println("XML parse error: " + e.getMessage());
			return null;
		} catch (SAXException e) {
			System.out.println("Wrong XML file structure: " + e.getMessage());
			return null;
		} catch (IOException e) {
			System.out.println("I/O exeption: " + e.getMessage());
			return null;
		}

		return doc;

	}
	
	public String getValue(Element item, String str) {      
	    NodeList n = item.getElementsByTagName(str);        
	    return this.getElementValue(n.item(0)).trim();
	}
	 
	public final String getElementValue( Node elem ) {
	         Node child;
	         if( elem != null){
	             if (elem.hasChildNodes()){
	                 for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){
	                     if( child.getNodeType() == Node.TEXT_NODE  ){
	                         return child.getNodeValue();
	                     }
	                 }
	             }
	         }
	         return "";
	  } 
}
