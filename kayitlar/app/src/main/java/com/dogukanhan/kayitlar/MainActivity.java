package com.dogukanhan.kayitlar;


import android.content.Intent;
import android.os.Bundle;
import com.dogukanhan.kayitlar.activity.MainScreen;
import com.dogukanhan.kayitlar.config.DatabaseHelper;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;

public class MainActivity extends OrmLiteBaseActivity<DatabaseHelper> {



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent it = new Intent(this, MainScreen.class);
        startActivity(it);


//

    }

}

