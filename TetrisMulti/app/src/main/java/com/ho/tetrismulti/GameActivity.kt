package com.ho.tetrismulti

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ho.tetrismulti.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 어떤 모드로 진입했는지 확인
        val mode = intent.getStringExtra("mode") ?: "single"

        if (mode == "single") {
            // 상대방 보드 숨기기
            binding.tvOpponentScore.visibility = View.GONE
            binding.opponentBoardView.visibility = View.GONE
        }
    }
}