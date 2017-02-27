package me.instabattle.app.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import me.instabattle.app.R;
import me.instabattle.app.managers.PhotoManager;
import me.instabattle.app.models.Entry;
import me.instabattle.app.settings.State;
import me.instabattle.app.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.desmond.squarecamera.CameraActivity;
import com.desmond.squarecamera.ImageUtility;

import java.nio.ByteBuffer;


public class ParticipatingActivity extends AppCompatActivity {

    private final String TAG = "ParticipatingActivity";

    private static final int REQUEST_CAMERA = 0;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private Point mSize;

    public static Class<?> gotHereFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);

        Display display = getWindowManager().getDefaultDisplay();
        mSize = new Point();
        display.getSize(mSize);

        requestForCameraPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_CAMERA) {
            Uri photoUri = data.getData();
            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), mSize.x, mSize.x);
            int byteCount = bitmap.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(byteCount);
            bitmap.copyPixelsToBuffer(buffer);
            final byte[] photoBytes = buffer.array();
            if (!State.creatingBattle) {
                State.chosenBattle.createEntryAndDo(new Callback<Entry>() {
                    @Override
                    public void onResponse(Call<Entry> call, Response<Entry> response) {
                        PhotoManager.upload(response.body().getImageName(), photoBytes);
                    }

                    @Override
                    public void onFailure(Call<Entry> call, Throwable t) {
                        //TODO
                        Log.e(TAG, "failed to add new entry: " + t);
                    }
                });
            } else {
                CreateBattleActivity.photoBytes = photoBytes;
            }
            Utils.showToast(this, "Nice photo, " + State.currentUser.getUsername() + "!");
            onBackPressed();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void requestForCameraPermission() {
        String permission = Manifest.permission.CAMERA;
        if (ContextCompat.checkSelfPermission(ParticipatingActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ParticipatingActivity.this, permission)) {
                showPermissionRationaleDialog("Test", permission);
            } else {
                requestForPermission(permission);
            }
        } else {
            launch();
        }
    }

    private void showPermissionRationaleDialog(final String message, final String permission) {
        new AlertDialog.Builder(ParticipatingActivity.this)
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ParticipatingActivity.this.requestForPermission(permission);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void requestForPermission(final String permission) {
        ActivityCompat.requestPermissions(ParticipatingActivity.this, new String[]{permission}, REQUEST_CAMERA_PERMISSION);
    }

    private void launch() {
        Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                final int numOfRequest = grantResults.length;
                final boolean isGranted = numOfRequest == 1 && PackageManager.PERMISSION_GRANTED == grantResults[numOfRequest - 1];
                if (isGranted) {
                    launch();
                }
                break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, gotHereFrom);
        startActivity(intent);
    }
}
