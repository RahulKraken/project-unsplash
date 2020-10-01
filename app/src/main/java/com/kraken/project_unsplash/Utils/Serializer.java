package com.kraken.project_unsplash.Utils;

import android.util.Log;

import com.google.gson.Gson;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.Models.User;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Serializer {

  private static final String TAG = "Serializer";

  // Gson object
  private Gson gson;

  /**
   * constructor
   * initialize gson object here
   */
  public Serializer() {
    gson = new Gson();
  }

  /**
   * convert json into Photo[]
   * @param raw : json <String>
   * @return : ArrayList<Photo>
   */
  public List<Photo> listPhotos(String raw) {
    Photo[] photos = gson.fromJson(raw, Photo[].class);
    return new ArrayList<>(Arrays.asList(photos));
  }

  /**
   * convert json into Collection[]
   * @param raw : json <String>
   * @return : ArrayList<Collection>
   */
  public List<Collection> listCollections(String raw) {
    Collection[] collections = gson.fromJson(raw, Collection[].class);
    return new ArrayList<>(Arrays.asList(collections));
  }

  /**
   * convert object into byte[]
   * @param object : Object
   * @return : byte[]
   */
  public static byte[] objectToByteArray(Object object) {
    Log.d(TAG, "objectToByteArray: trying to convert object to byte[]");
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(object);
      Log.d(TAG, "objectToByteArray: byte[]\n" + Arrays.toString(byteArrayOutputStream.toByteArray()));
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * convert byte[] into object
   * @param bytes : byte[]
   * @return : object
   */
  public static Object photoFromByteArray(byte[] bytes) {
    Log.d(TAG, "photoFromByteArray: trying to get object from byte[]");
    try {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
      ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
      return objectInputStream.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public User getUser(String response) {
    return gson.fromJson(response, User.class);
  }
}
