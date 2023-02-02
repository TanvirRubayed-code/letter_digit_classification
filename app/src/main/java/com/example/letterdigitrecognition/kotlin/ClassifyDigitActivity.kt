package com.example.letterdigitrecognition.kotlin

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.divyanshu.draw.widget.DrawView
import com.example.letterdigitrecognition.R


@Suppress("DEPRECATION")
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


        //Change status bar coclor

        //Change status bar coclor
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.statusbar)
    }

    override fun onDestroy() {
        Classify.close()
        super.onDestroy()

    }

    private fun classifyDrawing() {
        val bitmap = drawView?.getBitmap()
        if((bitmap!=null) && (Classify.isInitialized)){
            Classify.classifyAsyn(bitmap).addOnSuccessListener { resultText ->
//                predictionTextView?.text = resultText.toString()

                var prediction_resuto: String? = null
                when(resultText){
                    0-> prediction_resuto = "0"
                    1-> prediction_resuto = "1"
                    2-> prediction_resuto = "2"
                    3-> prediction_resuto = "3"
                    4-> prediction_resuto = "4"
                    5-> prediction_resuto = "5"
                    6-> prediction_resuto = "6"
                    7-> prediction_resuto = "7"
                    8-> prediction_resuto = "8"
                    9-> prediction_resuto = "9"
                    10-> prediction_resuto = "A"
                    11-> prediction_resuto = "B"
                    12-> prediction_resuto = "C"
                    13-> prediction_resuto = "D"
                    14-> prediction_resuto = "E"
                    15-> prediction_resuto = "F"
                    16-> prediction_resuto = "G"
                    17-> prediction_resuto = "H"
                    18-> prediction_resuto = "I"
                    19-> prediction_resuto = "J"
                    20-> prediction_resuto = "K"
                    21-> prediction_resuto = "L"
                    22-> prediction_resuto = "M"
                    23-> prediction_resuto = "N"
                    24-> prediction_resuto = "O"
                    25-> prediction_resuto = "P"
                    26-> prediction_resuto = "Q"
                    27-> prediction_resuto = "R"
                    28-> prediction_resuto = "S"
                    29-> prediction_resuto = "T"
                    30-> prediction_resuto = "U"
                    31-> prediction_resuto = "V"
                    32-> prediction_resuto = "W"
                    33-> prediction_resuto = "X"
                    34-> prediction_resuto = "Y"
                    35-> prediction_resuto = "Z"
                    36-> prediction_resuto = "a"
                    37-> prediction_resuto = "b"
                    38-> prediction_resuto = "d"
                    39-> prediction_resuto = "e"
                    40-> prediction_resuto = "f"
                    41-> prediction_resuto = "g"
                    42-> prediction_resuto = "h"
                    43-> prediction_resuto = "n"
                    44-> prediction_resuto = "q"
                    45-> prediction_resuto = "r"
                    46-> prediction_resuto = "t"


                }
                predictionTextView?.text = prediction_resuto


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