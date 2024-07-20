package com.example.ideasaver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;
import java.util.Calendar;

public class EditIdeaActivity extends AppCompatActivity {





    EditText IdeaName;
    EditText IdeaDescription;
    EditText IdeaSubsteps;
    ImageView addSubstep;
    Button SaveIdea;
    Button DeleteIDea;
    Button CompleteIdea;

    ArrayList<IdeaDetails> ideaDetails =new ArrayList<>();
    ArrayList<String> substeplist;

    MyAdapter3 myAdapter3;
    int ArraylistNumber;
    int numberOfSteps=1;

    public static final int CompletedIdeaTrue=1;
    public static final int CompletedIdeaFalse=0;


    Boolean SwitchIsOn=false;




    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;



    private void showTimePicker() {

        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();

        picker.show(getSupportFragmentManager(),"foxandroid");

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,picker.getHour());
                calendar.set(Calendar.MINUTE,picker.getMinute());
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);


            }
        });


    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_idea);
        createNotificationChannel();
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);




        getSupportActionBar().setTitle("Edit Idea");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.secondary)));


        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.secondary));
        }




        ListView listView=findViewById(R.id.substeplistview);
        IdeaName=findViewById(R.id.ideanameaddactvity);
        IdeaDescription=findViewById(R.id.ideadescriptionaddactvity);
        addSubstep=findViewById(R.id.ideasubstepbuttonaddactvity);
        IdeaSubsteps=findViewById(R.id.ideasubstepaddactvity);
        SaveIdea=findViewById(R.id.addideabuttoneaddactvity);
        DeleteIDea=findViewById(R.id.deleteideabuttoneaddactvity);
        ScrollView scrollView=findViewById(R.id.scrollView);
        CompleteIdea=findViewById(R.id.completeideabuttoneaddactvity);
        Switch ReminderSwitch=findViewById(R.id.SwitchforReminderEditActivity);









        substeplist=new ArrayList<>();
        myAdapter3=new MyAdapter3();
        listView.setAdapter(myAdapter3);


        Intent intent=getIntent();
        int Arraylistid=intent.getIntExtra("Arraylist_Id",0);





        DatabaseHelper databaseHelper=DatabaseHelper.getDB(EditIdeaActivity.this);
        ideaDetails=(ArrayList<IdeaDetails>) databaseHelper.ideaDetailsDAO().getAllIdeas();





        for(int i=0;i<ideaDetails.size();i++){
            if(Arraylistid==ideaDetails.get(i).getId()){
            ArraylistNumber=i;
            }
        }


        if(ideaDetails.get(ArraylistNumber).getAlarmIsEnabled()==AddIdeaActivity.REMINDER_ON){
            SwitchIsOn=true;
            ReminderSwitch.setChecked(true);
        }else{
            ReminderSwitch.setChecked(false);
        }


        ReminderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    showTimePicker();
                    SwitchIsOn=true;
                }else{
                    SwitchIsOn=false;
                    //Cancel the alarm bcox the task is completed
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent myIntent = new Intent(getApplicationContext(), ALertReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getApplicationContext(), ideaDetails.get(ArraylistNumber).getAlarmId(), myIntent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager.cancel(pendingIntent);
                }
            }
        });






        IdeaName.setText(ideaDetails.get(ArraylistNumber).getIdeaname());
        IdeaDescription.setText(ideaDetails.get(ArraylistNumber).getIdeadescription());
        substeplist=ideaDetails.get(ArraylistNumber).getSubsteps();

        // so that scrollview stop working when listview was touched and it can work
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        addSubstep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                substeplist.add(numberOfSteps+". "+IdeaSubsteps.getText().toString());
                myAdapter3.notifyDataSetChanged();
                IdeaSubsteps.getText().clear();
                numberOfSteps+=1;
            }
        });




        SaveIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int id=ideaDetails.get(ArraylistNumber).getId();
                String ideaname=IdeaName.getText().toString();
                String ideadescription=IdeaDescription.getText().toString();
                String Date=ideaDetails.get(ArraylistNumber).getSavedDate();




                Intent intent2=new Intent(EditIdeaActivity.this,ALertReceiver.class);
                int idforAlarm = (int) ideaDetails.get(ArraylistNumber).getAlarmId();
                PendingIntent pendingIntent=PendingIntent.getBroadcast(EditIdeaActivity.this,idforAlarm,intent2,PendingIntent.FLAG_IMMUTABLE);


                DatabaseHelper databaseHelper=DatabaseHelper.getDB(EditIdeaActivity.this);

                if(SwitchIsOn) {
                    if (calendar != null) {
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
                        databaseHelper.ideaDetailsDAO().updateIdea(new IdeaDetails(id,ideaname,ideadescription,Date,ideaDetails.get(ArraylistNumber).getIsCompleted(),idforAlarm,AddIdeaActivity.REMINDER_ON,substeplist));
                    }
                }else{
                    databaseHelper.ideaDetailsDAO().updateIdea(new IdeaDetails(id,ideaname,ideadescription,Date,ideaDetails.get(ArraylistNumber).getIsCompleted(),idforAlarm,AddIdeaActivity.REMINDER_OFF,substeplist));
                }



                Toast.makeText(EditIdeaActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(EditIdeaActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });




        CompleteIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id=ideaDetails.get(ArraylistNumber).getId();
                String ideaname=IdeaName.getText().toString();
                String ideadescription=IdeaDescription.getText().toString();
                String Date=ideaDetails.get(ArraylistNumber).getSavedDate();


                MediaPlayer mediaPlayer=MediaPlayer.create(EditIdeaActivity.this,R.raw.done);
                mediaPlayer.start();

                DatabaseHelper databaseHelper=DatabaseHelper.getDB(EditIdeaActivity.this);
                databaseHelper.ideaDetailsDAO().updateIdea(new IdeaDetails(id,ideaname,ideadescription,Date,CompletedIdeaTrue,ideaDetails.get(ArraylistNumber).getAlarmId(),ideaDetails.get(ArraylistNumber).getAlarmIsEnabled(),substeplist));




                //Cancel the alarm bcox the task is completed
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(getApplicationContext(), ALertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), ideaDetails.get(ArraylistNumber).getAlarmId(), myIntent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);


                Toast.makeText(EditIdeaActivity.this, "Completed You did it!!!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(EditIdeaActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });





        DeleteIDea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper databaseHelper=DatabaseHelper.getDB(EditIdeaActivity.this);
                databaseHelper.ideaDetailsDAO().deleteIdea(ideaDetails.get(ArraylistNumber));


                //Cancel the alarm bcox the task is completed
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent myIntent = new Intent(getApplicationContext(), ALertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        getApplicationContext(), ideaDetails.get(ArraylistNumber).getAlarmId(), myIntent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.cancel(pendingIntent);


                Toast.makeText(EditIdeaActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(EditIdeaActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


















    }






    public class MyAdapter3 extends BaseAdapter {

        @Override
        public int getCount() {
            return substeplist.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.editideasubsteplistview,viewGroup,false);
            TextView textView=view.findViewById(R.id.substepname);
            textView.setText(substeplist.get(i)+"");
            ImageView imageButton=view.findViewById(R.id.deletesubstep);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    substeplist.remove(i);
                    myAdapter3.notifyDataSetChanged();
                }
            });
            return view;
        }
    }


    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }






}