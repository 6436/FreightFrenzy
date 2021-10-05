package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2

@TeleOp
class TeleOp : OpMode() {
    private val drivetrain = MecanumDrivetrain()

    override fun init() {
        globalHardwareMap = hardwareMap
        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2

        drivetrain.initialize()
    }

    override fun loop() {
        drivetrain.update()
    }
}
