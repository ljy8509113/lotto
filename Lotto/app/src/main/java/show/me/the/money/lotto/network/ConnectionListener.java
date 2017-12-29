package show.me.the.money.lotto.network;

/**
 * Created by KOITT on 2017-12-27.
 */

public interface ConnectionListener {
    void connectionSuccess(String result, String identifier);
    void connectionFail(String msg, String identifier);
    void connectionProgress(int progress, String identifier);
}
