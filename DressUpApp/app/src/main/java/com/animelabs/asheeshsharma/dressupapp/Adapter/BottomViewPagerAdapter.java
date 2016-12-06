package com.animelabs.asheeshsharma.dressupapp.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.animelabs.asheeshsharma.dressupapp.Fragment.ProductFragmentBottom;
import com.animelabs.asheeshsharma.dressupapp.Fragment.ProductFragmentTop;
import com.animelabs.asheeshsharma.dressupapp.Model.Product;

import java.util.ArrayList;

/**
 * Created by Asheesh.Sharma on 03-12-2016.
 */
public class BottomViewPagerAdapter extends FragmentStatePagerAdapter
{
    public static int LOOPS_COUNT = 1000;
    private ArrayList<Product> mProducts;


    public BottomViewPagerAdapter(FragmentManager manager, ArrayList<Product> products)
    {
        super(manager);
        mProducts = products;
    }


    @Override
    public Fragment getItem(int position)
    {
        if (mProducts != null && mProducts.size() > 0)
        {
            position = position % mProducts.size(); // use modulo for infinite cycling
            return ProductFragmentBottom.newInstance(mProducts.get(position));
        }
        else
        {
            return ProductFragmentBottom.newInstance(null);
        }
    }


    @Override
    public int getCount()
    {
        if (mProducts != null && mProducts.size() > 0)
        {
            return mProducts.size()*LOOPS_COUNT; // simulate infinite by big number of products
        }
        else
        {
            return 1;
        }
    }
}
