package com.cit.exp_manager.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.cit.exp_manager.models.Transaction;
import com.cit.exp_manager.utils.Constants;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

//mvvm

public class MainViewModel extends AndroidViewModel {

    public MutableLiveData <RealmResults<Transaction>> transactions=new MutableLiveData<>();

    public MutableLiveData<Double> totalIncome=new MutableLiveData<>();
   public MutableLiveData<Double> totalExpense=new MutableLiveData<>();
    public MutableLiveData<Double> totalAmount=new MutableLiveData<>();

    Realm realm;

    Calendar calender;


    public MainViewModel(@NonNull Application application) {
        super(application);
        Realm.init(application);
        setepDatabase();
    }

    public void getTransaction(Calendar calendar){

        this.calender=calendar;
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        double income=0;
        double expense=0;
        double total=0;
        RealmResults <Transaction> newtransactions=null;


        if(Constants.SELECTED_TAB==Constants.DAILY){


       newtransactions=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() +(24*60*60*60*1000)))
                    .findAll();

            income=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() +(24*60*60*60*1000)))
                    .equalTo("type",Constants.INCOME)
                    .sum("amount").doubleValue();

            expense=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() +(24*60*60*60*1000)))
                    .equalTo("type",Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",new Date(calendar.getTime().getTime() +(24*60*60*60*1000)))
                    .sum("amount").doubleValue();




        } else if (Constants.SELECTED_TAB==Constants.MONTHLY) {


            calendar.set(Calendar.DAY_OF_MONTH,0);

            Date startTime=calendar.getTime();

               calendar.add(Calendar.MONTH,1);
            Date endTime=calendar.getTime();


          newtransactions=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",startTime)
                    .findAll();
            income=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",startTime)
                    .equalTo("type",Constants.INCOME)
                    .sum("amount").doubleValue();

            expense=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",startTime)
                    .equalTo("type",Constants.EXPENSE)
                    .sum("amount").doubleValue();

            total=realm.where(Transaction.class)
                    .greaterThanOrEqualTo("date",calendar.getTime())
                    .lessThan("date",startTime)
                    .sum("amount").doubleValue();


        }

        totalIncome.setValue(income);
        totalExpense.setValue(expense);
        totalAmount.setValue(total);
        transactions.setValue(newtransactions);

        // Select * from transactions
        // Select * from transactions where id = 5
//        RealmResults <Transaction> newtransactions=realm.where(Transaction.class)
//                .equalTo("date",calendar.getTime())
//                .findAll();



    }

    public void addTransactions(Transaction transaction){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(transaction);

        //some code here
        realm.commitTransaction();


    }

    public void deleteTransaction(Transaction transaction){
        realm.beginTransaction();
        transaction.deleteFromRealm();

        //some code here
        realm.commitTransaction();
        getTransaction(calender);


    }


    public void addTransactions(){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate((new Transaction(Constants.INCOME,"Business","Cash","Some note here",new Date(),500,new Date().getTime())));
        realm.copyToRealmOrUpdate((new Transaction(Constants.EXPENSE,"Investment","Cash","Some note here",new Date(),-900,new Date().getTime())));
        realm.copyToRealmOrUpdate((new Transaction(Constants.INCOME,"Rent","Other","Some note here",new Date(),500,new Date().getTime())));
        realm.copyToRealmOrUpdate((new Transaction(Constants.INCOME,"Business","Crd","Some note here",new Date(),500,new Date().getTime())));


        //some code here
        realm.commitTransaction();


    }

    void setepDatabase(){  //realm database implemantation

     ;
        realm=Realm.getDefaultInstance();

    }


}
