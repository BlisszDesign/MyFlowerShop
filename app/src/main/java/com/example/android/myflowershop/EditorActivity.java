package com.example.android.myflowershop;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.myflowershop.data.FlowersContract;
import com.example.android.myflowershop.data.Utilities;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentFlower;
    private ImageView flowerImage;
    private Spinner flowerCategorySpinner;
    private EditText flowerPriceEditText;
    private EditText flowerQuantityEditText;
    private EditText flowerNameEditText;
    private EditText flowerDescEditText;
    private EditText supplierNameEditText;
    private EditText supplierPhoneEditText;
    private Button saveFlower;

    public static int mType = FlowersContract.FlowersEntry.TYPE_CALLA;

    private boolean flowerHasChanged = false;

    private static final int EXISTING_PRODUCT_LOADER = 0;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            flowerHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_flower);

        Intent intent = getIntent();
        mCurrentFlower = intent.getData();

        if (mCurrentFlower == null) {

            setTitle(R.string.editor_add_flower);

            invalidateOptionsMenu();
        } else {

            setTitle(R.string.editor_edit_flower);
            getSupportLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
//
        }

        flowerImage = findViewById(R.id.image);
        flowerCategorySpinner = findViewById(R.id.category_name);
        flowerPriceEditText = findViewById(R.id.price);
        flowerQuantityEditText = findViewById(R.id.quantity);
        flowerNameEditText = findViewById(R.id.product_name);
        flowerDescEditText = findViewById(R.id.description);
        supplierNameEditText = findViewById(R.id.supplier_name);
        supplierPhoneEditText = findViewById(R.id.supplier_phonenumber);
        saveFlower = findViewById(R.id.button_save);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFlower();
            }
        };

        saveFlower.setOnClickListener(buttonListener);

        flowerCategorySpinner.setOnTouchListener(mTouchListener);
        flowerPriceEditText.setOnTouchListener(mTouchListener);
        flowerQuantityEditText.setOnTouchListener(mTouchListener);
        flowerNameEditText.setOnTouchListener(mTouchListener);
        flowerDescEditText.setOnTouchListener(mTouchListener);
        supplierNameEditText.setOnTouchListener(mTouchListener);
        supplierPhoneEditText.setOnTouchListener(mTouchListener);

        setupSpinner();

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {FlowersContract.FlowersEntry._ID,
                FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE,
                FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME,
                FlowersContract.FlowersEntry.COLUMN_PRODUCT_DESCRIPTION,
                FlowersContract.FlowersEntry.COLUMN_PRICE,
                FlowersContract.FlowersEntry.COLUMN_QUANTITY,
                FlowersContract.FlowersEntry.COLUMN_SUPPLIER_NAME,
                FlowersContract.FlowersEntry.COLUMN_SUPPLIER_PHONE_NUMBER};

        String selection = FlowersContract.FlowersEntry._ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mCurrentFlower))};
        CursorLoader loader = new CursorLoader(this, FlowersContract.FlowersEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        data.moveToFirst();
        int typeColumnIndex = data.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE);
        int flowersNameColumnIndex = data.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME);
        int flowersDescColumnIndex = data.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_PRODUCT_DESCRIPTION);
        int priceColumnIndex = data.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_PRICE);
        int quantityColumnIndex = data.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_QUANTITY);
        int supplierNameColumnIndex = data.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_NAME);
        int supplierPhoneColumnIndex = data.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_PHONE_NUMBER);

        int type = data.getInt(typeColumnIndex);
        Utilities.ImageId imageId = Utilities.getImage(type);

        String flowersName = data.getString(flowersNameColumnIndex);
        String flowersDesc = data.getString(flowersDescColumnIndex);
        int price = data.getInt(priceColumnIndex);
        int quantity = data.getInt(quantityColumnIndex);
        String supplierName = data.getString(supplierNameColumnIndex);
        String supplierPhone = data.getString(supplierPhoneColumnIndex);

        flowerImage.setImageResource(imageId.getmImageId());
        flowerPriceEditText.setText(String.valueOf(price));
        flowerQuantityEditText.setText(String.valueOf(quantity));
        flowerNameEditText.setText(flowersName);
        flowerDescEditText.setText(flowersDesc);
        supplierNameEditText.setText(supplierName);
        supplierPhoneEditText.setText(supplierPhone);

        switch (type) {
            case FlowersContract.FlowersEntry.TYPE_ROSE:
                flowerCategorySpinner.setSelection(1);
                break;
            case FlowersContract.FlowersEntry.TYPE_ORCHIDS:
                flowerCategorySpinner.setSelection(2);
                break;
            case FlowersContract.FlowersEntry.TYPE_WEDDING:
                flowerCategorySpinner.setSelection(3);
                break;
            case FlowersContract.FlowersEntry.TYPE_ROMANTIC:
                flowerCategorySpinner.setSelection(4);
                break;
            default:
                flowerCategorySpinner.setSelection(0);
                break;
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader = null;
    }

    private void showUnsavedDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_message);
        builder.setPositiveButton(R.string.button_discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.button_keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!flowerHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedDialog(discardButtonClickListener);
    }

    private void setupSpinner() {

        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_flower_type, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        flowerCategorySpinner.setAdapter(spinnerAdapter);

        flowerCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (position != 0) {
                        mType = position;
                    }
                } else {
                    mType = FlowersContract.FlowersEntry.TYPE_CALLA;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mType = FlowersContract.FlowersEntry.TYPE_CALLA;
            }
        });
    }

    private void saveFlower() {

        String name = flowerNameEditText.getText().toString().trim();
        String desc = flowerDescEditText.getText().toString().trim();
        String supplierName = supplierNameEditText.getText().toString().trim();
        String phone = supplierPhoneEditText.getText().toString().trim();
        String priceString = flowerPriceEditText.getText().toString().trim();
        String quantityString = flowerQuantityEditText.getText().toString().trim();

        int quantity = 0;

        if (mCurrentFlower == null ||
                TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) ||
                TextUtils.isEmpty(supplierName) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(priceString) || TextUtils.isEmpty(quantityString) ||
                mType == FlowersContract.FlowersEntry.TYPE_CALLA) {
            Toast.makeText(this, R.string.toast, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE, mType);
        values.put(FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME, name);
        values.put(FlowersContract.FlowersEntry.COLUMN_PRODUCT_DESCRIPTION, desc);
        values.put(FlowersContract.FlowersEntry.COLUMN_PRICE, Integer.valueOf(priceString));

        if (!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(FlowersContract.FlowersEntry.COLUMN_QUANTITY, quantity);
        values.put(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_NAME, supplierName);
        values.put(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_PHONE_NUMBER, phone);

        if (mCurrentFlower == null) {
            Uri newFlowerUri = getContentResolver().insert(FlowersContract.FlowersEntry.CONTENT_URI, values);

            if (newFlowerUri == null) {
                Toast.makeText(this, R.string.message_error_saving, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(EditorActivity.this, CatalogActivity.class);
                intent.setData(mCurrentFlower);
                startActivity(intent);
                Toast.makeText(this, R.string.message_saved, Toast.LENGTH_SHORT).show();
            }
        } else {
            int updatedRows = getContentResolver().update(mCurrentFlower, values, null, null);

            if (updatedRows == 0) {
                Toast.makeText(this, R.string.message_error_updating, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(EditorActivity.this, CatalogActivity.class);
                intent.setData(mCurrentFlower);
                startActivity(intent);

                Toast.makeText(this, R.string.message_updated, Toast.LENGTH_SHORT).show();
            }
                    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.insert_demo:
                insertDemoFlower();
                finish();
                return true;
            case android.R.id.home:
                if (!flowerHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertDemoFlower() {
        ContentValues values = new ContentValues();
        values.put(FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE, FlowersContract.FlowersEntry.TYPE_ROMANTIC);
        values.put(FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME, R.string.demo_name);
        values.put(FlowersContract.FlowersEntry.COLUMN_PRODUCT_DESCRIPTION, R.string.demo_desc);
        values.put(FlowersContract.FlowersEntry.COLUMN_PRICE, 13);
        values.put(FlowersContract.FlowersEntry.COLUMN_QUANTITY, 6);
        values.put(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_NAME, R.string.demo_supplier_name);
        values.put(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_PHONE_NUMBER, R.string.demo_supplier_phone);

        Uri insertDemoUri = getContentResolver().insert(FlowersContract.FlowersEntry.CONTENT_URI, values);
    }
}
