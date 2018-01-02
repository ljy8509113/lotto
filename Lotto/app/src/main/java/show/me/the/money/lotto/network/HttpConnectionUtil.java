package show.me.the.money.lotto.network;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by KOITT on 2017-12-27.
 */

public class HttpConnectionUtil extends AsyncTask<String, Void, Void> {
    String _identifier = "";
    String _obj = "";
    String _reqUrl = "";

    public String _url = "";
    ConnectionListener _listener;

    public HttpConnectionUtil(String url, ConnectionListener listener) {
        _url = url;
        _listener = listener;
    }

    public void requestSend(String obj, String type, String identifier) {
        if (type.toUpperCase().equals("GET")) {
            _reqUrl = _url += obj;
        } else {
            _obj = obj;
        }
        _identifier = identifier;
        execute(type);
    }

    @Override
    protected Void doInBackground(String... params) {
        Log.d("lee ", "param : " + params[0]);
        if (params[0].toUpperCase().equals("GET")) {
            try {
                // Get방식으로 전송 하기
                URL url = new URL(_reqUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "*/*");

                int resCode = urlConnection.getResponseCode();
                if (resCode != HttpsURLConnection.HTTP_OK) {
                    _listener.connectionFail(resCode + "", _identifier);
                } else {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String input;
                    StringBuffer sb = new StringBuffer();

                    while ((input = reader.readLine()) != null) {
                        sb.append(input);
                    }
                    _listener.connectionSuccess(sb.toString(), _identifier);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL obj = new URL(_url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod(params[0]);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                byte[] outputInBytes = _obj.getBytes("UTF-8");//params[0].getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write(outputInBytes);
                os.close();

                int retCode = conn.getResponseCode();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                br.close();
                String res = response.toString();
                if (_listener != null)
                    _listener.connectionSuccess(res, _identifier);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
        _listener.connectionTaskFinish();
    }
}