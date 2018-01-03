package show.me.the.money.lotto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by KOITT on 2017-12-27.
 */

public class Common {
    public static String URL = "http://www.nlotto.co.kr/common.do?";
    public static final int REMOVEALL = -1;
    public static Gson _gson = null;
    public static final int RANDOM_AREA = 50;

    public static Object getResponseObj(String resStr, Object resObj){
        if(_gson == null)
            _gson = new Gson();
        return _gson.fromJson(resStr, resObj.getClass());
    }

}
