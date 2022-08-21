package com.nirvana.blog.activity.luckydraw

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nirvana.blog.R

class GameActivity : AppCompatActivity() {

    companion object {
        var iconArray = arrayOf(
            R.mipmap.ac0,
            R.mipmap.ac1,
            R.mipmap.ac2,
            R.mipmap.ac3,
            R.mipmap.ac4,
            R.mipmap.ac5,
            R.mipmap.ac6,
            R.mipmap.ac7
        )
        var nameArray = arrayOf(
            R.string.game_name_0,
            R.string.game_name_1,
            R.string.game_name_2,
            R.string.game_name_3,
            R.string.game_name_4,
            R.string.game_name_5,
            R.string.game_name_6,
            R.string.game_name_7
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_main)
    }

}