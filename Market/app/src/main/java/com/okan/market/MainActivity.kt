package com.okan.market

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Android Test and Test are different
 * If you use your in your tests android components like
 * context, resource etc.. you need to write android Test
 * and you need a real device to run these kind of tests.
 * In android test you can not use ` `
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}
