package com.busy.looping.seproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginDatabase extends SQLiteOpenHelper {
    private static final String TAG = "MyLoginDatabase";
    private Context context;
    private static final String DATABASE_NAME = "loginDatabase.db";
    private static final String USER_TABLE_NAME = "users";
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
            sqlException.printStackTrace();
            errorMessage = sqlException.getMessage();
        } catch (IOException e) {
            e.printStackTrace();
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

    public boolean containsEmailPassword(String email, String password) {
        SQLiteDatabase db = LoginDatabase.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE email_id =? AND hashed_password =? " ;
        Cursor cursor = db.rawQuery(query, new String[] {email, getHashedPassword(password)});
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public static String getHashedPassword(String password) {
        byte[] encodedHash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            encodedHash = digest.digest(
                    password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return bytesToHex(encodedHash);
    }

    public boolean insertData (String email_id, String password, String role) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("email_id", email_id);
        contentValues.put("hashed_password", getHashedPassword(password));
        contentValues.put("role", role);

        SQLiteDatabase sqLiteDatabase = LoginDatabase.getInstance(context).getWritableDatabase();
        long res =sqLiteDatabase.insert(USER_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

        return res != -1;
    }

    private static String bytesToHex(@Nullable byte[] hash) {
        if (hash != null) {
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } else {
            return "null";
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
