package com.example.databasetest.library

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

/**
 * Fragment不像Activity一样必须有界面
 * 我们可以向Activity中添加一个隐藏的Fragment
 * 然后在隐藏的Fragment中对运行时权限的API进行封装
 * 这是一种非常轻量级的做法，因此不用担心隐藏Fragment会对Activity的性能造成什么影响
 */

//typealias关键字可以用于给任意类型指定一个别名
typealias PermissionsCallback = (Boolean, List<String>) -> Unit

class InvisibleFragment : Fragment() {

    //定义了一个callback变量作为运行时权限申请结果的回调通知方式
    //并声明成了一种函数类型变量
    // 该函数类型接收PermissionsCallback(也就是Boolean和List<String>)两种类型参数，无返回值
    private var calllback: PermissionsCallback? = null


    /**
     * 接收一个与callback变量类型相同的函数类型参数
     * 用vararg关键字接收了一个可变长度的permissions参数列表
     * 在这个方法中我们将传递进来的函数类型参数赋值给callback变量
     * 然后调用Fragment中提供的requestPermissions方法立即申请运行时权限
     * 并将permissions参数列表传递进去
     * 这样就可以实现由外部调用自主指定要申请哪些权限的功能了
     */

    fun requestNow(cb: PermissionsCallback, vararg permissions: String) {

        calllback = cb
        requestPermissions(permissions, 1)
    }

    /**
     * 重写onRequestPermissionsResult方法
     * 在这里处理运行时权限的申请结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == 1) {

            //使用deniedList列表来记录所有被用户拒绝的权限
            val deniedList = ArrayList<String>()

            for ((index, result) in grantResults.withIndex()) {

                //遍历grantResults数组，发现某个权限未被用户授权就将它添加到deniedList中
                if (result != PackageManager.PERMISSION_GRANTED) {

                    deniedList.add(permissions[index])
                }

            }

            //遍历结束后用一个allGranted变量来标识是否所有申请的权限均已被授权
            //判断依据时deniedList是否为空
            val allGranted = deniedList.isEmpty()

            //最后使用callback变量对运行时权限的申请结果进行回调
            calllback?.let { it(allGranted, deniedList) }
        }
    }
}