package com.example.android.myflowershop;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.android.myflowershop.data.FlowersContract;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FLOWERS_LOADER = 0;
    FlowersAdapter mFlowersAdapter;
    public ListView flowerListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flower_catalog);

        FloatingActionButton addItemButton = (FloatingActionButton) findViewById(R.id.add_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        flowerListView = findViewById(R.id.list_item);
        View emptyView = findViewById(R.id.empty_view);
        flowerListView.setEmptyView(emptyView);


        mFlowersAdapter = new FlowersAdapter(this, null);
        flowerListView.setAdapter(mFlowersAdapter);
        flowerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, DetailActivity.class);
                Uri uri = ContentUris.withAppendedId(FlowersContract.FlowersEntry.CONTENT_URI, id);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        getSupportLoaderManager().initLoader(FLOWERS_LOADER, null, this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {FlowersContract.FlowersEntry._ID,
                FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE,
                FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME,
                FlowersContract.FlowersEntry.COLUMN_PRICE,
                FlowersContract.FlowersEntry.COLUMN_QUANTITY};

        CursorLoader loader = new CursorLoader(this, FlowersContract.FlowersEntry.CONTENT_URI, projection, null, null, null);
        return loader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mFlowersAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mFlowersAdapter.swapCursor(null);
    }

    private void insertDemoFlower() {
        ContentValues values = new ContentValues();
        values.put(FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE, FlowersContract.FlowersEntry.TYPE_ROMANTIC);
        values.put(FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME, "Bouquet for birthday");
        values.put(FlowersContract.FlowersEntry.COLUMN_PRODUCT_DESCRIPTION, "Romantic bouquet for birthday. It is made of white and red roses and lilies.");
        values.put(FlowersContract.FlowersEntry.COLUMN_PRICE, 13);
        values.put(FlowersContract.FlowersEntry.COLUMN_QUANTITY, 6);
        values.put(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_NAME, "Flower Party");
        values.put(FlowersContract.FlowersEntry.COLUMN_SUPPLIER_PHONE_NUMBER, "06203733135");

        Uri insertDemoUri = getContentResolver().insert(FlowersContract.FlowersEntry.CONTENT_URI, values);
    }
   

    private void deleteAllData() {
        int deleteAll = getContentResolver().delete(FlowersContract.FlowersEntry.CONTENT_URI,null,null);
        if (deleteAll > 0) {
            Toast.makeText(this, "All flowers succesfully deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Looks like there was nothing flower to delete", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_demo:
                insertDemoFlower();
                return true;
            case R.id.delete_all:
                deleteAllData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}