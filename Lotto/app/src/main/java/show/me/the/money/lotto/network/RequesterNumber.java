package show.me.the.money.lotto.network;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by KOITT on 2017-12-27.
 */

public class RequesterNumber extends Requester {
    public String drwNo = "";

    public RequesterNumber(String drwNo){
        this.drwNo = drwNo;
    }

    @Override
    public String toStringGET() {
        Gson g = new Gson();
        String result = "method="+method() + "&" +"drwNo="+drwNo;
        return result;
    }

    public String method(){
        return "getLottoNumber";
    }
}
