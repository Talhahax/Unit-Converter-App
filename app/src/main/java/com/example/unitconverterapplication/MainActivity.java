package com.example.unitconverterapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.text.TextWatcher;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // declaring variables
    private Spinner mainUnitSpinner;
    private Spinner inputUnitSpinner;
    private Spinner resultUnitSpinner;
    private EditText inputValueEditText;
    private TextView resultTextView;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // initializing main select unit spinner
        mainUnitSpinner = findViewById(R.id.spinner_select_unit);
        inputUnitSpinner = findViewById(R.id.select_input_unit);
        resultUnitSpinner = findViewById(R.id.select_result_unit);
        inputValueEditText = findViewById(R.id.get_input_value);
        resultTextView = findViewById(R.id.get_result_value);
        resetButton = findViewById(R.id.btn_reset);

        // Setting up main unit spinner
        ArrayAdapter<CharSequence> mainUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.select_unit, android.R.layout.simple_spinner_item);
        mainUnitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mainUnitSpinner.setAdapter(mainUnitAdapter);
        mainUnitSpinner.setOnItemSelectedListener(this);

        if (inputValueEditText != null){
            // Adding TextWatcher to input value EditText
            inputValueEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No action performed before text changed
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // No action necessary while text is changing
                    performConversion();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset input value and result value to zero
                inputValueEditText.setText("");
                resultTextView.setText("");
            }
        });

        resultUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                performConversion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int unitsArrayResId;

        switch (position) {
            case 0: // Length
                unitsArrayResId = R.array.length_units;
                break;
            case 1: // Mass
                unitsArrayResId = R.array.mass_units;
                break;
            case 2: // Temperature
                unitsArrayResId = R.array.temperature_units;
                break;
            case 3: // Volume
                unitsArrayResId = R.array.volume_units;
                break;

            default:
                unitsArrayResId = R.array.length_units;
        }
        updateUnitSpinners(unitsArrayResId);

        performConversion();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    private void updateUnitSpinners(int unitsArrayResId) {
        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(this,
                unitsArrayResId, android.R.layout.simple_spinner_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputUnitSpinner.setAdapter(unitAdapter);
        resultUnitSpinner.setAdapter(unitAdapter);
    }

    private void performConversion(){
        if (mainUnitSpinner == null || mainUnitSpinner.getSelectedItem() == null ||
                inputUnitSpinner == null || inputUnitSpinner.getSelectedItem() == null ||
                resultUnitSpinner == null || resultUnitSpinner.getSelectedItem() == null) {
            resultTextView.setText("Invalid selection");
            return;
        }
        String mainUnit = mainUnitSpinner.getSelectedItem().toString();
        String inputUnit = inputUnitSpinner.getSelectedItem().toString();
        String resultUnit = resultUnitSpinner.getSelectedItem().toString();
        String inputValueString = inputValueEditText.getText().toString();

        if(inputValueString.isEmpty()){
            resultTextView.setText("");
            return;
        }

        double inputValue;
        try {
            inputValue = Double.parseDouble(inputValueString);
        } catch (NumberFormatException e) {
            resultTextView.setText("Invalid input");
            return;
        }

        double resultValue = 0;

        switch (mainUnit){
            case "Length":
                LengthConverter lengthConverter = new LengthConverter();
                resultValue = lengthConverter.convert(inputUnit, resultUnit, inputValue);
                break;
            case "Mass":
                MassConverter massConverter = new MassConverter();
                resultValue = massConverter.convert(inputUnit, resultUnit, inputValue);
                break;
            case "Temperature":
                TemperatureConverter temperatureConverter = new TemperatureConverter();
                resultValue = temperatureConverter.convert(inputUnit, resultUnit, inputValue);
                break;
            case "Volume":
                VolumeConverter volumeConverter = new VolumeConverter();
                resultValue = volumeConverter.convert(inputUnit, resultUnit, inputValue);
                break;
        }
        resultTextView.setText(String.valueOf(resultValue));
    }
}
class LengthConverter{
    double meterToKilometer(double value){
        return value / 1000;
    }
    double meterToCentimeter(double value){
        return value * 100;
    }
    double kilometerToMeter(double value){
        return value * 1000;
    }
    double kilometerToCentimeter(double value){
        return value * 100000;
    }
    double centimeterToMeter(double value){
        return value / 100;
    }
    double centimeterToKilometer(double value){
        return value / 100000;
    }

    double convert(String fromUnit, String toUnit, double value){
        switch (fromUnit){
            case "Meters":
                switch (toUnit){
                    case "Kilometers":
                        return meterToKilometer(value);
                    case "Centimeters":
                        return  meterToCentimeter(value);
                }
                break;
            case "Kilometers":
                switch (toUnit){
                    case "Meters":
                        return kilometerToMeter(value);
                    case "Centimeters":
                        return kilometerToCentimeter(value);
                }
                break;
            case "Centimeters":
                switch (toUnit){
                    case "Meters":
                        return centimeterToMeter(value);
                    case "Kilometers":
                        return centimeterToKilometer(value);
                }
                break;
        }
        return value;
    }
}
class MassConverter{
    double gramsToKilograms(double value){
        return value / 1000;
    }
    double gramsToPounds(double value){
        return value * 0.00220462;
    }
    double kilogramsToGrams(double value){
        return value * 1000;
    }
    double kilogramsToPounds(double value){
        return value * 2.20462;
    }
    double poundsToGrams(double value){
        return value * 453.592;
    }
    double poundsToKilograms(double value){
        return value * 0.453592;
    }
    double convert(String fromUnit, String toUnit, double value){
        switch (fromUnit){
            case "Grams":
                switch (toUnit){
                    case "Pounds":
                        return gramsToPounds(value);
                    case "Kilograms":
                        return gramsToKilograms(value);
                }
                break;
            case "Pounds":
                switch (toUnit){
                    case "Grams":
                        return poundsToGrams(value);
                    case "Kilograms":
                        return poundsToKilograms(value);
                }
                break;
            case "Kilograms":
                switch (toUnit){
                    case "Grams":
                        return kilogramsToGrams(value);
                    case "Pounds":
                        return kilogramsToPounds(value);
                }
                break;
        }
        return value;
    }
}
class TemperatureConverter{
    double celsiusToFahrenheit(double value){
        return value * 9/5 + 32;
    }
    double celsiusToKelvin(double value){
        return value + 273.15;
    }
    double fahrenheitToCelsius(double value){
        return (value - 32) * 5/9;
    }
    double fahrenheitToKelvin(double value){
        return (value - 32) * 5/9 + 273.15;
    }
    double kelvinToCelsius(double value){
        return value - 273.15;
    }
    double kelvinToFahrenheit(double value){
        return (value - 273.15) * 9/5 + 32;
    }

    double convert(String fromUnit, String toUnit, double value){
        switch (fromUnit){
            case "Celsius":
                switch (toUnit){
                    case "Fahrenheit":
                        return celsiusToFahrenheit(value);
                    case "Kelvin":
                        return celsiusToKelvin(value);
                }
                break;
            case "Fahrenheit":
                switch (toUnit){
                    case "Celsius":
                        return fahrenheitToCelsius(value);
                    case "Kelvin":
                        return fahrenheitToKelvin(value);
                }
                break;
            case "Kelvin":
                switch (toUnit){
                    case "Celsius":
                        return kelvinToCelsius(value);
                    case "Fahrenheit":
                        return kelvinToFahrenheit(value);
                }
                break;

        }
        return value;
    }
}

class VolumeConverter{
    double litersToMilliliters(double value){
        return value * 1000;
    }
    double litersToUSGallons(double value){
        return value / 3.78541;
    }
    double millilitersToLiters(double value){
        return value / 1000;
    }
    double millilitersToUSGallons(double value){
        return value / 3785.41;
    }
    double usGallonsToLiters(double value){
        return value * 3.78541;
    }
    double usGallonsToMilliliters(double value){
        return value * 3785.41;
    }

    double convert(String fromUnit, String toUnit, double value){
        switch (fromUnit){
            case "Liters":
                switch (toUnit){
                    case "Milliliters":
                        return litersToMilliliters(value);
                    case "US Gallons":
                        return litersToUSGallons(value);
                }
                break;
            case "Milliliters":
                switch (toUnit){
                    case "Liters":
                        return millilitersToLiters(value);
                    case "US Gallons":
                        return millilitersToUSGallons(value);
                }
                break;
            case "US Gallons":
                switch (toUnit){
                    case "Liters":
                        return usGallonsToLiters(value);
                    case "Milliliters":
                        return usGallonsToMilliliters(value);
                }
                break;
        }

        return value;
    }
}