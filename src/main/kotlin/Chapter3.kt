sealed class List<out A> {
    companion object {
        fun <A> of(vararg aa: A): List<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }
}

object Nil : List<Nothing>()

data class Cons<out A>(val head: A, val tail: List<A>) : List<A>()

fun <A, B> foldRight(xs: List<A>, init: B, f: (A, B) -> B): B =
    when (xs) {
        is Nil -> init
        is Cons -> f(xs.head, foldRight(xs.tail, init, f))
    }

fun <A> append(xs: List<A>, x: A): List<A> =
    when (xs) {
        is Nil -> Cons(x, Nil)
        is Cons -> Cons(xs.head, append(xs.tail, x))
    }

fun <A> append(xs: List<A>, ys: List<A>): List<A> =
    when (xs) {
        is Nil -> ys
        is Cons -> Cons(xs.head, append(xs.tail, ys))
    }

// 3.8
fun <A> length(xs: List<A>) =
    foldRight(xs, 0) { _, acc -> acc + 1 }

// 3.9
tailrec fun <A, B> foldLeft(xs: List<A>, init: B, f: (B, A) -> B): B =
    when (xs) {
        is Nil -> init
        is Cons -> foldLeft(xs.tail, f(init, xs.head), f)
    }

// 3.10
fun sum(xs: List<Int>) = foldLeft(xs, 0, Int::plus)
fun product(xs: List<Int>) = foldLeft(xs, 0, Int::times)

// 3.11
fun <A> reverse(xs: List<A>) = foldLeft(xs, Nil as List<A>) { acc, x -> Cons(x, acc) }

// 3.12
fun <A, B> foldLeft2(xs: List<A>, init: B, f: (B, A) -> B) =
    foldRight(xs,
        { x: B -> x },
        { cur, acc ->
            { x -> acc(f(x, cur)) }
        }
    ) (init)

fun <A, B> foldRight2(xs: List<A>, init: B, f: (A, B) -> B) =
    foldLeft(xs,
        { x: B -> x },
        { acc, cur ->
            { x -> acc(f(cur, x)) }
        }
    ) (init)

// 3.13
fun <A> append2(xs: List<A>, ys: List<A>) =
    foldRight(xs, ys) { cur, acc -> Cons(cur, acc) }

// 3.14
fun <A> flatten(xxs: List<List<A>>) =
    foldLeft(xxs, Nil as List<A>) { acc, xs -> append(acc, xs) }

// 3.15
fun increment(xs: List<Int>) =
    foldRight(xs, Nil as List<Int>) { cur, acc -> Cons(1 + cur, acc) }

// 3.17
fun <A, B> map(xs: List<A>, f: (A) -> B) =
    foldRight(xs, Nil as List<B>) { cur, acc -> Cons(f(cur), acc) }

// 3.18
fun <A> filter(xs: List<A>, f: (A) -> Boolean): List<A> =
    foldRight(xs, Nil as List<A>) { cur, acc -> if (f(cur)) Cons(cur, acc) else acc }

// 3.19
fun <A, B> flatMap(xa: List<A>, f: (A) -> List<B>): List<B> = flatten(map(xa, f))

// 3.20
fun <A> filter2(xs: List<A>, f: (A) -> Boolean): List<A> =
    flatMap(xs) { x -> if (f(x)) Nil else Cons(x, Nil) }

// 3.22
fun <A, B> zip(xs: List<A>, ys: List<B>): List<Pair<A, B>> = when (xs) {
    is Nil -> Nil
    is Cons -> when (ys) {
        is Nil -> Nil
        is Cons -> Cons(Pair(xs.head, ys.head), zip(xs.tail, ys.tail))
    }
}

fun <A, B, C> zipWith(xs: List<A>, ys: List<B>, f: (A, B) -> C): List<C> =
    map(zip(xs, ys)) { (x, y) -> f(x, y) }

