package com.pubg.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * This class contain all basic files operations. This is a easy version of original {@link File} & {@link java.nio.file.Files} class.
 * This class generally contain methods which are used by class {@link PubgUtils}.
 * @author Rahil khan
 * @version 1.0
 */
public class FileUtils {

    private final Activity context;


    /**
     * This constructor should only be called if the app has {@link Manifest.permission#WRITE_EXTERNAL_STORAGE}, Otherwise a permission request will be made.
     * @param activity The context for asking permission.
     */
    public FileUtils(Activity activity){
        this.context = activity;
        askStoragePermission();
    }



    synchronized private void askStoragePermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }


    /**
     * copy a file from assets folder to filesystem as provided by the path. The file can be in subdirectory.
     * @param filename The file to be copied
     * @param path The path where the asset is to be copied
     * @return True if assets copied successfully, false otherwise
     */
    public boolean copyFromAssets(String filename, String path) {
        File dir = new File(path);
        if (!dir.isDirectory()) {
            if (!dir.mkdir()) {
                return false;
            }
        }
        try {
            InputStream in = context.getAssets().open(filename);
            if (filename.contains("/"))
                filename = filename.substring(filename.lastIndexOf('/') + 1);
            File outFile = new File(path, filename);
            OutputStream out = new FileOutputStream(outFile);
            copyFiles(in, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }



    private void copyFiles(InputStream in, OutputStream out ) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        if (out != null) {
            try {
                out.close();
                in.close();
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Copy file from one directory to another.
     * @param filepath The file to be copied
     * @param to Path where to copy
     * @return true if file is copied successfully, false otherwise
     */
    public boolean copy(String filepath, String to) {
        if (filepath.endsWith("/")) {
            filepath = filepath.substring(0, filepath.lastIndexOf("/"));
        }
        String filename = filepath.substring(filepath.lastIndexOf("/"));
        if (!to.endsWith("/")) {
            to = to + "/" + filename;
        }
        File fromDir = new File(filepath);
        File toDir = new File(to);
        try {
            InputStream in = new FileInputStream(fromDir);
            OutputStream out = new FileOutputStream(toDir);
            copyFiles(in, out);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * copy all the files from assets folder to the path provided.
     * @param path The path where the assets is to be copied
     * @return true if file is copied successfully, false otherwise
     */
    public boolean copyAllAssets(String path) {
        boolean success = false;
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) {
            for (String filename : files) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(filename);
                    File outFile = new File(path, filename);
                    out = new FileOutputStream(outFile);
                    copyFiles(in, out);
                    success = true;
                } catch (IOException e) {
                    Log.e("tag", "Failed to copy asset file: " + filename, e);
                    success = false;
                } finally {
                    try {
                        in.close();
                        out.close();
                        out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return success;
    }


    /**
     * Moves a file from one directory to another.
     * @param filepath the file to be moved
     * @param dest Path where the file have to move
     * @return true if file is moved successfully, false otherwise
     */
    public boolean moveFile(String filepath, String dest) {
        File file = new File(filepath);
        return file.renameTo(new File(dest));
    }


    /**
     * Rename the file.
     * @param filepath File to be renamed
     * @param renameTo Name to rename the file
     * @return true if file is renamed successfully, false otherwise
     */
    public boolean rename(String filepath, String renameTo) {
        if (filepath.endsWith("/")) {
            filepath = filepath.substring(0, filepath.lastIndexOf("/"));
        }
        String filename = filepath.substring(filepath.lastIndexOf("/"));
        File file = new File(filepath);
        filepath = filepath.substring(0, filepath.lastIndexOf("/"));
        return file.renameTo(new File(filepath + "/" + renameTo));
    }


    /**
     * Deletes the file or folder.
     * @param path Path of file or folder to be deleted
     * @return true if deletion is successful, false otherwise
     */
    public boolean delete(@NonNull String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            for (File child : file.listFiles())
                delete(child.getPath());
        }
        return file.delete();
    }


    /**
     * Writes string to the text file
     * @param filepath Path of text file
     * @param data The string which have to be written in file
     * @return true if deletion is successful, false otherwise
     */
    public boolean writeFile(String filepath, String data,boolean append){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filepath,append));
            writer.println(data);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Writes string to the text file
     * @param filepath Path of text file
     * @return String as text of the file
     */
    public String readFile(String filepath){
        StringBuilder builder = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(filepath));
            while (scanner.hasNextLine()){
                builder.append(scanner.nextLine());
                builder.append('\n');
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * Remove a particular word or sentence from a text file
     * @param filepath Path of text file
     * @param words The words to be erased.
     */
    public void eraseWords(String filepath,String words){
        String data = readFile(filepath);
        data = data.replace(words,"");
        writeFile(filepath,data,true);
    }
}


