package class1.dwit.com.assignmentreminder.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import class1.dwit.com.assignmentreminder.domain.Assignment;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 4/23/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{

    // Database Name
    private static final String DATABASE_NAME = "assignment.db";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    //Table
    public static final String TABLE_ASSIGNMENT = "dwitAssignment";
    public static final String TABLE_REF = "dwitRef";

    // DWITStudent Table Columns names
    public final String D_ID = "_id";
    public final String D_name = "D_name";
    public final String D_deadline = "D_deadline";
    public final String D_url = "D_URL";
    public final String D_batch = "D_batch";
    public final String D_title = "D_title";
    public final String D_reference = "D_reference";
    public final String D_assignID = "D_assignID";
    public final String D_checked = "D_checked";
    public final String D_hasRang = "D_hasRang";

    Context cont;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.cont = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_REF_TABLE = "CREATE TABLE dwitRef"
                + "(" + D_ID + " INTEGER PRIMARY KEY," + D_reference + " string," + D_assignID + " string)";
        db.execSQL(CREATE_REF_TABLE);

        String CREATE_ASSIGN_TABLE = "CREATE TABLE " + TABLE_ASSIGNMENT
                + "(" + D_ID + " INTEGER PRIMARY KEY," + D_name + " string,"
                + D_deadline + " string," + D_hasRang + " string," + D_url + " string," + D_batch + " string,"+ D_checked + " string," + D_title + " string)";
        db.execSQL(CREATE_ASSIGN_TABLE);

//        String Show = "show tables";

//        db.execSQL(Show);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSIGNMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REF);
        onCreate(db);

    }

    public void insertReference(String reference, String assignId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(D_reference, reference);
        cv.put(D_assignID, assignId);
        db.insert(TABLE_REF, D_ID, cv);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        this.close();
    }

    public void setMarked(String assignID)
    {
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put("D_checked","True");

        dbs.update(TABLE_ASSIGNMENT,cv,"D_deadline="+assignID,null);
//        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();
        this.close();
    }

    public String getMarkedInfo(String assignID)
    {
        String Mark = "";
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();

        Cursor cursor = dbs.rawQuery("select D_checked from dwitAssignment where D_deadline = "+ assignID , null);

        if(cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            if(cursor.getString(cursor.getColumnIndex("D_checked")).equals("True"))
            {
                Mark = "True";
            }
            else
            {
                Mark = "False";
            }

        }
        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();

        return Mark;

    }

    public void setHasRang(String assignID)
    {
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put("D_hasRang","True");

        dbs.update(TABLE_ASSIGNMENT,cv,"D_deadline="+assignID,null);
//        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();
        this.close();
    }


    public String getReferences(String assignID)
    {
        String allReferences = "";
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();
        int i = 1;
//        Cursor cursor = dbs.query(TABLE_ASSIGNMENT, new String[]{D_ID, D_name, D_deadline, D_url,D_batch, D_title},null, null, null,null,null);
        Cursor cursor = dbs.rawQuery("select D_reference from dwitRef where D_assignID =  "+ assignID , null);
        if(cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            do
            {
                allReferences += i + ". " + cursor.getString(cursor.getColumnIndex("D_reference")) + "\n";
                i++;
            }while(cursor.moveToNext());
        }
        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();
        return allReferences;
    }

    public void insert_Assignment(Assignment assignment) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        ContentValues cv = new ContentValues();
        cv.put(D_name, assignment.getName());
        cv.put(D_deadline, assignment.getDeadline());
        cv.put(D_url, assignment.getURL());
        cv.put(D_batch, assignment.getBatch());
        cv.put(D_title,assignment.getAssignment_name());
        cv.put(D_checked,"False");
        cv.put(D_hasRang,"False");
        db.insert(TABLE_ASSIGNMENT, D_ID, cv);
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        Log.d("DataBaseHelper","Added assignment " + assignment.getDeadline());
        this.close();
    }

    public Assignment getNextAlert(String Batch)
    {
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();
        Long tsLong = System.currentTimeMillis()/1000 + 10800000;

        Assignment assignment = new Assignment();

        Cursor cursor = dbs.rawQuery("select * from dwitAssignment where D_deadline > "+ tsLong + " and D_batch = " + Batch + " and D_checked = 'False' order by D_deadline asc"  , null);
        String dateToRing = tsLong.toString();
        System.out.println(dateToRing + " " + cursor.getString(cursor.getColumnIndex(D_deadline)));
        if(cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            dateToRing = cursor.getString(cursor.getColumnIndex(D_deadline));
            assignment.setDeadline(cursor.getString(cursor.getColumnIndex(D_deadline)));
            assignment.setAssignment_name(cursor.getString(cursor.getColumnIndex(D_name)));
        }
        else
        {
            assignment.setDeadline("1525412280");
        }

        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();

        return assignment;

    }



    public List<Assignment> fetchAllAssignment(){
        List<Assignment> dataList = new LinkedList<>();
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();
        Cursor cursor = dbs.query(TABLE_ASSIGNMENT, new String[]{D_ID, D_name, D_deadline, D_url,D_batch, D_title},null, null, null,null,null);
//        Cursor cursor = dbs.rawQuery("select * from dwitAssignment where D_batch = 2017" , null);
        if(cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            do
            {
                Assignment ass = new Assignment() ;
                ass.setId(cursor.getInt(cursor.getColumnIndex(D_ID)));
                ass.setName(cursor.getString(cursor.getColumnIndex(D_name)));
                ass.setDeadline(cursor.getString(cursor.getColumnIndex(D_deadline)));
                ass.setURL(cursor.getString(cursor.getColumnIndex(D_url)));
                ass.setBatch(cursor.getString(cursor.getColumnIndex(D_batch)));
                ass.setAssignment_name(cursor.getString(cursor.getColumnIndex(D_title)));

                dataList.add(ass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();
        return dataList;
    }

    public List<Assignment> fetchBatchAssignment(String Batch){
        List<Assignment> dataList = new LinkedList<>();
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();
//        Cursor cursor = dbs.query(TABLE_ASSIGNMENT, new String[]{D_ID, D_name, D_deadline, D_url,D_batch, D_title},null, null, null,null,null);
        Cursor cursor = dbs.rawQuery("select * from dwitAssignment where D_batch = "+ Batch , null);
        if(cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            do
            {
                Assignment ass = new Assignment() ;
                ass.setId(cursor.getInt(cursor.getColumnIndex(D_ID)));
                ass.setName(cursor.getString(cursor.getColumnIndex(D_name)));
                ass.setDeadline(cursor.getString(cursor.getColumnIndex(D_deadline)));
                ass.setURL(cursor.getString(cursor.getColumnIndex(D_url)));
                ass.setBatch(cursor.getString(cursor.getColumnIndex(D_batch)));
                ass.setAssignment_name(cursor.getString(cursor.getColumnIndex(D_title)));

                dataList.add(ass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();
        return dataList;
    }

    public List<Assignment> getUpcommingAssignment(String batch)
    {
        List<Assignment> dataList = new LinkedList<>();
        SQLiteDatabase dbs = this.getReadableDatabase();
        dbs.beginTransaction();
        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
        Log.e(TAG,"The date ys = "+tsLong.toString());
//        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
////        String defaultValue = ;
//        long highScore = sharedPref.getString(R.string.classes);


        Cursor cursor = dbs.rawQuery("select * from dwitAssignment where D_batch = " + batch + " and D_checked = 'False'", null);
        if(cursor.getCount() != 0)
        {
            cursor.moveToFirst();
            do
            {
                Assignment ass = new Assignment() ;
                ass.setId(cursor.getInt(cursor.getColumnIndex(D_ID)));
                ass.setName(cursor.getString(cursor.getColumnIndex(D_name)));
                String deadline = cursor.getString(cursor.getColumnIndex(D_deadline));
                Long assDeadLine = Long.parseLong(deadline);

                Log.e(TAG, assDeadLine.toString());
                ass.setDeadline(cursor.getString(cursor.getColumnIndex(D_deadline)));
                ass.setURL(cursor.getString(cursor.getColumnIndex(D_url)));
                ass.setBatch(cursor.getString(cursor.getColumnIndex(D_batch)));
                ass.setAssignment_name(cursor.getString(cursor.getColumnIndex(D_title)));
                if(assDeadLine>tsLong)
                    dataList.add(ass);
            }while(cursor.moveToNext());
        }
        cursor.close();
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();
        return dataList;
    }


    public void delete(){
        SQLiteDatabase dbs = this.getWritableDatabase();
        dbs.beginTransaction();
        dbs.delete(TABLE_ASSIGNMENT,null,null);
        dbs.setTransactionSuccessful();
        dbs.endTransaction();
        dbs.close();
//        dbs.endTransaction();
//        this.close();
    }


}
