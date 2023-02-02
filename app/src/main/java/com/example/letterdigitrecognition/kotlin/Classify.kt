package com.example.letterdigitrecognition.kotlin

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Classify(private val context: Context) {
    private var interpreter: Interpreter?=null
    var isInitialized = false
        private set

    private var executorService: ExecutorService = Executors.newCachedThreadPool()

    private var inputImageHeight:Int = 0
    private var inputImageWidth:Int = 0
    private var modelInputSize:Int = 0

    fun initialize(): Task<Void> {
        val task = TaskCompletionSource<Void>()
        executorService.execute {
            try {
                inInitializeInterpreter()
                task.setResult(null)

            }catch (e: IOException){
                task.setException(e)
            }
        }
        return task.task
    }

    private fun inInitializeInterpreter() {
        val assetManager = context.assets
        val model = loadModelFile(assetManager,"mnist.tflite")
        var options = Interpreter.Options()
        options.setUseNNAPI(true)
        val interpreter = Interpreter(model)


        val inputShape = interpreter.getInputTensor(0).shape()

        inputImageHeight = inputShape[2]
        inputImageWidth = inputShape[1]
        modelInputSize = FLOAT_TYPE * inputImageWidth * inputImageHeight * PIXEL_VALUE

        this.interpreter = interpreter

        isInitialized = true



    }

    private fun loadModelFile(assetManager: AssetManager, filename:String): ByteBuffer {
        val fileDescriptor = assetManager.openFd(filename)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startoffset = fileDescriptor.startOffset
        val declaredlength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredlength)
    }

    fun classifyAsyn(bitmap: Bitmap):Task<Int> {
        val task = TaskCompletionSource<Int>()
        executorService.execute {
            val result = classify(bitmap)
            task.setResult(result)
        }
        return task.task
    }

    private fun converBitmaptoBytebuffer(bitmap: Bitmap):ByteBuffer{
        val byteBuffer = ByteBuffer.allocateDirect(modelInputSize)
        byteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(inputImageHeight*inputImageWidth)
        bitmap.getPixels(pixels,0,bitmap.width, 0, 0, bitmap.width,bitmap.height)

        for (pixelValue in pixels){
            val r = (pixelValue shr 16 and 0xFF)
            val g = (pixelValue shr 8 and 0xFF)
            val b = (pixelValue and 0xFF)

            val normalizedPixel = (r+g+b)/3.0f/255.0f
            byteBuffer.putFloat(normalizedPixel)
        }
        return byteBuffer
    }

    private fun classify(bitmap: Bitmap): Int {
        check(isInitialized){"TF lite is not initialized"}

        val resizeImage = Bitmap.createScaledBitmap(
            bitmap, inputImageWidth, inputImageHeight, true
        )

        val byteBffer = converBitmaptoBytebuffer(resizeImage)

        val output = Array(1){FloatArray(OUTPUT_CLASS)}

        interpreter?.run(byteBffer,output)

        val result = output[0]

        val maxindex = result.indices.maxBy { result[it] }

//        val resultString = "Prediction result %d\n Confidence :%2f".format(maxindex,result[maxindex])
//        return resultString
//


        val resultInt = maxindex
        return resultInt.toInt()

    }

    fun close() {
        executorService.execute {
            interpreter?.close()
        }
    }

    companion object {
        private const val FLOAT_TYPE = 4
        private const val PIXEL_VALUE = 1
        private const val OUTPUT_CLASS = 47
    }


}