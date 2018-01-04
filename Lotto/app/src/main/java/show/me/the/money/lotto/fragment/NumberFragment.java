package show.me.the.money.lotto.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import show.me.the.money.lotto.Common;
import show.me.the.money.lotto.LoadingView;
import show.me.the.money.lotto.R;
import show.me.the.money.lotto.data.DBManager;
import show.me.the.money.lotto.data.DataNumber;

/**
 * Created by KOITT on 2017-12-27.
 */

public class NumberFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    TextView[] _arrayTxtNumber = new TextView[6];
    ListView _listViewNumber;
    AdapterNumber _adapter;
    ArrayList<DataNumber> _arrayData = new ArrayList<>();
    ArrayList<DataNumber> _arrayAreaData = new ArrayList<>();
    ArrayList<NumberCount> _arrayNumberCount = null;

    ArrayList<Integer> _arrayResult = new ArrayList<>();

    class NumberCount {
        public int number;
        public int count;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_number, container, false);
        _arrayTxtNumber[0] = (TextView) view.findViewById(R.id.text_number_1);
        _arrayTxtNumber[1] = (TextView) view.findViewById(R.id.text_number_2);
        _arrayTxtNumber[2] = (TextView) view.findViewById(R.id.text_number_3);
        _arrayTxtNumber[3] = (TextView) view.findViewById(R.id.text_number_4);
        _arrayTxtNumber[4] = (TextView) view.findViewById(R.id.text_number_5);
        _arrayTxtNumber[5] = (TextView) view.findViewById(R.id.text_number_6);

        _listViewNumber = view.findViewById(R.id.list_number);
        _adapter = new AdapterNumber(_arrayData);
        _listViewNumber.setAdapter(_adapter);

        view.findViewById(R.id.button_make).setOnClickListener(this);
        view.findViewById(R.id.button_remove_all).setOnClickListener(this);
        view.findViewById(R.id.button_save).setOnClickListener(this);

        return view;
    }

    public void setInit(ArrayList<DataNumber> array) {
        _arrayAreaData = array;
        _arrayData = DBManager.Instance(getContext()).getAllData(true);
        reloadListView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_make:
                makeNumber();
                break;
            case R.id.button_save:
                DataNumber data = new DataNumber();
                for(int i=0; i<_arrayResult.size(); i++){
                    data.arrayNumber[i] = (int)_arrayResult.get(i);
                }
                _arrayData.add(data);
                DBManager.Instance(getContext()).insertData(data, true);
                reloadListView();
                break;
            case R.id.button_remove_all:
                removeIndex(Common.REMOVEALL);
                break;
        }
    }

    void makeNumber() {
        if(_arrayResult.size() > 0)
            _arrayResult.clear();

        if (_arrayNumberCount == null) {
            _arrayNumberCount = new ArrayList<>();

            for (int i = 0; i < _arrayAreaData.size(); i++) {
                DataNumber d = _arrayAreaData.get(i);
                if (_arrayNumberCount.size() == 0) {
                    for (int j = 0; j < d.arrayNumber.length; j++) {
                        NumberCount c = new NumberCount();
                        c.number = d.arrayNumber[j];
                        c.count += 1;
                        _arrayNumberCount.add(c);
                    }
                } else {
                    for (int j = 0; j < d.arrayNumber.length; j++) {
                        boolean isAdd = true;
                        for (int z = 0; z < _arrayNumberCount.size(); z++) {
                            if (d.arrayNumber[j] == _arrayNumberCount.get(z).number) {
                                _arrayNumberCount.get(z).count += 1;
                                isAdd = false;
                                break;
                            }
                        }

                        if (isAdd) {
                            NumberCount c = new NumberCount();
                            c.number = d.arrayNumber[j];
                            c.count += 1;
                            _arrayNumberCount.add(c);
                        }
                    }
                }
            }
        }

        Descending desc = new Descending();
        Collections.sort(_arrayNumberCount, desc);

        int firstLineIndex = (int) (_arrayNumberCount.size() * 0.3);

        ArrayList<NumberCount> arrayFirst = new ArrayList<>();
        ArrayList<NumberCount> arrayOther = new ArrayList<>();

        for(int i=0; i<=firstLineIndex; i++){
            arrayFirst.add(_arrayNumberCount.get(i));
        }

        for(int i=firstLineIndex+1; i<_arrayNumberCount.size(); i++){
            arrayOther.add(_arrayNumberCount.get(i));
        }


        _arrayResult.addAll(getRandomNumber(arrayFirst, 3));
        _arrayResult.addAll(getRandomNumber(arrayOther, 3));

        AscInt sort = new AscInt();
        Collections.sort(_arrayResult, sort);

        Log.d("lee", _arrayResult + "");

        for(int i=0; i<_arrayTxtNumber.length; i++){
            _arrayTxtNumber[i].setText(_arrayResult.get(i) + "");
        }

    }

    ArrayList<Integer> getRandomNumber(ArrayList<NumberCount> array, int count){
        ArrayList<Integer> arrayResult = new ArrayList<>();

        while(arrayResult.size() < count) {
            Random r = new Random();
            int number = array.get(r.nextInt(array.size())).number;

            if (arrayResult.size() == 0) {
                arrayResult.add(number);
            } else {
                boolean isAdd = true;
                for (int i = 0; i < arrayResult.size(); i++) {
                    if (arrayResult.get(i) == number) {
                        isAdd = false;
                        break;
                    }
                }
                if (isAdd) {
                    arrayResult.add(number);
                }
            }
        }
        return arrayResult;
    }

    void reloadListView(){
        _adapter.updateData(_arrayData);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                _adapter.notifyDataSetChanged();
            }
        });
    }

    public void removeIndex(int index){
        if(index == Common.REMOVEALL){
            //전체삭제
            _arrayData.clear();
            DBManager.Instance(getContext()).deleteRandomTable(Common.REMOVEALL);
        }else{
            DBManager.Instance(getContext()).deleteRandomTable(_arrayData.get(index).no);
            _arrayData.remove(index);
        }

        reloadListView();
    }

    class AdapterNumber extends BaseAdapter {
        AdapterNumber.ViewHolder _viewHolder = null;
        ArrayList<DataNumber> _array;

        public AdapterNumber(ArrayList<DataNumber> array){
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
            if(view == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.list_number_item, viewGroup, false);

                _viewHolder = new ViewHolder();
                _viewHolder.arrayText[0] = (TextView)view.findViewById(R.id.text_number_1);
                _viewHolder.arrayText[1] = (TextView)view.findViewById(R.id.text_number_2);
                _viewHolder.arrayText[2] = (TextView)view.findViewById(R.id.text_number_3);
                _viewHolder.arrayText[3] = (TextView)view.findViewById(R.id.text_number_4);
                _viewHolder.arrayText[4] = (TextView)view.findViewById(R.id.text_number_5);
                _viewHolder.arrayText[5] = (TextView)view.findViewById(R.id.text_number_6);
                _viewHolder.buttonRemove = view.findViewById(R.id.button_delete);

                final int index = i;
                _viewHolder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeIndex(index);
                    }
                });

                view.setTag(_viewHolder);
            }else {
                _viewHolder = (ViewHolder)view.getTag();
            }

            for(int x=0; x<_viewHolder.arrayText.length; x++){
                _viewHolder.arrayText[x].setText(_array.get(i).arrayNumber[x] + "");
            }
            return view;
        }

        public void updateData(ArrayList<DataNumber> array){
            _array = array;
        }

        class ViewHolder{
            public TextView [] arrayText = new TextView[6];
            public Button buttonRemove;
        }
    }

    class Descending implements Comparator<NumberCount> {
        @Override
        public int compare(NumberCount o1, NumberCount o2) {
            return o2.count - o1.count;
        }
    }

    class AscInt implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }
}


