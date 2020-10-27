package com.example.databasetest.library

import androidx.fragment.app.FragmentActivity

/**
 * 为了让PermissionX中的接口更加方便地被调用
 * 这里将PermissionX指定为单例类
 */

object PermisssionX {

    private const val TAG = "InvisibleFragment"

    /**
     * 这个方法接收一个FragmentActivity参数、一个可变长度的permissions参数列表、一个callback回调
     * FragmentActivity是AppCompatActivity的父类
     */
    fun request(
        activity: FragmentActivity,
        vararg permissions: String,
        callback: PermissionsCallback
    ) {

        //获取FragmentManager的实例
        val fragmentManager = activity.supportFragmentManager

        //调用findFragmentByTag
        val existedFragment = fragmentManager.findFragmentByTag(TAG)

        //判断传入的Activity参数中是否已经包含了指定TAG的Fragment
        // 也就是刚刚编写的InvisibleFragment
        val fragment = if (existedFragment != null) {

            //如果包含，则直接使用该Fragment
            existedFragment as InvisibleFragment

        } else {

            //否则创建一个新的InvisibleFragment实例
            val invisibleFragment = InvisibleFragment()

            //添加到并Activity中，同时指定一个TAG
            //添加结束后一定要调用commitNow方法，不能调用commit方法，因为commit不会立即执行添加操作
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()

            invisibleFragment
        }

        //有了InvisibleFragment的实例后
        //需要调用它的requestNow方法去申请运行时权限
        //申请结果会自动回调到callback参数中
        fragment.requestNow(callback, *permissions)
        //permissions参数这里实际上是一个数组
        //对于数组可以遍历可以通过下标访问但是不可以直接将它传递给另外一个接收可变长度参数的方法
        //*符号并不是指针的意思，而是将一个数组转换成可变长度参数传递过去
    }
}