package com.tjsoft.webhall.ui.wsbs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;


public class ZXTSActivity extends FragmentActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tj_zxts_container);

        Button btnAddFrag1 = (Button) findViewById(R.id.btn_add_frag1);
        Button btnAddFrag2 = (Button) findViewById(R.id.btn_add_frag2);
        Button btnRemoveFrage2 = (Button) findViewById(R.id.btn_remove_frag2);
        Button btnReplaceFrage1 = (Button) findViewById(R.id.btn_repalce_frag1);

        btnAddFrag1.setOnClickListener(this);
        btnAddFrag2.setOnClickListener(this);
        btnRemoveFrage2.setOnClickListener(this);
        btnReplaceFrage1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_remove_frag2) {
            removeFragment2();
        }else if(id == R.id.btn_repalce_frag1) {
            replaceFragment1();
        }
//        switch (id) {
////            case R.id.btn_add_frag1: {
////                Fragment1 fragment1 = new Fragment1();
////                addFragment(fragment1, "fragment1");
////            }
////            break;
////            case R.id.btn_add_frag2: {
////                Fragment2 fragment2 = new Fragment2();
////                addFragment(fragment2, "fragment2");
////            }
////            break;
//            case R.id.btn_remove_frag2: {
//                removeFragment2();
//            }
//            break;
//            case R.id.btn_repalce_frag1: {
//                replaceFragment1();
//            }
//        }

    }

    private void addFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_container, fragment, tag);
        transaction.commit();
    }

    private void removeFragment2() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag("fragment2");
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
    }

    private void replaceFragment1() {
//        FragmentManager manager = getSupportFragmentManager();
//        Fragment2 fragment2 = new Fragment2();
//        FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.fragment_container, fragment2);
//        transaction.commit();
    }
}
