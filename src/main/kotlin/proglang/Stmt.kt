package proglang

sealed interface Stmt {
    var next: Stmt?
    val lastInSequence: Stmt
        get() {
            var current: Stmt = this
            while (current.next != null) {
                current = current.next!!
            }
            return current
        }

    fun clone(): Stmt

    fun toString(indent: Int): String

    class Assign(
        val name: String,
        val expr: IntExpr,
        override var next: Stmt? = null,
    ) : Stmt {
        override fun toString(indent: Int): String {
            val str: StringBuilder = StringBuilder("${spacen(indent)}$name = $expr\n")
            if (next != null) {
                str.append(next!!.toString(indent))
            }
            return str.toString()
        }

        override fun clone() = Assign(name, expr, next)

        override fun toString(): String = this.toString(0)
    }

    class If(
        val condition: BoolExpr,
        val thenStmt: Stmt,
        val elseStmt: Stmt? = null,
        override var next: Stmt? = null,
    ) : Stmt {
        private val numspace = 4

        override fun toString(indent: Int): String {
            val str = StringBuilder("${spacen(indent)}if ($condition) {\n${thenStmt.toString(indent + numspace)}${spacen(indent)}}")
            if (elseStmt != null) {
                str.append(" else {\n${elseStmt.toString(indent + numspace)}${spacen(indent)}}")
            }
            str.append("\n")
            if (next != null) {
                str.append(next!!.toString(indent))
            }
            return str.toString()
        }

        override fun clone() = If(condition, thenStmt, elseStmt, next)

        override fun toString(): String = this.toString(0)
    }

    class While(
        val condition: BoolExpr,
        val body: Stmt? = null,
        override var next: Stmt? = null,
    ) : Stmt {
        private val numspace = 4

        override fun toString(indent: Int): String {
            val str = StringBuilder("${spacen(indent)}while ($condition) {\n")
            if (body != null) {
                str.append("${body.toString(indent + numspace)}")
            }
            str.append("${spacen(indent)}}\n")
            if (next != null) {
                str.append(next!!.toString(indent))
            }
            return str.toString()
        }

        override fun clone() = While(condition, body, next)

        override fun toString(): String = this.toString(0)
    }
}

fun Stmt.step(store: MutableMap<String, Int>): Stmt? =
    when (this) {
        is Stmt.Assign -> {
            // store[name] = expr.eval(store)
            store.put(name, expr.eval(store))
            this.next
        }
        is Stmt.If -> {
            if (condition.eval(store)) {
                val then = thenStmt
                then.lastInSequence.next = this.next
                then
            } else {
                if (elseStmt != null) {
                    val els = elseStmt
                    els.lastInSequence.next = this.next
                    els
                } else {
                    this.next
                }
            }
        }
        is Stmt.While -> {
            if (this.condition.eval(store)) {
                if (this.body != null) {
                    val loopbody = this.body.clone()
                    loopbody.lastInSequence.next = this
                    loopbody
                } else {
                    this
                }
            } else {
                this.next
            }
        }
    }

private fun spacen(n: Int): String {
    val str: StringBuilder = StringBuilder()
    for (i in 1..n) {
        str.append(" ")
    }
    return str.toString()
}
