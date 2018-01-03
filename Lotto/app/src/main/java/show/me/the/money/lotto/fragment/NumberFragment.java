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

public class NumberFragment extends android.support.v4.app.Fragment implements View.OnClickListener{

    TextView [] _arrayTxtNumber = new TextView[6];
    ListView _listViewNumber;
    AdapterNumber _adapter;
    ArrayList<DataNumber> _arrayData = new ArrayList<>();
    ArrayList<DataNumber> _arrayAreaData = new ArrayList<>();
    ArrayList<NumberCount> _arrayNumberCount = null;

    class NumberCount{
        public int number;
        public int count;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ){
        View view = inflater.inflate(R.layout.fragment_number, container, false);
        _arrayTxtNumber[0] = (TextView)view.findViewById(R.id.text_number_1);
        _arrayTxtNumber[1] = (TextView)view.findViewById(R.id.text_number_2);
        _arrayTxtNumber[2] = (TextView)view.findViewById(R.id.text_number_3);
        _arrayTxtNumber[3] = (TextView)view.findViewById(R.id.text_number_4);
        _arrayTxtNumber[4] = (TextView)view.findViewById(R.id.text_number_5);
        _arrayTxtNumber[5] = (TextView)view.findViewById(R.id.text_number_6);

        _listViewNumber = view.findViewById(R.id.list_number);
        _adapter = new AdapterNumber(_arrayData);

        view.findViewById(R.id.button_make).setOnClickListener(this);
        view.findViewById(R.id.button_remove_all).setOnClickListener(this);
        view.findViewById(R.id.button_save).setOnClickListener(this);

        return view;
    }

    public void setInit(ArrayList<DataNumber> array){
        _arrayAreaData = array;
        _arrayData = DBManager.Instance(getContext()).getAllData(true);
        reloadListView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_make :
                makeNumber();
                break;
            case R.id.button_save:

                break;
            case R.id.button_remove_all:
                removeIndex(Common.REMOVEALL);
                break;
        }
    }

    void makeNumber(){
        if(_arrayNumberCount == null){
            _arrayNumberCount = new ArrayList<>();

            for(int i=0; i<_arrayAreaData.size(); i++){
                DataNumber d = _arrayAreaData.get(i);
                if(_arrayNumberCount.size() == 0){
                    for(int j=0; j<d.arrayNumber.length; j++){
                        NumberCount c = new NumberCount();
                        c.number = d.arrayNumber[j];
                        c.count += 1;
                        _arrayNumberCount.add(c);
                    }
                }else{
                    for(int j=0; j<d.arrayNumber.length; j++){
                        boolean isAdd = true;
                        for(int z=0; z<_arrayNumberCount.size(); z++){
                            if(d.arrayNumber[j] == _arrayNumberCount.get(z).number) {
                                _arrayNumberCount.get(z).count += 1;
                                isAdd = false;
                                break;
                            }
                        }

                        if(isAdd){
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

        int firstLineIndex = (int)(_arrayNumberCount.size() * 0.3);
        int remainderIndex = _arrayNumberCount.size() - firstLineIndex;

        boolean isStop = false;
        int [] array = {-1,-1,-1,-1,-1,-1};
        int arrayIndex = 0;

        while(!isStop){
            Random r = new Random();
            int index = r.nextInt(firstLineIndex);

            if(array[0] == -1){
                array[0] = _arrayNumberCount.get(index).number;
                arrayIndex ++;
            }else{
                boolean isAdd = true;
                for(int i=0; i<array.length; i++){
                    if(array[i] != -1){
                        if(array[i] == _arrayNumberCount.get(index).number){
                            break;
                        }
                    }
                }
            }

        }

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
            View v = view;
            if(v == null){
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.list_number_item, viewGroup, false);

                _viewHolder = new ViewHolder();
                _viewHolder.arrayText[0] = (TextView)v.findViewById(R.id.text_number_1);
                _viewHolder.arrayText[1] = (TextView)v.findViewById(R.id.text_number_2);
                _viewHolder.arrayText[2] = (TextView)v.findViewById(R.id.text_number_3);
                _viewHolder.arrayText[3] = (TextView)v.findViewById(R.id.text_number_4);
                _viewHolder.arrayText[4] = (TextView)v.findViewById(R.id.text_number_5);
                _viewHolder.arrayText[5] = (TextView)v.findViewById(R.id.text_number_6);
                _viewHolder.buttonRemove = v.findViewById(R.id.button_delete);

                final int index = i;
                _viewHolder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        removeIndex(index);
                    }
                });

                v.setTag(_viewHolder);
            }else {
                _viewHolder = (ViewHolder)v.getTag();
            }

            for(int x=0; x<_viewHolder.arrayText.length; x++){
                _viewHolder.arrayText[i].setText(_array.get(i).arrayNumber[i] + "");
            }
            return v;
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
}


