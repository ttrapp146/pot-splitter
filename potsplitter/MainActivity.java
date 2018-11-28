package com.ttrapp14622.potsplitter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    Spinner potSplitValue;
    EditText entrantsValue;
    EditText entreeFeeValue;
    EditText potBonusValue;
    TextView entrantsLabel;
    TextView entreeFeeLabel;
    TextView potBonusLabel;
    TextView potSplitLabel;
    Button splitPotButton;
    CheckBox showFormulaCheckbox;
    TextView errorMessagesText;

    int entrants;
    double entreeFee;
    double potBonus;
    double fightMoney = 0;
    String potSplit;
    String prizeDistribution = "";
    String mathFormula = "";
    String itemValue = "";
    double firstPrize = 0;
    double prizePercentage;
    double prizeTotalCheck = 0;
    double prizeTotal;
    int index = 0;
    int rank = 1;
    int mathPower = 1;
    int powerUseCount = 0;
    int prizeWinners = 0;
    boolean errorFound = false;
    boolean showFormula;


    ArrayList<String> potSplitList = new ArrayList<>();
    ArrayAdapter<String> potSplitAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, "ca-app-pub-5126780395628695~5196737233");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);


        potSplitValue = (Spinner) findViewById(R.id.potSplitValue);
        entrantsValue = (EditText) findViewById(R.id.entrantsValue);
        entreeFeeValue = (EditText) findViewById(R.id.entreeFeeValue);
        potBonusValue = (EditText) findViewById(R.id.potBonusValue);
        entrantsLabel = (TextView) findViewById(R.id.entrantsLabel);
        entreeFeeLabel = (TextView) findViewById(R.id.entreeFeeLabel);
        potBonusLabel = (TextView) findViewById(R.id.potBonusLabel);
        potSplitLabel = (TextView) findViewById(R.id.potSplitLabel);
        splitPotButton = (Button) findViewById(R.id.splitPotButton);
        showFormulaCheckbox = (CheckBox) findViewById(R.id.showFormulaCheckbox);
        errorMessagesText = (TextView) findViewById(R.id.errorDisplay);

        final Resources resources = getResources();

        entrantsLabel.setText(resources.getString(R.string.main_entrants_label));
        entreeFeeLabel.setText(resources.getString(R.string.main_entreefee_label));
        potBonusLabel.setText(resources.getString(R.string.main_potbonus_label));
        potSplitLabel.setText(resources.getString(R.string.main_potsplit_label));
        showFormulaCheckbox.setText(resources.getString(R.string.main_showformula_label));


        //set entree fee field to only accept money format

        entreeFeeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int index = 0;
                int maxChars = 20;
                String input = s.toString();
                StringBuilder stringBuilder = new StringBuilder();


                if (input.length() > 1 && input.startsWith("0")) {
                    if (!input.contains(".") || !input.startsWith("0.")) {
                        index = entreeFeeValue.getSelectionStart();
                        entreeFeeValue.setText(stringBuilder.append(input.substring(1)));
                        entreeFeeValue.setSelection(index - 1);
                    }

                }


                if (input.contains(".")) {

                    maxChars = s.toString().indexOf(".") + 3;
                    if (input.length() > maxChars) {
                        index = entreeFeeValue.getSelectionStart();
                        entreeFeeValue.setText(stringBuilder.append(input.substring(0, maxChars)));
                        entreeFeeValue.setSelection(maxChars);
                    }
                }


            }
        });

        //set potbonus field to only accept money format

        potBonusValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                int index = 0;
                int maxChars = 20;
                String input = s.toString();
                StringBuilder stringBuilder = new StringBuilder();

                if (input.length() > 1 && input.startsWith("0")) {
                    if (!input.contains(".") || !input.startsWith("0.")) {
                        index = potBonusValue.getSelectionStart();
                        potBonusValue.setText(stringBuilder.append(input.substring(1)));
                        potBonusValue.setSelection(index - 1);
                    }

                }


                if (input.contains(".")) {

                    maxChars = s.toString().indexOf(".") + 3;
                    if (input.length() > maxChars) {
                        index = potBonusValue.getSelectionStart();
                        potBonusValue.setText(stringBuilder.append(input.substring(0, maxChars)));
                        potBonusValue.setSelection(maxChars);
                    }
                }

            }
        });

        //loads saved potsplits or creates file if file dne

        if (!FileManager.check(MainActivity.this)) {

            potSplitList.add(getString(R.string.splitlist_initialvalue));
            potSplitList.add("60/30/10");
            potSplitList.add("70/20/10");
            potSplitList.add("60/20/10/4/2/1");
            potSplitList.add(resources.getString(R.string.edit_split_list_option));

            FileManager.save(potSplitList, this);

        } else {
            potSplitList = FileManager.load(this);
        }

        //setup array of potsplits

        potSplitAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, potSplitList);

        potSplitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.potSplitValue.setAdapter(potSplitAdapter);

        this.potSplitValue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemValue = parent.getItemAtPosition(position).toString();


                if (!itemValue.contains(getString(R.string.splitlist_initialvalue)) && potSplitList.contains(getString(R.string.splitlist_initialvalue))) {
                    potSplitList.remove(potSplitList.indexOf(getString(R.string.splitlist_initialvalue)));
                    potSplitAdapter.notifyDataSetChanged();
                    parent.setSelection(position - 1);
                    FileManager.save(potSplitList, MainActivity.this);
                }

                //moves to pot list edit activity
                if (itemValue.contentEquals(resources.getString(R.string.edit_split_list_option))) {
                    startActivity(new Intent(MainActivity.this, ListEditActivity.class));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        View.OnClickListener calculatePot = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //assign some values to 0 if left empty
                if (TextUtils.isEmpty(entrantsValue.getText()))
                    entrants = 0;
                else
                    entrants = Integer.valueOf(entrantsValue.getText().toString());

                if (TextUtils.isEmpty(entreeFeeValue.getText()))
                    entreeFee = 0;
                else
                    entreeFee = Double.valueOf(entreeFeeValue.getText().toString());

                if (TextUtils.isEmpty(potBonusValue.getText()))
                    potBonus = 0;
                else
                    potBonus = Double.valueOf(potBonusValue.getText().toString());

                if (showFormulaCheckbox.isChecked())
                    showFormula = true;
                else
                    showFormula = false;

                index = 0;
                rank = 1;
                mathPower = 1;
                powerUseCount = 0;
                prizeWinners = 0;
                prizeTotalCheck = 0;
                prizeDistribution = "";
                mathFormula = "";
                firstPrize = 0;
                fightMoney = 0;
                prizePercentage = 0;

                prizeTotal = entrants * entreeFee + potBonus;

                potSplit = itemValue;

                // / needed to make sure loop runs for last prize distribution
                if (potSplit.endsWith("/") == false)
                    potSplit = potSplit.concat("/");


                //check that needed information is entered and calculate prizes
                if (entrants == 0)
                    //errorMessagesText.setText("Error:  Enter number of entrants");
                    errorMessagesText.setText(resources.getString(R.string.no_entrants_error));
                else if (potBonus == 0 && entreeFee == 0)
                    //errorMessagesText.setText("Error:  Enter pot bonus or entree fee");
                    errorMessagesText.setText(resources.getString(R.string.no_fight_money_error));
                else if (ErrorCheck.potSplitCheck(MainActivity.this, potSplit, entrants))
                    errorMessagesText.setText(resources.getString(R.string.main_errormessages_text, ErrorCheck.displayErrors()));
                else {
                    errorMessagesText.setText("");
                    while (potSplit.contains("/")) {
                        index = potSplit.indexOf('/');

                        prizePercentage = Double.valueOf(potSplit.substring(0, index));
                        fightMoney = Math.round(prizeTotal * prizePercentage);
                        fightMoney /= 100;

                        // separate rank 1 separately to add any missing $$$ due to rounding
                        // to rank 1 winnings if any
                        if (rank == 1) {
                            firstPrize += fightMoney;
                            prizeTotalCheck += fightMoney;
                            rank++;
                        }

                        // calculate rank 1-4 separately since ranks 5 and below have ties
                        // to account for
                        else if (rank < 5) {
                            if (rank % 10 == 2)
                                prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.nd_place) + df.format(fightMoney) + "\n");
                            else if (rank % 10 == 3)
                                prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.rd_place) + df.format(fightMoney) + "\n");
                            else
                                prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.th_place) + df.format(fightMoney) + "\n");
                            mathFormula = mathFormula.concat(" + " + df.format(fightMoney));
                            prizeWinners = rank;
                            rank++;
                            prizeTotalCheck += fightMoney;
                        }
                        // calculate ranks 5 and below
                        else {
                            if (powerUseCount < 2) {
                                powerUseCount++;
                                prizeTotalCheck += fightMoney * Math.pow(2, mathPower);
                                prizeWinners = (int) (rank - 1 + Math.pow(2, mathPower));
                                if (rank % 10 == 1 && rank % 100 != 11)
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.st_place) + df.format(fightMoney) + "\n");
                                else if (rank % 10 == 2 && rank % 100 != 12)
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.nd_place) + df.format(fightMoney) + "\n");
                                else if (rank % 10 == 3 && rank % 100 != 13)
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.rd_place) + df.format(fightMoney) + "\n");
                                else
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.th_place) + df.format(fightMoney) + "\n");
                                mathFormula = mathFormula.concat(" + (" + df.format(fightMoney) + " * " + (int) Math.pow(2, mathPower) + ")");
                                rank = (int) (rank + Math.pow(2, mathPower));
                            } else {
                                mathPower++;
                                prizeWinners = (int) (rank - 1 + Math.pow(2, mathPower));
                                prizeTotalCheck += fightMoney * Math.pow(2, mathPower);
                                if (rank % 10 == 1 && rank % 100 != 11)
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.st_place) + df.format(fightMoney) + "\n");
                                else if (rank % 10 == 2 && rank % 100 != 12)
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.nd_place) + df.format(fightMoney) + "\n");
                                else if (rank % 10 == 3 && rank % 100 != 13)
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.rd_place) + df.format(fightMoney) + "\n");
                                else
                                    prizeDistribution = prizeDistribution.concat(rank + resources.getString(R.string.th_place) + df.format(fightMoney) + "\n");
                                mathFormula = mathFormula.concat(" + (" + df.format(fightMoney) + " * " + (int) Math.pow(2, mathPower) + ")");
                                rank = (int) (rank + Math.pow(2, mathPower));
                                powerUseCount = 1;
                            }

                        }
                        potSplit = potSplit.substring(index + 1);
                    }

                    //will make sure there is enough money for all winners.  unlikely to happen and users may want to distribute whatever is available
//                    if (prizeTotal / 100 * prizePercentage < 0.01)
//                        errorMessagesText.setText("Error:  Not enough money...");
//                    else {


                    DisplayResults.formatResults(MainActivity.this, prizeTotalCheck, prizeWinners, firstPrize, prizeDistribution, entrants, entreeFee, potBonus, prizeTotal, potSplitValue.getSelectedItem().toString(), mathFormula, showFormula);
                    startActivity(new Intent(MainActivity.this, ResultsActivity.class));
                    //}
                }


            }
        };
        splitPotButton.setOnClickListener(calculatePot);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Setup options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final Resources resources = getResources();

        if (item.getItemId() == R.id.contactMenuItem) {
            final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_contact_info, null);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
            alertBuilder.setView(view);
            final TextView emailAddress = (TextView) findViewById(R.id.contactAlertEmail);

            alertBuilder.setCancelable(true).setPositiveButton(resources.getString(R.string.send_email_button_label), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType(resources.getString(R.string.email_type));
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{resources.getString(R.string.email_address)});
                    intent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.email_subject));

                    try {
                        startActivity(Intent.createChooser(intent, resources.getString(R.string.select_email_app_label)));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, resources.getString(R.string.no_email_installed_message), Toast.LENGTH_SHORT).show();
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
//            final View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert_donate, null);
//
//            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
//            alertBuilder.setView(view);
//            final TextView donateAddress = (TextView) findViewById(R.id.donateAlertAddress);
//
//            alertBuilder.setCancelable(true).setPositiveButton(resources.getString(R.string.donate_button), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                    Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.donate_address)));
//                    try {
//                        startActivity(Intent.createChooser(webIntent, resources.getString(R.string.select_web_app_label)));
//                    } catch (android.content.ActivityNotFoundException ex) {
//                        Toast.makeText(MainActivity.this, resources.getString(R.string.cannot_open_browser_message), Toast.LENGTH_SHORT).show();
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

}


