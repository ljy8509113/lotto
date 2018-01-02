package show.me.the.money.lotto;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatDialog;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * Created by KOITT on 2018-01-02.
 */

public class LoadingView {
    static LoadingView _instance = null;
    AppCompatDialog _progressDialog = null;
//    ProgressBar _loadingBar = null;

    public static LoadingView Instance(){
        if(_instance == null) {
            _instance = new LoadingView();
        }
        return _instance;
    }

    public void show(Context context){
        if (_progressDialog != null && _progressDialog.isShowing()) {
            _progressDialog.show();
        } else {
            _progressDialog = new AppCompatDialog(context);
            _progressDialog.setContentView(R.layout.loading_view);
//            LinearLayout bg = _progressDialog.findViewById(R.id.layout_progress);
//            bg.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//            ProgressBar loadingBar = (ProgressBar) _progressDialog.findViewById(R.id.progress);
//            loadingBar.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            _progressDialog.setCancelable(false);
            _progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            _progressDialog.show();
        }
    }

    public void hide(){
        if(_progressDialog != null && _progressDialog.isShowing() ){
            _progressDialog.hide();
            _progressDialog.dismiss();
        }
    }

}
