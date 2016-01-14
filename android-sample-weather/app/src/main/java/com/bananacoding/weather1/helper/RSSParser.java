package com.bananacoding.weather1.helper;

import com.bananacoding.weather1.model.RSSWeather;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;


/**
 * Created by chamnarn on 12/28/15.
 */
public class RSSParser {
    private static String TAG_CHANNEL = "channel";
    private static String TAG_ITEM = "item";
    private static String TAG_TEMP = "temp";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_YWEATHER_CONDITION = "yweather:condition";

    private static String TAG_WOEID = "woeid";
    private static String TAG_RESULTS = "results";
    private static String TAG_RESULT = "Result";
    public RSSWeather getRSSFeedWeather(String rss_url) {
        String rss_feed_xml;

        // get RSS XML from rss url
        rss_feed_xml = this.getXmlFromUrl(rss_url);

        // check if RSS XML fetched or not
        if (rss_feed_xml != null) {
            // successfully fetched rss xml
            // parse the xml
            try {
                Document doc = this.getDomElement(rss_feed_xml);
                NodeList nodeList = doc.getElementsByTagName(TAG_CHANNEL);
                Element e = (Element) nodeList.item(0);

                NodeList items = e.getElementsByTagName(TAG_ITEM);
                Element e1 = (Element) items.item(0);

                String title = this.getValue(e1, TAG_TITLE);
                String link = this.getValue(e1, TAG_LINK);
                String pubdate = this.getValue(e1, TAG_PUB_DATE);

                //Get nodes from tag name.
                NodeList nodeList2 = e1.getElementsByTagName(TAG_YWEATHER_CONDITION);
                //Get node value from attributes.
                String temp = nodeList2.item(0).getAttributes().getNamedItem(TAG_TEMP).getNodeValue();

                RSSWeather rssWeather = new RSSWeather(title, link, temp, pubdate);
                return rssWeather;
            } catch (Exception e) {
                // Check log for errors
                e.printStackTrace();
            }
        }

        // return item list
        return null;
    }

    public RSSWeather getRSSFeedWeather2(String query_url) {
        String query_feed_xml;

        // get RSS XML from rss url
        query_feed_xml = this.getXmlFromUrl(query_url);

        // check if RSS XML fetched or not
        if (query_feed_xml != null) {
            // successfully fetched rss xml
            // parse the xml
            try {
                Document doc = this.getDomElement(query_feed_xml);
                NodeList nodeList = doc.getElementsByTagName(TAG_RESULTS);
                Element e = (Element) nodeList.item(0);

                NodeList items = e.getElementsByTagName(TAG_RESULT);
                Element e1 = (Element) items.item(0);

                String woeid = this.getValue(e1, TAG_WOEID);

                RSSWeather rssLatLong = new RSSWeather(woeid);
                return rssLatLong;
            } catch (Exception e) {
                // Check log for errors
                e.printStackTrace();
                System.out.println("Woeid no have---------");
            }
        }
        // return item list
        return null;
    }

    /**
     * Method to get xml content from url HTTP Get request
     */
    public String getXmlFromUrl(String url) {
        String xml = null;

        try {
            // request method is GET
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            xml = EntityUtils.toString(httpEntity, HTTP.UTF_8);

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

    /**
     * Getting XML DOM element
     *
     * @param XML string
     */
    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }

    /**
     * Getting node value
     *
     * @param elem element
     */
    public final String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child
                        .getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE || (child.getNodeType() == Node.CDATA_SECTION_NODE)) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }

    /**
     * Getting node value
     *
     * @param Element node
     * @param key     string
     */
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }


}
