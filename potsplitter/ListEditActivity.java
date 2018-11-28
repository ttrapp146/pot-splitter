package com.ttrapp14622.potsplitter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListEditActivity extends AppCompatActivity {
    //Used to edit pot split list

    static ArrayList<String> stringList = new ArrayList();
    private PotSplitRecyclerViewAdapter potSplitAdapter;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialize item values for recycler view

        int i = 0;
        stringList = FileManager.load(this);
        productList = new ArrayList<>();
        final Resources resources = getResources();

        while (i < stringList.size() - 1) {
            productList.add(new Product(i, stringList.get(i)));
            i++;
        }

        RecyclerView splitList = (RecyclerView) findViewById(R.id.splitList);
        splitList.setLayoutManager(new LinearLayoutManager(this));

        //ArrayAdapter<String> potSplitAdapter = new ArrayAdapter<String>(ListEditActivity.this, R.layout.activity_list_item, stringList);

        potSplitAdapter = new PotSplitRecyclerViewAdapter(this, productList);
        splitList.setAdapter(potSplitAdapter);

        Button saveChangesButton = (Button) findViewById(R.id.saveChangesButton);
        Button cancelChangesButton = (Button) findViewById(R.id.cancelChangesButton);
        Button addNewSplitButton = (Button) findViewById(R.id.addNewSplitButton);


        //recreates file with new values.  adds option to edit list back to end of list, but option is not saved
        View.OnClickListener saveChanges = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringList.removeAll(stringList);
                int j = 0;
                while (j < productList.size()) {
                    stringList.add(productList.get(j).getSplit());
                    j++;
                }
                stringList.add(resources.getString(R.string.edit_split_list_option));
                FileManager.save(stringList, ListEditActivity.this);
                startActivity(new Intent(ListEditActivity.this, MainActivity.class));

            }
        };

        saveChangesButton.setOnClickListener(saveChanges);

        //navigates back to main activity
        View.OnClickListener cancelChanges = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListEditActivity.this, MainActivity.class));

            }
        };

        cancelChangesButton.setOnClickListener(cancelChanges);

        //creates alert to add new potsplit value
        View.OnClickListener addNewSplit = new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (productList.size() > 9)
                    Toast.makeText(ListEditActivity.this, resources.getString(R.string._max_splits), Toast.LENGTH_SHORT).show();
                else {

                    final View view = LayoutInflater.from(ListEditActivity.this).inflate(R.layout.activity_add_split_alert, null);

                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListEditActivity.this);
                    alertBuilder.setView(view);
                    final EditText userInput = (EditText) view.findViewById(R.id.addSplitInput);
                    final TextView errorMessages = (TextView) view.findViewById(R.id.addSplitErrorCheck);


                    final Dialog alert = alertBuilder.create();


                    alert.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {

                            Button addSplit = (Button) view.findViewById(R.id.addSplitButtonX);

                            addSplit.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    //checks if split already exists, checks for any issues with split, then adds split if no issues
                                    boolean splitExists = false;
                                    int k = 0;
                                    while (k < productList.size()) {
                                        if (userInput.getText().toString().matches(productList.get(k).getSplit()))
                                            splitExists = true;
                                        k++;
                                    }

                                    if (splitExists)
                                        errorMessages.setText(resources.getString(R.string.split_detected));
                                    else if (ErrorCheck.potSplitCheck(ListEditActivity.this, userInput.getText().toString().concat("/"), 999999999))
                                        errorMessages.setText(ErrorCheck.displayErrors());
                                    else {


                                        productList.add(new Product(0, userInput.getText().toString()));
                                        potSplitAdapter.notifyItemInserted(productList.size() - 1);
                                        potSplitAdapter.notifyItemRangeChanged(productList.size() - 1, 1);
                                        alert.dismiss();
                                    }
                                }
                            });
                        }
                    });

                    alert.show();


                }

            }
        };

        addNewSplitButton.setOnClickListener(addNewSplit);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //toolbar setup same as main.  could create single custom toolbar for easier code management
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final Resources resources = getResources();

        if (item.getItemId() == R.id.contactMenuItem) {
            final View view = LayoutInflater.from(ListEditActivity.this).inflate(R.layout.alert_contact_info, null);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListEditActivity.this);
            alertBuilder.setView(view);
            final TextView emailAddress = (TextView) findViewById(R.id.contactAlertEmail);

            alertBuilder.setCancelable(true).setPositiveButton(resources.getString(R.string.send_email_button_label), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(resources.getString(R.string.email_type));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.email_address)});
                    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject));

                    try {
                        startActivity(Intent.createChooser(intent, resources.getString(R.string.select_email_app_label)));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(ListEditActivity.this, resources.getString(R.string.no_email_installed_message), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            alertBuilder.setCancelable(true).setNegativeButton(resources.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            final Dialog alert = alertBuilder.create();
            alert.show();
        }

//        if (item.getItemId() == R.id.donateMenuItem) {
//            final View view = LayoutInflater.from(ListEditActivity.this).inflate(R.layout.alert_donate, null);
//
//            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListEditActivity.this);
//            alertBuilder.setView(view);
//            final TextView donateAddress = (TextView) findViewById(R.id.donateAlertAddress);
//
//            alertBuilder.setCancelable(true).setPositiveButton(resources.getString(R.string.donate_button), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.donate_address)));
//                    try {
//                        startActivity(Intent.createChooser(webIntent, resources.getString(R.string.select_web_app_label)));
//                    } catch (android.content.ActivityNotFoundException ex) {
//                        Toast.makeText(ListEditActivity.this, resources.getString(R.string.cannot_open_browser_message), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//            alertBuilder.setCancelable(true).setNegativeButton(resources.getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            });
//
//            final Dialog alert = alertBuilder.create();
//            alert.show();
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //sets back button to go to new instance of main activity so edit list is not selected
        startActivity(new Intent(ListEditActivity.this, MainActivity.class));
    }
}
