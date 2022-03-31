package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.DEGREES_PER_ROTATION
import org.firstinspires.ftc.teamcode.hardware.Imu

@TeleOp
class Circumference : Drivetrain() {
    private val imu = Imu()

    override fun init() {
        super.init()

        imu.initialize()
    }

    private var firstAngle = 0.0
    private var firstAngleFlag = true
    private var allAngles = Triple(0.0, 0.0, 0.0)
    private var allAnglesFlag = true
    override fun loop() {
        super.loop()

        if (gamepad1.x) {
            for (index in motors.indices) motors[index].power =
                POWER * if (index % 2 == 0) -1 else 1
        } else {
            for (motor in motors) {
                motor.power = 0.0
            }
        }

        if (gamepad1.a) {
            if (firstAngleFlag) {
                firstAngle = imu.firstAngle
                firstAngleFlag = false
            }
        } else {
            firstAngleFlag = true
        }

        if (gamepad1.b) {
            if (allAnglesFlag) {
                allAngles = imu.allAngles
                allAnglesFlag = false
            }
        } else {
            allAnglesFlag = true
        }

        telemetry.addData(
            "x-odometry calculated heading",
            br.currentPosition / 74198.33941731641 * DEGREES_PER_ROTATION
        )
        // constant for y-odometry should be 0.5 of what it is because there are 2 motors, but it's easier to have 1 number
        telemetry.addData(
            "y-odometry calculated heading",
            (-fl.currentPosition + fr.currentPosition) / 133794.1723051728 * DEGREES_PER_ROTATION
        )
        telemetry.addData("first angle", firstAngle)
        telemetry.addData("all angles", allAngles)
    }
}
