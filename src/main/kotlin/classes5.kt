/*
Окружность в треугольнике (обязательно использование класса Точка
и класса Треугольник. Класс Окружность и другие классы - по желанию)
Треугольник расположен на координатной плоскости и описан координатами
своих вершин. Написать программу вычисляющую координаты центра вписанной
в треугольник окружности и ее радиус.
*/
import kotlin.math.sqrt

data class Point(val x: Double, val y: Double) {
    fun distanceTo(other: Point): Double {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy)
    }
}

class Triangle(val a: Point, val b: Point, val c: Point) {
    fun calculateCircumcircle(): Circle? {
        try {
            val ax = a.x
            val ay = a.y
            val bx = b.x
            val by = b.y
            val cx = c.x
            val cy = c.y

            val d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by))

            val ux = ((ax * ax + ay * ay) * (by - cy) + (bx * bx + by * by) * (cy - ay) + (cx * cx + cy * cy) * (ay - by)) / d
            val uy = ((ax * ax + ay * ay) * (cx - bx) + (bx * bx + by * by) * (ax - cx) + (cx * cx + cy * cy) * (bx - ax)) / d

            val center = Point(ux, uy)

            val radius = sqrt(a.distanceTo(center) * b.distanceTo(center) * c.distanceTo(center) / (a.distanceTo(b) * b.distanceTo(c) * c.distanceTo(a)))

            return Circle(center, radius)
        } catch (e: Exception) {
            println("Не удалось вычислить описанную окружность: ${e.message}")
            return null
        }
    }
}

data class Circle(val center: Point, val radius: Double)

fun user_input(): Triangle {
    println("Введите вершины треугольника: ")
    val ax = readCoordinate("Введите координаты x и y первой вершины: ", "x1: ")
    val ay = readCoordinate("", "y1: ")
    println()
    val bx = readCoordinate("Введите координаты x и y второй вершины: ", "x2: ")
    val by = readCoordinate("", "y2: ")
    println()
    val cx = readCoordinate("Введите координаты x и y третьей вершины: ", "x3: ")
    val cy = readCoordinate("", "y3: ")
    return Triangle(Point(ax, ay), Point(bx, by), Point(cx, cy))
}

fun readCoordinate(promptMessage: String, coordinateLabel: String): Double {
    while (true) {
        if (promptMessage.isNotEmpty()) println(promptMessage)
            print(coordinateLabel)
        val input = readlnOrNull()?.toDoubleOrNull()
        if (input != null)
            return input
        println("Введено недопустимое значение. Пожалуйста, введите число.")
    }
}

fun main() {
    val triangle = user_input()
    val circle = triangle.calculateCircumcircle()
    println()
    if (circle != null) {
        if (circle.center.x == Double.NEGATIVE_INFINITY || circle.center.y == Double.NEGATIVE_INFINITY
            || circle.center.x == Double.POSITIVE_INFINITY || circle.center.y == Double.POSITIVE_INFINITY)
            println("Вы ввели несуществующий треугольник.")
        else
            println("Центр окружности, вписанного в треугольник, находится в координатах (${circle.center.x}, ${circle.center.y}), его радиус - ${circle.radius}")
    }
}
