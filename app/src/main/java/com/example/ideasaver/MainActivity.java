package com.example.ideasaver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    com.google.android.material.floatingactionbutton.FloatingActionButton AddIdea;
//    MyAdapter adapter;

    ArrayList<Customdata> IdeasList=new ArrayList<>();
    ArrayList<IdeaDetails> ideaDetails =new ArrayList<>();




    TabLayout tabLayout;
    TabItem tabItem1,tabItem2;
    ViewPager viewPager;
    PageAdapter pageAdapter;


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.menu_list,menu);
//        MenuItem menuItem=menu.findItem(R.id.action_search);
//        SearchView searchView=(SearchView)menuItem.getActionView();
//        searchView.setQueryHint("Type here to Search");
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//
//                return false;
//            }
//        });
//
//        return super.onCreateOptionsMenu(menu);
//    }
    Boolean FirstTime=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean previouslyStarted = prefs.getBoolean("sadfsdf", false);
        if(!previouslyStarted) {


            ArrayList<String> arrayList=new ArrayList<>();
            arrayList.add("step 1");
            arrayList.add("step 2");
            arrayList.add("step 3");
            DatabaseHelper databaseHelper=DatabaseHelper.getDB(MainActivity.this);
            String Idea_description1="IdeaSaver is an innovative app designed to be your creative partner, empowering you to save, nurture, and act upon your ideas effectively. Whether you're an entrepreneur, a student, or a professional, this app is your secret weapon to unlock your full potential and achieve your goals.";
            String Idea_description2="With IdeaSaver, you have the perfect tool to capture, organize, and execute your ideas. Download the app now and unlock a world of boundless creativity and productivity.";
            databaseHelper.ideaDetailsDAO().addIdea(new IdeaDetails("Idea Saver", Idea_description1, "12/04/2023", EditIdeaActivity.CompletedIdeaFalse, 84645, AddIdeaActivity.REMINDER_OFF,arrayList));
            databaseHelper.ideaDetailsDAO().addIdea(new IdeaDetails("To-Do-List", Idea_description2, "12/04/2023", EditIdeaActivity.CompletedIdeaFalse, 84646, AddIdeaActivity.REMINDER_OFF,arrayList));
            databaseHelper.ideaDetailsDAO().addIdea(new IdeaDetails("Idea Name", "Idea Description", "12/04/2023", EditIdeaActivity.CompletedIdeaFalse, 84647, AddIdeaActivity.REMINDER_OFF,arrayList));




            SharedPreferences.Editor edit = prefs.edit();
            edit.putBoolean("sadfsdf", Boolean.TRUE);
            edit.commit();
        }



        tabLayout = findViewById(R.id.TabLayoutMainActivity);
        tabItem1 = findViewById(R.id.allIdeasTabItem);
        tabItem2 = findViewById(R.id.CompIdeasTabItem);
        viewPager = findViewById(R.id.viewpagerMainActivity);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());


                if (tab.getPosition() == 0 || tab.getPosition() == 1) {
                    pageAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


//        Remove action bar
//        if(getSupportActionBar()!=null){
//            this.getSupportActionBar().hide();
//        }

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.complimantary)));


        //change colour of Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.complimantary));
        }


//        AddIdea=findViewById(R.id.addIdea);
//        ListView mylistView= this.<ListView>findViewById(R.id.List_VIew);
//        adapter=new MyAdapter();
//        mylistView.setAdapter(adapter);
//
//        DatabaseHelper databaseHelper=DatabaseHelper.getDB(MainActivity.this);
//        ideaDetails=(ArrayList<IdeaDetails>) databaseHelper.ideaDetailsDAO().getAllIdeas();
//


//        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent=new Intent(MainActivity.this,EditIdeaActivity.class);
//                intent.putExtra("Arraylist_Number",i);
//                startActivity(intent);
//            }
//        });


//        if(Permission.activitychange==true){
//            Intent Intent2 = getIntent();
//            String idea_name = Intent2.getStringExtra("IDEA_NAME");
//            String idea_description = Intent2.getStringExtra("IDEA_DESCRIPTION");
//            ArrayList<String> idea_substep = (ArrayList<String>) getIntent().getSerializableExtra("IDEA_SUBSTEP");
//            saveData(idea_name,idea_description,idea_substep);
//            Permission.activitychange=false;
//            loadData();
//        }


//        AddIdea.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(MainActivity.this,AddIdeaActivity.class);
//                startActivity(intent);
//            }
//        });
//    }


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
//        }else{
//        }
//
//    }

//    public void saveData(String IdeaName,String IdeaDescription, ArrayList<String> IdeaSubsteps) {
//
//        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("DATA",MODE_PRIVATE);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        Gson gson=new Gson();
//        IdeasList.add(new Customdata(IdeaName,IdeaDescription,IdeaSubsteps));
//        String json=gson.toJson(IdeasList);
//        editor.putString("Ideas_Data",json);
//        editor.apply();
//
//    }


//
//    public class MyAdapter extends BaseAdapter{
//
//        @Override
//        public int getCount() {
//            return ideaDetails.size();
//        }
//
//        @Override
//        public Object getItem(int i) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int i) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int i, View view, ViewGroup viewGroup) {
//            view=getLayoutInflater().inflate(R.layout.cardview,viewGroup,false);
//            TextView textView=view.findViewById(R.id.title);
//            TextView textView2=view.findViewById(R.id.description);
//
//                textView.setText(ideaDetails.get(i).getIdeaname()+"");
//                textView2.setText(ideaDetails.get(i).getIdeadescription()+"");
//            return view;
//        }
//    }
    }
}