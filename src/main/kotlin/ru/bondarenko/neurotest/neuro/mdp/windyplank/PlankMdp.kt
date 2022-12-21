package ru.bondarenko.neurotest.neuro.mdp.windyplank

import org.deeplearning4j.gym.StepReply
import org.deeplearning4j.rl4j.mdp.MDP
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.deeplearning4j.rl4j.space.ObservationSpace
import org.json.JSONObject

class PlankMdp : MDP<PlankState, Int, DiscreteSpace> {

    private val actionsSize = 2
    private val rewardAmount = 1.0
    private val environment = EnvironmentState()
    private val visualizer = ShowMePlank(environment)
    private val inputNum = 1

    //    private val observationSpace: ObservationSpace<TestState> = ArrayObservationSpace(IntArray(1))
    override fun getObservationSpace(): ObservationSpace<PlankState> {
        return PlankObservationSpace(IntArray(1){inputNum})
    }

    private val actionSpace: DiscreteSpace = DiscreteSpace(actionsSize)
    override fun getActionSpace(): DiscreteSpace {
        return actionSpace
    }

    private var plankState = PlankState()

    override fun reset(): PlankState {
        plankState = PlankState()
        return plankState
    }

    override fun close() {}

    override fun isDone(): Boolean {
        return environment.outOfRange(plankState)
    }

    override fun newInstance(): MDP<PlankState, Int, DiscreteSpace> {
        return PlankMdp()
    }

    override fun step(action: Int): StepReply<PlankState> {
        when (action) {
            0 -> plankState.dec()
            1 -> plankState.inc()
        }

        val reward = when {
            environment.outOfRange(plankState) -> {
                environment.fails ++
                -1.0
            }else -> 0.0
//                rewardAmount - abs(testState.x) * 0.1
        }

        plankState.step++

        environment.renderFrame(plankState)
        visualizer.update(plankState)

        return StepReply(plankState, reward, isDone, JSONObject("{}"))
    }
}