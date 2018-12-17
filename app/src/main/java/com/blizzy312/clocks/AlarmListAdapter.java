package com.blizzy312.clocks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmViewHolder> {

    private static ClickListener clickListener;

//    private AlarmViewModel mAlarmViewModel;

    class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private final TextView textViewTimeFrame;
        private final TextView textViewDescriptionFrame;
        private final TextView textViewDaysFrame;
        private final Switch alarmState;


        private AlarmViewHolder(final View itemView) {
            super(itemView);
            Log.e("debug", "AlarmViewHolder: " );
//            mAlarmViewModel = ViewModelProviders.of().get(AlarmViewModel.class);
            textViewTimeFrame = itemView.findViewById(R.id.TimeFrame);
            textViewDescriptionFrame = itemView.findViewById(R.id.DescriptionFrame);
            textViewDaysFrame = itemView.findViewById(R.id.DaysFrame);
            alarmState = itemView.findViewById(R.id.AlarmStatus);


//            alarmState.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }

    }

    public void setOnItemClickListener(ClickListener clickListener) {
        AlarmListAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
    }

    private final LayoutInflater mInflater;
    private List<AlarmDetail> mAlarms; // Cached copy of words

    public AlarmListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public AlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.alarm_row_view_container, parent, false);
        Log.e("debug", "onCreateViewHolder: " );
        return new AlarmViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlarmViewHolder holder, final int position) {

        final AlarmDetail current = mAlarms.get(position);
        holder.textViewTimeFrame.setText(current.getTime());
        holder.textViewDescriptionFrame.setText((current.getDescription()));
        holder.textViewDaysFrame.setText(convertDays(current));
        holder.alarmState.setChecked(current.isAlarmState());
        /*holder.alarmState.setOnCheckedChangeListener( new Switch.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    current.setAlarmState(true);
                    Log.e("debug", "position:" + position + " is true" );
                    mAlarms.set(position,current);

                } else {
                    current.setAlarmState(false);
                    Log.e("debug", "position:" + position + " is false" );
                    mAlarms.set(position,current);
                }
                setAlarms(mAlarms);
            }
        });*/

    }

    public void setAlarms(List<AlarmDetail> alarms){
        mAlarms = alarms;
        Log.e("debug", "setAlarms is called from list adapter " );
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mAlarms has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mAlarms != null){
            Log.e("debug", "getItemCount: "+mAlarms.size() );
            return mAlarms.size();
        } else {
            return 0;
        }
    }

    public AlarmDetail getAlarmAtPosition (int position) {
        return mAlarms.get(position);
    }

    private String convertDays(String val){
        String temp = "";
        if(val.equals("1111111")){
            return "Everyday";
        }
        if(val.equals("1000001")){
            return "Weekends";
        }
        if(val.equals("0111110")){
            return "Weekdays";
        }
        if(val.charAt(0) == '1'){
            temp += "Sun ";
        }
        if(val.charAt(1) == '1'){
            temp += "Mon ";
        }
        if(val.charAt(2) == '1'){
            temp += "Tue ";
        }
        if(val.charAt(3) == '1'){
            temp += "Wed ";
        }
        if(val.charAt(4) == '1'){
            temp += "Thur ";
        }
        if(val.charAt(5) == '1'){
            temp += "Fr ";
        }
        if(val.charAt(6) == '1'){
            temp += "Sat ";
        }
        return temp;
    }

    private String convertDays(AlarmDetail alarmDetail){
        String alarmDays = "";
        boolean sun, mon, tue, wed, thu, fri, sat;
        sun = alarmDetail.isSunday();
        mon = alarmDetail.isMonday();
        tue = alarmDetail.isTuesday();
        wed = alarmDetail.isWednesday();
        thu = alarmDetail.isThursday();
        fri = alarmDetail.isFriday();
        sat = alarmDetail.isSaturday();

        Log.e("debug", "monday: " + mon );
        Log.e("debug", "tuesday: " + tue );
        Log.e("debug", "wednesday: " + wed );
        Log.e("debug", "thursday: " + thu );
        Log.e("debug", "friday: " + fri );
        Log.e("debug", "saturday: " + sat );
        Log.e("debug", "sunday: " + sun );

        if(sun & mon & tue & wed & thu & fri & sat){
            return "Everyday";
        }
        if(sun & sat & !mon & !tue & !wed & !thu & !fri){
            return  "Weekends";
        }
        if(!sun & !sat & mon & tue & wed & thu & fri){
            return "Weekdays";
        }
        if(sun){
            alarmDays += "Sun ";
        }
        if(mon){
            alarmDays += "Mon ";
        }
        if(tue){
            alarmDays += "Tue ";
        }
        if(wed){
            alarmDays += "Wed ";
        }
        if(thu){
            alarmDays += "Thu ";
        }
        if(fri){
            alarmDays += "Fri ";
        }
        if(sat){
            alarmDays += "Sat ";
        }
        return alarmDays;
    }

}
