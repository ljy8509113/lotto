package show.me.the.money.lotto.network;

import android.util.Log;

import java.util.ArrayList;

import show.me.the.money.lotto.Common;

/**
 * Created by KOITT on 2017-12-27.
 */

public class ConnectionManager implements ConnectionListener{
    static ConnectionManager _instance = null;
    ConnectionListener _listener = null;
    HttpConnectionUtil _util = null;

    ArrayList<RequestObj> _arrayReqObject = new ArrayList<>();

    class RequestObj{
        String identifier = "";
        String obj = "";
        String type = "";
        public RequestObj(String identifier, String obj, String type){
            this.identifier = identifier;
            this.obj = obj;
            this.type = type;
        }

    }

    public static ConnectionManager Instance(){
        if(_instance == null)
            _instance = new ConnectionManager();
        return _instance;
    }

    public void send(ConnectionListener listener, Requester obj, String identifier){
        RequestObj req = new RequestObj(identifier, obj.toStringGET(),"GET");
        _arrayReqObject.add(req);
        _listener = listener;

        send();
    }

    void send(){
        Log.d("lee", "send _arrayCount : " + _arrayReqObject.size());
        RequestObj req = _arrayReqObject.get(0);
        if(_util == null) {
            _util = new HttpConnectionUtil(Common.URL, this);
            _util.requestSend(req.obj, "GET", req.identifier);
        }
    }

    @Override
    public void connectionSuccess(String result, String identifier) {
        _arrayReqObject.remove(0);
        if(_listener != null) {
            Log.d("lee ", "success : " + result);
            Log.d("lee ", "array size : " + _arrayReqObject.size());
            _listener.connectionSuccess(result, identifier);
        }
    }

    @Override
    public void connectionFail(String msg, String identifier) {
        _arrayReqObject.remove(0);
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

    @Override
    public void connectionTaskFinish() {
        _util = null;
        Log.d("lee", "req : " + "connectionTaskFinish - arraySize : "+ _arrayReqObject.size());
        if(_arrayReqObject.size() > 0){
            send();
        }
    }
}
