package com.xujl.task;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : xujl
 *     e-mail : 597355068@qq.com
 *     time   : 2018/7/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class RxLifeList implements ITaskFinish {
    private List<RxLife> mRxLifeList;

    public RxLifeList () {
        mRxLifeList = new ArrayList<>();
    }

    public void add (RxLife life) {
        if (life == null) {
            return;
        }
        life.setITaskFinish(this);
        mRxLifeList.add(life);
    }

    public void onDestroy () {
        for (RxLife rxLife : mRxLifeList) {
            if (rxLife != null) {
                rxLife.onDestroy();
            }
        }
        mRxLifeList.clear();
    }

    @Override
    public void onFinish (RxLife rxLife) {
        if (mRxLifeList.contains(rxLife)) {
            mRxLifeList.remove(rxLife);
        }
    }
}
