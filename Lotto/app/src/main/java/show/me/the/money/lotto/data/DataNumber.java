package show.me.the.money.lotto.data;

import show.me.the.money.lotto.network.ResponseNumber;

/**
 * Created by KOITT on 2017-12-29.
 */

public class DataNumber {
    public int no=0;
    public int [] arrayNumber = new int[6];
    public int bonus = 0;

    public void setNumber(int no, int number1, int number2, int number3, int number4, int number5, int number6, int bonus){
        this.no = no;
        arrayNumber[0] = number1;
        arrayNumber[1] = number2;
        arrayNumber[2] = number3;
        arrayNumber[3] = number4;
        arrayNumber[4] = number5;
        arrayNumber[5] = number6;
        this.bonus = bonus;
    }

    public void setNumber(ResponseNumber res){
        no = res.drwNo;
        arrayNumber[0] = res.drwtNo1;
        arrayNumber[1] = res.drwtNo2;
        arrayNumber[2] = res.drwtNo3;
        arrayNumber[3] = res.drwtNo4;
        arrayNumber[4] = res.drwtNo5;
        arrayNumber[5] = res.drwtNo6;
        bonus = res.bnusNo;
    }

}
