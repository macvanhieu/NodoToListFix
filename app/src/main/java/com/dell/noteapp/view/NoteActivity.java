package com.dell.noteapp.view;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.InputType;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dell.noteapp.R;
import com.dell.noteapp.database.AlarmReminderConstants;
import com.dell.noteapp.entity.Note;
import com.dell.noteapp.reminder.AlarmScheduler;
import com.dell.noteapp.utils.UtilsHelper;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NoteActivity extends AppCompatActivity implements
        TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener,LoaderManager.LoaderCallbacks<Cursor>{

    Toolbar toolbar;
    EditText edTitle, edNote;
    TextView tvDate,create_edit;
    Note noteM = new Note();
    String option;
    UtilsHelper utilsHelper;
    private static final int EXISTING_VEHICLE_LOADER = 0;


    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private Calendar mCalendar;
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private boolean mRepeat = false;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;

    private Uri mCurrentReminderUri;
    private boolean mVehicleHasChanged = false;

    // Values for orientation change
    private static final String KEY_TITLE = "title_key";
    private static final String KEY_TIME = "time_key";
    private static final String KEY_DATE = "date_key";
    private static final String KEY_REPEAT = "repeat_key";
    private static final String KEY_REPEAT_NO = "repeat_no_key";
    private static final String KEY_REPEAT_TYPE = "repeat_type_key";
    private static final String KEY_ACTIVE = "active_key";


    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mVehicleHasChanged = true;
            return false;
        }
    };

    // Request code for voice input
    private static final int REQUEST_CODE = 1234;
    private Calendar fromC = Calendar.getInstance();
    private Calendar toC = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        utilsHelper = new UtilsHelper(this);
        init();
        try {
            setUpLoading();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intent = getIntent();
        mCurrentReminderUri = intent.getData();


        // Initialize default values
        mActive = "true";
        mRepeat = true;
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mCalendar = Calendar.getInstance();
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;



        // Setup TextViews using reminder values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");


        // To save state on device rotation
        if (savedInstanceState != null) {
            String savedTitle = savedInstanceState.getString(KEY_TITLE);
            edTitle.setText(savedTitle);
            mTitle = savedTitle;

            String savedTime = savedInstanceState.getString(KEY_TIME);
            mTimeText.setText(savedTime);
            mTime = savedTime;

            String savedDate = savedInstanceState.getString(KEY_DATE);
            mDateText.setText(savedDate);
            mDate = savedDate;

            String saveRepeat = savedInstanceState.getString(KEY_REPEAT);
            mRepeatText.setText(saveRepeat);
          //  mRepeat = saveRepeat;

            String savedRepeatNo = savedInstanceState.getString(KEY_REPEAT_NO);
            mRepeatNoText.setText(savedRepeatNo);
            mRepeatNo = savedRepeatNo;

            String savedRepeatType = savedInstanceState.getString(KEY_REPEAT_TYPE);
            mRepeatTypeText.setText(savedRepeatType);
            mRepeatType = savedRepeatType;

            mActive = savedInstanceState.getString(KEY_ACTIVE);
        }

    }


    // Hàm ánh xạ
    private void init() {
        // Initialize Views
        mDateText = (TextView) findViewById(R.id.set_date);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);
        mRepeatSwitch = (Switch) findViewById(R.id.repeat_switch);
        mRepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);

        edTitle = findViewById(R.id.edTitle);
        edNote = findViewById(R.id.edNote);
        tvDate = findViewById(R.id.tvDate);
        create_edit = findViewById(R.id.create_edit);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edTitle.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                edTitle.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }



    // Hàm load select loại update hay tao mới trên thanh toolbar
    public void setUpLoading() throws ParseException {
        Intent intent = getIntent();
        option = intent.getStringExtra("option");
        noteM = (Note) intent.getSerializableExtra("Note");
        if(noteM!=null) {
            edTitle.setText(noteM.getTitle());
        edNote.setText(noteM.getContent());
        tvDate.setText(utilsHelper.setUpDate(noteM.getDate()));
    }
        if (option != null){
            if(option.equals("Add")){
                create_edit.setText("Create note");
            }else if(option.equals("Update")){
                create_edit.setText("Edit note");
            }
        }


    }

    // Create menu hiển thi trên thanh toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (option != null){
            if(option.equals("Add")){
                getMenuInflater().inflate(R.menu.menu_add,menu);
            }else if(option.equals("Update")) {
                getMenuInflater().inflate(R.menu.menu_note, menu);
                if(noteM.isFavorite()) {
                    menu.getItem(3).getIcon().setTint(getResources().getColor(R.color.colorPrimary));
                }else{
                    menu.getItem(3).getIcon().setTint(getResources().getColor(R.color.textFormatWhite));
                }
            }

        }

        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
       // getMenuInflater().inflate(R.menu.menu_add_reminder, menu);

        return true;
    }

    // Xử lý các sự kiện trên thanh menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        final String title = edTitle.getText().toString();
        final String content = edNote.getText().toString();
        final String txtdate = mDateText.getText().toString();
        final String txtTime = mTimeText.getText().toString();
        final String repeat = mRepeatText.getText().toString();
        final String repeatInterval = mRepeatNoText.getText().toString();
        final Date date = new Date();
        switch (id){
            case R.id.menu_share:
                shareNote(noteM);
                break;
            case R.id.menu_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
                builder.setTitle("Are you sure you want to delete this note?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        utilsHelper.deleteNote(noteM,"n");
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();

                break;
            case R.id.menu_fav:
                if(noteM.isFavorite()){
                    item.getIcon().setTint(getResources().getColor(R.color.textFormatWhite));
                    noteM.setFavorite(false);
                    utilsHelper.updateFV(noteM);
                }else{
                    item.getIcon().setTint(getResources().getColor(R.color.colorPrimary));
                    noteM.setFavorite(true);
                    utilsHelper.updateFV(noteM);
                }
                break;
            case R.id.menu_saveAdd:
                if (title.isEmpty()) {
                    edTitle.setError("Please enter title!");
                    edTitle.requestFocus();
                    break;
                }

                if (content.isEmpty()) {
                    edNote.setError("Please enter content");
                    edNote.requestFocus();
                    break;
                }
                utilsHelper.save(new Note(),title,content,txtdate, txtTime, repeat,repeatInterval,mRepeat, option);
                createNotifi();
                break;
            case R.id.menu_saveUpdate:
                if (title.isEmpty()) {
                    edTitle.setError("Please enter title!");
                    edTitle.requestFocus();
                    break;
                }

                if (content.isEmpty()) {
                    edNote.setError("Please enter content");
                    edNote.requestFocus();
                    break;
                }
                utilsHelper.save(new Note(),title,content,txtdate, txtTime, repeat,repeatInterval,mRepeat, option);
                createNotifi();
                break;
//            case R.id.voice:
//                Log.e("CLICK","v");
//                // gone
//              //  startVoiceRecognitionActivity();
//                break;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    // Khởi tạo thông báo
    private void createNotifi(){
        //Khởi tạo conten để đẩy dữ liệu vào
        ContentValues values = new ContentValues();
        // put các giá trị
        values.put(AlarmReminderConstants.AlarmReminderEntry.KEY_TITLE, mTitle);
        values.put(AlarmReminderConstants.AlarmReminderEntry.KEY_DATE, mDate);
        values.put(AlarmReminderConstants.AlarmReminderEntry.KEY_TIME, mTime);
        values.put(AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT, mRepeat);
        values.put(AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT_NO, mRepeatNo);
        values.put(AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT_TYPE, mRepeatType);
        values.put(AlarmReminderConstants.AlarmReminderEntry.KEY_ACTIVE, mActive);
        // Set up calender for creating the notification
        // set giá trị calendar
        mCalendar.set(Calendar.MONTH, --mMonth);
        mCalendar.set(Calendar.YEAR, mYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
        mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
        mCalendar.set(Calendar.MINUTE, mMinute);
        mCalendar.set(Calendar.SECOND, 0);

        // time
        long selectedTimestamp =  mCalendar.getTimeInMillis();
        // Check repeat type
        if (mRepeatType.equals("Minute")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMinute;
        } else if (mRepeatType.equals("Hour")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milHour;
        } else if (mRepeatType.equals("Day")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milDay;
        } else if (mRepeatType.equals("Week")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milWeek;
        } else if (mRepeatType.equals("Month")) {
            mRepeatTime = Integer.parseInt(mRepeatNo) * milMonth;
        }

        if (mCurrentReminderUri == null) {
            // This is a NEW reminder, so insert a new reminder into the provider,
            // returning the content URI for the new reminder.
            Uri newUri = getContentResolver().insert(AlarmReminderConstants.AlarmReminderEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        } else {

            int rowsAffected = getContentResolver().update(mCurrentReminderUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_reminder_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_reminder_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        // Create a new notification
        if (mActive.equals("true")) {
            if (mRepeat) {
                new AlarmScheduler().setRepeatAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri, mRepeatTime);
            } else if (mRepeat) {
                new AlarmScheduler().setAlarm(getApplicationContext(), selectedTimestamp, mCurrentReminderUri);
            }

            Toast.makeText(this, "Alarm time is " + selectedTimestamp,
                    Toast.LENGTH_LONG).show();
        }

        // Create toast to confirm new reminder
        Toast.makeText(getApplicationContext(), "Saved",
                Toast.LENGTH_SHORT).show();
    }


    // cho phép share các ứng dụng khác
    private void shareNote(Note note) {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, note.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, note.getContent());

        startActivity(Intent.createChooser(share, "Share note!"));
    }



    // Cho phép xử dụng voice
    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.voice_hint);
        startActivityForResult(intent, REQUEST_CODE);
    }

    /**
     * Handle the results from the voice recognition
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            if (matches.size() > 0) {
                if (edNote.getText().toString().length() == 0) {
                    edNote.setText(matches.get(0));
                    edNote.setSelection(edNote.getText().toString().length());
                } else {
                    Spanned spanText = (SpannedString) TextUtils.concat(edNote.getText(), " " + matches.get(0));
                    edNote.setText(spanText);
                    edNote.setSelection(edNote.getText().toString().length());
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void hideSoftKeyboard() {
        if (this.getCurrentFocus() != null) {
            try {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getApplicationWindowToken(), 0);
            } catch (RuntimeException e) {
                //ignore
            }
        }
    }

    // On clicking Time picker
    public void setTime(View v){
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.setThemeDark(false);
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    // On clicking Date picker
    public void setDate(View v){
        Calendar now = Calendar.getInstance();
       DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }

    // Obtain time from time picker
    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        if (minute < 10) {
            mTime = hourOfDay + ":" + "0" + minute;
        } else {
            mTime = hourOfDay + ":" + minute;
        }
        mTimeText.setText(mTime);
    }

    // Obtain date from date picker
    @Override
    public void onDateSet(DatePickerDialog view, int year,
                          int monthOfYear, int dayOfMonth) {
        monthOfYear ++;
        mDay = dayOfMonth;
        mMonth = monthOfYear;
        mYear = year;
        mDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        mDateText.setText(mDate);
    }



    // On clicking the repeat switch
    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat =true;
            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
        } else {
            mRepeat = false;
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    // On clicking repeat type button
    public void selectRepeatType(View v){
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        // Create List Dialog
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
            }
        });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking repeat interval button
    public void setRepeatNo(View v){
       AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        }
                        else {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }

    // On pressing the back button
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                AlarmReminderConstants.AlarmReminderEntry._ID,
                AlarmReminderConstants.AlarmReminderEntry.KEY_TITLE,
                AlarmReminderConstants.AlarmReminderEntry.KEY_DATE,
                AlarmReminderConstants.AlarmReminderEntry.KEY_TIME,
                AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT,
                AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT_NO,
                AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT_TYPE,
                AlarmReminderConstants.AlarmReminderEntry.KEY_ACTIVE,
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentReminderUri,         // Query the content URI for the current reminder
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            int titleColumnIndex = cursor.getColumnIndex(AlarmReminderConstants.AlarmReminderEntry.KEY_TITLE);
            int dateColumnIndex = cursor.getColumnIndex(AlarmReminderConstants.AlarmReminderEntry.KEY_DATE);
            int timeColumnIndex = cursor.getColumnIndex(AlarmReminderConstants.AlarmReminderEntry.KEY_TIME);
            int repeatColumnIndex = cursor.getColumnIndex(AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT);
            int repeatNoColumnIndex = cursor.getColumnIndex(AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT_NO);
            int repeatTypeColumnIndex = cursor.getColumnIndex(AlarmReminderConstants.AlarmReminderEntry.KEY_REPEAT_TYPE);
            int activeColumnIndex = cursor.getColumnIndex(AlarmReminderConstants.AlarmReminderEntry.KEY_ACTIVE);

            // Extract out the value from the Cursor for the given column index
            String title = cursor.getString(titleColumnIndex);
            String date = cursor.getString(dateColumnIndex);
            String time = cursor.getString(timeColumnIndex);
            String repeat = cursor.getString(repeatColumnIndex);
            String repeatNo = cursor.getString(repeatNoColumnIndex);
            String repeatType = cursor.getString(repeatTypeColumnIndex);
            String active = cursor.getString(activeColumnIndex);



            // Update the views on the screen with the values from the database
            edNote.setText(title);
            mDateText.setText(date);
            mTimeText.setText(time);
            mRepeatNoText.setText(repeatNo);
            mRepeatTypeText.setText(repeatType);
            mRepeatText.setText("Every " + repeatNo + " " + repeatType + "(s)");
            // Setup up active buttons
            // Setup repeat switch
            if (repeat.equals("false")) {
                mRepeatSwitch.setChecked(false);
                mRepeatText.setText(R.string.repeat_off);

            } else if (repeat.equals("true")) {
                mRepeatSwitch.setChecked(true);
            }

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
