package com.example.deathcalculator;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static java.time.temporal.ChronoUnit.*;


/**
 * Jared Haeme
 * 3/12/2019
 * This program calculates a persons age given their birth or death date or can calculate a birth or death date given an age and one either of the dates
 */
public class MainActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Calculates the age, birth date or death date when the calculate button is clicked
     * @param view view for calculate button
     */
    public void onClick(View view) {
        EditText txtBornD = (EditText) findViewById(R.id.txtBornDay);
        EditText txtBornM = (EditText) findViewById(R.id.txtBornMonth);
        EditText txtBornY = (EditText) findViewById(R.id.txtBornYear);
        EditText txtDeathD = (EditText) findViewById(R.id.txtDeathDay);
        EditText txtDeathM = (EditText) findViewById(R.id.txtDeathMonth);
        EditText txtDeathY = (EditText) findViewById(R.id.txtDeathYear);
        EditText txtAgeD = (EditText) findViewById(R.id.txtAgeDays);
        EditText txtAgeY = (EditText) findViewById(R.id.txtAgeYear);
        TextView txtResult=(TextView) findViewById(R.id.txtResult);

        String strBornD=txtBornD.getText().toString();
        String strBornM=txtBornM.getText().toString();
        String strBornY=txtBornY.getText().toString();
        String strDeathD=txtDeathD.getText().toString();
        String strDeathM=txtDeathM.getText().toString();
        String strDeathY=txtDeathY.getText().toString();
        String strAgeY=txtAgeY.getText().toString();
        String strAgeD=txtAgeD.getText().toString();
        if(tryParseInt(strBornD) && tryParseInt(strBornM) &&tryParseInt(strBornY) && tryParseInt(strDeathD) && tryParseInt(strDeathM) && tryParseInt(strDeathY) ){
            int bornD=Integer.parseInt(strBornD);
            int bornM=Integer.parseInt(strBornM);
            int bornY=Integer.parseInt(strBornY);
            int deathD=Integer.parseInt(strDeathD);
            int deathM=Integer.parseInt(strDeathM);
            int deathY=Integer.parseInt(strDeathY);
            if(isValidDate(bornM, bornD, bornY)) {
                LocalDate birthDate = LocalDate.of(bornY, bornM, bornD);
                if(isValidDate(deathM, deathD, deathY)) {
                    LocalDate deathDate = LocalDate.of(deathY, deathM, deathD);
                    calcAge(birthDate, deathDate, txtResult);
                }else{
                    txtResult.setTextColor(Color.RED);
                    txtResult.setText("Invalid death date.");
                    txtResult.setVisibility(View.VISIBLE);
                }
            }else {
                txtResult.setTextColor(Color.RED);
                txtResult.setText("Invalid birth date.");
                txtResult.setVisibility(View.VISIBLE);
            }




        }else if(tryParseInt(strBornD) && tryParseInt(strBornM) &&tryParseInt(strBornY) && tryParseInt(strAgeY)&& tryParseInt(strAgeD)){
            int bornD=Integer.parseInt(strBornD);
            int bornM=Integer.parseInt(strBornM);
            int bornY=Integer.parseInt(strBornY);
            if(isValidDate(bornM, bornD, bornY)){
                LocalDate birthDate = LocalDate.of(bornY, bornM, bornD);
                int ageD=Integer.parseInt(strAgeD);
                int ageY=Integer.parseInt(strAgeY);
                calcDeathDate(birthDate,(int)(ageY*365.25+ageD), txtResult);
            }else{
                txtResult.setTextColor(Color.RED);
                txtResult.setText("Invalid birth date.");
                txtResult.setVisibility(View.VISIBLE);
            }


        }else if(tryParseInt(strDeathD) && tryParseInt(strDeathM) &&tryParseInt(strDeathY) && tryParseInt(strAgeY)&& tryParseInt(strAgeD)){
            int deathD=Integer.parseInt(strDeathD);
            int deathM=Integer.parseInt(strDeathM);
            int deathY=Integer.parseInt(strDeathY);
            if(isValidDate(deathM, deathD, deathY)){
                LocalDate deathDate = LocalDate.of(deathY, deathM, deathD);
                int ageD = Integer.parseInt(strAgeD);
                int ageY = Integer.parseInt(strAgeY);
                calcBirthDate(deathDate, (int)(ageY * 365.25 + ageD), txtResult);
            }else{
                txtResult.setTextColor(Color.RED);
                txtResult.setText("Invalid death date.");
                txtResult.setVisibility(View.VISIBLE);
            }
        }else{
            txtResult.setTextColor(Color.RED);
            txtResult.setText("Invalid entry.");
            txtResult.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Calculates the age given the birth and death date
     * @param birthDate the day the person was born
     * @param deathDate the day the person died
     * @param txtResult the text field where the answer is displayed
     */
    public void calcAge(LocalDate birthDate, LocalDate deathDate, TextView txtResult){
        boolean validInput=true;
        if(birthDate.isAfter(deathDate)){
            validInput=false;
            txtResult.setTextColor(Color.RED);
            txtResult.setText("Invalid entry. Birth date after death date");
            txtResult.setVisibility(View.VISIBLE);
        }
        //From kyle workmen on lovelace
        long years = birthDate.until(deathDate, YEARS);
        birthDate = birthDate.plusYears(years);
        long days = birthDate.until(deathDate, DAYS);
        if(validInput) {
            txtResult.setTextColor(Color.BLACK);
            txtResult.setText("This person is " + years + " years and " + days + " days old.");
            txtResult.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Calculates the date of birth given the death date and number of days lived and displays it
     * @param deathDate day the person died
     * @param daysLived the number of days the person lived
     * @param txtResult the textview where the result is displayed
     */
    public void calcBirthDate(LocalDate deathDate, int daysLived, TextView txtResult){

        LocalDate birthDate=deathDate.minusDays(daysLived);
        txtResult.setTextColor(Color.BLACK);
        txtResult.setText("Birth date was  "+birthDate.getMonth()+" "+birthDate.getDayOfMonth()+", "+birthDate.getYear());
        txtResult.setVisibility(View.VISIBLE);

    }

    /**
     * Calculates the death date given the birth date and the number of days lived and displays the answer
     * @param birthDate
     * @param daysLived
     * @param txtResult
     */
    public void calcDeathDate(LocalDate birthDate, int daysLived, TextView txtResult){

        LocalDate deathDate=birthDate.plusDays(daysLived);
        txtResult.setTextColor(Color.BLACK);
        txtResult.setText("Death date was "+deathDate.getMonth()+" "+deathDate.getDayOfMonth()+", "+deathDate.getYear());
        txtResult.setVisibility(View.VISIBLE);
    }

    /**
     * checks if a num can be converted to an int
     * @param value a string
     * @return
     */
    // got from https://stackoverflow.com/questions/8391979/does-java-have-a-int-tryparse-that-doesnt-throw-an-exception-for-bad-data
    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Clears all text in the views
     * @param view the clear button that clears the fields when clicked
     */
    public void clear(View view){
        EditText txtBornD = (EditText) findViewById(R.id.txtBornDay);
        EditText txtBornM = (EditText) findViewById(R.id.txtBornMonth);
        EditText txtBornY = (EditText) findViewById(R.id.txtBornYear);
        EditText txtDeathD = (EditText) findViewById(R.id.txtDeathDay);
        EditText txtDeathM = (EditText) findViewById(R.id.txtDeathMonth);
        EditText txtDeathY = (EditText) findViewById(R.id.txtDeathYear);
        EditText txtAgeD = (EditText) findViewById(R.id.txtAgeDays);
        EditText txtAgeY = (EditText) findViewById(R.id.txtAgeYear);
        TextView txtResult=(TextView) findViewById(R.id.txtResult);

        txtBornD.setText("");
        txtBornM.setText("");
        txtBornY.setText("");
        txtDeathD.setText("");
        txtDeathM.setText("");
        txtDeathY.setText("");
        txtAgeD.setText("");
        txtAgeY.setText("");
        txtResult.setText("");
        txtResult.setVisibility(View.GONE);

    }

    /**
     * Checks if a given date is valid
     * @param month
     * @param day
     * @param year
     * @return
     */
    public boolean isValidDate(int month, int day, int year){

        if (year>=0&& month>=1 && month<=12){
            // got code from https://stackoverflow.com/questions/1021324/java-code-for-calculating-leap-year/1021373#1021373
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);

            if((month==1 || month==3 || month==5 || month==7 || month==8 || month==10 || month==12) && (day>=1 && day<=31)){
                return true;
            }else if((month==4 || month==6 || month==9 || month==11) && (day>=1 && day<=30)){
                return true;
            }else if((month==2 && day>=1&& day<=28) || (month==2 && cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365 && day>=1 && day<=29) ){
                return true;
            }
        }
        return false;
    }

}