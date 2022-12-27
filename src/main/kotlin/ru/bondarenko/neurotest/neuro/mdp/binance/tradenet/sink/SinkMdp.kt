package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.sink

import org.deeplearning4j.gym.StepReply
import org.deeplearning4j.rl4j.mdp.MDP
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.deeplearning4j.rl4j.space.ObservationSpace
import org.json.JSONObject
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.DataBaseConfig
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.TradeEnvironment
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.TradeObservationSpace
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.TradeState
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data.TradesAdapter
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.visualise.ShowTrade

class SinkMdp(
    private val netConfig: TradeNetSinkConfig,
    private val startTime: Long,
    private val endTime: Long,
    private val dataAdapter: TradesAdapter,
    private val tradeEnvironment: TradeEnvironment
) : MDP<TradeState, Int, DiscreteSpace> {
    private val dataBaseConfig = DataBaseConfig()
    private var tradeState = TradeState(startTime, dataAdapter)
    private var yesCount = 0
    private var noCount = 0
    private var rightCount = 0
    private var wrongCount = 0
    private var printCounter = 0
    private val visualizer = ShowTrade()
    override fun getObservationSpace(): ObservationSpace<TradeState> {
        return TradeObservationSpace(netConfig.featuresShape, netConfig.normalizationLow, netConfig.normalizationHigh)
    }

    override fun getActionSpace(): DiscreteSpace {
        return netConfig.actionSpace
    }

    override fun reset(): TradeState {
        yesCount = 0
        noCount = 0
        wrongCount = 0
        rightCount = 0
        tradeState = TradeState(startTime, dataAdapter)
        return tradeState
    }

    override fun close() {
        visualizer.close()
    }

    override fun isDone(): Boolean {
        return tradeState.currentTime >= endTime
    }

    override fun newInstance(): MDP<TradeState, Int, DiscreteSpace> {
        return SinkMdp(netConfig, startTime, endTime, dataAdapter, tradeEnvironment)
    }

    override fun step(action: Int?): StepReply<TradeState> {
        val sinkAction = when (action) {
            1 -> SinkAction.YES
            else -> SinkAction.NO
        }

        val reward: Double = if (SinkAction.YES == sinkAction) {
            yesCount++
            val diff =
                tradeEnvironment.getDiff(tradeState.currentTime-netConfig.predictionTime, netConfig.predictionTime)
            if (diff < 0) {
                rightCount++; 1.0
            } else {
                wrongCount++; -1.0
            }
        } else {
            noCount++
            0.0
        }
        tradeState.currentTime += dataBaseConfig.period

        if (printCounter++ >= 500) {
            printCounter = 0
            println("yes: $yesCount no: $noCount wrong: $wrongCount right: $rightCount")
        }

        visualizer.newData(tradeState, sinkAction)
        return StepReply(tradeState, reward, isDone, JSONObject("{}"))
    }
}