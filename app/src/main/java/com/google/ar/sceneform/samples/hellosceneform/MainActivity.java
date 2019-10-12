/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final double MIN_OPENGL_VERSION = 3.0;

  private ArFragment arFragment;
  private ViewRenderable andyRenderable;
  private  ModelRenderable myrenderable;
  @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  // CompletableFuture requires api level 24
  // FutureReturnValueIgnored is not valid
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (!checkIsSupportedDeviceOrFinish(this)) {
      return;
    }
      ArrayList<Places> myplaces = new ArrayList<Places>();

         Places a = new Places();
         a.setName("VCC Mall");
         a.setDes("The Vinayak City Centre serves as one of the major malls in Allahabad");
         a.setDistance("9 km");
         //a.setImg_id(R.drawable.vcc);
         myplaces.add(a);


          Places b = new Places();
          b.setName("Allahabad Fort");
          b.setDes("large Fort dating to the 16th century");
          b.setDistance("To be Set");
          //b.setImg_id(R.drawable.Akbar_Fort_Allahabad);
          myplaces.add(b);

          Places c = new Places();
          c.setName("Swaraj Bhavan");
          c.setDes("Museum in the Nehru family mansion");
          c.setDistance("To be Set");
          //c.setImg_id(R.drawable.Swaraj_Bhavan);
          myplaces.add(c);

          Places d = new Places();
          d.setName("All Saints Cathedral");
          d.setDes("Cathedral, History and Architecture");
          d.setDistance("To be Set");
          //d.setImg_id(R.drawable.all_saints);
          myplaces.add(d);

          Places e = new Places();
          e.setName("Anand Bhavan");
          e.setDes("Ornate 1930s house museum and artifacts");
          e.setDistance("To be Set");
          //e.setImg_id(R.drawable.anand);
          myplaces.add(e);

          Places f = new Places();
          f.setName("Khusro Bagh");
          f.setDes("Large walled garden with mausoleums");
          f.setDistance("To be Set");
          //f.setImg_id(R.drawable.khusro);
          myplaces.add(f);

          Places g = new Places();
          g.setName("Allahabad Museum");
          g.setDes("Extensive national museum opened in 1954");
          g.setDistance("To be Set");
         // g.setImg_id(R.drawable.museum);
          myplaces.add(g);

          Places h = new Places();
          h.setName("Triveni Sangam");
          h.setDes("Sacred Hindu site at river confluence");
          h.setDistance("To be Set");
         // h.setImg_id(R.drawable.sangam);
          myplaces.add(h);

    setContentView(R.layout.activity_ux);
    arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

    // When you build a Renderable, Sceneform loads its resources in the background while returning
    // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
    ViewRenderable.builder()
        .setView(this, R.layout.location_view)
        .build()
        .thenAccept(renderable -> andyRenderable = renderable)
        .exceptionally(
            throwable -> {
              Toast toast =
                  Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
              toast.setGravity(Gravity.CENTER, 0, 0);
              toast.show();
              return null;
            });

      ModelRenderable.builder()
              .setSource(this, Uri.parse("flying sacuer.sfb"))
              .build()
              .thenAccept(renderable -> myrenderable = renderable)
              .exceptionally(
                      throwable -> {
                          Toast toast =
                                  Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                          toast.setGravity(Gravity.CENTER, 0, 0);
                          toast.show();
                          return null;
                      });

    arFragment.setOnTapArPlaneListener(
        (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
          if (andyRenderable == null) {
            return;
          }
            Anchor anchor = hitResult.createAnchor();
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene());
            Node base = createSolarSystem();
            anchorNode.addChild(base);
          // Create the Anchor.
         // Anchor anchor = hitResult.createAnchor();
          //AnchorNode anchorNode = new AnchorNode(anchor);
          //anchorNode.setParent(arFragment.getArSceneView().getScene());

          // Create the transformable andy and add it to the anchor.
          TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
          andy.setParent(anchorNode);
          andy.setRenderable(andyRenderable);
          andy.select();
        });
  }

    private Node createSolarSystem() {

      Node base = new Node();
      base.setRenderable(myrenderable);

      Node card = new Node();
      card.setParent(base);
      card.setLocalPosition(new Vector3(0.0f,1.0f,0.0f));
      card.setRenderable(andyRenderable);
      return base;
    }

    /**
   * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
   * on this device.
   *
   * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
   *
   * <p>Finishes the activity if Sceneform can not run
   */
  public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
    if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
      Log.e(TAG, "Sceneform requires Android N or later");
      Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
      activity.finish();
      return false;
    }
    String openGlVersionString =
        ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
            .getDeviceConfigurationInfo()
            .getGlEsVersion();
    if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
      Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
      Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
          .show();
      activity.finish();
      return false;
    }
    return true;
  }
}
