package com.example.ideasaver;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ideasaver.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class AddIdeaActivity extends AppCompatActivity {

    EditText IdeaName;
    EditText IdeaDescription;
    EditText IdeaSubsteps;
    ImageView addSubstep;
    Button SaveIdea;
    ArrayList<Customdata> Customdatalist;
    ArrayList<String> substeplist;

    MyAdapter2 myAdapter2;

    int numberOfSteps=1;

    Boolean SwitchIsOn=false;


    InterstitialAd mInterstitialAd;


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
        getSupportActionBar().setTitle("Add Idea");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);


        MobileAds.initialize(AddIdeaActivity.this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });




        AdRequest adRequest = new AdRequest.Builder().build();



        InterstitialAd.load(AddIdeaActivity.this,"ca-app-pub-1486678075354721/8732466484", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Toast.makeText(AddIdeaActivity.this, "loaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                        Toast.makeText(AddIdeaActivity.this, "failed", Toast.LENGTH_SHORT).show();
                    }
                });













        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.secondary)));

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.secondary));
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

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(AddIdeaActivity.this);
                } else {
                }




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
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

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