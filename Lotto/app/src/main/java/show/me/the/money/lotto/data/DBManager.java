package show.me.the.money.lotto.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import show.me.the.money.lotto.Common;

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

    public void insertData(DataNumber data, boolean isRandom ){
        if(isRandom)
            _helper.addRandomNumberInfo(data);
        else
            _helper.addNumberInfo(data);
    }

    public ArrayList<DataNumber> getAllData(boolean isRandom){
        if(isRandom)
            return _helper.getRandomAllData();
        else
            return _helper.getAllData();
    }

    public DataNumber getData(int no, boolean isRandom){
        return _helper.getNoInfo(no);
    }

    public void deleteRandomTable(int index){
        if(index == Common.REMOVEALL)
            _helper.deleteAll();
        else
            _helper.deleteContact(index);
    }

    public int getTotalCount(){
        return instance._helper.getContactsCount();
    }

    class DBContactHelper extends SQLiteOpenHelper {
        // All Static variables

        final static String DB_NAME = "lotto_db";
        // Contacts table name
        private static final String TABLE_WIN = "win_number_info";
        static final String TABLE_RANDOM = "random_number";

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

            db.execSQL("CREATE TABLE "+TABLE_RANDOM+"(" +
                    KEY_NO+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_NUMBER+"0 INTEGER," +
                    KEY_NUMBER+"1 INTEGER," +
                    KEY_NUMBER+"2 INTEGER," +
                    KEY_NUMBER+"3 INTEGER," +
                    KEY_NUMBER+"4 INTEGER," +
                    KEY_NUMBER+"5 INTEGER" +
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

            if(cursor != null){
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
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM "+TABLE_WIN, null);

            return numRows;
        }

        //Random
        public ArrayList<DataNumber> getRandomAllData() {
            ArrayList<DataNumber> listData = new ArrayList<>();
            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_RANDOM +" ORDER BY "+KEY_NO+" desc";

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if(cursor != null){
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
                            0
                    );
                    listData.add(data);
                }
            }
            return listData;
        }

        public void addRandomNumberInfo(DataNumber contact) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_NO, contact.no); // Contact Name
            for(int i=0; i<contact.arrayNumber.length; i++){
                values.put(KEY_NUMBER+i, contact.arrayNumber[i]); // Contact Phone
            }
            // Inserting Row
            db.insert(TABLE_RANDOM, null, values);
        }

        public void deleteContact(int index) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_RANDOM, KEY_NO + " = ?", new String[]{String.valueOf(index)});
        }

        public void deleteAll(){
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_RANDOM, null, null);
        }

    }
}
