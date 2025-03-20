package proglang
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class ProgramExecutor(
    val body: Stmt,
    val lock: ReentrantLock,
    val pausetime: Long,
    val store: MutableMap<String, Int>,
) : Thread() {
    override fun run() {
        var current: Stmt? = body
        while (current != null) {
            sleep(pausetime)
            lock.withLock {
                current = current!!.step(store)
            }
        }
    }
}
