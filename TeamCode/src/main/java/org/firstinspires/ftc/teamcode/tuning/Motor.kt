package org.firstinspires.ftc.teamcode.tuning

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx

@TeleOp
class Motor : OpMode() {
    private companion object {
        const val INCREMENT = 0.05
    }

    private lateinit var motor: DcMotorEx

    override fun init() {
        motor = hardwareMap.get(DcMotorEx::class.java, ::motor.name)

        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        motor.targetPosition = 0

        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    private var power = 0.0
    private var flag = true
    override fun loop() {
        if (gamepad1.x) {
            if (flag) {
                power += INCREMENT
                flag = false
            }
        } else if (gamepad1.y) {
            if (flag) {
                power -= INCREMENT
                flag = false
            }
        } else {
            flag = true
        }

        motor.power = power

        telemetry.addData("power", power)
    }
}
