package com.example.project03.ui.components

import android.Manifest
import android.view.ViewGroup
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project03.ui.navigation.BottomNavigationBar
import com.example.project03.ui.navigation.ContentBottomSheet
import com.example.project03.viewmodel.MainViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.util.concurrent.Executor


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(navController: NavController){
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = { CameraTrigger(cameraController, executor) },
        floatingActionButtonPosition = FabPosition.Center
    ){padding ->
        if(permissionState.status.isGranted){
            Camera(cameraController, lifecycle, modifier = Modifier.padding(padding))
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
fun CameraTrigger(cameraController: LifecycleCameraController, executor: Executor){
    Button(onClick = { /*TODO*/ }) {
        Text(text = "Camera")
        val file = File.createTempFile("imagetest", ".jpg")
        val outputDirectory = ImageCapture.OutputFileOptions.Builder(file).build()
        cameraController.takePicture(outputDirectory, executor, object: ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println(outputFileResults.savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                println()
            }

        })
    }


}