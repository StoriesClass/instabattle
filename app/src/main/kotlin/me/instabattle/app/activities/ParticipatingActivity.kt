package me.instabattle.app.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import me.instabattle.app.R
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import org.jetbrains.anko.*
import permissions.dispatcher.*

@RuntimePermissions
class ParticipatingActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_test)

        showCameraWithPermissionCheck()
    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @OnShowRationale(Manifest.permission.CAMERA)
    fun showRationaleForCamera(request: PermissionRequest) {
        AlertDialog.Builder(this@ParticipatingActivity)
                .setMessage("Test")
                .setPositiveButton("Ok") { _, _ -> request.proceed() }
                .setNegativeButton("Cancel") { _, _ -> request.cancel() }
                .show()
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    fun showCamera() {
        startActivity<CameraViewActivity>()
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    fun showDeniedForCamera() {
        toast("Camera permission denied")
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    fun showNeverAskForCamera() {
        toast("Never ask camera permission")
    }

    override fun onBackPressed() {
        val intent = Intent(this, gotHereFrom)
        startActivity(intent)
    }

    companion object {
        var gotHereFrom: Class<*>? = null
    }
}
