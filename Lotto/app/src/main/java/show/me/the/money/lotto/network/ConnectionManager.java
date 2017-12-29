package show.me.the.money.lotto.network;

import android.util.Log;

import org.json.JSONObject;

import show.me.the.money.lotto.Common;

/**
 * Created by KOITT on 2017-12-27.
 */

public class ConnectionManager implements ConnectionListener{
    static ConnectionManager _instance = null;
    ConnectionListener _listener = null;

    public static ConnectionManager Instance(){
        if(_instance == null)
            _instance = new ConnectionManager();
        return _instance;
    }

    public void send(ConnectionListener listener, Requester obj, String identifier){
        _listener = listener;

        HttpConnectionUtil util = new HttpConnectionUtil(Common.URL, this);
        util.requestSend(obj.toStringGET(), "GET", identifier);
    }

    @Override
    public void connectionSuccess(String result, String identifier) {
        if(_listener != null) {
            Log.d("lee ", "success : " + result);
            _listener.connectionSuccess(result, identifier);
        }
    }

    @Override
    public void connectionFail(String msg, String identifier) {
        if(_listener != null) {
            Log.d("lee ", "fail : " + msg);
            _listener.connectionFail(msg, identifier);
        }
    }

    @Override
    public void connectionProgress(int progress, String identifier) {
        if(_listener != null) {
            _listener.connectionProgress(progress, identifier);
        }
    }
}
