package com.animelabs.asheeshsharma.dressupapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Asheesh.Sharma on 03-12-2016.
 */
public class Product implements Parcelable {

    public Product(){}
    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public String getTitleProduct() {
        return titleProduct;
    }

    public void setTitleProduct(String titleProduct) {
        this.titleProduct = titleProduct;
    }

    public String getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(String dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getArticleNumber() {
        return articleNumber;
    }

    public void setArticleNumber(String articleNumber) {
        this.articleNumber = articleNumber;
    }

    private String pathToImage;

    public Product(String pathToImage, String titleProduct, String dateOfCreation, String price, String articleNumber, String articleType){
        this.pathToImage = pathToImage;
        this.dateOfCreation = dateOfCreation;
        this.titleProduct = titleProduct;
        this.price = price;
        this.articleNumber = articleNumber;
        this.articleType = articleType;
    }
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>(){
        @Override
        public Product createFromParcel(Parcel in) {
            String pathToProduct = in.readString();
            String titleProduct = in.readString();
            String dateOfCreation = in.readString();
            String price = in.readString();
            String articleNumber = in.readString();
            String articleType = in.readString();
            return new Product(pathToProduct, titleProduct, dateOfCreation, price, articleNumber, articleType);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
    @Override
    public String toString() {
        return "Product{" +
                "pathToImage='" + pathToImage + '\'' +
                ", titleProduct='" + titleProduct + '\'' +
                ", dateOfCreation='" + dateOfCreation + '\'' +
                ", price='" + price + '\'' +
                ", articleNumber='" + articleNumber + '\'' +
                '}';
    }

    private String titleProduct;
    private String dateOfCreation;
    private String price;
    private String articleNumber;

    public String getArticleType() {
        return articleType;
    }

    public void setArticleType(String articleType) {
        this.articleType = articleType;
    }

    private String articleType;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pathToImage);
        dest.writeString(titleProduct);
        dest.writeString(dateOfCreation);
        dest.writeString(price);
        dest.writeString(articleNumber);
        dest.writeString(articleType);
    }
}
