package com.example.android.myflowershop;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myflowershop.data.FlowersContract;
import com.example.android.myflowershop.data.Utilities;

public class FlowersAdapter extends CursorAdapter {

    ImageView flowerImage;
    TextView flowerName;
    ImageButton buy;
    TextView quantityNumber;
    TextView quantityText;
    TextView priceNumber;
    TextView dollar;


    public FlowersAdapter (Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View cardView = LayoutInflater.from(context).inflate(R.layout.list_item, parent,false);
        return cardView;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        flowerImage = view.findViewById(R.id.flower_image);
        flowerName = view.findViewById(R.id.flower_name);
        buy = view.findViewById(R.id.buy_button);
        quantityNumber = view.findViewById(R.id.quantity);
        quantityText = view.findViewById(R.id.quantity_text);
        priceNumber = view.findViewById(R.id.price);
        dollar = view.findViewById(R.id.dollar);

        int typeColumnIndex = cursor.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_FLOWERS_TYPE);
        int nameColumnIndex = cursor.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(FlowersContract.FlowersEntry.COLUMN_PRICE);

        int type = cursor.getInt(typeColumnIndex);
        String name = cursor.getString(nameColumnIndex);
        int quantity = cursor.getInt(quantityColumnIndex);
        int price = cursor.getInt(priceColumnIndex);

        Utilities.ImageId imageId = Utilities.getImage(type);
        flowerImage.setImageResource(imageId.getmImageId());

        flowerName.setText(name);
        quantityNumber.setText(String.valueOf(quantity));
        priceNumber.setText(String.valueOf(price));

        final Uri uri = ContentUris.withAppendedId(FlowersContract.FlowersEntry.CONTENT_URI,
                cursor.getInt(cursor.getColumnIndexOrThrow(FlowersContract.FlowersEntry._ID)));


        int idColumnIndex = cursor.getColumnIndex(FlowersContract.FlowersEntry._ID);
        Integer id = cursor.getInt(idColumnIndex);
        Sale saleFlowers = new Sale(id, quantity);
        buy.setTag(saleFlowers);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlowersAdapter.Sale saleFlower = (FlowersAdapter.Sale) v.findViewById(R.id.buy_button).getTag();

                if (saleFlower.getQuantity() > 0) {
                    Integer newQuantity = saleFlower.getQuantity() - 1;

                    ContentValues values = new ContentValues();
                    values.put(FlowersContract.FlowersEntry.COLUMN_QUANTITY, newQuantity);
                    context.getContentResolver().update(uri, values, null, null);
                }
            }
        });
    }


    public static class Sale {
        private int mId;
        private int mQuantity;

        public Sale(int mId, int mQuantity) {
            this.mId = mId;
            this.mQuantity = mQuantity;
        }

        public int getId() {
            return mId;
        }

        public int getQuantity() {
            return mQuantity;
        }
    }


}
