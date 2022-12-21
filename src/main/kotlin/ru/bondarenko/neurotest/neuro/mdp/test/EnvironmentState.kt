package ru.bondarenko.neurotest.neuro.mdp.test

import kotlin.random.Random


class EnvironmentState {
    private var steps = 0
    private var directionSteps = 0
    var direction = Direction.NONE
    private val changeDirectionProbability = 1
    private val interactionPower = 0.8
    val minX = -10
    val maxX = 10
    var fails = 0

    fun renderFrame(state: TestState) {
        // may be change direction?
        if (randomBoolProbability(changeDirectionProbability)) {
            direction = randomDirection()
            directionSteps = 0
        }

        when (direction) {
            Direction.RIGHT -> state.x += interactionPower
            Direction.LEFT -> state.x -= interactionPower
            else -> {}
        }

        steps++
        directionSteps++
    }


    private fun randomBoolProbability(percent: Int): Boolean {
        return Random.nextInt(0, 99) < percent
    }

    private fun randomDirection(): Direction {
        return Direction.values()[Random.nextInt(0, Direction.values().size - 1)]
    }

    fun outOfRange(state: TestState): Boolean {
        return !(state.x > minX && state.x < maxX)
    }

    enum class Direction {
        RIGHT, LEFT, NONE
    }


}

