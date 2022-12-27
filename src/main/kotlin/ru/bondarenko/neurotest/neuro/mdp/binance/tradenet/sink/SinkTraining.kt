package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.sink

import org.deeplearning4j.rl4j.learning.Learning
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense
import org.deeplearning4j.rl4j.network.dqn.IDQN
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.deeplearning4j.rl4j.util.DataManager
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import ru.bondarenko.neurotest.neuro.mdp.binance.repository.TradesRepo
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.DQNFactoryMultilayerDense
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.TradeEnvironment
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.TradeState
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data.TradesAdapter

@Component
open class SinkTraining(val repo: TradesRepo) : CommandLineRunner {
    private val tradeNetConfig = TradeNetSinkConfig()
    override fun run(vararg args: String?) {
        runLearning()
    }

    private fun runLearning() {
        //record the training data in rl4j-data in a new folder
        val manager = DataManager()
        val netConfig = TradeNetSinkConfig()
        val tradesAdapter = TradesAdapter(repo)
        //define the mdp
        val mdp = SinkMdp(netConfig, 1608681602604, 1608687896675, tradesAdapter, TradeEnvironment(tradesAdapter, netConfig))

        //define the training method
        val dql: Learning<TradeState, Int, DiscreteSpace, IDQN<*>> =
            QLearningDiscreteDense(mdp, DQNFactoryMultilayerDense(tradeNetConfig.conf), tradeNetConfig.testQl, manager)

        //start the training
        dql.train()
        manager.save(dql)
        mdp.close()
    }
}