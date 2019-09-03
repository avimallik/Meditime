package com.armavi.medi.time.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.armavi.medi.time.R;
import com.armavi.medi.time.data.DatabaseHelper;
import com.armavi.medi.time.model.Alarm;
import com.armavi.medi.time.service.AlarmReceiver;
import com.armavi.medi.time.service.LoadAlarmsService;
import com.armavi.medi.time.util.ViewUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class AddEditAlarmFragment extends Fragment {

    private TimePicker mTimePicker;
    private EditText mLabel;
    private CheckBox mMon, mTues, mWed, mThurs, mFri, mSat, mSun;
    private Spinner description, medType;
    private TextView checkBoxSelectAll;




    String [] descriptionListInfo = {"After Meal", "Before Meal", "Anytime"};
    String [] medicineTypeInfo = {"Tablet", "Capsule", "Syrup", "Injection", "Saline", "Ointment", "Not Known"};


    String descriptionRecord = "";
    String medicineRecord = "";


    public static AddEditAlarmFragment newInstance(Alarm alarm) {

        Bundle args = new Bundle();
        args.putParcelable(AddEditAlarmActivity.ALARM_EXTRA, alarm);

        AddEditAlarmFragment fragment = new AddEditAlarmFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_add_edit_alarm, container, false);

        setHasOptionsMenu(true);

        final Alarm alarm = getAlarm();

        mTimePicker = (TimePicker) v.findViewById(R.id.edit_alarm_time_picker);
        ViewUtils.setTimePickerTime(mTimePicker, alarm.getTime());

        mLabel = (EditText) v.findViewById(R.id.edit_alarm_label);
        mLabel.setText(alarm.getLabel());

        description = (Spinner) v.findViewById(R.id.description);
//        description.setText(alarm.getDescription());

        List<String> scheduleType = new ArrayList<String>();
        scheduleType .add(getResources().getString(R.string.after_meal));
        scheduleType .add(getResources().getString(R.string.before_meal));
        scheduleType .add(getResources().getString(R.string.anytime));

        List<String> medTypeCat = new ArrayList<String>();
        medTypeCat.add(getResources().getString(R.string.tablet));
        medTypeCat.add(getResources().getString(R.string.capsule));
        medTypeCat.add(getResources().getString(R.string.syrup));
        medTypeCat.add(getResources().getString(R.string.injection));
        medTypeCat.add(getResources().getString(R.string.saline));
        medTypeCat.add(getResources().getString(R.string.ointment));
        medTypeCat.add(getResources().getString(R.string.not_know));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, scheduleType);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        description.setAdapter(adapter);

        medType = (Spinner) v.findViewById(R.id.medType);
        ArrayAdapter<String> adapterMedType = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, medTypeCat);
        adapterMedType.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        medType.setAdapter(adapterMedType);

        checkBoxSelectAll = (TextView) v.findViewById(R.id.checkBoxSelectAll);

        mMon = (CheckBox) v.findViewById(R.id.edit_alarm_mon);
        mTues = (CheckBox) v.findViewById(R.id.edit_alarm_tues);
        mWed = (CheckBox) v.findViewById(R.id.edit_alarm_wed);
        mThurs = (CheckBox) v.findViewById(R.id.edit_alarm_thurs);
        mFri = (CheckBox) v.findViewById(R.id.edit_alarm_fri);
        mSat = (CheckBox) v.findViewById(R.id.edit_alarm_sat);
        mSun = (CheckBox) v.findViewById(R.id.edit_alarm_sun);

        setDayCheckboxes(alarm);

        checkBoxSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMon.setChecked(true);
                mTues.setChecked(true);
                mWed.setChecked(true);
                mThurs.setChecked(true);
                mFri.setChecked(true);
                mSat.setChecked(true);
                mSun.setChecked(true);
            }
        });


        description.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Spinner Position Value
                switch (position){

                    case 0:
                        descriptionRecord = "After Meal";
                        break;

                    case 1:
                        descriptionRecord = "Before Meal";
                        break;

                    case 2:
                        descriptionRecord = "Anytime";
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        medType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Spinner Position Value
                switch (position){

                    case 0:
                        medicineRecord = "Tablet";
                        break;

                    case 1:
                        medicineRecord = "Capsule";
                        break;

                    case 2:
                        medicineRecord = "Syrup";
                        break;

                    case 3:
                        medicineRecord = "Injection";
                        break;

                    case 4:
                        medicineRecord = "Saline";
                        break;

                    case 5:
                        medicineRecord = "Ointment";
                        break;

                    case 6:
                        medicineRecord = "Not Known";
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_alarm_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                save();
                break;
            case R.id.action_delete:
                delete();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Alarm getAlarm() {
        return getArguments().getParcelable(AddEditAlarmActivity.ALARM_EXTRA);
    }

    private void setDayCheckboxes(Alarm alarm) {
        mMon.setChecked(alarm.getDay(Alarm.MON));
        mTues.setChecked(alarm.getDay(Alarm.TUES));
        mWed.setChecked(alarm.getDay(Alarm.WED));
        mThurs.setChecked(alarm.getDay(Alarm.THURS));
        mFri.setChecked(alarm.getDay(Alarm.FRI));
        mSat.setChecked(alarm.getDay(Alarm.SAT));
        mSun.setChecked(alarm.getDay(Alarm.SUN));
    }

    private void save() {

        final Alarm alarm = getAlarm();

        final Calendar time = Calendar.getInstance();
        time.set(Calendar.MINUTE, ViewUtils.getTimePickerMinute(mTimePicker));
        time.set(Calendar.HOUR_OF_DAY, ViewUtils.getTimePickerHour(mTimePicker));
        alarm.setTime(time.getTimeInMillis());

        alarm.setLabel(mLabel.getText().toString());
        alarm.setDescription(descriptionRecord);
        alarm.setMedType(medicineRecord);

        alarm.setDay(Alarm.MON, mMon.isChecked());
        alarm.setDay(Alarm.TUES, mTues.isChecked());
        alarm.setDay(Alarm.WED, mWed.isChecked());
        alarm.setDay(Alarm.THURS, mThurs.isChecked());
        alarm.setDay(Alarm.FRI, mFri.isChecked());
        alarm.setDay(Alarm.SAT, mSat.isChecked());
        alarm.setDay(Alarm.SUN, mSun.isChecked());

        final int rowsUpdated = DatabaseHelper.getInstance(getContext()).updateAlarm(alarm);
        final int messageId = (rowsUpdated == 1) ? R.string.update_complete : R.string.update_failed;

        Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();

        AlarmReceiver.setReminderAlarm(getContext(), alarm);

        getActivity().finish();

    }

    private void delete() {

        final Alarm alarm = getAlarm();

        final AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext(), R.style.DeleteAlarmDialogTheme);
        builder.setTitle(R.string.delete_dialog_title);
        builder.setMessage(R.string.delete_dialog_content);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                //Cancel any pending notifications for this alarm
                AlarmReceiver.cancelReminderAlarm(getContext(), alarm);

                final int rowsDeleted = DatabaseHelper.getInstance(getContext()).deleteAlarm(alarm);
                int messageId;
                if(rowsDeleted == 1) {
                    messageId = R.string.delete_complete;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                    LoadAlarmsService.launchLoadAlarmsService(getContext());
                    getActivity().finish();
                } else {
                    messageId = R.string.delete_failed;
                    Toast.makeText(getContext(), messageId, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.no, null);
        builder.show();

    }


}
