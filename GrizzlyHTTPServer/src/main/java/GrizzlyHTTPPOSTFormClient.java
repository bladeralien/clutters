import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by peiyuchao on 16/6/16.
 */
public class GrizzlyHTTPPOSTFormClient {

    public static void main(String[] args) throws Exception {

        String url = "http://localhost:8080/postform";
        String xmlString = "<?xml version=\"1.0\"?>\n" +
                "<catalog>\n" +
                "   <book id=\"bk101\">\n" +
                "      <author>Gambardella, Matthew</author>\n" +
                "      <title>XML Developer's Guide</title>\n" +
                "      <genre>Computer</genre>\n" +
                "      <price>44.95</price>\n" +
                "      <publish_date>2000-10-01</publish_date>\n" +
                "      <description>An in-depth look at creating applications \n" +
                "      with XML.</description>\n" +
                "   </book>\n" +
                "</catalog>";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", "User-Agent");

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("para1", "para1val"));
        urlParameters.add(new BasicNameValuePair("para2", "para2val"));
        urlParameters.add(new BasicNameValuePair("xml", xmlString));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line + "\n");
        }

        System.out.println(result.toString());
    }
}
