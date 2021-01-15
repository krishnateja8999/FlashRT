package com.example.flashnew.HelperClasses;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.flashnew.Server.Utils;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.logging.Level;

public class UploadImages {
    public static Storage setCredentials(InputStream credentialFile) {
        InputStream credentialsStream = null;
        ;
        Credentials credentials = null;
        try {
            credentialsStream = credentialFile;
            credentials = GoogleCredentials.fromStream(credentialsStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return StorageOptions.newBuilder()
                .setProjectId("digitalizacao-174822").setCredentials(credentials)
                .build().getService();
    }


    public static String transmitImageFile(Storage storage, String srcFileName, String newName) {
        File file = new File(srcFileName);
        byte[] fileContent = null;
        try {
            //fileContent = Files.readAllBytes(file.toPath());
            fileContent = new byte[(int) file.length()];
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            fis.read(fileContent);
            Log.e("TAG", "transmitImageFileByte2: " + Arrays.toString(fileContent));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        if (fileContent == null)
            return null;
        if (fileContent.length == 0)
            return null;
        BlobInfo.Builder newBuilder = Blob.newBuilder(BucketInfo.of("gunsandroses-909459-dontcry-571723"),
                newName);
        BlobInfo blobInfo = newBuilder.setContentType("image/png").build();
        try {
            Blob blob = storage.create(blobInfo, fileContent);
            String bucket = blob.getBucket();
            String contentType = blob.getContentType();
            Log.e("TAG", "CONTENT TYPE: " + contentType);
            Log.e("TAG", "transmitImageFile: " + "File " + srcFileName + " uploaded to bucket " + bucket + " as " + newName);
            File delete = new File(srcFileName);
            if (delete.exists()) {
                if (delete.delete()) {
                    System.out.println("file Deleted :" + srcFileName);
                } else {
                    System.out.println("file not Deleted :" + srcFileName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newName;
    }
}
