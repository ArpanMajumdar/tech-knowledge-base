package com.arpan.buttonclickapp

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

private val TAG = "MainActivity"
private const val TEXT_CONTENT = "TextContent"

class MainActivity : AppCompatActivity() {

    private val userInput by lazy { findViewById<EditText>(R.id.editText) }
    private val button by lazy { findViewById<Button>(R.id.button) }
    private val textView by lazy { findViewById<TextView>(R.id.textView) }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: called")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.clear()
        userInput.clear()
        textView.movementMethod = ScrollingMovementMethod()

        button.setOnClickListener {
            Log.d(TAG, "onClick: called")
            textView.apply {
                append(userInput.text)
                append("\n")
            }
            userInput.clear()
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart: called")
        super.onStart()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        Log.d(TAG, "onRestoreInstanceState: called")
        super.onRestoreInstanceState(savedInstanceState)
        textView.text = savedInstanceState.getString(TEXT_CONTENT)
    }

    override fun onResume() {
        Log.d(TAG, "onResume: called")
        super.onResume()
    }

    override fun onPause() {
        Log.d(TAG, "onPause: called")
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "onSaveInstanceState: called")
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_CONTENT, textView.text.toString())
    }

    override fun onStop() {
        Log.d(TAG, "onStop: called")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: called")
        super.onDestroy()
    }

    override fun onRestart() {
        Log.d(TAG, "onRestart: called")
        super.onRestart()
    }

    private fun TextView.clear() {
        this.text = ""
    }

}
