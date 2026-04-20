package com.ho.tetrismulti

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.ho.tetrismulti.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // ↓ 버튼 클릭 추가
        binding.btnSinglePlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("mode", "single")
            startActivity(intent)
        }

        binding.btnMultiPlay.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("mode", "multi")
            startActivity(intent)
        }
    }
}