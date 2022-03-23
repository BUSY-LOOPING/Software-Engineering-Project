package com.busy.looping.seproject;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.busy.looping.seproject.Util.FileReaderUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;

public class LoginDatabase extends SQLiteOpenHelper {
    private static final String TAG = "MyLoginDatabase";
    private Context context;
    private static final String DATABASE_NAME = "loginDatabase.db";
    private static final String USER_TABLE_NAME = "user_table";
    private static WeakReference<LoginDatabase> mInstance;

    private String errorMessage = "";

    @NonNull
    public static synchronized LoginDatabase getInstance(@NonNull Context context) {
        if (mInstance == null || mInstance.get() == null) {
            mInstance = new WeakReference<>(new LoginDatabase(context, DATABASE_NAME, null, 1));
        }
        return mInstance.get();
    }


    public LoginDatabase(@NonNull Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            String sql = FileReaderUtil.readTextFile(context.getResources().openRawResource(R.raw.create_table));
            Toast.makeText(context, "here", Toast.LENGTH_SHORT).show();

            String[] queries = sql.split(";;;");

            for (String query : queries) {
                Log.d(TAG, "onCreate: " + query);
                sqLiteDatabase.execSQL(query);
            }
        } catch (SQLException sqlException) {
            errorMessage = sqlException.getMessage();
        } catch (IOException e) {
            errorMessage += "\n" + e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ USER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
