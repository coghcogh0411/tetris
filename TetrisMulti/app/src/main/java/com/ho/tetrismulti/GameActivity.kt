package com.ho.tetrismulti

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ho.tetrismulti.databinding.ActivityGameBinding
import com.ho.tetrismulti.game.GameEngine
import com.ho.tetrismulti.game.TetrisBoard

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var engine: GameEngine
    private val board = TetrisBoard()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mode = intent.getStringExtra("mode") ?: "single"

        if (mode == "single") {
            binding.tvOpponentScore.visibility = View.GONE
            binding.opponentBoardView.visibility = View.GONE
        }

        // 보드 뷰 연결
        binding.myBoardView.board = board

        // 게임 엔진 생성
        engine = GameEngine(
            board = board,
            onUpdate = {
                binding.myBoardView.invalidate()  // 화면 다시 그리기
            },
            onScoreUpdate = {
                binding.tvMyScore.text = "점수: ${board.score}"
            },
            onGameOver = {
                showGameOverDialog()
            }
        )

        // 버튼 조작
        binding.btnLeft.setOnClickListener {
            board.moveLeft()
            binding.myBoardView.invalidate()
        }

        binding.btnRight.setOnClickListener {
            board.moveRight()
            binding.myBoardView.invalidate()
        }

        binding.btnRotate.setOnClickListener {
            board.rotate()
            binding.myBoardView.invalidate()
        }

        binding.btnDown.setOnClickListener {
            board.hardDrop()
            binding.myBoardView.invalidate()
            binding.tvMyScore.text = "점수: ${board.score}"
        }

        // 게임 시작!
        engine.start()
    }

    // 게임 오버 다이얼로그
    private fun showGameOverDialog() {
        AlertDialog.Builder(this)
            .setTitle("게임 오버")
            .setMessage("점수: ${board.score}")
            .setPositiveButton("다시 시작") { _, _ ->
                recreate()  // 액티비티 재시작
            }
            .setNegativeButton("메뉴로") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .show()
    }

    // 앱 백그라운드 시 일시정지
    override fun onPause() {
        super.onPause()
        engine.pause()
    }

    // 앱 복귀 시 재개
    override fun onResume() {
        super.onResume()
        if (!board.isGameOver) {
            engine.resume()
        }
    }

    // 액티비티 종료 시 엔진 정리
    override fun onDestroy() {
        super.onDestroy()
        engine.stop()
    }
}