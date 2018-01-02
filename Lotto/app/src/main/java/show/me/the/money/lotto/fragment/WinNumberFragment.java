package show.me.the.money.lotto.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import show.me.the.money.lotto.LoadingView;
import show.me.the.money.lotto.R;
import show.me.the.money.lotto.data.DBManager;
import show.me.the.money.lotto.data.DataNumber;
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
    ArrayList<DataNumber> _arrayData = new ArrayList<>();
    int _lastNoCount = 0;
    int _currentCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
        View view = inflater.inflate(R.layout.fragment_win_number, container, false);//(View) getLayoutInflater(). inflate(R.layout.fragment_win_number, null);
        _listWinNumber = (ListView) view.findViewById(R.id.list_win_number);
        _adapter = new WinNumber(_arrayData);
        _listWinNumber.setAdapter(_adapter);

        _currentCount = DBManager.Instance(getContext()).getTotalCount();
        DataNumber data;
        if(_currentCount > 0){
           data = DBManager.Instance(getContext()).getData(_currentCount);
           if(data != null){
               _currentCount = data.no;
           }
        }else{
            _currentCount = 0;
        }

        onLoad();
        return view;
    }

    public void onLoad(){
        LoadingView.Instance().show(getContext());
        RequesterNumber number = new RequesterNumber("");
        ConnectionManager.Instance().send(this, number,"lastNumber");
    }

    @Override
    public void connectionSuccess(String result, String identifier) {
        if(identifier.equals("number")) {
            Gson gson = new Gson();
            ResponseNumber num = gson.fromJson(result, ResponseNumber.class);

            DataNumber data = new DataNumber();
            data.setNumber(num);

            DBManager.Instance(getContext()).insertData(data);

            if(_currentCount < _lastNoCount){
                _currentCount++;
                RequesterNumber number = new RequesterNumber(_currentCount+"");
                ConnectionManager.Instance().send(this, number,"number");
            }else{
                _arrayData = DBManager.Instance(getContext()).getAllData();
                _adapter.updateData(_arrayData);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        LoadingView.Instance().hide();
                        _adapter.notifyDataSetChanged();
                    }
                });
            }
        }else if(identifier.equals("lastNumber")){
            Gson gson = new Gson();
            ResponseNumber num = gson.fromJson(result, ResponseNumber.class);
            _lastNoCount = num.drwNo;

            if(_lastNoCount > _currentCount){
                _currentCount ++;
                RequesterNumber number = new RequesterNumber(_currentCount+"");
                ConnectionManager.Instance().send(this, number,"number");
            }else{
                _arrayData = DBManager.Instance(getContext()).getAllData();
                _adapter.updateData(_arrayData);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        LoadingView.Instance().hide();
                        _adapter.notifyDataSetChanged();
                    }
                });
            }
        }
    }

    @Override
    public void connectionFail(String msg, String identifier) {
        LoadingView.Instance().hide();
        Toast.makeText(getContext(), "로딩 실패", Toast.LENGTH_LONG).show();
    }

    @Override
    public void connectionProgress(int progress, String identifier) {

    }

    @Override
    public void connectionTaskFinish() {

    }

    public class WinNumber extends BaseAdapter{
        ViewHolder _viewHolder = null;
        ArrayList<DataNumber> _array;

        public WinNumber(ArrayList<DataNumber> array){
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

            _viewHolder.noText.setText(_array.get(i).no + "");
            _viewHolder.oneText.setText(_array.get(i).arrayNumber[0] + "");
            _viewHolder.twoText.setText(_array.get(i).arrayNumber[1] + "");
            _viewHolder.threeText.setText(_array.get(i).arrayNumber[2] + "");
            _viewHolder.fourText.setText(_array.get(i).arrayNumber[3] + "");
            _viewHolder.fiveText.setText(_array.get(i).arrayNumber[4] + "");
            _viewHolder.sixText.setText(_array.get(i).arrayNumber[5] + "");
            _viewHolder.bonusText.setText(_array.get(i).bonus + "");

            return v;
        }

        public void updateData(ArrayList<DataNumber> array){
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


