package com.example.android.myflowershop;

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
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.myflowershop.data.FlowersContract;
import com.example.android.myflowershop.data.Utilities;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Uri mCurrentFlower;
    private int inStock;
    private String supplierPhone;
    private ImageView flowerImage;
    private TextView flowerCategory;
    private TextView flowerPrice;
    private TextView flowerQuantity;
    private TextView flowerName;
    private TextView flowerDescription;
    private TextView supplierNameTV;
    private TextView supplierPhoneTV;
    private Button increaseQuantity;
    private Button decreaseQuantity;
    private Button editButton;
    private Button deleteButton;

    private static final int EXISTING_PRODUCT_LOADER = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.flower_detail);

        final Intent intent = getIntent();
        mCurrentFlower = intent.getData();

        flowerImage = (ImageView) findViewById(R.id.image);
        flowerCategory = (TextView) findViewById(R.id.category_name);
        flowerPrice = (TextView) findViewById(R.id.price);
        flowerQuantity = (TextView) findViewById(R.id.quantity);
        flowerName = (TextView) findViewById(R.id.product_name);
        flowerDescription = (TextView) findViewById(R.id.description);
        supplierNameTV = (TextView) findViewById(R.id.supplier_name);
        supplierPhoneTV = (TextView) findViewById(R.id.supplier_phonenumber);
        increaseQuantity = (Button) findViewById(R.id.button_increase);
        decreaseQuantity = (Button) findViewById(R.id.button_decrease);
        editButton = (Button) findViewById(R.id.button_edit);
        deleteButton = (Button) findViewById(R.id.button_delete);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.button_decrease:
                        if (inStock > 0) {
                            inStock--;
                            updateQuantity();
                        }
                        break;
                    case R.id.button_increase:
                        if (inStock < 100) {
                            inStock++;
                            updateQuantity();
                        }
                        break;
                    case R.id.supplier_phonenumber:
                        Intent intentCall = new Intent(Intent.ACTION_DIAL);
                        intentCall.setData(Uri.parse("tel:" + supplierPhone));
                        startActivity(intentCall);
                        break;
                    case R.id.button_edit:
                        Intent intentEdit = new Intent(DetailActivity.this, EditorActivity.class);
                        intentEdit.setData(mCurrentFlower);
                        startActivity(intentEdit);
                        break;
                    case R.id.button_delete:
                        showDeleteDialog();
                        break;
                }
            }
        };
        supplierPhoneTV.setOnClickListener(listener);
        increaseQuantity.setOnClickListener(listener);
        decreaseQuantity.setOnClickListener(listener);
        editButton.setOnClickListener(listener);
        deleteButton.setOnClickListener(listener);

        getSupportLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
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
        String[] selectionArgs = new String[] {String.valueOf(ContentUris.parseId(mCurrentFlower))};
        CursorLoader loader = new CursorLoader(this, FlowersContract.FlowersEntry.CONTENT_URI, projection, selection, selectionArgs, null);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount()<1) {
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
        supplierPhone = data.getString(supplierPhoneColumnIndex);

        flowerImage.setImageResource(imageId.getmImageId());
        flowerCategory.setText(imageId.getmFlowerTypeString());
        flowerPrice.setText(String.valueOf(price));
        flowerQuantity.setText(String.valueOf(quantity));
        flowerName.setText(flowersName);
        flowerDescription.setText(flowersDesc);
        supplierNameTV.setText(supplierName);
        supplierPhoneTV.setText(supplierPhone);

        inStock = quantity;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader = null;
    }

    private void updateQuantity () {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FlowersContract.FlowersEntry.COLUMN_QUANTITY, inStock);
        String selection = FlowersContract.FlowersEntry._ID + "=?";
        String[] selectionArgs = new String[] {String.valueOf(ContentUris.parseId(mCurrentFlower))};
        getContentResolver().update(mCurrentFlower, contentValues, selection, selectionArgs);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void deleteFlower() {
        if (mCurrentFlower != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentFlower, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Delete successful", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void showDeleteDialog () {
        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(this);
        deleteBuilder.setMessage("Delete this flower?");
        deleteBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFlower();
            }
        });

        deleteBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = deleteBuilder.create();
        alertDialog.show();
    }
}