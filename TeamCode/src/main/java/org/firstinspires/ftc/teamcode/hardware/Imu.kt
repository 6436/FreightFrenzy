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

    val angle
        get() = imu.angularOrientation.firstAngle.toDouble()

    private var lastAngle = 0.0
    var heading = 0.0
        get() {
            var changeHeading = angle - lastAngle

            if (abs(changeHeading) > 180.0) changeHeading -= sign(changeHeading) * 360.0

            field += changeHeading

            lastAngle = angle
            return field
        }
}
