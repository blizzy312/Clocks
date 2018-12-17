package com.blizzy312.clocks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import java.util.Calendar;

public class AlarmCreateActivity extends AppCompatActivity {

    public static final String ID_REPLY = "id.REPLY";
    public static final String TIME_REPLY = "time.REPLY";
    public static final String HOURS_REPLY = "hours.REPLY";
    public static final String MINUTES_REPLY = "minutes.REPLY";
    public static final String DESCRIPTION_REPLY = "description.REPLY";
    public static final String DAYS_REPLY = "days.REPLY";
    public static final String STATE_REPLY = "state.REPLY";
    public static final String IS_MONDAY_REPLY = "is_monday.REPLY";
    public static final String IS_TUESDAY_REPLY = "is_tuesday.REPLY";
    public static final String IS_WEDNESDAY_REPLY = "is_wednesday.REPLY";
    public static final String IS_THURSDAY_REPLY = "is_thursday.REPLY";
    public static final String IS_FRIDAY_REPLY = "is_friday.REPLY";
    public static final String IS_SATURDAY_REPLY = "is_saturday.REPLY";
    public static final String IS_SUNDAY_REPLY = "is_sunday.REPLY";


    Button buttonAddAlarm, buttonCancel;
    NumberPicker numberPickerHours, numberPickerMinutes;
    Button buttonSunday, buttonMonday, buttonTuesday,buttonWednesday,
            buttonThursday, buttonFriday, buttonSaturday;
    EditText editTextDescription;
    private int hours, minutes;
    private boolean isSunday, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday;
    AlarmDetail newAlarm;

    Bundle modify;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Remove notification bar */
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_alarm_create);
        /* ------ Hide additional system bars ------ */
        hideSystemUI();

        newAlarm = new AlarmDetail();
        modify = getIntent().getExtras();

        buttonAddAlarm = findViewById(R.id.addAlarmButton);
        buttonAddAlarm.setOnClickListener(addAlarmListener);
        buttonCancel = findViewById(R.id.cancelButton);
        buttonCancel.setOnClickListener(cancelListener);

        numberPickerHours = findViewById(R.id.hoursPicker);
        numberPickerHours.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);
        numberPickerHours.setOnValueChangedListener(onHoursChangeListener);

        numberPickerMinutes = findViewById(R.id.minutesPicker);
        numberPickerMinutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);
        numberPickerMinutes.setOnValueChangedListener(onMinutesChangeListener);

        buttonSunday = findViewById(R.id.sundayButton);
        buttonSunday.setOnClickListener(buttonSundayListener);
        buttonMonday = findViewById(R.id.mondayButton);
        buttonMonday.setOnClickListener(buttonMondayListener);
        buttonTuesday = findViewById(R.id.tuesdayButton);
        buttonTuesday.setOnClickListener(buttonTuesdayListener);
        buttonWednesday = findViewById(R.id.wednesdayButton);
        buttonWednesday.setOnClickListener(buttonWednesdayListener);
        buttonThursday = findViewById(R.id.thursdayButton);
        buttonThursday.setOnClickListener(buttonThursdayListener);
        buttonFriday = findViewById(R.id.fridayButton);
        buttonFriday.setOnClickListener(buttonFridayListener);
        buttonSaturday = findViewById(R.id.saturdayButton);
        buttonSaturday.setOnClickListener(buttonSaturdayListener);

        editTextDescription = findViewById(R.id.edit_description);
        isSunday = isMonday = isTuesday = isWednesday = isThursday = isFriday = isSaturday = false;

        if( modify.getInt(ID_REPLY) != -1 ){
            setUpUI(modify);
        }
        Log.e("debug", "AlarmCreateActivity: onCreate ");
    }



    private void setUpUI(Bundle modify){
        numberPickerHours.setValue(modify.getInt(HOURS_REPLY));
        numberPickerHours.setOnValueChangedListener(onHoursChangeListener);
        numberPickerMinutes.setValue(modify.getInt(MINUTES_REPLY));
        numberPickerMinutes.setOnValueChangedListener(onMinutesChangeListener);
        editTextDescription.setText(modify.getString(DESCRIPTION_REPLY));

        isSunday = modify.getBoolean(IS_SUNDAY_REPLY);
        isMonday = modify.getBoolean(IS_MONDAY_REPLY);
        isTuesday = modify.getBoolean(IS_TUESDAY_REPLY);
        isWednesday = modify.getBoolean(IS_WEDNESDAY_REPLY);
        isThursday = modify.getBoolean(IS_THURSDAY_REPLY);
        isFriday = modify.getBoolean(IS_FRIDAY_REPLY);
        isSaturday = modify.getBoolean(IS_SATURDAY_REPLY);

        buttonMonday.setSelected(isMonday);
        buttonTuesday.setSelected(isTuesday);
        buttonWednesday.setSelected(isWednesday);
        buttonThursday.setSelected(isThursday);
        buttonFriday.setSelected(isFriday);
        buttonSaturday.setSelected(isSaturday);
        buttonSunday.setSelected(isSunday);

    }

    private ImageButton.OnClickListener addAlarmListener =
            new ImageButton.OnClickListener(){
                @Override
                public void onClick(View v){
                    Bundle extras = getIntent().getExtras();
                    Intent replyIntent = new Intent();
                    if( extras.getInt(ID_REPLY) != -1 ){
                        Log.e("debug", "AlarmCreateActivity is equal to modify " );
                        replyIntent.putExtra(ID_REPLY, extras.getInt(ID_REPLY));
                    }
                    String time = formatNumber(hours)+":"+formatNumber(minutes);
                    replyIntent.putExtra(TIME_REPLY, time);
                    if (TextUtils.isEmpty(editTextDescription.getText())){
                        replyIntent.putExtra(DESCRIPTION_REPLY,"None");
                    } else {
                        replyIntent.putExtra(DESCRIPTION_REPLY,String.valueOf(editTextDescription.getText()));
                    }
                    replyIntent.putExtra(STATE_REPLY, true);
                    replyIntent.putExtra(HOURS_REPLY, hours);
                    replyIntent.putExtra(MINUTES_REPLY, minutes);
                    replyIntent.putExtra(IS_MONDAY_REPLY,isMonday);
                    replyIntent.putExtra(IS_TUESDAY_REPLY,isTuesday);
                    replyIntent.putExtra(IS_WEDNESDAY_REPLY,isWednesday);
                    replyIntent.putExtra(IS_THURSDAY_REPLY,isThursday);
                    replyIntent.putExtra(IS_FRIDAY_REPLY,isFriday);
                    replyIntent.putExtra(IS_SATURDAY_REPLY,isSaturday);
                    replyIntent.putExtra(IS_SUNDAY_REPLY,isSunday);

                    Log.e("debug", "monday: " + isMonday );
                    Log.e("debug", "tuesday: " + isTuesday );
                    Log.e("debug", "wednesday: " + isWednesday );
                    Log.e("debug", "thursday: " + isThursday );
                    Log.e("debug", "friday: " + isFriday );
                    Log.e("debug", "saturday: " + isSaturday );
                    Log.e("debug", "sunday: " + isSunday );

                    setResult(RESULT_OK, replyIntent);

                    finish();
                }
            };
    private ImageButton.OnClickListener cancelListener =
            new ImageButton.OnClickListener(){
                @Override
                public void onClick(View v){
                    finish();
                }
            };
    private NumberPicker.OnValueChangeListener onHoursChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    hours = picker.getValue();
                }
            };
    private NumberPicker.OnValueChangeListener onMinutesChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    minutes = picker.getValue();
                }
            };
    private Button.OnClickListener buttonSundayListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    isSunday = !isSunday;
                    buttonSunday.setSelected(isSunday);
                }
            };
    private Button.OnClickListener buttonMondayListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    isMonday = !isMonday;
                    buttonMonday.setSelected(isMonday);
                }
            };
    private Button.OnClickListener buttonTuesdayListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    isTuesday = !isTuesday;
                    buttonTuesday.setSelected(isTuesday);
                }
            };
    private Button.OnClickListener buttonWednesdayListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    isWednesday = !isWednesday;
                    buttonWednesday.setSelected(isWednesday);
                }
            };
    private Button.OnClickListener buttonThursdayListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    isThursday = !isThursday;
                    buttonThursday.setSelected(isThursday);
                }
            };
    private Button.OnClickListener buttonFridayListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    isFriday = !isFriday;
                    buttonFriday.setSelected(isFriday);
                }
            };
    private Button.OnClickListener buttonSaturdayListener =
            new Button.OnClickListener(){
                @Override
                public void onClick(View v){
                    isSaturday = !isSaturday;
                    buttonSaturday.setSelected(isSaturday);
                }
            };
    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }

    private String formatNumber(int num){
        if (num < 10){
            return "0" + num;
        }
        return "" + num;
    }

    private void setAlarm(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
    private void cancelAlarm() {
        if (alarmManager!= null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}
