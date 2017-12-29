package show.me.the.money.lotto.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import show.me.the.money.lotto.R;

/**
 * Created by KOITT on 2017-12-27.
 */

public class NumberFragment extends android.support.v4.app.Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
        return inflater.inflate(R.layout.fragment_number, container, false);
    }
}
