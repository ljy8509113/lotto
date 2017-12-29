package show.me.the.money.lotto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import show.me.the.money.lotto.R;
import show.me.the.money.lotto.data.DBManager;
import show.me.the.money.lotto.network.ConnectionListener;
import show.me.the.money.lotto.network.ConnectionManager;
import show.me.the.money.lotto.network.RequesterNumber;
import show.me.the.money.lotto.network.ResponseNumber;

/**
 * Created by KOITT on 2017-12-27.
 */

public class WinNumberFragment extends android.support.v4.app.Fragment implements ConnectionListener {

    ListView _listWinNumber;
    WinNumber _adapter;
    ArrayList<ResponseNumber> _arrayData = new ArrayList<>();
    int _lastNoCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){

        View view = inflater.inflate(R.layout.fragment_win_number, container, false);//(View) getLayoutInflater(). inflate(R.layout.fragment_win_number, null);
        _listWinNumber = (ListView) view.findViewById(R.id.list_win_number);
        _adapter = new WinNumber(_arrayData);
        _listWinNumber.setAdapter(_adapter);

        long rowCount = DBManager.Instance(getContext()).getRowCount();
        if(rowCount > 0){

        }else{

        }

        return view;
    }

    public void onLoad(){
        RequesterNumber num = new RequesterNumber("200");
        ConnectionManager.Instance().send(this, num, "number");
    }

    @Override
    public void connectionSuccess(String result, String identifier) {
        if(identifier.equals("number")) {
            Gson gson = new Gson();
            ResponseNumber num = gson.fromJson(result, ResponseNumber.class);

            if (_arrayData.size() > 0) {
                int lastNumber = _arrayData.get(_arrayData.size() - 1).drwNo;
                if (num.drwNo > lastNumber)
                    _arrayData.add(num);
            } else {
                _arrayData.add(num);
            }



            _adapter.updateData(_arrayData);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    _adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void connectionFail(String msg, String identifier) {

    }

    @Override
    public void connectionProgress(int progress, String identifier) {

    }

    public class WinNumber extends BaseAdapter{
        ViewHolder _viewHolder = null;
        ArrayList<ResponseNumber> _array;

        public WinNumber(ArrayList<ResponseNumber> array){
            _array = array;
        }

        @Override
        public int getCount() {
            return _array.size();
        }

        @Override
        public Object getItem(int i) {
            return _array.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            if(v == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.list_win_number_item, viewGroup, false);

                _viewHolder = new ViewHolder();
                _viewHolder.noText = (TextView)v.findViewById(R.id.text_no);
                _viewHolder.oneText = (TextView)v.findViewById(R.id.text_number_1);
                _viewHolder.twoText = (TextView)v.findViewById(R.id.text_number_2);
                _viewHolder.threeText = (TextView)v.findViewById(R.id.text_number_3);
                _viewHolder.fourText = (TextView)v.findViewById(R.id.text_number_4);
                _viewHolder.fiveText = (TextView)v.findViewById(R.id.text_number_5);
                _viewHolder.sixText = (TextView)v.findViewById(R.id.text_number_6);
                _viewHolder.bonusText = (TextView)v.findViewById(R.id.text_bonus);
                v.setTag(_viewHolder);
            }else {
                _viewHolder = (ViewHolder)v.getTag();
            }

            _viewHolder.noText.setText(_array.get(i).drwNo + "");
            _viewHolder.oneText.setText(_array.get(i).drwtNo1 + "");
            _viewHolder.twoText.setText(_array.get(i).drwtNo2 + "");
            _viewHolder.threeText.setText(_array.get(i).drwtNo3 + "");
            _viewHolder.fourText.setText(_array.get(i).drwtNo4 + "");
            _viewHolder.fiveText.setText(_array.get(i).drwtNo5 + "");
            _viewHolder.sixText.setText(_array.get(i).drwtNo6 + "");
            _viewHolder.bonusText.setText(_array.get(i).bnusNo + "");

            return v;
        }

        public void updateData(ArrayList<ResponseNumber> array){
            _array = array;
        }

        class ViewHolder{
            public TextView noText = null;
            public TextView oneText = null;
            public TextView twoText = null;
            public TextView threeText = null;
            public TextView fourText = null;
            public TextView fiveText = null;
            public TextView sixText = null;
            public TextView bonusText = null;
        }
    }

}


