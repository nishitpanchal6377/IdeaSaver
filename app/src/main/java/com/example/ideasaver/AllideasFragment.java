package com.example.ideasaver;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class AllideasFragment extends Fragment {



//    MyAdapter myAdapter;
    MyAdapter myAdapter;

    ArrayList<Customdata> IdeasList=new ArrayList<>();
    ArrayList<IdeaDetails> ideaDetails =new ArrayList<>();







    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_list, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        MenuItem item2 = menu.findItem(R.id.action_addIdeas);
        MenuItem item3 = menu.findItem(R.id.action_more);


        item3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Toast.makeText(getContext(), "Work in Progress", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        item2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                Toast.makeText(getContext(), "Rate Us", Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getContext(),AddIdeaActivity.class);
//                startActivity(intent);
                return false;
            }
        });

        SearchView searchView = new SearchView(((AppCompatActivity) getActivity()).getSupportActionBar().getThemedContext());

        // MenuItemCompat.setShowAsAction(item, //MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | //MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        //  MenuItemCompat.setActionView(item, searchView);
        // These lines are deprecated in API 26 use instead
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        item.setActionView(searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });
        searchView.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {

                                          }
                                      }
        );
    }









    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true); // Add this to enable menu options in action bar!
        DatabaseHelper databaseHelper=DatabaseHelper.getDB(getContext());
        ideaDetails=(ArrayList<IdeaDetails>) databaseHelper.ideaDetailsDAO().getAllIdeas();
         myAdapter=new MyAdapter();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View FragmentView=inflater.inflate(R.layout.fragment_allideas,container,false);



        ListView listView=FragmentView.findViewById(R.id.List_VIew);
        listView.setAdapter(myAdapter);


        FloatingActionButton floatingActionButton=FragmentView.findViewById(R.id.floating_action_button_fab_with_listview);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),AddIdeaActivity.class);
                startActivity(intent);
            }
        });








        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent=new Intent(getActivity(),EditIdeaActivity.class);
                intent.putExtra("Arraylist_Id",ideaDetails.get(i).getId());
                startActivity(intent);

            }
        });


        return FragmentView;

    }




    public class MyAdapter extends BaseAdapter implements Filterable {

        ArrayList<IdeaDetails> filterlist;

        public MyAdapter() {
            this.filterlist = ideaDetails;
        }

        @Override
        public int getCount() {
            return ideaDetails.size();
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
            view=getLayoutInflater().inflate(R.layout.cardview,viewGroup,false);
            TextView textView=view.findViewById(R.id.title);
            TextView textView2=view.findViewById(R.id.description);
            TextView textView3=view.findViewById(R.id.SavedDate);

            textView.setText(ideaDetails.get(i).getIdeaname()+"");
            textView2.setText(ideaDetails.get(i).getIdeadescription()+"");
            textView3.setText(ideaDetails.get(i).getSavedDate()+"");

            return view;
        }

        @Override
        public Filter getFilter() {

            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {


                    constraint = constraint.toString().toLowerCase();
                    FilterResults result = new FilterResults();
                    if(constraint != null && constraint.toString().length() > 0)
                    {
                        ArrayList<IdeaDetails> filteredItems = new ArrayList<IdeaDetails>();

                        for(int i = 0, l = filterlist.size(); i < l; i++)
                        {
                            IdeaDetails Ideas = filterlist.get(i);
                            if(Ideas.getIdeaname().toString().toLowerCase().contains(constraint))
                                filteredItems.add(Ideas);
                        }
                        result.count = filteredItems.size();
                        result.values = filteredItems;
                    }
                    else
                    {
                        synchronized(this)
                        {
                            result.values = filterlist;
                            result.count = filterlist.size();
                        }
                    }
                    return result;
                }


                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    ideaDetails = (ArrayList<IdeaDetails>)filterResults.values;
                    notifyDataSetChanged();
                }
            };
        }
    }






}