package show.me.the.money.lotto.data;

/**
 * Created by KOITT on 2017-12-29.
 */

public class DataNumber {
    int no=0;
    int [] arrayNumber = new int[6];
    int bonus = 0;

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

}
