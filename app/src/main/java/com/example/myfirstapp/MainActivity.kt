package com.example.myfirstapp

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.text.style.UpdateAppearance
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


        /* animateImage(imageView1, 0)
        animateImage(imageView2, 1000)
        animateImage(imageView3, 2000)
        animateImage(imageView4, 3000)*/


        val secondLineText: TextView = findViewById(R.id.secondLineText)
        val symbolText: TextView = findViewById(R.id.symbolText)

        val characterContainer = findViewById<LinearLayout>(R.id.characterContainer)
        val textView = findViewById<TextView>(R.id.myTextView)
        val textSP = "This is an example of clickable text."

        applySpanToTextView(
            textView,
            textSP,
            "clickable text",
            Color.BLUE
        ) {
            Toast.makeText(this@MainActivity, "Clicked!", Toast.LENGTH_SHORT).show()
        }

        val text = "Get Started!"

        animateText(text, characterContainer, this@MainActivity)

        Handler().postDelayed({
            val words = secondLineText.text.toString().split(" ")
            secondLineText.text = ""

            for (i in words.indices) {
                secondLineText.visibility = View.VISIBLE
                Handler().postDelayed({
                    secondLineText.append("${words[i]} ")
                    val anim = AnimationUtils.loadAnimation(this, R.anim.second_line_word_anima)
                    secondLineText.startAnimation(anim)
                }, i * 500L)
            }
        }, text.length * 150L)

        Handler().postDelayed({
            symbolText.visibility = View.VISIBLE
            val anim = AnimationUtils.loadAnimation(this, R.anim.second_line_symbol_anima)
            symbolText.startAnimation(anim)
        }, text.length * 150L + secondLineText.text.length * 200L)
    }
}

fun animateImage(imageView: ImageView, delay: Long) {
    imageView.visibility = View.INVISIBLE

    imageView.postDelayed({
        imageView.visibility = View.VISIBLE

        val scaleX = ObjectAnimator.ofFloat(imageView, View.SCALE_X, 0.5f, 1f)
        val scaleY = ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 0.5f, 1f)
        scaleX.duration = 500
        scaleY.duration = 500

        val alpha = ObjectAnimator.ofFloat(imageView, View.ALPHA, 0f, 1f)
        alpha.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, alpha)
        animatorSet.start()
    }, delay)
}

private fun animateText(text: String, animatedTextView: LinearLayout, context: Context) {

    for (i in text.indices) {
        Handler().postDelayed({
            val characterTextView = LayoutInflater.from(context)
                .inflate(R.layout.character_text_view, animatedTextView, false) as TextView
            characterTextView.text = text[i].toString()
            animatedTextView.addView(characterTextView)

            val anim = AnimationUtils.loadAnimation(context, R.anim.new_frist)
            characterTextView.startAnimation(anim)
        }, i * 100L)
    }
}

private fun applySpanToTextView(
    textView: TextView,
    fullText: String,
    spanText: String,
    spanColor: Int,
    onClickAction: () -> Unit
) {
    val spannableString = SpannableString(fullText)
    val startIndex = fullText.indexOf(spanText)
    val endIndex = startIndex + spanText.length

    if (startIndex == -1) return // Span text not found in full text

    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClickAction()
        }
    }

    spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableString.setSpan(ForegroundColorSpan(spanColor), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    spannableString.setSpan(UnderlineSpan(), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    textView.text = spannableString
    textView.movementMethod = LinkMovementMethod.getInstance()
}
