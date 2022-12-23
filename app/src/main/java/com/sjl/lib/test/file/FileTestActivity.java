package com.sjl.lib.test.file;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sjl.core.mvp.BaseActivity;
import com.sjl.core.net.RxHttpUtils;
import com.sjl.core.net.file.FileCallback;
import com.sjl.core.util.file.FileUtils;
import com.sjl.core.util.log.LogUtils;
import com.sjl.lib.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * TODO
 *
 * @author Kelly
 * @version 1.0.0
 * @filename FileTestActivity
 * @time 2022/12/20 13:26
 * @copyright(C) 2022 song
 */
public class FileTestActivity extends BaseActivity {
    @BindView(R.id.tv_msg)
    TextView tv_msg;
    @BindView(R.id.ll_msg)
    LinearLayout ll_msg;


    public static final String ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator;// sd路径
    /**
     * 下载文件存储路径
     */
    private static final String DOWNLOAD_PATH = ROOT_PATH + File.separator + "downloadTest";
    /**
     * 带上传文件路径
     */
    private static final String UPLOAD_PATH = ROOT_PATH + File.separator + "uploadTest";

    @Override
    protected int getLayoutId() {
        return R.layout.file_test_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {

    }

    public void btnDownload(View view) {
        String url = "https://dd.myapp.com/sjy.00004/16891/apk/02E39FD45808731AFBFB01790CA89091.apk?fsname=cn.gov.tax.its_1.8.9_10809.apk";
        File file = new File(DOWNLOAD_PATH, "test.apk");
        ll_msg.removeAllViews();
        ViewHolder viewHolder = createViewHolder(-1);
        ll_msg.addView(viewHolder.view);

        RxHttpUtils.getInstance().download(url, file,0, new FileCallback<File>() {


            @Override
            public void onProgress(int progress, long total, long speed, int id) {
                viewHolder.mProgress.setText(progress + "%");
                viewHolder.mFileSize.setText(FileUtils.formatFileSize(total));
                viewHolder.mRate.setText(FileUtils.formatFileSize(speed) + "/s");
            }

            @Override
            public void onCompleted(File file) {
                showMsg("文件下载完成：" + file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                showMsg("文件下载异常:" + e.getMessage());
                LogUtils.e(e);
            }
        });
    }

    private ViewHolder createViewHolder(int index) {
        ViewHolder viewHolder = new ViewHolder(this, index);
        return viewHolder;
    }


    public void btnUpload(View view) {
        String url = "http://192.168.114.10:8080/springmvc_demo/upload.htmls?method=upload";
        File file = new File(UPLOAD_PATH, "file_paths.xml");
        File file2 = new File(UPLOAD_PATH, "network_security_config.xml");
        List<File> files = new ArrayList<>();
        files.add(file);
        files.add(file2);
        List<String> fileNames = new ArrayList<>();
        fileNames.add("file");
        fileNames.add("file");


        ll_msg.removeAllViews();
        Map<Integer, ViewHolder> temp = new LinkedHashMap<>();
        for (int i = 0; i < files.size(); i++) {
            ViewHolder viewHolder = createViewHolder(i);
            ll_msg.addView(viewHolder.view);
            temp.put(i, viewHolder);
        }
        RxHttpUtils.getInstance().upload(url, files, fileNames, null, new FileCallback<List<File>>() {

            @Override
            public void onProgress(int progress, long total, long speed, int id) {
                ViewHolder vh = temp.get(id);
                if (vh != null) {
                    vh.mProgress.setText(progress + "%");
                    vh.mFileSize.setText(FileUtils.formatFileSize(total));
                    vh.mRate.setText(FileUtils.formatFileSize(speed) + "/s");
                }
            }

            @Override
            public void onCompleted(List<File> file) {
                showMsg("文件上传完成：" + file.size());
            }

            @Override
            public void onError(Throwable e) {
                showMsg("文件上传异常:" + e.getMessage());
                LogUtils.e(e);
            }
        });
    }

    private void showMsg(String msg) {
        if (isDestroy(this)) {
            return;
        }
        tv_msg.setText(msg);
    }


    static class ViewHolder {
        TextView mIndex;
        TextView mFileSize;
        TextView mProgress;
        TextView mRate;
        View view;

        public ViewHolder(Context context, int index) {
            view = LayoutInflater.from(context).inflate(R.layout.file_rate_item, null);
            mIndex = view.findViewById(R.id.tv_index);
            mFileSize = view.findViewById(R.id.tv_file_size);
            mProgress = view.findViewById(R.id.tv_progress);
            mRate = view.findViewById(R.id.tv_rate);
            if (index > -1){
                mIndex.setText(index + ":");
            }
        }

        public View getView() {
            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxHttpUtils.getInstance().clear();
    }
}
