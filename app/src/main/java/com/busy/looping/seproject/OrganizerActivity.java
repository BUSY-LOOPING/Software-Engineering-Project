package com.busy.looping.seproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.busy.looping.seproject.models.EventModel;
import com.busy.looping.seproject.models.UserModel;

import java.io.InputStream;
import java.util.ArrayList;

public class OrganizerActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText eventNameEt, priceEt, venueEt, smallDescEt, fullDescEt;
    private GeneralRecyclerViewAdapter adapter;
    private NumberPicker noSeatsPicker;
    private TimePicker timePicker;
    private DatePicker datePicker;
    private ImageView imageView, icon;
    private Spinner spinner;
    private Button addEventBtn;
    private String pathToCoverPic = "";
    private boolean set = false;
    private final String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private UserModel currentUser;
    private ArrayList<EventModel> list;

    public static final int PICK_IMAGE = 1;
    public static final int EVENT_DETAIL_ACT = 2;
    public static final int REQUEST_CODE = 100;
    private final String [] events = new String[]{"Movies", "Comedy Shows", "Music Shows", "Plays", "Amusement Parks", "Gaming Events"};
    private final Integer [] eventIcons = new Integer[] {R.drawable.film_reel, R.drawable.comedy, R.drawable.microphone, R.drawable.theater, R.drawable.amusement_park, R.drawable.champion};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_layout);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_status_bar));

        currentUser = LoginDatabase.getInstance(this).getCurrentSignedInUser();
        init();
        listeners();
        setAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void listeners() {
        imageView.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            } else {
                launchImagePickerIntent();
            }
        });
        icon.setOnClickListener(v -> {
            UserProfileBottomSheet bottomSheet = new UserProfileBottomSheet(this);
            bottomSheet.show(getSupportFragmentManager(), "");
        });

        addEventBtn.setOnClickListener(v -> {
            if (validateFields()) {
                if (LoginDatabase.getInstance(this).
                        insertInEvents(eventNameEt.getText().toString().trim(),
                                Double.parseDouble(priceEt.getText().toString().trim()),
                                noSeatsPicker.getValue(),
                                smallDescEt.getText().toString().trim(),
                                fullDescEt.getText().toString().trim(),
                                getFormattedDateTime(),
                                venueEt.getText().toString().trim(),
                                true,
                                pathToCoverPic,
                                Integer.parseInt(currentUser.getId()),
                                events[spinner.getSelectedItemPosition()]
                        )) {
                    Toast.makeText(this, "New event added", Toast.LENGTH_SHORT).show();
                    EventModel lastEvent = LoginDatabase.getInstance(this).getLastEvent();
                    if (lastEvent != null) {
                        list.add(lastEvent);
                        adapter.notifyItemInserted(list.size());
                    }
                }
            }
        });
    }

    @SuppressLint("NewApi")
    private String getFormattedDateTime() {

        return datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth() + " "
                + timePicker.getHour() + ":" + timePicker.getMinute() + ":00";
    }

    private boolean validateFields() {
        if (currentUser == null) {
            Toast.makeText(this, "Sign in to continue", Toast.LENGTH_SHORT).show();
            return false;
        } else if (eventNameEt.getText().toString().equals("")) {
            eventNameEt.setError("Event Name Required");
            return false;
        } else if (priceEt.getText().toString().equals("")) {
            priceEt.setError("Price required");
            return false;
        } else if (Double.parseDouble(priceEt.getText().toString()) < 0) {
            priceEt.setError("Price cannot be negative");
            return false;
        } else if (venueEt.getText().toString().equals("")) {
            venueEt.setError("Venue Required");
            return false;
        }
        return true;
    }

    private void launchImagePickerIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void setAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);
        SpinnerAdapter adapter = new SpinnerAdapter(this, R.layout.spinner_value_layout, events, eventIcons);
        spinner.setAdapter(adapter);
    }

    private void init() {
        spinner = findViewById(R.id.spinner);
        list = LoginDatabase.getInstance(this).getEventsAddedBy(currentUser.getId());
        eventNameEt = findViewById(R.id.eventName_et);
        priceEt = findViewById(R.id.price_et);
        venueEt = findViewById(R.id.venue_et);
        smallDescEt = findViewById(R.id.small_desc_et);
        fullDescEt = findViewById(R.id.full_desc_et);
        recyclerView = findViewById(R.id.prevListedRecyclerView);
        noSeatsPicker = findViewById(R.id.seats_picker);
        datePicker = findViewById(R.id.datePicker);
        timePicker = findViewById(R.id.timePicker);
        datePicker.setMinDate(System.currentTimeMillis());
        imageView = findViewById(R.id.img_picker);
        addEventBtn = findViewById(R.id.addEvent_btn);
        noSeatsPicker.setMinValue(1);
        noSeatsPicker.setMaxValue(1000);
        noSeatsPicker.setEnabled(true);
        recyclerView.setHasFixedSize(false);
        adapter = new GeneralRecyclerViewAdapter(list, this);
        icon = findViewById(R.id.icon);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //do
                launchImagePickerIntent();
            } else {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action

            if (data == null) {
                //Display an error
                return;
            }
//            try {
//                final InputStream inputStream = getContentResolver().openInputStream(data.getData());
//                final Bitmap selectedImage = BitmapFactory.decodeStream(inputStream);
//                imageView.setColorFilter(null);
//                if (!set) {
//                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//                    imageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    imageView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    set = true;
//                }
//                imageView.setImageResource(0);
//                imageView.setImageBitmap(selectedImage);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            imageView.setColorFilter(null);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
//            imageView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//            imageView.setImageURI(data.getData());
            pathToCoverPic = data.getData().toString();

            Glide.with(this)
                    .load(data.getData())
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            if (!set) {
                                imageView.setAdjustViewBounds(true);
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                                set = true;
                            }
                            return false;
                        }
                    })
                    .into(imageView);
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        } else if (requestCode == EVENT_DETAIL_ACT) {
            if (data != null) {
                EventModel deletedEvent = (EventModel) data.getSerializableExtra("cancelledEvent");
                if (deletedEvent != null) {
                    LoginDatabase.getInstance(this).deleteEvent(deletedEvent);
                    int index = list.indexOf(deletedEvent);
                    if (index > -1) {
                        list.remove(index);
                        adapter.notifyItemRemoved(index);
                        adapter.notifyItemRangeChanged(index, list.size());
                    }
                }
            }
        }

    }

}
