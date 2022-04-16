package com.busy.looping.seproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.busy.looping.seproject.Util.FileReaderUtil;
import com.busy.looping.seproject.models.BookingModel;
import com.busy.looping.seproject.models.EventModel;
import com.busy.looping.seproject.models.UserModel;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginDatabase extends SQLiteOpenHelper {
    private static final String TAG = "MyLoginDatabase";
    private final Context context;
    private static final String DATABASE_NAME = "loginDatabase.db";
    private static final String USER_TABLE_NAME = "users";
    private static final String EVENTS_TABLE_NAME = "events";
    private static final String BOOKING_TABLE_NAME = "booking";
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    public boolean containsEmailPassword(String email, String password) {
        SQLiteDatabase db = LoginDatabase.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE email_id =? AND hashed_password =? ";
        Cursor cursor = db.rawQuery(query, new String[]{email, getHashedPassword(password)});
        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }

    public void setCurrentSignedIn(@NonNull String email) {
        SQLiteDatabase db = LoginDatabase.getInstance(context).getWritableDatabase();
//        String query = ""
        ContentValues contentValues = new ContentValues();
        contentValues.put("signed_in", true);
        db.update(USER_TABLE_NAME, contentValues, "email_id =?", new String[]{email});
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

    public boolean insertData(String email_id, String password, String role) {

        ContentValues contentValues = new ContentValues();
        contentValues.put("email_id", email_id);
        contentValues.put("hashed_password", getHashedPassword(password));
        contentValues.put("role", role);

        SQLiteDatabase sqLiteDatabase = LoginDatabase.getInstance(context).getWritableDatabase();
        long res = sqLiteDatabase.insert(USER_TABLE_NAME, null, contentValues);
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


    @SuppressLint("Range")
    @Nullable
    public UserModel getCurrentSignedInUser() {
        UserModel model = null;
        SQLiteDatabase db = LoginDatabase.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + USER_TABLE_NAME + " WHERE signed_in =?";
        Cursor cursor = db.rawQuery(query, new String[]{"1"});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                model = new UserModel(cursor.getString(cursor.getColumnIndex("user_id")),
                        cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("email_id")),
                        cursor.getString(cursor.getColumnIndex("hashed_password")),
                        cursor.getString(cursor.getColumnIndex("role")),
                        cursor.getString(cursor.getColumnIndex("address")),
                        cursor.getString(cursor.getColumnIndex("phone_no")));
            }
            cursor.close();
        }
        return model;
    }

    public boolean insertInEvents(@NonNull String event_name,
                                  double price,
                                  int no_seats,
                                  @Nullable String small_description,
                                  @Nullable String full_description,
                                  @NonNull String date_n_time,
                                  @NonNull String venue,
                                  boolean isConfirmed,
                                  @Nullable String pathCoverPhoto,
                                  int added_by,
                                  @NonNull String eventType) {
        if (small_description != null && small_description.equals("")) {
            small_description = null;
        }
        if (full_description != null && full_description.equals("")) {
            full_description = null;
        }
        if (pathCoverPhoto != null && pathCoverPhoto.equals("")) {
            pathCoverPhoto = null;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("event_name", event_name);
        contentValues.put("price", price);
        contentValues.put("no_seats", no_seats);
        contentValues.put("small_description", small_description);
        contentValues.put("full_description", full_description);
        contentValues.put("date_n_time", date_n_time);
        contentValues.put("venue", venue);
        contentValues.put("confirmed", isConfirmed);
        contentValues.put("photo_path", pathCoverPhoto);
        contentValues.put("added_by", added_by);
        contentValues.put("event_type", eventType);


        SQLiteDatabase sqLiteDatabase = LoginDatabase.getInstance(context).getWritableDatabase();
        long res = sqLiteDatabase.insert(EVENTS_TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();

        return res != -1;
    }

    @SuppressLint("Range")
    @NonNull
    public ArrayList<EventModel> getEventsAddedBy(@NonNull String id) {
        ArrayList<EventModel> res = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = LoginDatabase.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + EVENTS_TABLE_NAME + " WHERE added_by =?";
        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{id});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String date_n_time = cursor.getString(cursor.getColumnIndex("date_n_time"));
                String[] arr = date_n_time.split(" ");
                res.add(new EventModel(cursor.getString(cursor.getColumnIndex("event_name")),
                        cursor.getString(cursor.getColumnIndex("event_id")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getString(cursor.getColumnIndex("no_seats")),
                        cursor.getString(cursor.getColumnIndex("small_description")),
                        cursor.getString(cursor.getColumnIndex("full_description")),
                        arr[1],
                        arr[0],
                        cursor.getString(cursor.getColumnIndex("venue")),
                        cursor.getString(cursor.getColumnIndex("photo_path")),
                        cursor.getString(cursor.getColumnIndex("confirmed")).equals("1"),
                        cursor.getString(cursor.getColumnIndex("event_type"))));
            }
            cursor.close();
        }
        return res;
    }

    @SuppressLint("Range")
    @NonNull
    public ArrayList<EventModel> getAllEvents() {
        ArrayList<EventModel> res = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = LoginDatabase.getInstance(context).getReadableDatabase();
        String query = "SELECT * FROM " + EVENTS_TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String date_n_time = cursor.getString(cursor.getColumnIndex("date_n_time"));
                String[] arr = date_n_time.split(" ");
                res.add(new EventModel(cursor.getString(cursor.getColumnIndex("event_name")),
                        cursor.getString(cursor.getColumnIndex("event_id")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getString(cursor.getColumnIndex("no_seats")),
                        cursor.getString(cursor.getColumnIndex("small_description")),
                        cursor.getString(cursor.getColumnIndex("full_description")),
                        arr[1],
                        arr[0],
                        cursor.getString(cursor.getColumnIndex("venue")),
                        cursor.getString(cursor.getColumnIndex("photo_path")),
                        cursor.getString(cursor.getColumnIndex("confirmed")).equals("1"),
                        cursor.getString(cursor.getColumnIndex("event_type"))));
            }
            cursor.close();
        }
        return res;
    }

    @SuppressLint("Range")
    @Nullable
    public EventModel getLastEvent() {
        EventModel eventModel = null;
        String query = "SELECT * FROM " + EVENTS_TABLE_NAME;
        SQLiteDatabase db = LoginDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToLast()) {
                @SuppressLint("Range") String date_n_time = cursor.getString(cursor.getColumnIndex("date_n_time"));
                String[] arr = date_n_time.split(" ");
                eventModel = new EventModel(cursor.getString(cursor.getColumnIndex("event_name")),
                        cursor.getString(cursor.getColumnIndex("event_id")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getString(cursor.getColumnIndex("no_seats")),
                        cursor.getString(cursor.getColumnIndex("small_description")),
                        cursor.getString(cursor.getColumnIndex("full_description")),
                        arr[1],
                        arr[0],
                        cursor.getString(cursor.getColumnIndex("venue")),
                        cursor.getString(cursor.getColumnIndex("photo_path")),
                        cursor.getString(cursor.getColumnIndex("confirmed")).equals("1"),
                        cursor.getString(cursor.getColumnIndex("event_type")));
            }
            cursor.close();
            db.close();
        }
        return eventModel;
    }

    public void logOut(@NonNull String email) {
        SQLiteDatabase db = LoginDatabase.getInstance(context).getWritableDatabase();
//        String query = ""
        ContentValues contentValues = new ContentValues();
        contentValues.put("signed_in", false);
        db.update(USER_TABLE_NAME, contentValues, "email_id =?", new String[]{email});
    }

    public void updateUser(@NonNull String email, @NonNull String userName, @NonNull String phoneNumber, @NonNull String address) {
        SQLiteDatabase db = LoginDatabase.getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", userName);
        contentValues.put("address", address);
        contentValues.put("phone_no", phoneNumber);
        db.update(USER_TABLE_NAME, contentValues, "email_id =?", new String[]{email});
    }

    public void deleteEvent(@NonNull EventModel eventModel) {
        SQLiteDatabase db = LoginDatabase.getInstance(context).getWritableDatabase();
        db.delete(BOOKING_TABLE_NAME, "event =?", new String[]{eventModel.getEventId()});
        db.delete(EVENTS_TABLE_NAME, "event_id =?", new String[]{eventModel.getEventId()});
    }

    public boolean insertInBooking(@NonNull String bookingId,
                                   @NonNull @FloatRange(from = 0f) Double price,
                                   @IntRange(from = 1) int no_tickets,
                                   @Nullable @FloatRange(from = 0) Double discount,
                                   @Nullable @FloatRange(from = 0f) Double tax,
                                   @NonNull Integer event_id,
                                   @NonNull Integer user_id,
                                   @NonNull Integer originalNoSeats) {
        SQLiteDatabase db = LoginDatabase.getInstance(context).getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("booking_id", bookingId);
        contentValues.put("price", price);
        contentValues.put("no_tickets", no_tickets);
        contentValues.put("discount", discount);
        contentValues.put("tax", tax);
        contentValues.put("event", event_id);
        contentValues.put("booked_by", user_id);
        long res = db.insert(BOOKING_TABLE_NAME, null, contentValues);

        contentValues.clear();
        int newNoSeats = originalNoSeats - no_tickets;
        contentValues.put("no_seats", newNoSeats);
        long res2 = db.update(EVENTS_TABLE_NAME,  contentValues, "event_id =?", new String[]{String.valueOf(event_id)});

        return res != -1 && res2 != -1;
    }

    @SuppressLint("Range")
    @Nullable
    public BookingModel getLastBooking() {
        BookingModel bookingModel = null;
        String query = "SELECT * FROM " + BOOKING_TABLE_NAME;
        SQLiteDatabase db = LoginDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null) {
            if (cursor.moveToLast()) {
                bookingModel = new BookingModel(
                        cursor.getString(cursor.getColumnIndex("booking_id")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        cursor.getString(cursor.getColumnIndex("no_tickets")),
                        cursor.getString(cursor.getColumnIndex("discount")),
                        cursor.getString(cursor.getColumnIndex("tax")),
                        cursor.getString(cursor.getColumnIndex("event")),
                        cursor.getString(cursor.getColumnIndex("booked_by"))
                );
            }
            cursor.close();
        }
        return bookingModel;
    }

}
