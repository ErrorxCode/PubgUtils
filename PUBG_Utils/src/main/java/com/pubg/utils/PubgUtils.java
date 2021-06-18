package com.pubg.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCodec;
import android.os.Environment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


/**
 * This class contains method to manage PUBGM External files which is provided by method {@link Activity#getExternalFilesDir}.
 * @author Rahil khan
 * @version 1.0
 */
public class PubgUtils {

    private final Activity activity;
    private final FileUtils utils;
    private final String VERSION;
    public static final String GLOBAL = "com.tencent.ig";
    public static final String KOREAN = "com.pubg.krmobile";
    public static final String TAIWAN = "com.rekoo.pubgm";
    public static final String VIETNAM = "com.vng.pubgmobile";
    public static final String LITE = "com.tencent.iglite";
    private final String data;
    private final String saved;
    private final SharedPreferences preferences;
    private final SharedPreferences.Editor editor;



    public PubgUtils(Activity activity, String Version) {
        this.VERSION = Version;
        this.activity = activity;
        utils = new FileUtils(activity);
        data = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + VERSION + "/";
        saved = Environment.getExternalStorageDirectory().getPath() + "/Android/data/" + VERSION + "/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/";
        preferences = activity.getSharedPreferences("pref_" + System.currentTimeMillis(), Context.MODE_PRIVATE);
        editor = preferences.edit();
    }


    /**
     * Call this method to do something for onetime or more.
     * @param eventInterface The code to execute
     */
    public void oneTimeEvent(OneTimeEventInterface eventInterface) {
        boolean doAgain = preferences.getBoolean("isEventDone", false);
        if (!doAgain) {
            editor.putBoolean("isEventDone",eventInterface.OneTimeEvent());
        }
        editor.apply();
    }


    /**
     * Call this method to show dialog for onetime or more
     * @param dialogInterface The dialog code that is to be shown
     */
    public void oneTimeDialog(OneTimeDialogInterface dialogInterface) {
        boolean showAgain = preferences.getBoolean("isDialogShown", false);
        if (!showAgain) {
            boolean bool = dialogInterface.OneTimeDialog(new AlertDialog.Builder(activity));
            editor.putBoolean("isDialogShown", bool);
        }
        editor.apply();
    }

    /**
     * Only clears Logs of game
     */
    public void clearLogs() {
        utils.delete(saved + "Logs");
        utils.delete(saved + "LightData");
    }


    /**
     * Clears all the cached data of the game. This do not clear logs.
     */
    public void clearCaches() {
        utils.delete(data + "cache");
        utils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/TGPA", VERSION));
        utils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/ProgramBinaryCache", VERSION));
        utils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/CacheFile.txt", VERSION));
        utils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/vmpcloudconfig.json", VERSION));
        utils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/PufferTmpDir", VERSION));
        utils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/StatEventReportedFlag", VERSION));
        utils.delete(Environment.getExternalStorageDirectory() + String.format("/Android/data/%s/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/UpdateInfo", VERSION));
        utils.delete(Environment.getExternalStorageDirectory() + "/tencent");
    }


    /**
     * Deletes all the external data of the game including resource & maps.
     */
    public void clearData() {
        utils.delete(data + "files");
    }

    /**
     * Copies .pak file to the Paks folder of the game.
     * @param pakpath The file to be copied
     */
    public void copyPak(String pakpath) {
        utils.copy(pakpath, saved + "Paks");
    }

    /**
     * Copies .sav file to the Paks folder of the game.
     * @param savpath The file to be copied
     */
    public void copySav(String savpath) {
        utils.copy(savpath, saved + "SaveGames");
    }

    /**
     * Deletes the .pak file from the Paks folder of the game
     * @param pakname The .pak file to be deleted.
     */
    public void deletePak(String pakname) {
        utils.delete(saved + "Paks/" + pakname);
    }

    /**
     * Deletes the .sav file from the Paks folder of the game
     * @param savname The .pak file to be deleted.
     */
    public void deleteSav(String savname) {
        utils.delete(saved + "SaveGames/" + savname);
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
     * {@link PubgUtils#deactivateFirewall()} must be called after entering or exiting the match or game.
     * @return <code>true</code> only & only if version is korean & ports are blocked successfully, <code>false</code> otherwise
     */
    public boolean activateFirewall() {
        if (VERSION.equals(KOREAN)) {
            try {
                Scanner scanner = new Scanner(activity.getAssets().open("on.sh"));
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
     * Unblocks all the IP address which are blocked by the method {@link PubgUtils#activateFirewall()}.
     * @return <code>true</code> only & only if version is korean & ports are blocked successfully, <code>false</code> otherwise
     */
    public boolean deactivateFirewall() {
        try {
            Scanner scanner = new Scanner(activity.getAssets().open("off.sh"));
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
     * @return
     */
    public boolean startPUBG() {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(VERSION);
        if (intent == null)
            return false;
        else
            activity.startActivity(intent);
        return true;
    }


    /**
     * This encrypts the provided string using the AES -128bit algorithm & by rahil's encryption key.
     * @param string The string to be encrypted
     * @return Encrypted string
     * @throws Exception if the string is not in correct encoding. Many other reasons can be there.
     */
    public String encrypt(String string) throws Exception {
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
    public String decrypt(String string) throws Exception{
        SecretKeySpec key = new SecretKeySpec("thisisrahilenc16".getBytes(),"AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] enc = cipher.doFinal(string.getBytes());
        return new String(enc);
    }

}
