package com.example.ideasaver;

//import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import java.util.TimeZone;

public class AddIdeaActivity extends AppCompatActivity {

    EditText IdeaName;
    EditText IdeaDescription;
    EditText IdeaSubsteps;
    ImageView addSubstep;
    ImageView get_back_to_main_activity_ios;
    Button SaveIdea;
    ArrayList<Customdata> Customdatalist;
    ArrayList<String> substeplist;

    MyAdapter2 myAdapter2;

    int numberOfSteps=1;

    Boolean SwitchIsOn=false;



    public static final int REMINDER_ON=1;
    public static final int REMINDER_OFF=0;







    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    int id=0;







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




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_idea);
        createNotificationChannel();
        getSupportActionBar().hide();
        //18/12/2024
//        getSupportActionBar().setTitle("Add Idea");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);


        get_back_to_main_activity_ios=findViewById(R.id.back_main_activity_ios);
        get_back_to_main_activity_ios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddIdeaActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });





        //To request Permission for notification of mobiles having android version >13 or sdk >33
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},99);
        }











//18/12/2024
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.complimantary)));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.complimantary));
        }


        Switch aSwitch=findViewById(R.id.RemindDailySwitch);
        ListView listView=findViewById(R.id.substeplistview);
        IdeaName=findViewById(R.id.ideanameaddactvity);
        IdeaDescription=findViewById(R.id.ideadescriptionaddactvity);
        addSubstep=findViewById(R.id.ideasubstepbuttonaddactvity);
        IdeaSubsteps=findViewById(R.id.ideasubstepaddactvity);
        SaveIdea=findViewById(R.id.addideabuttoneaddactvity);
        ScrollView scrollView=findViewById(R.id.scrollView);
        substeplist=new ArrayList<>();
        Customdatalist=new ArrayList<>();

        myAdapter2=new MyAdapter2();
        listView.setAdapter(myAdapter2);


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    showTimePicker();
                    SwitchIsOn=true;
                }else{
                    SwitchIsOn=false;

                    Toast.makeText(AddIdeaActivity.this, "Reminder Off", Toast.LENGTH_SHORT).show();
                }
            }
        });







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
                myAdapter2.notifyDataSetChanged();
                IdeaSubsteps.getText().clear();
                numberOfSteps+=1;
            }
        });



        SaveIdea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent intent2=new Intent(AddIdeaActivity.this,ALertReceiver.class);
                int id = (int) System.currentTimeMillis();
                PendingIntent pendingIntent=PendingIntent.getBroadcast(AddIdeaActivity.this,id,intent2,PendingIntent.FLAG_IMMUTABLE);

                if(SwitchIsOn) {
                    if (calendar != null) {
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);


                        //23/12/2024
//                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);


                    }
                }




                String ideaname=IdeaName.getText().toString();
                String ideadescription=IdeaDescription.getText().toString();


                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());

                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                String Date=currentDay+"/"+currentMonth+"/"+currentYear;


                DatabaseHelper databaseHelper=DatabaseHelper.getDB(AddIdeaActivity.this);




                if(SwitchIsOn) {
                    databaseHelper.ideaDetailsDAO().addIdea(new IdeaDetails(ideaname, ideadescription, Date, EditIdeaActivity.CompletedIdeaFalse, id, REMINDER_ON,substeplist));
                }else {
                    databaseHelper.ideaDetailsDAO().addIdea(new IdeaDetails(ideaname, ideadescription, Date, EditIdeaActivity.CompletedIdeaFalse, id, REMINDER_OFF,substeplist));
                }


                Toast.makeText(AddIdeaActivity.this, "Your Idea Is Safe Now!!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddIdeaActivity.this,MainActivity.class);
                startActivity(intent);
//                intent.putExtra("IDEA_NAME",IdeaName.getText().toString());
//                intent.putExtra("IDEA_DESCRIPTION",IdeaDescription.getText().toString());
//                intent.putExtra("IDEA_SUBSTEP", substeplist);
//                Permission.activitychange=true;


//                saveData(IdeaName.getText().toString(),IdeaDescription.getText().toString(),substeplist);
//                Toast.makeText(AddIdeaActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//                loadData();
            }


        });

    }



//    public void loadData() {
//        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("DATA",MODE_PRIVATE);
//        Gson gson=new Gson();
//        String json=sharedPreferences.getString("Ideas_Data",null);
//
//        Type type=new TypeToken<ArrayList<Customdata>>(){
//        }.getType();
//
//        Customdatalist=gson.fromJson(json,type);
//        if(Customdatalist==null){
//            Toast.makeText(AddIdeaActivity.this,"need data", Toast.LENGTH_SHORT).show();
//        }else{
//            for(int i=0;Customdatalist.size()>i;i++){
////                Toast.makeText(AddIdeaActivity.this, Customdatalist.get(i).Ideaname.toString(), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//    }
//
//    public void saveData(String IdeaName,String IdeaDescription, ArrayList<String> IdeaSubsteps) {
//
//        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("DATA",MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        Gson gson=new Gson();
//        Customdatalist.add(new Customdata(IdeaName,IdeaDescription,IdeaSubsteps));
//        String json=gson.toJson(Customdatalist);
//        editor.putString("Ideas_Data",json);
//        editor.apply();
//
//    }





    public class MyAdapter2 extends BaseAdapter {

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
            view=getLayoutInflater().inflate(R.layout.substeplist_view,viewGroup,false);
            TextView textView=view.findViewById(R.id.substepname);
            textView.setText(substeplist.get(i)+"");
            ImageView imageButton=view.findViewById(R.id.deletesubstep);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    substeplist.remove(i);
                    myAdapter2.notifyDataSetChanged();
                }
            });
            return view;
        }
    }



    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "ReminderChannel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("foxandroid",name,importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }









    }



}