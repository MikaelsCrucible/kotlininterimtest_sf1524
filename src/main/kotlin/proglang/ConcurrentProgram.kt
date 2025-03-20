package proglang
import java.util.concurrent.locks.ReentrantLock

class ConcurrentProgram(
    val bodies: List<Stmt>,
    val times: List<Long>,
    val lock: ReentrantLock = ReentrantLock(),
) {
    init {
        if (bodies.size != times.size) {
            throw IllegalArgumentException()
        }
    }

    fun execute(store: Map<String, Int>): Map<String, Int> {
        val workingstore: MutableMap<String, Int> = store.toMutableMap()
        val threads: MutableList<ProgramExecutor> = mutableListOf()
        try {
            for (i in 0..<bodies.size) {
                val t = ProgramExecutor(bodies[i], lock, times[i], workingstore)
                t.start()
                threads.add(t)
            }
            for (thread in threads) {
                thread.join()
            }
        } catch (_: UndefinedBehaviourException) {
        }
        return workingstore
    }
}
