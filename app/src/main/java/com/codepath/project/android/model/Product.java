package com.codepath.project.android.model;

import com.codepath.project.android.data.TestData;
import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.ArrayList;

@ParseClassName("Product")
public class Product extends ParseObject {

    public Product() {
        super();
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getBrand() {
        return getString("brand");
    }

    public void setBrand(String brand) {
        put("brand", brand);
    }

    public int getRatingCount() {
        return getInt("ratingCount");
    }

    public void incrementRatingCount() {
        this.increment("ratingCount");
    }

    public int getTotalRating() {
        return getInt("totalRating");
    }

    public void addToTotalRating(int rating) {
        this.increment("totalRating", rating);
    }

    public double getAverageRating() {
        return getDouble("averageRating");
    }

    public void setRating(int rating) {
        addToTotalRating(rating);
        int totalRating = getTotalRating();
        incrementRatingCount();
        int ratingCount = getRatingCount();
        double averageRating = (double) totalRating/ratingCount;
        put("averageRating", averageRating);
    }

    public int getViews() {
        return getInt("views");
    }

    public void incrementViews() {
        this.increment("views");
    }

    public int getReviewCount() {
        return getInt("reviewCount");
    }

    public void incrementReviewCount() {
        this.increment("reviewCount");
    }

    public String getImageUrl() {
        return getString("imageUrl");
    }

    public JSONArray getImageSetUrls() {
        return getJSONArray("imageSetUrls");
    }

    public void setImageUrl(String imageUrl) {
        put("imageUrl", imageUrl);
    }

    public static ArrayList<Product> createReviewList(int length){
        ArrayList<Product> productList = new ArrayList<Product>();
        for(int i = 0; i < length; i++){
            Product product = new Product();
            String name = TestData.reviewItemName[i%3];
            String imageUrl = TestData.reviewItemUrl[i%3];
            product.setName(name);
            product.setImageUrl(imageUrl);
            productList.add(product);
        }
        return productList;
    }

    public static ArrayList<Product> createCategoryList(int length, ViewType type){
        ArrayList<Product> productList = new ArrayList<Product>();

        int size = (type == ViewType.GRID ) ? (length - 1) : length;

        for(int i = 0; i < size; i++){
            Product product = new Product();
            String name = TestData.catItemName[i%4];
            String imageUrl = TestData.catItemUrl[i%4];
            product.setName(name);
            product.setImageUrl(imageUrl);
            productList.add(product);
        }
        if(type == ViewType.GRID) {
            Product more = new Product();
            more.setName("");
            more.setImageUrl(TestData.MORE_URL);
            productList.add(more);
        }
        return productList;
    }

    public String getVideo() {
        // the stored videoUrl points to full youtube link
        // have to split the url for the "id"
        String videoUrl = getString("videoUrl");
        String videoId = videoUrl.split("=")[1];
        return videoId;
    }

    public JSONArray getVideoSetUrls() {
        return getJSONArray("videoSetUrls");
    }

    public String getCategory() {
        return getString("category");
    }

    public String getSubCategory() {
        return getString("subCategory");
    }
}
