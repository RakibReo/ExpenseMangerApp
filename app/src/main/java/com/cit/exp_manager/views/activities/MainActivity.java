package com.cit.exp_manager.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.cit.exp_manager.R;
import com.cit.exp_manager.adapters.TransactionsAdapter;
import com.cit.exp_manager.databinding.ActivityMainBinding;
import com.cit.exp_manager.models.Transaction;
import com.cit.exp_manager.utils.Constants;
import com.cit.exp_manager.utils.Helper;
import com.cit.exp_manager.viewmodels.MainViewModel;
import com.cit.exp_manager.views.fragments.AddTransactionFragment;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    Calendar calendar;
   // Realm realm;

    /*
    0= Daily
    1=Monthly
    2=Calender
    3= Summary
    4=Notes
    *  */



    //implement mvvm

    public MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialize mvvm
        viewModel=new ViewModelProvider(this).get(MainViewModel.class);


       // setepDatabase(); //call realm database


        setSupportActionBar(binding.toolBar);
        getSupportActionBar().setTitle("Transactions");

        Constants.setCategories();

        calendar=Calendar.getInstance();
        updateDate();

        binding.nextDateBtn.setOnClickListener(c->{

            if(Constants.SELECTED_TAB==Constants.DAILY){

                calendar.add(Calendar.DATE,1);

            }else if(Constants.SELECTED_TAB==Constants.MONTHLY){

                calendar.add(Calendar.MONTH,1);
            }

            updateDate();


        });
        binding.previousDateBtn.setOnClickListener(c->{

            if(Constants.SELECTED_TAB==Constants.DAILY){

                calendar.add(Calendar.DATE,-1);

            }else if(Constants.SELECTED_TAB==Constants.MONTHLY){

                calendar.add(Calendar.MONTH,-1);
            }



            updateDate();


        });

        binding.floatingActionButton.setOnClickListener(v -> {               //+ symbol fragment implemented  here
    new AddTransactionFragment().show(getSupportFragmentManager(),null);

        });

    binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){


        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            if(tab.getText().equals("Monthly")){

                Constants.SELECTED_TAB=1;
                updateDate();

            }else  if(tab.getText().equals("Daily")){

                Constants.SELECTED_TAB=0;
                updateDate();

            }


            //Toast.makeText(MainActivity.this, tab.getText().toString(),Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    });





  viewModel.transactions.observe(this, new Observer<RealmResults<Transaction>>() {
      @Override
      public void onChanged(RealmResults<Transaction> transactions) {


          TransactionsAdapter transactionsAdapter=new TransactionsAdapter(MainActivity.this,transactions);
          binding.transactionsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
          binding.transactionsList.setAdapter(transactionsAdapter);
          if(transactions.size()>0){

              binding.emptyState.setVisibility(View.GONE);


          }else{
              binding.emptyState.setVisibility(View.VISIBLE);

          }


      }
  });

  viewModel.totalIncome.observe(this, new Observer<Double>() {
      @Override
      public void onChanged(Double aDouble) {
          binding.incomeLbl.setText(String.valueOf(aDouble));
      }
  });
 viewModel.totalExpense.observe(this, new Observer<Double>() {
      @Override
      public void onChanged(Double aDouble) {
          binding.expenseLbl.setText(String.valueOf(aDouble));

      }
  });
 viewModel.totalAmount.observe(this, new Observer<Double>() {
      @Override
      public void onChanged(Double aDouble) {
          binding.totalLbl.setText(String.valueOf(aDouble));
      }
  });


  viewModel.getTransaction(calendar);;






    }

    public void getTransaction(){

        viewModel.getTransaction(calendar);
    }



    void  updateDate(){

        if( Constants.SELECTED_TAB==Constants.DAILY){
            SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM, yyyy");
            binding.currentDate.setText(dateFormat.format(calendar.getTime()));


        }else if(Constants.SELECTED_TAB==Constants.MONTHLY){
            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));


        }

         viewModel.getTransaction(calendar);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}