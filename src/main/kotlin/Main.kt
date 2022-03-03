
fun main() {
    val ex1: List<Int> = Nil
    val ex3: List<String> = Cons("a", Cons("b", Nil))
    val ex4: List<Int> = Cons(1, Cons(2, Cons(4, Nil)))
    val ex5 = Cons(5,Cons(6, Cons(7, Nil)))
    val ex6 = List.of(List.of(1,2,3), List.of(4), List.of(5,6))
    println(flatten(ex6))
    print(flatMap(List.of(1, 2, 3)) { i -> List.of(i, i) })
}