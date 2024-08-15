package com.example.myapplication

import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvDeviceInfo: TextView = findViewById(R.id.tvDeviceInfo)
        tvDeviceInfo.text = getDeviceInfo()

        val tvCameraInfo: TextView = findViewById(R.id.tvCameraInfo)
        tvCameraInfo.text = getCameraInfo()

        val tvScreenSize: TextView = findViewById(R.id.tvScreenSize)
        tvScreenSize.text = getScreenSizeInfo()

        val tvScreenDensity: TextView = findViewById(R.id.tvScreenDensity)
        tvScreenDensity.text = getScreenDensityInfo()

        val tvRamInfo: TextView = findViewById(R.id.tvRamInfo)
        tvRamInfo.text = getRamInfo()

        val tvStorageInfo: TextView = findViewById(R.id.tvStorageInfo)
        tvStorageInfo.text = getStorageInfo()
    }

    private fun getDeviceInfo(): String {
        val sb = StringBuilder()
        sb.append("Modelo: ").append(Build.MODEL).append("\n")
        sb.append("Fabricante: ").append(Build.MANUFACTURER).append("\n")
        sb.append("Versión de Android: ").append(Build.VERSION.RELEASE).append("\n")
        sb.append("SDK: ").append(Build.VERSION.SDK_INT).append("\n")
        sb.append("Procesador: ").append(Build.HARDWARE).append("\n")
        sb.append("Display Info: ").append(Build.DISPLAY).append("\n")
        sb.append("Densidad:").append(resources.displayMetrics.densityDpi).append(" DPI \n")
        sb.append("RAM total: ").append(Runtime.getRuntime().totalMemory() / (1024 * 1024)).append(" MB \n")

        return sb.toString()
    }

    private fun getCameraInfo(): String {
        val cameraInfo = StringBuilder()
        val cameraManager = getSystemService(CAMERA_SERVICE) as android.hardware.camera2.CameraManager
        cameraManager.cameraIdList.forEachIndexed { index, id ->
            val characteristics = cameraManager.getCameraCharacteristics(id)
            val facing = characteristics.get(android.hardware.camera2.CameraCharacteristics.LENS_FACING)
            val facingString = when (facing) {
                android.hardware.camera2.CameraCharacteristics.LENS_FACING_FRONT -> "Frontal"
                android.hardware.camera2.CameraCharacteristics.LENS_FACING_BACK -> "Trasera"
                else -> "Otra"
            }
            cameraInfo.append("Cámara ${index + 1}: $facingString\n")
        }
        return cameraInfo.toString()
    }

    private fun getScreenSizeInfo(): String {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        val widthPixels = metrics.widthPixels
        val heightPixels = metrics.heightPixels
        val density = metrics.density
        val widthDp = widthPixels / density
        val heightDp = heightPixels / density

        val screenSizeInches = Math.sqrt(Math.pow(widthDp.toDouble(), 2.0) + Math.pow(heightDp.toDouble(), 2.0)) / 160.0
        return "Tamaño de: %.2f pulgadas".format(screenSizeInches)
    }

    private fun getScreenDensityInfo(): String {
        return "Densidad píxeles: ${resources.displayMetrics.densityDpi} DPI"
    }

    private fun getRamInfo(): String {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        val availableMemoryMB = memoryInfo.availMem / (1024 * 1024)
        return "Memoria RAM: $availableMemoryMB MB"
    }

    private fun getStorageInfo(): String {
        val totalStorage = Environment.getExternalStorageDirectory().totalSpace / (1024 * 1024)
        val availableStorage = Environment.getExternalStorageDirectory().usableSpace / (1024 * 1024)
        return "Almacenamiento total: $totalStorage MB\nAlmacenamiento disponible: $availableStorage MB"
    }
}