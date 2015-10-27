package team.justtag.testapp;

import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by junwoo on 2015-10-22.
 */
public class Communication {
    String session_id=null;
    public String key(String url){
        HttpClient httpClient = new DefaultHttpClient();
        String result = "";
        try {
            HttpGet httpGetRequest = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGetRequest);
            result = getResult(httpResponse);
        }catch(Exception e) {
           // e.getStackTraceString();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }

    return result;
    }

    public String login(String url, String encoding){
        HttpClient httpClient = new DefaultHttpClient();
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(encoding);
            httpPost.setEntity(entity);
            httpPost.setHeader("Cookie", session_id);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            result = getResult(httpResponse);
        }catch(Exception e) {
            // e.getStackTraceString();
        }finally {
            httpClient.getConnectionManager().shutdown();
        }

        return result;
    }

    public String getResult(HttpResponse httpResponse) {
        StringBuilder JSONdata = new StringBuilder();
        byte[] buffer = new byte[1024];
        try {
            StatusLine statusLine = httpResponse.getStatusLine(); // 문자열 HTTP⁄1.1 200 OK
            try {
                Header[] headers = httpResponse.getHeaders("Set-Cookie");
                session_id = headers[0].getValue();
                Log.e("sesstionId", "sesstionId : " + session_id);
            }catch(Exception e){

            }
            int statusCode = statusLine.getStatusCode();
            if (statusCode > 199 && statusCode < 300) {
                HttpEntity entity = httpResponse.getEntity();
                if (entity != null) {
                    InputStream inputStream = entity.getContent();
                    BufferedInputStream bis = new BufferedInputStream(inputStream);
                    int bytesRead = 0;
                    while ((bytesRead = bis.read(buffer)) != -1) {
                        String line = new String(buffer, 0, bytesRead);
                        JSONdata.append(line);
                    }
                    inputStream.close();
                }
            }
        } catch (Exception e) {

        } finally {
            return JSONdata.toString();
        }
    }

}
