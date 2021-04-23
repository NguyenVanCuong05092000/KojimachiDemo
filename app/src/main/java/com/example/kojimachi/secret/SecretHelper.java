package com.example.kojimachi.secret;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.example.kojimachi.constant.AppConstants;

public class SecretHelper {
    private final String TAG = "SecretHelper";

    public String getTextDecrypt(Context context, String text, String tag) {
        try {
            if (AppConstants.LOG_DEBUG) Log.e(TAG, "encrypt->" + text);
            // decrypt the text
            String decryptText = decrypt(context, text);
            if (AppConstants.LOG_DEBUG) Log.e(TAG, "decrypt->" + tag + "->" + decryptText);
            return decryptText;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "error";
    }

    public void printTextEncrypted(Context context, String text, String tag) {
        String hash = getKeyHash(context);
        if (AppConstants.LOG_DEBUG) Log.e(TAG, "hash->" + hash + "$length:" + hash.length());
        if (AppConstants.LOG_DEBUG) Log.e(TAG, tag + "->" + text);
        String encryptText = encrypt(context, text);//
        if (AppConstants.LOG_DEBUG) Log.e(TAG, "encrypt->" + encryptText);
        // decrypt the text
        String decryptText = decrypt(context, encryptText);
        if (AppConstants.LOG_DEBUG) Log.e(TAG, "decrypt->" + decryptText);
    }

    private String secretKey;

    private String getSecretKey(Context context) {
        if (TextUtils.isEmpty(secretKey))
            secretKey = getSecretKey(getKeyHash(context).trim(), 32);
        return secretKey;
    }

    private SecretKeySpec secretKeySpec;

    private SecretKeySpec getSecretKeySpec(Context context) {
        if (secretKeySpec == null)
            secretKeySpec = new SecretKeySpec(getSecretKey(context).getBytes(StandardCharsets.UTF_8), "AES");
        return secretKeySpec;
    }

    private String randomVector;

    private String getRandomVector(Context context) {
        if (TextUtils.isEmpty(randomVector))
            randomVector = getSecretKey(getKeyHash(context).trim(), 16);
        return randomVector;
    }

    private IvParameterSpec ivParameterSpec;

    private IvParameterSpec getIvParameterSpec(Context context) {
        if (ivParameterSpec == null)
            ivParameterSpec = new IvParameterSpec(getRandomVector(context).getBytes(StandardCharsets.UTF_8));
        return ivParameterSpec;
    }

    private String getSecretKey(String hash, int length) {
        if (!TextUtils.isEmpty(hash)) {
            if (hash.length() >= length)
                return hash.substring(0, length);
            else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(hash);
                for (int i = hash.length(); i < length; i++)
                    stringBuilder.append("0");
                return stringBuilder.toString();
            }
        } else
            return "0000000000000000" + (length == 32 ? "0000000000000000" : "");
    }

    private String encrypt(Context context, String value) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(context), getIvParameterSpec(context));
            return Base64.encodeToString(cipher.doFinal(value.getBytes()), Base64.DEFAULT);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "error";
    }

    private String decrypt(Context context, String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(context), getIvParameterSpec(context));
            return new String(cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT)));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "error";
    }

    private String keyHash;

    private String getKeyHash(Context context) {
        if (TextUtils.isEmpty(keyHash)) {
            PackageInfo packageInfo;
            try {
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                        PackageManager.GET_SIGNATURES);

                for (Signature signature : packageInfo.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    keyHash = new String(Base64.encode(md.digest(), 0));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return TextUtils.isEmpty(keyHash) ? "" : keyHash;
    }
}
