package ru.pl.sunset

import android.animation.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation.INFINITE
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import ru.pl.sunset.databinding.ActivityMainBinding

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val blueSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.blue_sky)
    }

    private val sunsetSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.sunset_sky)
    }

    private val nightSkyColor: Int by lazy {
        ContextCompat.getColor(this, R.color.night_sky)
    }

    private var isDay = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        startPulsing(binding.sun)
        startPulsing(binding.sunReflection)

        binding.scene.setOnClickListener {
            startAnimation()
        }

    }


    private fun startAnimation() {
        val sunYStart = binding.sun.top.toFloat()
        val sunYEnd = binding.sky.bottom.toFloat() + 50


        val sunReflectionYStart = binding.sunReflection.top.toFloat()
        val sunReflectionYEnd = -binding.sunReflection.height.toFloat() - 50

        val heightAnimator = ObjectAnimator
            .ofFloat(binding.sun, "y", sunYStart, sunYEnd)
            .setDuration(3000)
        heightAnimator.interpolator = AccelerateInterpolator()

        val heightReflectionAnimator = ObjectAnimator
            .ofFloat(binding.sunReflection, "y", sunReflectionYStart, sunReflectionYEnd)
            .setDuration(3000)
        heightReflectionAnimator.interpolator = AccelerateInterpolator()


        val sunsetSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", blueSkyColor, sunsetSkyColor)
            .setDuration(3000)
        sunsetSkyAnimator.setEvaluator(ArgbEvaluator())

        val nightSkyAnimator = ObjectAnimator
            .ofInt(binding.sky, "backgroundColor", sunsetSkyColor, nightSkyColor)
            .setDuration(1500)
        nightSkyAnimator.setEvaluator(ArgbEvaluator())

        val animatorSet = AnimatorSet()
        animatorSet.play(heightAnimator)
            .with(heightReflectionAnimator)
            .with(sunsetSkyAnimator)
            .before(nightSkyAnimator)

        isDay = if (isDay) {
            animatorSet.start()
            false
        } else {
            animatorSet.reverse()
            true
        }

    }


    private fun startPulsing(view: View) {
        val firstScale = 1f
        val secondScale = 1.2f

        val pulseX = PropertyValuesHolder.ofFloat(View.SCALE_X, firstScale, secondScale)
        val pulseY = PropertyValuesHolder.ofFloat(View.SCALE_Y, firstScale, secondScale)

        ObjectAnimator
            .ofPropertyValuesHolder(view, pulseX, pulseY).apply {
                duration = 2000
                repeatCount = INFINITE
                repeatMode = ValueAnimator.REVERSE
                start()
            }
    }
}