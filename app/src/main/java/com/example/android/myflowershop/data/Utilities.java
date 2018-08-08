package com.example.android.myflowershop.data;

import com.example.android.myflowershop.R;

public final class Utilities {

    public static class ImageId {
        private int mImageId;
        private String mFlowerTypeString;

        public ImageId (int imageId, String flowerTypeString) {
            this.mImageId = imageId;
            this.mFlowerTypeString = flowerTypeString;
        }

        public int getmImageId() {
            return mImageId;
        }

        public String getmFlowerTypeString() {
            return mFlowerTypeString;
        }
    }

    public static ImageId getImage (int flowersType) {
        String flowerTypeString;
        int imageId;
        switch (flowersType) {
            case 0:
                flowerTypeString = "Calla";
                imageId = R.drawable.calla;
                break;
            case 1:
                flowerTypeString = "Rose";
                imageId = R.drawable.rose;
                break;
            case 2:
                flowerTypeString = "Orchids";
                imageId = R.drawable.orchids;
                break;
            case 3:
                flowerTypeString = "Wedding bouquet";
                imageId = R.drawable.wedding;
                break;
            default:
                flowerTypeString = "Romantic bouquet";
                imageId = R.drawable.romantic;
                break;
        }
        return new ImageId(imageId,flowerTypeString);
    }
}
