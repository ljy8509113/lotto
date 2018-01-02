package show.me.the.money.lotto.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by KOITT on 2017-12-29.
 */

public class DBManager {
    static DBManager instance = null;
    DBContactHelper _helper;

    public static DBManager Instance(Context context){
        if(instance == null) {
            instance = new DBManager();
            instance.init(context);
        }
        return instance;
    }

    public void init(Context context){
        _helper = new DBContactHelper(context);
    }

    public void insertData(DataNumber data ){
        _helper.addNumberInfo(data);
    }

    public ArrayList<DataNumber> getAllData(){
        return _helper.getAllData();
    }

    public DataNumber getData(int no){
        return _helper.getNoInfo(no);
    }

    public int getTotalCount(){
        return instance._helper.getContactsCount();
    }

    class DBContactHelper extends SQLiteOpenHelper {
        // All Static variables

        final static String DB_NAME = "lotto_db";
        // Contacts table name
        private static final String TABLE_WIN = "win_number_info";

        // Contacts Table Columns names
        private static final String KEY_NO = "index_no";
        private static final String KEY_NUMBER = "number";
        private static final String KEY_BONUS = "bonus";

        public DBContactHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+TABLE_WIN+"(" +
                    KEY_NO+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_NUMBER+"0 INTEGER," +
                    KEY_NUMBER+"1 INTEGER," +
                    KEY_NUMBER+"2 INTEGER," +
                    KEY_NUMBER+"3 INTEGER," +
                    KEY_NUMBER+"4 INTEGER," +
                    KEY_NUMBER+"5 INTEGER," +
                    KEY_BONUS+" INTEGER"+
                    ");");
            Log.d("lee", "create table");
        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_WIN);
            // Create tables again
            onCreate(db);
        }

        /**
         * CRUD 함수
         */
        // 새로운 Contact 함수 추가
        public void addNumberInfo(DataNumber contact) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NO, contact.no); // Contact Name
            for(int i=0; i<contact.arrayNumber.length; i++){
                values.put(KEY_NUMBER+i, contact.arrayNumber[i]); // Contact Phone
            }
            values.put(KEY_BONUS, contact.bonus);

            // Inserting Row
            db.insert(TABLE_WIN, null, values);
//            db.close(); // Closing database connection
        }

        // id 에 해당하는 Contact 객체 가져오기
        public DataNumber getNoInfo(int no) {
            SQLiteDatabase db = this.getReadableDatabase();
            String [] column = new String[8];
            column[0] = KEY_NO;
            for(int i=1; i<column.length-1; i++){
                column[i] = KEY_NUMBER+(i-1);
            }
            column[7] = KEY_BONUS;
            Cursor cursor = db.query(TABLE_WIN, column, KEY_NO + "=?", new String[] { String.valueOf(no) }, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();

                DataNumber data = new DataNumber();
                data.setNumber(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7))
                );
                return data;
            }else{
                return null;
            }
        }

        // 모든 Contact 정보 가져오기
        public ArrayList<DataNumber> getAllData() {
            ArrayList<DataNumber> listData = new ArrayList<>();
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_WIN +" ORDER BY "+KEY_NO+" desc";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
//            if (cursor.moveToFirst()) {
//                do {
//                    Contact contact = new Contact();
//                    contact.setID(Integer.parseInt(cursor.getString(0)));
//                    contact.setName(cursor.getString(1));
//                    contact.setPhoneNumber(cursor.getString(2));
//                    // Adding contact to list
//                    contactList.add(contact);
//                } while (cursor.moveToNext());
//            }

            while (cursor.moveToNext()){
                DataNumber data = new DataNumber();
                data.setNumber(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        Integer.parseInt(cursor.getString(2)),
                        Integer.parseInt(cursor.getString(3)),
                        Integer.parseInt(cursor.getString(4)),
                        Integer.parseInt(cursor.getString(5)),
                        Integer.parseInt(cursor.getString(6)),
                        Integer.parseInt(cursor.getString(7))
                );
                listData.add(data);
            }
            // return contact list
            return listData;
        }

        //Contact 정보 업데이트
//        public int updateContact(DataNumber data) {
//            SQLiteDatabase db = this.getWritableDatabase();
//
//            ContentValues values = new ContentValues();
//            values.put(KEY_NAME, contact.getName());
//            values.put(KEY_PH_NO, contact.getPhoneNumber());
//
//            // updating row
//            return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
//                    new String[] { String.valueOf(contact.getID()) });
//        }
//
//        // Contact 정보 삭제하기
//        public void deleteContact(Contact contact) {
//            SQLiteDatabase db = this.getWritableDatabase();
//            db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                    new String[] { String.valueOf(contact.getID()) });
//            db.close();
//        }

        // Contact 정보 숫자
        public int getContactsCount() {
//            String countQuery = "SELECT  * FROM " + TABLE_WIN;
//            SQLiteDatabase db = this.getReadableDatabase();
//            Cursor cursor = db.rawQuery(countQuery, null);
//
//            return cursor.getCount();

            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM "+TABLE_WIN, null);

            return numRows;
        }
    }


//    public long getRowCount(){
//        return _helper.selectQueryTableCount();
//    }
//
//    public String getSelectData(int no){
//        return _helper.selectQueryNumber(no);
//    }
//
//    public String getSelectAll(){
//        return _helper.
//    }
//
//    public void insert(int no, int[]array){
//        _helper.insert(no, array);
//    }
//
//    class DBHelper extends SQLiteOpenHelper{
//        // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
//        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//            super(context, name, factory, version);
//        }
//
//        // DB를 새로 생성할 때 호출되는 함수
//        @Override
//        public void onCreate(SQLiteDatabase db) {
//            // 새로운 테이블 생성
//            db.execSQL("CREATE TABLE "+TABLE_WIN+"(" +
//                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                    " number INTEGER," +
//                    " number_1 INTEGER," +
//                    " number_2 INTEGER," +
//                    " number_3 INTEGER," +
//                    " number_4 INTEGER," +
//                    " number_5 INTEGER," +
//                    " number_6 INTEGER," +
//                    " bonus INTEGER"+
//                    ");");
//        }
//
//        // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
//        @Override
//        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//        }
//
//        public void insert(int no, int[] arrayNumber) {
//            // 읽고 쓰기가 가능하게 DB 열기
//            SQLiteDatabase db = getWritableDatabase();
//            // DB에 입력한 값으로 행 추가
//            String query = "";
//            for (int i = 0; i < arrayNumber.length; i++) {
//                if (arrayNumber.length - 1 == i)
//                    query += "'" + arrayNumber[i] + "'";
//                else
//                    query += "'" + arrayNumber[i] + "',";
//
//            }
//            db.execSQL("INSERT INTO " + TABLE_WIN + " VALUES('" + no + "'," + query + ");");
//        }
//
//        public void update(int no, int[] number) {
//            SQLiteDatabase db = getWritableDatabase();
//            // 입력한 항목과 일치하는 행의 가격 정보 수정
//            db.execSQL("UPDATE "+TABLE_WIN+" SET no=" + no +
//                        " WHERE number1='" + number[0] + "'," +
//                        "number2='"+number[1]+"'," +
//                        "number3='"+number[2]+"'," +
//                        "number4='"+number[3]+"'," +
//                        "number5='"+number[4]+"'," +
//                        "number6='"+number[5]+"'," +
//                        "bonus='" +number[6]+"');" );
//        }
//
//        public void delete(String item) {
////            SQLiteDatabase db = getWritableDatabase();
////            // 입력한 항목과 일치하는 행 삭제
////            db.execSQL("DELETE FROM "+TABLE_WIN+" WHERE item='" + item + "';");
//        }
//
//        public String selectQueryNumber(int no) {
//            // 읽기가 가능하게 DB 열기
//            SQLiteDatabase db = getReadableDatabase();
//            String result = "";
//
////            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
////            Cursor cursor = db.rawQuery("SELECT * FROM "+TABLE_WIN, null);
////            while (cursor.moveToNext()) {
////                result += cursor.getString(0)
////                        + " : "
////                        + cursor.getString(1)
////                        + " | "
////                        + cursor.getInt(2)
////                        + "원 "
////                        + cursor.getString(3)
////                        + "\n";
////            }
//
//            String [] arrayColumns = {"number", "number1", "number2", "number3", "number4", "number5", "number6", "bonus"};
//            String query = SQLiteQueryBuilder.buildQueryString(false, TABLE_WIN, arrayColumns,"number == "+no+";",null, null,null,null);
//
//            Log.d("lee", "query : " + query);
//
//            Cursor cursor = db.rawQuery( query, null);
//
//            while(cursor.moveToNext()){
//                int colid = cursor.getColumnIndex(TABLE_WIN);
//                String name = cursor.getString(colid);
//                Log.d("lee", "query result : " + name);
//            }
//
//            return result;
//        }
//
//        public long selectQueryTableCount(){
//            SQLiteDatabase db = getReadableDatabase();
//            long cnt  = DatabaseUtils.queryNumEntries(db, TABLE_WIN);
//
//            return cnt;
//        }
//
//        public String selectAll(){
//            SQLiteDatabase db = getReadableDatabase();
//        }
//    }

}
