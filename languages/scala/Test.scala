abstract class List[T] {
  def reduceLeft(op: (T, T) => T): T = this match {
    case Nil     => throw new Error("Nil.reduiceLeft is not defined")
    case x :: xs => (xs foldLeft x)(op)
  }

  def foldLeft[U](z: U)(op: (U, T) => U): U = this match {
    case Nil     => z
    case x :: xs => (xs foldLeft op(z, x))(op)
  }

  def reduceRight(op: (T, T) => T): T = this match {
    case Nil      => throw new Error("Nil.reduceRight not defined")
    case x :: Nil => x
    case x :: xs  => op(x, xs.reduceRight(op))
  }

  def foldRight[U](z: U)(op: (T, U) => U): U = this match {
    case Nil     => z
    case x :: xs => op(x, (xs foldRight z)(op))
  }

  def concat[T](xs: List[T], ys: List[T]): List[T] = (xs foldRight ys)(_ :: _)
}

object Test extends App {

  def factorial(n: Int): Int = {
    if (n == 0) 1
    else n * factorial(n - 1)
  }

  def concat[T](xs: List[T], ys: List[T]): List[T] = xs match {
    case List()   => ys
    case x :: xs1 => x :: concat(xs1, ys)
  }
}
