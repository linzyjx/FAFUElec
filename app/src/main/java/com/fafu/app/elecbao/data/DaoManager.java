package com.fafu.app.elecbao.data;

import android.content.Context;

import com.fafu.app.elecbao.FAFUElec;

import org.greenrobot.greendao.database.Database;

public class DaoManager {

    private DaoSession mDaoSession;

    private static DaoManager INSTANCE;

    public static DaoManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DaoManager(FAFUElec.getContext());
        }
        return INSTANCE;
    }

    private DaoManager(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "Elec", null) {
            @Override
            public void onUpgrade(Database db, int oldVersion, int newVersion) {
                super.onUpgrade(db, oldVersion, newVersion);
            }
        };
        mDaoSession = new DaoMaster(helper.getWritableDb()).newSession();
    }

    /**
     * 向外提供DaoSession，获取相应DAO类进行增删改查
     */
    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
