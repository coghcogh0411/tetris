package com.ho.tetrismulti.game

import kotlinx.coroutines.*

class GameEngine(
    val board: TetrisBoard,
    val onUpdate: () -> Unit,       // 화면 다시 그리기
    val onScoreUpdate: () -> Unit,  // 점수 업데이트
    val onGameOver: () -> Unit      // 게임 오버
) {
    private var job: Job? = null
    private val scope = CoroutineScope(Dispatchers.Main)

    // 게임 시작
    fun start() {
        job = scope.launch {
            while (isActive) {
                delay(1000)

                if (board.isGameOver) {
                    onGameOver()
                    break
                }

                board.moveDown()
                onUpdate()
                onScoreUpdate()

            }
        }
    }

    // 일시정지
    fun pause() {
        job?.cancel()
    }

    // 재개
    fun resume() {
        start()
    }

    // 종료
    fun stop() {
        job?.cancel()
        scope.cancel()
    }
}