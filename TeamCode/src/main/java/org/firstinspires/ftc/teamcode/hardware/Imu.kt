package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.teamcode.hardwareMap
import kotlin.math.abs
import kotlin.math.sign

class Imu {
    lateinit var imu: BNO055IMU

    fun initialize() {
        val parameterz = BNO055IMU.Parameters()

        parameterz.angleUnit = BNO055IMU.AngleUnit.DEGREES

        imu = hardwareMap.get(BNO055IMU::class.java, ::imu.name)

        imu.initialize(parameterz)
    }

    val firstAngle get() = imu.angularOrientation.firstAngle.toDouble()
    val allAngles get() = Triple(imu.angularOrientation.firstAngle.toDouble(),imu.angularOrientation.secondAngle.toDouble(), imu.angularOrientation.thirdAngle.toDouble())

    private var lastAngle = 0.0
    var heading = 0.0
        get() {
            var changeHeading = firstAngle - lastAngle

            if (abs(changeHeading) > 180.0) changeHeading -= sign(changeHeading) * 360.0

            field += changeHeading

            lastAngle = firstAngle
            return field
        }
}
