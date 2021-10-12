package org.firstinspires.ftc.teamcode

import kotlin.math.*

fun Double.toRadians() = this * PI / 180.0

typealias Point = Vector
data class Vector(val x: Double = 0.0, val y: Double = 0.0) {
    constructor(x: Number, y: Number) : this(x.toDouble(), y.toDouble())

    val magnitude = hypot(x, y)

    private val heading = atan2(y, x)

    fun rotatedAboutOrigin(displacementAngle: Double): Vector {
        val heading = this.heading + displacementAngle.toRadians()
        return Vector(
            magnitude * cos(heading),
            magnitude * sin(heading)
        )
    }

    operator fun plus(other: Vector): Vector {
        return Vector(
            x + other.x,
            y + other.y
        )
    }

    operator fun minus(other: Vector): Vector {
        return Vector(
            x - other.x,
            y - other.y
        )
    }

    operator fun times(scalar: Double): Vector {
        return Vector(
            x * scalar,
            y * scalar
        )
    }

    operator fun div(scalar: Double): Vector {
        return Vector(
            x / scalar,
            y / scalar
        )
    }
}
