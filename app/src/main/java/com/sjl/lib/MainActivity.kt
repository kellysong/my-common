package com.sjl.lib

import android.Manifest
import android.util.Log
import android.view.View
import com.sjl.core.manager.CachedThreadManager
import com.sjl.core.mvvm.BaseActivity
import com.sjl.core.permission.PermissionsManager
import com.sjl.core.permission.PermissionsResultAction
import com.sjl.core.util.AdbUtils
import com.sjl.core.util.DeviceIdUtils
import com.sjl.core.util.StopWatch
import com.sjl.core.util.ViewUtils
import com.sjl.lib.test.LogTestActivity
import com.sjl.lib.test.MyBaseDialogFragment
import com.sjl.lib.test.SavedStateActivity

import com.sjl.lib.test.mvvm.NetActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 测试入口
 */
class MainActivity : BaseActivity() {


    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        tv_msg!!.text = "hello world！ my-common-lib"
    }

    override fun initListener() {}
    override fun initData() {
        println("DeviceI:" + DeviceIdUtils.getDeviceId(this))

        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        PermissionsManager.getInstance()
                .requestPermissionsIfNecessaryForResult(this, permissions, object : PermissionsResultAction() {
                    override fun onGranted() {}
                    override fun onDenied(permission: String) {}
                })
    }

    fun btnTestLog(view: View) {
        ViewUtils.openActivity(this, LogTestActivity::class.java)
    }

    fun btnTestNet(view: View) {
        ViewUtils.openActivity(this, NetActivity::class.java)
    }

    fun btnBseFragment(view: View) {
        //报错，把fragment的定义放到别的文件里，不要用匿名内部类的方式 
        /* val baseDialogFragment =  object :BaseDialogFragment(Gravity.CENTER, true){
             override fun getLayoutResId(): Int {
                 return R.layout.dialog_fragment
             }

         }*/
        val baseDialogFragment = MyBaseDialogFragment()
        baseDialogFragment.show(supportFragmentManager, "test")

    }

    fun btnTestSavedState(view: View) {
        ViewUtils.openActivity(this, SavedStateActivity::class.java)
    }

    fun btnTestAdbUtils(view: View) {
        CachedThreadManager.getInstance().execute {
            Log.i("TAG", "你好1")
            Log.w("TAG", "你好2")
            Log.e("TAG", "你好3 错误")
            /*AdbUtils.pullLogCatLog(this@MainActivity);
                AdbUtils.pullAnrLog(this@MainActivity);
                StringBuilder sb = new StringBuilder();
                AdbUtils.pullLogCatErrorLog(this@MainActivity, sb);
                System.out.println("错误日志：" + sb.toString());
                AdbUtils.pullFdInfo(this@MainActivity);
                AdbUtils.pullApkInfo(this@MainActivity);*/
            /*AdbUtils.pullLogCatLog(this@MainActivity);
                AdbUtils.pullAnrLog(this@MainActivity);
                StringBuilder sb = new StringBuilder();
                AdbUtils.pullLogCatErrorLog(this@MainActivity, sb);
                System.out.println("错误日志：" + sb.toString());
                AdbUtils.pullFdInfo(this@MainActivity);
                AdbUtils.pullApkInfo(this@MainActivity);*/
            AdbUtils.getScreenshots(this@MainActivity)
        }
    }

    fun btnTime(view: View?) {
        CachedThreadManager.getInstance().execute {
            val sw = StopWatch("test")
            sw.start("task1")
            // do something
            try {
                Thread.sleep(100)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            sw.stop()
            sw.start("task2")
            // do something
            try {
                Thread.sleep(200)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            sw.stop()
            println("sw.prettyPrint()~~~~~~~~~~~~~~~~~")
            System.out.println(sw.prettyPrint())
        }
    }
}