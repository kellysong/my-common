package com.sjl.lib

import android.Manifest
import android.content.Intent
import android.util.Log
import android.view.View
import com.sjl.core.manager.CachedThreadManager
import com.sjl.core.mvvm.BaseActivity
import com.sjl.core.permission.PermissionsManager
import com.sjl.core.permission.PermissionsResultAction
import com.sjl.core.permission.SpecialPermission
import com.sjl.core.util.*
import com.sjl.core.util.activityresult.ActivityResultUtils
import com.sjl.core.util.file.FileUtils
import com.sjl.core.util.log.LogUtils
import com.sjl.lib.manager.LoginManager
import com.sjl.lib.test.MyBaseDialogFragment
import com.sjl.lib.test.mvp.LogTestActivity
import com.sjl.lib.test.mvp.PermissionsTestActivity
import com.sjl.lib.test.mvvm.activity.KEY_RESULT
import com.sjl.lib.test.mvvm.activity.NetActivity
import com.sjl.lib.test.mvvm.activity.SavedStateActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.Exception

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

        val permissions = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
        PermissionsManager.getInstance()
                .requestPermissionsIfNecessaryForResult(this, permissions, object : PermissionsResultAction() {
                    override fun onGranted() {
                        LogUtils.d("权限授权通过")
                    }

                    override fun onDenied(permission: String) {
                        LogUtils.d("权限拒绝：$permission")
                    }
                })
    }

    private fun requestFilePermission() {
        PermissionsManager.getInstance().requestSpecialPermission(SpecialPermission.MANAGE_ALL_FILES_ACCESS,this,object : PermissionsResultAction() {
            override fun onGranted() {
                LogUtils.d("MANAGE_EXTERNAL_STORAGE权限授权通过")
            }

            override fun onDenied(permission: String) {
                LogUtils.d("MANAGE_EXTERNAL_STORAGE权限拒绝：$permission")
            }
        })
    }

    fun btnTestLog(view: View) {
        ViewUtils.openActivity(this, LogTestActivity::class.java)
    }

    fun btnTestNet(view: View) {
        ViewUtils.openActivity(this, NetActivity::class.java)
    }

    fun btnTestBaseDialogFragment(view: View) {
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
            "你好1".v() //扩展函数调用

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

    fun btnTestPermission(view: View) {
        ViewUtils.openActivity(this, PermissionsTestActivity::class.java)
    }

    fun btnTestActivityResultUtils(view: View) {
        val intent = Intent(this, NetActivity::class.java)
        val requestCode = 10
        ActivityResultUtils.init(this).startActivityForResult(intent, requestCode, object : ActivityResultUtils.Callback {

            override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                val result = data?.getStringExtra(KEY_RESULT)
                LogUtils.i("requestCode=$requestCode,resultCode = $resultCode,result = $result")
            }
        })

    }

    fun btnTestLogin(view: View) {
        LoginManager.toLogin(this, object : LoginManager.LoginCallback {
            override fun onLoginResult(loginResult: Boolean) {
                if (loginResult) {
                    ToastUtils.showShort(this@MainActivity, "登陆成功");
                } else {
                    ToastUtils.showShort(this@MainActivity, "登陆失败");
                }
            }
        })
    }

    fun btnTestImgSave(view: android.view.View) {
        FileUtils.savePhoto(this,ViewUtils.viewToBitmap(view),object : FileUtils.SaveResultCallback{
            override fun onSavedSuccess(file: File) {
                LogUtils.d("Saved Success")
            }

            override fun onSavedFailed(e: Exception) {
                LogUtils.d("Saved Failed")
            }
        })
    }
}