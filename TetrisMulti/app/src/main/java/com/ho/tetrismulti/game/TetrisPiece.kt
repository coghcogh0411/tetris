package com.ho.tetrismulti.game

// 블록 색상 (각 모양별)
enum class PieceType(val color: Int) {
    I(0xFF00FFFF.toInt()),  // 하늘색
    O(0xFFFFFF00.toInt()),  // 노란색
    T(0xFF800080.toInt()),  // 보라색
    S(0xFF00FF00.toInt()),  // 초록색
    Z(0xFFFF0000.toInt()),  // 빨간색
    J(0xFF0000FF.toInt()),  // 파란색
    L(0xFFFF8000.toInt())   // 주황색
}

// 각 블록 모양 정의 (1 = 채워진 칸)
val PIECES = mapOf(
    PieceType.I to arrayOf(
        intArrayOf(0,0,0,0),
        intArrayOf(1,1,1,1),
        intArrayOf(0,0,0,0),
        intArrayOf(0,0,0,0)
    ),
    PieceType.O to arrayOf(
        intArrayOf(1,1),
        intArrayOf(1,1)
    ),
    PieceType.T to arrayOf(
        intArrayOf(0,1,0),
        intArrayOf(1,1,1),
        intArrayOf(0,0,0)
    ),
    PieceType.S to arrayOf(
        intArrayOf(0,1,1),
        intArrayOf(1,1,0),
        intArrayOf(0,0,0)
    ),
    PieceType.Z to arrayOf(
        intArrayOf(1,1,0),
        intArrayOf(0,1,1),
        intArrayOf(0,0,0)
    ),
    PieceType.J to arrayOf(
        intArrayOf(1,0,0),
        intArrayOf(1,1,1),
        intArrayOf(0,0,0)
    ),
    PieceType.L to arrayOf(
        intArrayOf(0,0,1),
        intArrayOf(1,1,1),
        intArrayOf(0,0,0)
    )
)

data class TetrisPiece(
    val type: PieceType,
    var x: Int = 3,   // 보드 위 x 위치
    var y: Int = 0,   // 보드 위 y 위치
    var shape: Array<IntArray> = PIECES[type]!!
) {
    // 블록 회전 (시계 방향)
    fun rotate(): Array<IntArray> {
        val size = shape.size
        val rotated = Array(size) { IntArray(size) }
        for (row in 0 until size) {
            for (col in 0 until size) {
                rotated[col][size - 1 - row] = shape[row][col]
            }
        }
        return rotated
    }

    // 현재 타입의 색상 반환
    fun color() = type.color
}