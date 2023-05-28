package com.sjl.core.util.file;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.widget.Toast;

import com.sjl.core.app.BaseApplication;
import com.sjl.core.util.io.IOUtils;
import com.sjl.core.util.log.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.RequiresApi;

/**
 * 文件操作工具类
 *
 * @author song
 */
public class FileUtils {


    /**
     * 保存bitmap为图片
     *
     * @param context
     * @param bmp
     * @param saveResultCallback
     */
    public static void savePhoto(final Context context, final Bitmap bmp, final SaveResultCallback saveResultCallback) {
        final File sdDir = getSDPath();
        if (sdDir == null) {
            Toast.makeText(context, "设备自带的存储不可用", Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                File appDir = new File(sdDir.getAbsolutePath());
                if (!appDir.exists()) {
                    appDir.mkdir();
                }
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//设置以当前时间格式为图片名称
                String fileName = df.format(new Date()) + ".png";
                File file = new File(appDir, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    saveResultCallback.onSavedSuccess(file);
                    updateAlbum(context,file);
                } catch (Exception e) {
                    saveResultCallback.onSavedFailed(e);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void updateAlbum(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //更新图库
            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, file.getName());
            values.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);
            ContentResolver contentResolver = context.getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  values);
            if (uri == null) {
                return;
            }
            try {
                OutputStream outputStream = contentResolver.openOutputStream(uri);
                FileInputStream fileInputStream = new FileInputStream(file);
                FileUtils.fileCopy(fileInputStream, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        }
    }


    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static File getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED); //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
        }
        return sdDir;
    }

    /**
     * 获取存储路径
     *
     * @param context
     * @param isRemovable 为false时得到的是内置sd卡路径，为true则为外置sd卡路径。
     * @return
     */
    @RequiresApi(Build.VERSION_CODES.M)
    public static String getStoragePath(Context context, boolean isRemovable) throws Exception {
        StorageManager mStorageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //7.0以上
            List<StorageVolume> storageVolumes = mStorageManager.getStorageVolumes();
            LogUtils.i("storageVolumes：" + storageVolumes.size());
            for (StorageVolume storageVolume : storageVolumes) {
                boolean removable = storageVolume.isRemovable();
                Class<?> storageVolumeClazz = storageVolume.getClass();
                Method getPath = storageVolumeClazz.getMethod("getPath");
                String path = (String) getPath.invoke(storageVolume);
                if (isRemovable == removable) {
                    return path;
                }
            }
        } else {
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method is_Removable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            LogUtils.i("storageVolumes：" + length);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                if (isRemovable == (Boolean) is_Removable.invoke(storageVolumeElement)) {
                    return path;
                }
            }
        }
        return null;
    }

    /**
     * 获取Cache文件夹
     *
     * @return
     */
    public static String getCachePath() {
        if (isSdCardExist()) {
            return BaseApplication.getContext()
                    .getExternalCacheDir()
                    .getAbsolutePath();
        } else {
            return BaseApplication.getContext()
                    .getCacheDir()
                    .getAbsolutePath();
        }
    }

    /**
     * 获取file文件夹,临时数据存放在cache目录下，持久化的数据存储在files
     *
     * @return
     */
    public static String getFilePath() {
        if (isSdCardExist()) {//有SD卡的情况：应用的存储目录
            return BaseApplication.getContext()
                    .getExternalFilesDir(null)
                    .getAbsolutePath();
        } else {//无SD卡的情况：应用的存储目录
            return BaseApplication.getContext()
                    .getFilesDir()
                    .getAbsolutePath();
        }
    }

    /**
     * 获取文件夹
     *
     * @param filePath
     * @return
     */
    public static File getFolder(String filePath) {
        File file = new File(filePath);
        //如果文件夹不存在，就创建它
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * 获取文件
     *
     * @param filePath
     * @return
     */
    public static synchronized File getFile(String filePath) {
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                //创建父类文件夹
                getFolder(file.getParent());
                //创建文件
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 统计目录大小
     *
     * @param file
     * @return
     */
    public static long getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {
                return file.length();
            }
        } else {
            return 0;
        }
    }

    /**
     * 格式化文件大小
     *
     * @param size
     * @return
     */
    public static String formatFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"b", "kb", "M", "G", "T"};
        //计算单位的，原理是利用lg,公式是 lg(1024^n) = nlg(1024)，最后 nlg(1024)/lg(1024) = n。
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        //计算原理是，size/单位值。单位值指的是:比如说b = 1024,KB = 1024^2
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }


    /**
     * 判断是否挂载了SD卡
     *
     * @return
     */
    public static boolean isSdCardExist() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    /**
     * 递归删除文件夹下的文件
     *
     *
     * @param dirPath
     */
    public static synchronized void deleteFile(String dirPath) throws IOException {
        File file = new File(dirPath);
        cleanDirectory(file);

    }

    /**
     * 清空目录下的文件
     * @param file
     * @throws IOException
     */
    public static void cleanDirectory(File file) throws IOException {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {  // null if security restricted
                throw new IOException("Failed to list contents of " + file);
            }
            for (File subFile : files) {
                cleanDirectory(subFile);
            }
        }
        //删除文件
        file.delete();
    }


    /**
     * 删除目录和目录下的文件
     * @param dir
     * @throws IOException
     */
    public static void deleteDirectory(File dir) throws IOException {
        if (!dir.exists()) {
            return;
        }
        cleanDirectory(dir);
        if (!dir.delete()) {
            String message =
                    "Unable to delete dir " + dir + ".";
            throw new IOException(message);
        }
    }

    /**
     * 获取文件的编码格式
     *
     * @param fileName
     * @return
     */
    public static CharsetEnum getCharset(String fileName) {
        BufferedInputStream bis = null;
        CharsetEnum charset = CharsetEnum.GBK;
        byte[] first3Bytes = new byte[3];
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(fileName));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xEF
                    && first3Bytes[1] == (byte) 0xBB
                    && first3Bytes[2] == (byte) 0xBF) {
                charset = CharsetEnum.UTF8;
                checked = true;
            }
            /*
             * 不支持 UTF16LE 和 UTF16BE
            else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = Charset.UTF16LE;
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE
                    && first3Bytes[1] == (byte) 0xFF) {
                charset = Charset.UTF16BE;
                checked = true;
            } else */

            bis.mark(0);
            if (!checked) {
                while ((read = bis.read()) != -1) {
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) // 双字节 (0xC0 - 0xDF)
                            // (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) {// 也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = CharsetEnum.UTF8;
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return charset;
    }

    /**
     * 使用NIO进行快速的文件拷贝
     *
     * @param source 源文件
     * @param target 目标文件
     * @throws IOException
     */
    @SuppressWarnings("resource")
    public static void fileCopy(File source, File target) throws IOException {
        FileChannel inChannel = new FileInputStream(source).getChannel();
        FileChannel outChannel = new FileOutputStream(target).getChannel();
        try {
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, maxCount, outChannel);
            }
            LogUtils.i("file copy success!");
        } finally {
            if (inChannel != null) {
                inChannel.close();
            }
            if (outChannel != null) {
                outChannel.close();
            }
        }
    }
    /**
     * 常规文件拷贝
     *
     * @param source
     * @param target
     * @throws IOException
     */
    public static void fileCopy2(File source, File target) throws IOException {
        fileCopy(new FileInputStream(source),new FileOutputStream(target));
    }

    /**
     * 常规文件拷贝
     *
     * @param in
     * @param target
     * @throws IOException
     */
    public static void fileCopy(InputStream in, File target) throws IOException {
        fileCopy(in,new FileOutputStream(target));
    }


    /**
     * 输入流拷贝到输出流
     * @param in
     * @param out
     * @throws IOException
     */
    public static void fileCopy(InputStream in, OutputStream out) throws IOException {
        try {
            byte buffer[] = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            LogUtils.i("stream copy success!");
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
    /**
     * 复制文件夹及其中的文件
     *
     * @param oldPath String 原文件夹路径
     * @param newPath String 复制后的路径
     * @return
     */
    public static boolean copyFolder(String oldPath, String newPath) {
        return copyFolder(oldPath,oldPath,null);
    }


    /**
     * 复制文件夹及其中的文件
     *
     * @param oldPath String 原文件夹路径
     * @param newPath String 复制后的路径
     * @param excludeFiles 需要排除的文件名
     * @return
     */
    public static boolean copyFolder(String oldPath, String newPath,List<String> excludeFiles) {
        try {

            File oldFile = new File(oldPath);
            String[] files = oldFile.list();
            if (files == null || files.length == 0) {
                return false;
            }
            File newFile = new File(newPath);
            if (!newFile.exists()) {
                newFile.mkdirs();
            }

            File temp;
            for (String file : files) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file);
                } else {
                    temp = new File(oldPath + File.separator + file);
                }
                if (temp.isDirectory()) {   //如果是子文件夹
                    copyFolder(oldPath + File.separator + file, newPath + File.separator + file, excludeFiles);
                } else if (temp.exists() && temp.isFile() && temp.canRead()) {
                    if (excludeFiles != null && excludeFiles.size() > 0 && excludeFiles.contains(temp.getName())) {
                        continue;
                    }
                    InputStream inputStream = new FileInputStream(temp);
                    fileCopy(inputStream, new File(newPath + File.separator + temp.getName()));
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 复制url对象到指定文件，相当于网页另存为
     *
     * @param source
     * @param destination
     * @throws IOException
     */
    public static void copyURLToFile(URL source, File destination) throws IOException {
        InputStream input = source.openStream();
        fileCopy(input, destination);
    }

    public interface SaveResultCallback {
        void onSavedSuccess(File file);

        void onSavedFailed(Exception e);
    }

    /**
     * Reads the contents of a file
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, Charset.defaultCharset());
    }

    /**
     * Reads the contents of a file into a String
     *
     * @param file
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String readFileToString(File file, String encoding) throws IOException {
        return readFileToString(file, CharsetEnum.toCharset(encoding));
    }

    /**
     * Reads the contents of a file into a String
     *
     * @param file
     * @param encoding
     * @return
     * @throws IOException
     */
    public static String readFileToString(File file, Charset encoding) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return IOUtils.toString(in, CharsetEnum.toCharset(encoding));
        } finally {
            IOUtils.close(in);
        }
    }
    /**
     * 写入文本到文件
     *
     * @param file
     * @param data
     * @throws IOException
     */
    public static void writeTextToFile(File file, String data) throws IOException {
        writeByteArrayToFile(file, data.getBytes("utf-8"), false);
    }

    /**
     * 写入字节数组到文件
     *
     * @param file
     * @param data
     * @throws IOException
     */
    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        writeByteArrayToFile(file, data, false);
    }
    /**
     * 写入字节数组到文件
     *
     * @param file
     * @param data
     * @param append true 追加写，false覆盖
     * @throws IOException
     */
    public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file, append);
            out.write(data);
            out.flush();
        } finally {
            IOUtils.close(out);
        }
    }
}
