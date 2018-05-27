package com.chrissetiana.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked
     */
    public void increment(View view) {
        if (quantity == 100) {
            // Show an error message as toast
            Toast.makeText(this, "You cannot have more than 100 cup of coffee", Toast.LENGTH_SHORT).show();
            // Exit method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked
     */
    public void decrement(View view) {
        if (quantity == 1) {
            // Show an error message as toast
            Toast.makeText(this, "You cannot have less than 1 cup of coffee", Toast.LENGTH_SHORT).show();
            // Exit method early because there's nothing left to do
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Get user name
        EditText text = findViewById(R.id.name_field);
        String name = text.getText().toString();

        // Figure out if the user wants whipped cream topping
        CheckBox whippedCreamCheckbox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckbox.isChecked();

        // Figure out if the user wants chocolate topping
        CheckBox chocolateCheckbox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckbox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = (createOrderSummary(name, price, hasWhippedCream, hasChocolate));

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        // Only email apps should handle this
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Just Java order for " + name);
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order based on the current quantity.
     */
    private int calculatePrice(boolean addWhippedCream, boolean addChocolate) {
        int basePrice = 5;

        // Add $1 if the user wants whipped cream
        if (addWhippedCream) {
            basePrice += 1;
        }

        // Add $2 if the user wants chocolate
        if (addChocolate) {
            basePrice += 2;
        }

        // Calculate the total order price by multiplying by quantity
        return quantity * basePrice;
    }

    /**
     * This method displays the order summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = "Name: " + getString(R.string.order_summary_name, name) +
                "\nAdd whipped cream? " + addWhippedCream +
                "\nAdd chocolate? " + addChocolate +
                "\nQuantity: " + quantity +
                "\nPrice: $" + price +
                "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /*getString(R.string.order_summary_name, name)
    getString(R.string.order_summary_whipped_cream, addWhippedCream)
    getString(R.string.order_summary_chocolate, addChocolate)
    getString(R.string.order_summary_quantity, quantity)
    getString(R.string.order_summary_price,
     NumberFormat.getCurrencyInstance().format(price))
    getString(R..string.thank_you)*/

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }
}