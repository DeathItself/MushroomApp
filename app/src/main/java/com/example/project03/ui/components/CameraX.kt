package com.example.project03.ui.components

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.concurrent.Executor


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(){
    val permissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val context = LocalContext.current
    val cameraController = remember{
        LifecycleCameraController(context)
    }

    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit){
        permissionState.launchPermissionRequest()
    }

    val executor = ContextCompat.getMainExecutor(context)
    val capturedImage = remember { mutableStateOf<ImageBitmap?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { CameraTrigger(cameraController, executor, capturedImage, context) },
        floatingActionButtonPosition = FabPosition.Center
    ){padding ->
        if(permissionState.status.isGranted){
            if (capturedImage.value != null) {
                Image(
                    bitmap = capturedImage.value!!,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            } else {
                Camera(cameraController, lifecycle, modifier = Modifier.padding(padding))
            }
        }else{
            Text(text = "Permiso Denegado")
        }
    }
}

@Composable
fun Camera(
    cameraController: LifecycleCameraController,
    lifecycle: LifecycleOwner,
    modifier: Modifier = Modifier
){

    cameraController.bindToLifecycle(lifecycle)
    AndroidView(factory = {context ->
        val previewView = PreviewView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        previewView.controller = cameraController
        previewView
    })
}


@Composable
fun CameraTrigger(
    cameraController: LifecycleCameraController,
    executor: Executor,
    capturedImage: MutableState<ImageBitmap?>,
    context: Context
){
    val file = File.createTempFile("imagetest", ".jpg")
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
    Button(onClick = {
        cameraController.takePicture(outputDirectory, executor, object: ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val bitmap = BitmapFactory.decodeFile(file.absolutePath)
                val imagePath = saveImageToInternalStorage(context, bitmap)
                if (imagePath != null) {
                    println("Imagen guardada en: $imagePath")
                } else {
                    println("Error al guardar la imagen")
                }
                val imageBitmap: ImageBitmap = bitmap.asImageBitmap()
                capturedImage.value = imageBitmap
            }

            override fun onError(exception: ImageCaptureException) {
                println()
            }
        })
    }) {
        Text(text = "Camera")

    }
}

fun saveImageToInternalStorage(context: Context, bitmap: Bitmap): String? {
    val directory = File("/storage/emulated/0/DCIM/Mushtool") // Obtén el directorio de archivos privados de la aplicación
    val fileName = "imagen_${System.currentTimeMillis()}.jpg" // Crea un nombre de archivo único
    val file = File(directory, fileName) // Crea el archivo en el directorio

    var outputStream: OutputStream? = null
    try {
        outputStream = FileOutputStream(file)
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            outputStream
        ) // Guarda la imagen en el archivo
        return file.absolutePath // Devuelve la ruta del archivo guardado
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    } finally {
        outputStream?.close()
    }
}