package com.pubg.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.pubg.utils.FileUtils;
import com.pubg.utils.OneTimeDialogInterface;
import com.pubg.utils.OneTimeEventInterface;

import java.io.IOException;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * This class contains method to manage PUBGM External files which is provided by method {@link Activity#getExternalFilesDir}.
 * @author Rahil khan
 * @version 1.0
 * @since 2021
 */
public class PubgUtils {

    private final String VERSION;
    private static final String GLOBAL = "com.tencent.ig";
    private static final String KOREAN = "com.pubg.krmobile";
    private static final String TAIWAN = "com.rekoo.pubgm";
    private static final String VIETNAM = "com.vng.pubgmobile";
    private static final String LITE = "com.tencent.iglite";
    private static final String BGMI = "com.pubg.imobile";
    private final String data;
    private final String saved;


    @SuppressLint("CommitPrefEdits")
    public PubgUtils(@NonNull String Version) {
        this.VERSION = Version;
        data = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + VERSION + "/";
        saved = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + VERSION + "/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/";
    }



    /**
     * Only clears Logs of game
     */
    public void clearLogs() {
        FileUtils.delete(saved + "Logs");
        FileUtils.delete(saved + "LightData");
    }


    /**
     * Clears all the cached data of the game. This do not clear logs.
     */
    public void clearCaches() {
        FileUtils.delete(data + "cache");
        FileUtils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/TGPA", VERSION));
        FileUtils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/ProgramBinaryCache", VERSION));
        FileUtils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/CacheFile.txt", VERSION));
        FileUtils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/vmpcloudconfig.json", VERSION));
        FileUtils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferTmpDir", VERSION));
        FileUtils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/StatEventReportedFlag", VERSION));
        FileUtils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/UpdateInfo", VERSION));
        FileUtils.delete(Environment.getExternalStorageDirectory() + "/tencent");
    }


    /**
     * Deletes all the external data of the game including resource & maps.
     */
    public void clearData() {
        FileUtils.delete(data + "files");
    }

    /**
     * Copies .pak file to the Paks folder of the game.
     * @param pakpath The file path of the pak file.
     */
    public void copyPak(String pakpath) {
        FileUtils.copy(pakpath, saved + "Paks");
    }

    /**
     * Copies .sav file to the Paks folder of the game.
     * @param savpath The file path of the sav file.
     */
    public void copySav(String savpath) {
        FileUtils.copy(savpath, saved + "SaveGames");
    }

    /**
     * Deletes the .pak file from the Paks folder of the game
     * @param pakname The .pak file to be deleted.
     */
    public void deletePak(String pakname) {
        FileUtils.delete(saved + "Paks/" + pakname);
    }

    /**
     * Deletes the .sav file from the Paks folder of the game
     * @param savname The .pak file to be deleted.
     */
    public void deleteSav(String savname) {
        FileUtils.delete(saved + "SaveGames/" + savname);
    }


    /**
     * Executes shell commands at the kernel level.
     * @param command The command to be executed
     * @return <code>true</code> if command executed successfully, false otherwise
     */
    public boolean runShell(String command) {
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            try {
                Runtime.getRuntime().exec(command, null, null);
            } catch (IOException ioException) {
                ioException.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     * Runs the pre-compiled native code (cpp).
     * @param cpppath The path of the cpp
     * @param isRooted boolean specifying weather to run cpp as root
     * @return <code>true</code> if cpp is executed successfully, false otherwise
     */
    public boolean runCPP(String cpppath, boolean isRooted) {
        if (isRooted)
            return runShell("su -c " + cpppath);
        else
            return runShell(cpppath);
    }


    /**
     * Blocks the IP address on which the client listen at lobby. This reduces server side ban in lobby
     * {@link PubgUtils#deactivateFirewall(Context)} ()} must be called after entering or exiting the match or game.
     * @return <code>true</code> only & only if version is korean & ports are blocked successfully, <code>false</code> otherwise
     */
    public boolean activateFirewall(Context context) {
        if (VERSION.equals(KOREAN)) {
            try {
                Scanner scanner = new Scanner(context.getAssets().open("on.sh"));
                while (scanner.hasNextLine())
                    runShell(scanner.nextLine());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else
            return false;
    }


    /**
     * Unblocks all the IP address which are blocked by the method {@link PubgUtils#activateFirewall(Context)}.
     * @return <code>true</code> only & only if version is korean & ports are blocked successfully, <code>false</code> otherwise
     */
    public boolean deactivateFirewall(Context context) {
        try {
            Scanner scanner = new Scanner(context.getAssets().open("off.sh"));
            while (scanner.hasNextLine())
                runShell(scanner.nextLine());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Starts the game
     * @return <code>true</code> if {@link PubgUtils#VERSION} is installed, <code>false</code> otherwise
     */
    public boolean startPUBG(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(VERSION);
        if (intent == null)
            return false;
        else
            context.startActivity(intent);
        return true;
    }

    /**
     * This class contain some extra method that may be useful while creating app. The methods are off-topics & doesn't belongs to {@link PubgUtils} class.
     */
    public static class Extra {
        /**
         * Call this method to do something for one or more time. In {@link OneTimeEventInterface} method,
         * you have to return <code>true</code> if you want event to occur for one more time.
         * <code>false</code> otherwise. Event is the code that has to be executed.
         * @param eventInterface The interface that include the event.
         * @param id The unique id for this event.
         */
        public static void oneTimeEvent(Context context,int id,OneTimeEventInterface eventInterface) {
            SharedPreferences preferences = context.getSharedPreferences("otp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            boolean doAgain = preferences.getBoolean("isEventDone_" + id, true);
            if (doAgain) {
                editor.putBoolean("isEventDone_" + id,eventInterface.OneTimeEvent()).apply();
            }
        }


        /**
         * Call this method to show alertdialog for one or more time. In {@link OneTimeDialogInterface} method,
         * you have to return <code>true</code> if you want to show alertdialog for one more time,
         * <code>false</code> otherwise. You don't need to call {@link AlertDialog.Builder#show()} method.
         * @param dialogInterface The interface that include alertdialog code.
         * @param id The unique id for this alertdialog.
         */
        public static void oneTimeDialog(Context context,int id, OneTimeDialogInterface dialogInterface) {
            SharedPreferences preferences = context.getSharedPreferences("otp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            boolean showAgain = preferences.getBoolean("isDialogShown_" + id, true);
            if (showAgain) {
                boolean bool = dialogInterface.OneTimeDialog(builder);
                editor.putBoolean("isDialogShown_" + id, bool).apply();
                builder.show();
            }
        }


        /**
         * This encrypts the provided string using the AES -128bit algorithm & by rahil's encryption key.
         * @param string The string to be encrypted
         * @return Encrypted string
         * @throws Exception if the string is not in correct encoding. Many other reasons can be there.
         */
        public static String encrypt(String string) throws Exception {
            SecretKeySpec key = new SecretKeySpec("thisisrahilenc16".getBytes(),"AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE,key);
            byte[] enc = cipher.doFinal(string.getBytes());
            return new String(enc);
        }


        /**
         * This decrypts the provided string using the AES -128bit algorithm & by rahil's encryption key.
         * @param string The string to be decrypted
         * @return decrypted string
         * @throws Exception if the encrypted string is not in correct encoding. Many other reasons can be there.
         */
        public static String decrypt(String string) throws Exception{
            SecretKeySpec key = new SecretKeySpec("thisisrahilenc16".getBytes(),"AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE,key);
            byte[] enc = cipher.doFinal(string.getBytes());
            return new String(enc);
        }
    }
}
