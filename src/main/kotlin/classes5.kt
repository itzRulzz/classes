/*
Окружность в треугольнике (обязательно использование класса Точка
и класса Треугольник. Класс Окружность и другие классы - по желанию)
Треугольник расположен на координатной плоскости и описан координатами
своих вершин. Написать программу вычисляющую координаты центра вписанной
в треугольник окружности и ее радиус.
*/
import kotlin.math.sqrt

sealed class Either<out L, out R> {
    data class Left<out L>(val value: L) : Either<L, Nothing>()
    data class Right<out R>(val value: R) : Either<Nothing, R>()
}

data class Point(val x: Double, val y: Double) {
    fun distanceTo(other: Point): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy)
    }
}

sealed class CalculationError {
    object DivisionByZero : CalculationError()
    object InvalidTriangle : CalculationError()
    data class Other(val exception: Exception) : CalculationError()
}

class Triangle(val a: Point, val b: Point, val c: Point) {
    fun calculateCircumcircle(): Either<CalculationError, Circle> {
        return try {
            val ax = a.x
            val ay = a.y
            val bx = b.x
            val by = b.y
            val cx = c.x
            val cy = c.y

            val d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by))

            if (d == 0.0) {
                return Either.Left(CalculationError.DivisionByZero)
            }

            val ux = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d
            val uy = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d

            val center = Point(ux, uy)

            val radius = sqrt(a.distanceTo(center) * b.distanceTo(center) * c.distanceTo(center) / (a.distanceTo(b) * b.distanceTo(c) * c.distanceTo(a)))

            Either.Right(Circle(center, radius))
        } catch (e: Exception) {
            Either.Left(CalculationError.Other(e))
        }
    }
}

data class Circle(val center: Point, val radius: Double)

fun user_input(): Either<CalculationError, Triangle> {
    println("Введите вершины треугольника: ")
    val ax = readCoordinate("Введите координаты x и y первой вершины: ", "x1: ")
    val ay = readCoordinate("", "y1: ")
    println()
    val bx = readCoordinate("Введите координаты x и y второй вершины: ", "x2: ")
    val by = readCoordinate("", "y2: ")
    println()
    val cx = readCoordinate("Введите координаты x и y третьей вершины: ", "x3: ")
    val cy = readCoordinate("", "y3: ")
    println()

    val triangle = Triangle(Point(ax, ay), Point(bx, by), Point(cx, cy))

    return if (isTriangleValid(triangle)) {
        Either.Right(triangle)
    } else {
        Either.Left(CalculationError.InvalidTriangle)
    }
}

fun readCoordinate(promptMessage: String, coordinateLabel: String): Double {
    while (true) {
        if (promptMessage.isNotEmpty()) println(promptMessage)
        print(coordinateLabel)
        val input = readLine()?.toDoubleOrNull()
        if (input != null)
            return input
        println("Введено недопустимое значение. Пожалуйста, введите число.")
    }
}

fun isTriangleValid(triangle: Triangle): Boolean {
    // Implement your validation logic here
    val a = triangle.a.distanceTo(triangle.b)
    val b = triangle.b.distanceTo(triangle.c)
    val c = triangle.c.distanceTo(triangle.a)
    return a + b > c && a + c > b && b + c > a
}

fun main() {
    when (val result = user_input()) {
        is Either.Left -> println("Ошибка: ${result.value}")
        is Either.Right -> {
            val triangle = result.value
            when (val circleResult = triangle.calculateCircumcircle()) {
                is Either.Left -> println("Ошибка: ${circleResult.value}")
                is Either.Right -> {
                    val circle = circleResult.value
                    println("Центр окружности, вписанного в треугольник, находится в координатах (${circle.center.x}, ${circle.center.y}), его радиус - ${circle.radius}")
                }
            }
        }
    }
}