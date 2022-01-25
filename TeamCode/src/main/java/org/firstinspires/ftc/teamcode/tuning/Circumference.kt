package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardware.Imu
import kotlin.math.absoluteValue

@TeleOp
class Circumference : Drivetrain() {
    private val imu = Imu()

    override fun init() {
        super.init()

        imu.initialize()
    }

    private var angle = 0.0
    private var flag = true
    override fun loop() {
        super.loop()

        if (gamepad1.x) {
            for (i in motors.indices) motors[i].power = POWER * if (i % 2 == 0) 1 else -1
        } else {
            for (motor in motors) {
                motor.power = 0.0
            }
        }

        if (gamepad1.b) {
            if (flag) {
                angle = imu.angle
                flag = false
            }
        } else {
            flag = true
        }

        telemetry.addData("average", motors.map { it.currentPosition.absoluteValue }.average())
        telemetry.addData("angle", angle)
    }
}
