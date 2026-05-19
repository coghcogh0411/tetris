package com.ho.tetrismulti.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.ho.tetrismulti.game.TetrisBoard

class TetrisBoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    var board: TetrisBoard? = null  // 외부에서 보드 연결

    private val paint = Paint()

    // 한 칸의 크기 (뷰 크기에 맞게 자동 계산)
    private val cellSize: Float
        get() = width / (board?.cols ?: 10).toFloat()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val b = board ?: return  // 보드 없으면 그리지 않음

        drawBackground(canvas, b)
        drawGrid(canvas, b)
        drawCurrentPiece(canvas, b)
        drawGhost(canvas, b)     // 블록이 떨어질 위치 미리보기
    }

    // 배경 그리기
    private fun drawBackground(canvas: Canvas, board: TetrisBoard) {
        // 전체 배경
        paint.color = Color.parseColor("#1a1a2e")
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // 고정된 블록 그리기
        for (row in 0 until board.rows) {
            for (col in 0 until board.cols) {
                val color = board.grid[row][col]
                if (color != 0) {
                    drawCell(canvas, col, row, color)
                }
            }
        }
    }

    // 격자선 그리기
    private fun drawGrid(canvas: Canvas, board: TetrisBoard) {
        paint.color = Color.parseColor("#2a2a4e")
        paint.strokeWidth = 1f
        paint.style = Paint.Style.STROKE

        for (col in 0..board.cols) {
            canvas.drawLine(
                col * cellSize, 0f,
                col * cellSize, height.toFloat(),
                paint
            )
        }
        for (row in 0..board.rows) {
            canvas.drawLine(
                0f, row * cellSize,
                width.toFloat(), row * cellSize,
                paint
            )
        }
        paint.style = Paint.Style.FILL
    }

    // 현재 떨어지는 블록 그리기
    private fun drawCurrentPiece(canvas: Canvas, board: TetrisBoard) {
        val piece = board.currentPiece
        for (row in piece.shape.indices) {
            for (col in piece.shape[row].indices) {
                if (piece.shape[row][col] == 0) continue
                drawCell(canvas, piece.x + col, piece.y + row, piece.color())
            }
        }
    }

    // 고스트 블록 (떨어질 위치 미리보기)
    private fun drawGhost(canvas: Canvas, board: TetrisBoard) {
        val piece = board.currentPiece

        // 바닥까지 얼마나 내려갈 수 있는지 계산
        var ghostY = piece.y
        while (board.isValidPosition(piece, dy = ghostY - piece.y + 1)) {
            ghostY++
        }
        if (ghostY == piece.y) return  // 이미 바닥이면 스킵

        // 반투명하게 그리기
        for (row in piece.shape.indices) {
            for (col in piece.shape[row].indices) {
                if (piece.shape[row][col] == 0) continue
                drawCellGhost(canvas, piece.x + col, ghostY + row, piece.color())
            }
        }
    }

    // 일반 셀 그리기
    private fun drawCell(canvas: Canvas, col: Int, row: Int, color: Int) {
        val left = col * cellSize + 1f
        val top = row * cellSize + 1f
        val right = left + cellSize - 2f
        val bottom = top + cellSize - 2f

        // 블록 본체
        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawRect(left, top, right, bottom, paint)

        // 블록 테두리 (입체감)
        paint.color = Color.WHITE
        paint.alpha = 60
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawRect(left, top, right, bottom, paint)
        paint.alpha = 255
        paint.style = Paint.Style.FILL
    }

    // 고스트 셀 그리기 (반투명)
    private fun drawCellGhost(canvas: Canvas, col: Int, row: Int, color: Int) {
        val left = col * cellSize + 1f
        val top = row * cellSize + 1f
        val right = left + cellSize - 2f
        val bottom = top + cellSize - 2f

        paint.color = color
        paint.alpha = 60  // 반투명
        paint.style = Paint.Style.FILL
        canvas.drawRect(left, top, right, bottom, paint)
        paint.alpha = 255
    }
}