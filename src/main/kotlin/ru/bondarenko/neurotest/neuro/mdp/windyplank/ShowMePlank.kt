package ru.bondarenko.neurotest.neuro.mdp.windyplank

import java.awt.*
import javax.swing.JFrame
import javax.swing.JPanel


class ShowMePlank(environment: EnvironmentState) {
    private val title = "Show test"
    private val envPanel: EnvPanel = EnvPanel(environment)

    init {
        EventQueue.invokeLater {
            val frame = JFrame()
            frame.title = title
            frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
            frame.layout = BorderLayout()
            frame.add(envPanel, BorderLayout.CENTER)
            frame.pack()
            frame.isVisible = true
        }
    }

    fun update(state: PlankState) {
        envPanel.lastState = state
        envPanel.repaint()
    }

    class EnvPanel//setSize((environment.maxX - environment.minX) * scale, scale)
        (private val environment: EnvironmentState) : JPanel() {
        var lastState = PlankState()
        private val scale = 100
        private val shiftX = 10
        private val shiftY = 1

        init {
            layout = BorderLayout()
            this.preferredSize = Dimension((environment.maxX - environment.minX) * scale, scale)
        }

        // this will set the preferred size of the jpanel to be one that fits the image
        override fun getPreferredSize(): Dimension {
            return Dimension((environment.maxX - environment.minX) * scale +10, 200)
        }

        override fun paintComponent(g: Graphics) {
            val firstLineY=40
            val lineHeight=20
            super.paintComponent(g)
            g.drawLine(
                (shiftX + environment.minX) * scale,
                shiftY * scale,
                (shiftX + environment.maxX) * scale,
                shiftY * scale
            )
            g.fillRect(
                (shiftX + lastState.x.toInt()) * scale,
                shiftY * scale,
                scale, scale
            )

            if (g is Graphics2D) {
                g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                )
                g.drawString("Fails: " + environment.fails, 1, firstLineY)
                g.drawString("Direction: " + environment.direction, 1, firstLineY+lineHeight)
            }
        }
    }

}