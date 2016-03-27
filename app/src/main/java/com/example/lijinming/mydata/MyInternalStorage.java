package com.example.lijinming.mydata;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/3/27.
 */
public class MyInternalStorage {



    SimpleDateFormat formatter    =   new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
    Date curDate    =   new    Date(System.currentTimeMillis());//获取当前时间
    String    Str   =    formatter.format(curDate);//获取系统时间


    //需要保存当前调用对象的Context
    private Context context;

    public MyInternalStorage(Context context) {
        this.context = context;
    }
    /**
     * 存储数据到Sd card
     * @param inputText 为要保存的数据
     * */
    public void saveToSdcard(String inputText)  {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            Log.e("main", "本设备有存储卡！");
            String basePath = getExternalStorageBasePath();
            BufferedWriter writer = null;
            File file = new File(basePath+"/"+Str+".txt");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(file);
                writer = new BufferedWriter(new OutputStreamWriter(out));
                writer.write(inputText);
                Log.e("TAG", "write successful");
                //   Log.e("filename", String.valueOf(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (writer != null) {
                        writer.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**从Sdcard中读取数据
     * @return 读取的数据以String格式返回
     * */
    public String get()  {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            File filename = new File(getExternalStorageBasePath()+"/"+Str+".txt");
            in = new FileInputStream(filename);
            Log.e("filename", String.valueOf(filename));
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return content.toString();
    }
    /**
     * 以追加的方式在指定文件的末尾添加内容
     * @param content 追加的内容
     */
    public void append(String content) throws IOException {
        String file = getExternalStorageBasePath()+"/"+Str+".txt";
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(content.getBytes());
        fos.close();
    }
    /**
     * 删除文件
     * @param filename 文件名
     * @return 是否成功
     */
    public boolean delete(String filename) {
        File file =new File(filename);
        file.delete();
        return true;
    }
    /**
     * 获取SD card指定存储路径下的所有文件名
     * @return 文件名数组
     */
    public List<String> queryAllFile() {
        File file = new File(Environment.getExternalStorageDirectory()+"/MyDATA/");
        File [] files  =  file.listFiles();
        List <String> pathname = new ArrayList<>();
        for (File f:files){
            pathname.add(f.toString());
        }
        return pathname;
    }
    private boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    /**
     * 获取存储文件的根路径
     * @return
     */
    private String getExternalStorageBasePath(){
        if(isExternalStorageWriteable()){
            File file = new File(Environment.getExternalStorageDirectory()+"/MyDATA/");
            file.mkdirs();
            Log.e("getAbsolutePath",file.getAbsolutePath());
            return file.getAbsolutePath();
        }else {
            Toast.makeText(context, "SD card不可读写", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
