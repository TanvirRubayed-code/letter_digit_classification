package com.example.letterdigitrecognition.kotlin

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import com.example.letterdigitrecognition.R
import com.divyanshu.draw.widget.DrawView


class ClassifyDigitActivity : AppCompatActivity() {

    private var drawView:DrawView?=null
    private var clearButton: Button?=null
    private var predictionTextView: TextView?=null
    private val Classify = Classify(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_classify_digit2)

        drawView = findViewById(R.id.draw_view)
        drawView?.setStrokeWidth(50.0f)
        drawView?.setColor(Color.WHITE)
        drawView?.setBackgroundColor(Color.BLACK)

        predictionTextView = findViewById(R.id.prediction_text_view)
        clearButton = findViewById(R.id.clear_button)

        clearButton?.setOnClickListener{
            drawView?.clearCanvas()
            predictionTextView?.text = getString(R.string.prediction_placeholder)

        }

        drawView?.setOnTouchListener { _, motionEvent ->
            drawView?.onTouchEvent(motionEvent)
            if(motionEvent.action== MotionEvent.ACTION_UP){
                classifyDrawing()
            }
            true
        }
        Classify.initialize().addOnFailureListener { e-> Log.e(TAG,"onCreate error",e) }


    }

    override fun onDestroy() {
        Classify.close()
        super.onDestroy()

    }

    private fun classifyDrawing() {
        val bitmap = drawView?.getBitmap()
        if((bitmap!=null) && (Classify.isInitialized)){
            Classify.classifyAsyn(bitmap).addOnSuccessListener { resultText ->
                predictionTextView?.text = resultText
            }
                .addOnFailureListener { _->
                    predictionTextView?.text = getString(R.string.classification_error)
                }
        }
    }



    companion object{
        private val TAG = "MainActivity"
    }
}