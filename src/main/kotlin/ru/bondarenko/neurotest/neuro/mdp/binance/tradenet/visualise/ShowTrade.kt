package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.visualise

import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.TradeState
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.config.TradeAction
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data.TradesAdapter
import java.awt.*
import java.awt.event.WindowEvent
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import javax.swing.JFrame
import javax.swing.JPanel

class ShowTrade {
    private val title = "Show test"
    private val chartPanel: ChartPanel = ChartPanel()
    private val frame = JFrame()

    init {
        EventQueue.invokeLater {
            frame.title = title
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = BorderLayout()
            frame.add(chartPanel, BorderLayout.CENTER)
            frame.pack()
            frame.isVisible = true
        }
    }

    fun close() {
        frame.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
        frame.dispatchEvent(WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
    }

    fun newData(state: TradeState, action: TradeAction) {
        chartPanel.lastState = state
        chartPanel.addData(state.data, action)
        chartPanel.repaint()
    }

    class ChartPanel : JPanel() {
        var lastState: TradeState? = null
        private val border = 100
        private val xWidth = 1000
        private val yHeight = 1000
        private var scaleY = 0.5
        private var shiftY = 20000.0
        private val entities = ConcurrentLinkedQueue<VisualState>()

        init {
            layout = BorderLayout()
            this.preferredSize = Dimension(xWidth, yHeight)
        }

        // this will set the preferred size of the jpanel to be one that fits the image
        override fun getPreferredSize(): Dimension {
            return Dimension(xWidth, yHeight)
        }

        fun addData(data: Optional<TradesAdapter.Data>, action: TradeAction) {
            if (data.isEmpty) return
            if (lastState == null) return
            entities.add(VisualState(data.get(), action,  lastState!!.lastReward))
            while (entities.size >= xWidth) entities.remove(entities.first())
        }

        override fun paintComponent(g: Graphics) {
            super.paintComponent(g)
            if (g is Graphics2D) {
                var x = 0
                var minVal = Double.MAX_VALUE
                var maxVal = Double.MIN_VALUE
                var prevY = 0.0
                entities.stream().forEach { state ->
                    var y = (state.data.price - shiftY)
                    y *= scaleY

                    g.color=Color.BLACK
                    g.drawLine(x - 1, yHeight - prevY.toInt(), x, yHeight - y.toInt())

                    g.color = pickDolor(state)
                    if (state.tradeAction == TradeAction.DOWN) {
                        g.drawOval(x, yHeight - y.toInt(), 5, 5)
                    } else {
                        g.drawOval(x, yHeight - y.toInt(), 5, 5)
                    }
                    minVal = minVal.coerceAtMost(state.data.price)
                    maxVal = maxVal.coerceAtLeast(state.data.price)
                    x++
                    prevY = y
                }
                g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                )

                scaleY = yHeight.toDouble() / (maxVal - minVal)
                shiftY = minVal

            }
        }
        fun pickDolor(state: VisualState): Color{
            return when{
                state.reward > 0 -> Color.GREEN
                state.reward < 0 -> Color.RED
                else    -> Color.BLUE
            }
        }

        data class VisualState(val data: TradesAdapter.Data, val tradeAction: TradeAction, val reward: Double)
    }
}