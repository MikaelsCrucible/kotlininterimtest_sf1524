package proglang

sealed interface IntExpr {
    class Add(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = lhs.toString() + " + " + rhs.toString()
    }

    class Literal(
        val value: Int,
    ) : IntExpr {
        override fun toString(): String = value.toString()
    }

    class Var(
        val name: String,
    ) : IntExpr {
        override fun toString(): String = name
    }

    class Mul(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = lhs.toString() + " * " + rhs.toString()
    }

    class Sub(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = lhs.toString() + " - " + rhs.toString()
    }

    class Div(
        val lhs: IntExpr,
        val rhs: IntExpr,
    ) : IntExpr {
        override fun toString(): String = lhs.toString() + " / " + rhs.toString()
    }

    class Fact(
        val expr: IntExpr,
    ) : IntExpr {
        override fun toString(): String = expr.toString() + "!"
    }

    class Paren(
        val expr: IntExpr,
    ) : IntExpr {
        override fun toString(): String = "($expr)"
    }
}

fun IntExpr.eval(store: Map<String, Int>): Int =
    when (this) {
        is IntExpr.Add -> lhs.eval(store) + rhs.eval(store)
        is IntExpr.Literal -> value
        is IntExpr.Var -> store[name] ?: throw UndefinedBehaviourException("Such variable doesn't exist.")
        is IntExpr.Mul -> lhs.eval(store) * rhs.eval(store)
        is IntExpr.Sub -> lhs.eval(store) - rhs.eval(store)
        is IntExpr.Div ->
            if (rhs.eval(store) ==
                0
            ) {
                throw UndefinedBehaviourException("Cannot be divided by 0.")
            } else {
                lhs.eval(store) / rhs.eval(store)
            }
        is IntExpr.Fact ->
            if (expr.eval(store) <
                0
            ) {
                throw UndefinedBehaviourException("Factorial for negative number doesn't exists")
            } else {
                facth(expr.eval(store))
            }
        is IntExpr.Paren -> expr.eval(store)
        // else -> throw UnsupportedOperationException("The above should account for all kinds of IntExpr.")
    }

private fun facth(n: Int): Int {
    if (n == 0 || n == 1) return 1
    return facth(n - 1) * n
}
