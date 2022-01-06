package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import kotlin.math.sign
import kotlin.math.abs

class Imu {
    private lateinit var imu: BNO055IMU

    fun initialize() {
        val parameterz = BNO055IMU.Parameters()

        parameterz.angleUnit = BNO055IMU.AngleUnit.DEGREES

        imu = hardwareMap.get(BNO055IMU::class.java, ::imu.name)

        imu.initialize(parameterz)
    }

    private var lastAngle = 0.0
    var heading = 0.0
        get() {
            val angle = imu.angularOrientation.firstAngle.toDouble()
//
//            var changeHeading = angle - lastAngle
//
//            if (abs(changeHeading) > 180.0) changeHeading -= sign(changeHeading) * 360.0
//
//            field += changeHeading
//
//            lastAngle = angle
//            return field
            return angle
        }
}
