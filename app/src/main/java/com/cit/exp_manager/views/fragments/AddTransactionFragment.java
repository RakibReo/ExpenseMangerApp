package com.cit.exp_manager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.cit.exp_manager.R;
import com.cit.exp_manager.adapters.AccountsAdapter;
import com.cit.exp_manager.adapters.CategoryAdapter;
import com.cit.exp_manager.databinding.FragmentAddTransactionBinding;
import com.cit.exp_manager.databinding.ListDialogBinding;
import com.cit.exp_manager.models.Account;
import com.cit.exp_manager.models.Category;
import com.cit.exp_manager.models.Transaction;
import com.cit.exp_manager.utils.Constants;
import com.cit.exp_manager.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTransactionFragment extends BottomSheetDialogFragment {    //has changed


    public AddTransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }
 FragmentAddTransactionBinding binding;
    Transaction transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddTransactionBinding.inflate(inflater);

     transaction=new Transaction();

        binding.incomeBtn.setOnClickListener(v->{

        binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
    binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
    binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));

    transaction.setType(Constants.INCOME);

        });


        binding.expenseBtn.setOnClickListener(v->{

            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.green));

            transaction.setType(Constants.EXPENSE);

        });

        binding.date.setOnClickListener(v->{

            DatePickerDialog  datePickerDialog=new DatePickerDialog(getContext());
             datePickerDialog.setOnDateSetListener((datePicker, year, month, dayOfMonth) -> {

                 Calendar calendar=Calendar.getInstance();
                  calendar.set(Calendar.DAY_OF_MONTH,datePicker.getDayOfMonth());
                  calendar.set(Calendar.MONTH,datePicker.getMonth());
                  calendar.set(Calendar.YEAR,datePicker.getYear());

                 SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM, yyyy");
                String dateToShow=dateFormat.format(calendar.getTime());
                 binding.date.setText(dateToShow);
                   transaction.setId(calendar.getTime().getTime());
                 transaction.setDate(calendar.getTime());
             });
            datePickerDialog.show();



        });

        binding.category.setOnClickListener(c->{

            ListDialogBinding dialogBinding=ListDialogBinding.inflate(inflater);
            AlertDialog categoryDialog=new AlertDialog.Builder(getContext()).create();
            categoryDialog.setView(dialogBinding.getRoot());




            CategoryAdapter categoryAdapter=new CategoryAdapter(getContext(), Constants.categoryList, new CategoryAdapter.CategoryClickListener() {
                @Override
                public void onCategoryClicked(Category category) {
                    binding.category.setText(category.getCategoryName());
                    transaction.setCategory(category.getCategoryName());
                    categoryDialog.dismiss();
                }
            });
            dialogBinding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            dialogBinding.recyclerView.setAdapter(categoryAdapter);

            categoryDialog.show();

        });

        binding.account.setOnClickListener(c->{
            ListDialogBinding dialogBinding=ListDialogBinding.inflate(inflater);
            AlertDialog accountDialog=new AlertDialog.Builder(getContext()).create();
            accountDialog.setView(dialogBinding.getRoot());

            ArrayList <Account> accountList=new ArrayList<>();
            accountList.add(new Account(0,"Cash"));

            accountList.add(new Account(0,"Bank"));

            accountList.add(new Account(0,"Bkash"));

            accountList.add(new Account(0,"Nagad"));

            accountList.add(new Account(0,"Other"));


            AccountsAdapter accountsAdapter= new AccountsAdapter(getContext(), accountList, new AccountsAdapter.AccountsClickListener() {
                @Override
                public void onAccountSelected(Account account) {
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountDialog.dismiss();
                }
            });

            dialogBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            //Divider line
            dialogBinding.recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
            dialogBinding.recyclerView.setAdapter(accountsAdapter);
            accountDialog.show();


        });
        binding.saveTransactionBtn.setOnClickListener(v->{

   double amount= Double.parseDouble(binding.amount.getText().toString());
   String note=binding.note.getText().toString();

   if(transaction.getType().equals(Constants.EXPENSE)){
       transaction.setAmount(amount*-1);

   }else{
       transaction.setAmount(amount);

   }


   transaction.setNote(note);
            ((MainActivity)getActivity()).viewModel.addTransactions(transaction);
            ((MainActivity)getActivity()).getTransaction();
            dismiss();

        });

        return binding.getRoot();
    }
}