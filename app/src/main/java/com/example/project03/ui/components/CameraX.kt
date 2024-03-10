package com.example.project03.ui.components

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.exifinterface.media.ExifInterface
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.project03.R
import com.example.project03.model.ImagePath
import com.example.project03.ui.screens.addMushrooms.Screenshot
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(navController: NavController){
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }

    val context = LocalContext.current
    val cameraController = remember{
        LifecycleCameraController(context)
    }
    val executor = ContextCompat.getMainExecutor(context)

    val lifecycle = LocalLifecycleOwner.current
    val capturedImage = remember { mutableStateOf<Bitmap?>(null) }

    if(permissionState.status.isGranted){
        if (capturedImage.value != null) {
            Screenshot(navController)
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Camera(
                    cameraController,
                    lifecycle,
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.BottomCenter
            ){
                CameraTrigger(cameraController, executor , capturedImage)
            }
        }
    }else{
        Text(text = stringResource(R.string.permission_denied))
    }

}

@Composable
fun Camera(
    cameraController: LifecycleCameraController,
    lifecycle: LifecycleOwner,
){
    val aspectRatio = 3f / 4f
    Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                val previewView = PreviewView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
                cameraController.bindToLifecycle(lifecycle)
                previewView.controller = cameraController
                previewView
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(
                    aspectRatio,
                    matchHeightConstraintsFirst = true
                )
                .padding(aspectRatio.dp)
        )
    }
}

@Composable
fun CameraTrigger(
    cameraController: LifecycleCameraController,
    executor: Executor,
    capturedImage: MutableState<Bitmap?>
){
    val file = File.createTempFile("imagetest", ".jpg")
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    Button(
        onClick = {
            cameraController.takePicture(outputDirectory, executor, object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                    val imagePath = saveImageToInternalStorage(bitmap, file.absolutePath)
                    if (imagePath != null) {
                        println("Imagen guardada en: $imagePath")
                        ImagePath.imagePath = imagePath
                    } else {
                        println("Error al guardar la imagen")
                    }
                    capturedImage.value = bitmap
                }

                override fun onError(exception: ImageCaptureException) {
                    println()
                }
            })
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        )

    ) {
        Icon(Icons.Default.Camera, contentDescription = "Tomar foto")
    }
}

fun saveImageToInternalStorage(bitmap: Bitmap, imagePath: String): String? {
    val directory = File("/storage/emulated/0/DCIM/Mushtool")
    val fileName = "imagen_${System.currentTimeMillis()}.jpg"
    val file = File(directory, fileName)

    var outputStream: OutputStream? = null
    return try {
        outputStream = FileOutputStream(file)
        val correctedBitmap = correctImageOrientation(bitmap, imagePath)
        correctedBitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            outputStream
        )
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        outputStream?.close()
    }
}

fun correctImageOrientation(bitmap: Bitmap, imagePath: String): Bitmap {
    val exif = ExifInterface(imagePath)
    val orientation = exif.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_UNDEFINED
    )

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270f)
        else -> bitmap
    }
}


fun rotateImage(bitmap: Bitmap, degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}