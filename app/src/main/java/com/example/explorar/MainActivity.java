package com.example.explorar;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.explorar.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArFragment arFragment;
    private ArSceneView arSceneView;
    private Anchor centerAnchor;
    private String link = "https://firebasestorage.googleapis.com/v0/b/testingforvariousthings.appspot.com/o/thomas_the_tank_engine.glb?alt=media&token=8ab4b489-20f1-447e-83f3-5cad04a33c28";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        arSceneView = arFragment.getArSceneView();

        // Set clear background color for better scene visibility
        arSceneView.setBackgroundColor(Color.TRANSPARENT);

        /*arSceneView.getScene().addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {
                // Calculate screen center in world space
                Display display = getWindowManager().getDefaultDisplay();
                Point screenSize = new Point();
                display.getSize(screenSize);
                float centerX = screenSize.x / 2f;
                float centerY = screenSize.y / 2f;
                float[] translation = {centerX, centerY, 0};
                float[] rotation = {0, 0, 0, 0};
                Pose centerPose = new Pose(translation, rotation);

                // Create or update anchor
//                            if (centerAnchor == null) {
//                                centerAnchor = arSceneView.getSession().createAnchor(centerPose);
//                                arSceneView.getScene().addAnchor(centerAnchor);
//                            } else {
//                                centerAnchor.setPose(centerPose); // Update anchor position
//                            }
                centerAnchor = arSceneView.getSession().createAnchor(centerPose);

                // Attach model node to anchor
                //modelNode.setParent(centerAnchor);
            }
        });*/

        // Load the glb model asynchronously to optimize startup performance
        ModelRenderable.builder()
                .setSource(arSceneView.getContext(), R.raw.andy)
                .build()
                .thenAccept(modelRenderable -> {
                    // Create a Node to attach the model to
                    Node modelNode = new Node();
                    modelNode.setRenderable(modelRenderable);

                    // Calculate screen center in world space
//                    DisplayMetrics metrics = new DisplayMetrics();
//                    getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                    float centerX = metrics.widthPixels / 2f;
//                    float centerY = metrics.heightPixels / 2f;
//                    float[] translation = {centerX, centerY, 0};
//                    float[] rotation = {0, 0, 0, 0};
//                    Pose centerPose = new Pose(translation, rotation);
//                    Vector3 tran = arSceneView.getScene().getCamera().getWorldPosition();
//                    Quaternion qua = arSceneView.getScene().getCamera().getWorldRotation();
//                    translation = new float[]{tran.x, tran.y, tran.z};
//                    rotation = new float[]{qua.x, qua.y, qua.z, qua.w};
//                    Pose n = new Pose(translation, rotation);
//                    centerAnchor = arSceneView.getSession().createAnchor(n);
//
                    AnchorNode anchorNode = new AnchorNode(centerAnchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                    transformableNode.setParent(anchorNode);
                    transformableNode.setRenderable(modelRenderable);
                    arSceneView.getScene().addChild(anchorNode);
                    transformableNode.select();

                    transformableNode.setEnabled(true);

                    // Add the Node to the scene
//                    arSceneView.getScene().addChild(modelNode);
//                    modelNode.setOnTouchListener((hitResult, motionEvent) -> {
//                        // Handle touch events on the model (optional)
//                        return true;
//                    });
                })
                .exceptionally(throwable -> {
                    // Handle loading errors (optional)
                    return null;
                });

        /*BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arSceneView != null) {
            try {
                arSceneView.resume();
            } catch (CameraNotAvailableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (arSceneView != null) {
            arSceneView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (arSceneView != null) {
            arSceneView.destroy();
        }
    }
}
/*package com.example.explorar;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.explorar.databinding.ActivityMainBinding;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;


public class MainActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ActivityMainBinding binding;
    
    private String link = "https://firebasestorage.googleapis.com/v0/b/testingforvariousthings.appspot.com/o/thomas_the_tank_engine.glb?alt=media&token=8ab4b489-20f1-447e-83f3-5cad04a33c28";
    private ModelRenderable modelRenderable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
//        arFragment.setOnTapPlaneGlbModel("oil_pump_jack.glb");
//
//        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
//            Anchor anchor = hitResult.createAnchor();
//            ModelRenderable.builder().setSource(this, Uri.parse(link))
//                    .build()
//                    .thenAccept(modelRenderable -> {
//                        addModelToScene(anchor, modelRenderable);
//                    }).exceptionally(throwable -> {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setMessage(throwable.getMessage()).show();
//                        return null;
//                    });
//        }));
//            Anchor anchor = hitResult.createAnchor();

//            ModelRenderable.builder().setSource(this,
//                            RenderableSource.builder()
//                                    .setSource(this
//                                            ,
//                                            RenderableSource.SourceType.GLB))
//                    .build()
//                    .thenAccept(modelRenderable -> {
//                        addModelToScene(anchor, modelRenderable);
//                    }).exceptionally(throwable -> {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setMessage(throwable.getMessage()).show();
//                        return null;
//                    });
//        }));
            
        setUpModel();
        setUpPlane();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);
        transformableNode.setRenderable(modelRenderable);
        //arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();
    }

    private void setUpModel() {

        ModelRenderable.builder()
                .setSource(this, Uri.parse(link)).setRegistryId(link)
                .build()
                .thenAccept(renderable -> modelRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast.makeText(MainActivity.this,"Model can't be Loaded", Toast.LENGTH_SHORT).show();
                    return null;
                });
    }

    private void setUpPlane(){
        arFragment.setOnTapArPlaneListener(new BaseArFragment.OnTapArPlaneListener() {
            @Override
            public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());
                createModel(anchorNode);
            }
        });
    }

    private void createModel(AnchorNode anchorNode){
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.setParent(anchorNode);
        node.setRenderable(modelRenderable);
        node.select();

    }
}*/