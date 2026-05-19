package com.ho.tetrismulti.game

class TetrisBoard(
    val cols: Int = 10,  // 가로 10칸
    val rows: Int = 20   // 세로 20칸
) {
    // 보드 상태 (0 = 빈칸, 나머지 = 색상값)
    val grid = Array(rows) { IntArray(cols) { 0 } }

    var currentPiece: TetrisPiece = spawnPiece()  // 현재 블록
    var score = 0
    var isGameOver = false

    // 새 블록 생성 (랜덤)
    fun spawnPiece(): TetrisPiece {
        val type = PieceType.entries.random()
        return TetrisPiece(type, x = cols / 2 - 2, y = 0)
    }

    // 블록이 해당 위치에 놓일 수 있는지 체크
    fun isValidPosition(piece: TetrisPiece, dx: Int = 0, dy: Int = 0): Boolean {
        for (row in piece.shape.indices) {
            for (col in piece.shape[row].indices) {
                if (piece.shape[row][col] == 0) continue  // 빈칸 스킵

                val newX = piece.x + col + dx
                val newY = piece.y + row + dy

                // 벽 충돌 체크
                if (newX < 0 || newX >= cols) return false
                if (newY >= rows) return false
                if (newY < 0) continue

                // 다른 블록과 충돌 체크
                if (grid[newY][newX] != 0) return false
            }
        }
        return true
    }

    // 블록 왼쪽 이동
    fun moveLeft() {
        if (isValidPosition(currentPiece, dx = -1)) {
            currentPiece.x--
        }
    }

    // 블록 오른쪽 이동
    fun moveRight() {
        if (isValidPosition(currentPiece, dx = 1)) {
            currentPiece.x++
        }
    }

    // 블록 아래로 이동 (자동 낙하)
    fun moveDown(): Boolean {
        return if (isValidPosition(currentPiece, dy = 1)) {
            currentPiece.y++
            true
        } else {
            // 더 내려갈 수 없으면 고정
            placePiece()
            false
        }
    }

    // 블록 즉시 바닥으로 (하드 드롭)
    fun hardDrop() {
        while (moveDown()) { /* 바닥까지 내려감 */ }
    }

    // 블록 회전
    fun rotate() {
        val rotated = currentPiece.rotate()
        val original = currentPiece.shape
        currentPiece.shape = rotated
        // 회전 후 벽 충돌나면 원상복구
        if (!isValidPosition(currentPiece)) {
            currentPiece.shape = original
        }
    }

    // 블록을 보드에 고정
    private fun placePiece() {
        for (row in currentPiece.shape.indices) {
            for (col in currentPiece.shape[row].indices) {
                if (currentPiece.shape[row][col] == 0) continue
                val boardY = currentPiece.y + row
                val boardX = currentPiece.x + col
                if (boardY >= 0) {
                    grid[boardY][boardX] = currentPiece.color()
                }
            }
        }
        clearLines()       // 줄 제거 체크
        currentPiece = spawnPiece()  // 새 블록 생성

        // 새 블록이 놓일 자리 없으면 게임 오버
        if (!isValidPosition(currentPiece)) {
            isGameOver = true
        }
    }

    // 꽉 찬 줄 제거 & 점수 계산
    private fun clearLines() {
        var linesCleared = 0

        // 아래서부터 위로 체크 (테트리스 표준 방식)
        var row = rows - 1
        while (row >= 0) {
            // 해당 줄이 꽉 찼는지 확인
            if (grid[row].all { it != 0 }) {
                // 꽉 찬 줄 제거 — 위 줄들을 한 칸씩 아래로 내림
                for (r in row downTo 1) {
                    for (c in 0 until cols) {
                        grid[r][c] = grid[r - 1][c]
                    }
                }
                // 맨 윗줄 비우기
                for (c in 0 until cols) {
                    grid[0][c] = 0
                }
                linesCleared++
                // row는 그대로 유지 (방금 내려온 줄 다시 체크)
            } else {
                row--
            }
        }

        if (linesCleared == 0) return

        // 점수 계산
        score += when (linesCleared) {
            1 -> 100
            2 -> 300
            3 -> 500
            4 -> 800
            else -> 0
        }
    }
}