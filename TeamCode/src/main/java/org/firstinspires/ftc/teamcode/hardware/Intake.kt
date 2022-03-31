package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.gamepad1
import org.firstinspires.ftc.teamcode.hardwareMap
import org.firstinspires.ftc.teamcode.telemetry

class Intake {
    private companion object {
        const val VERY_SLOW_POWER = 0.1
        const val FAST_POWER = 0.75
        const val POWER = 0.6
    }

    private lateinit var intake: DcMotorEx

    fun initialize() {
        intake = hardwareMap.get(DcMotorEx::class.java, ::intake.name)

        intake.direction = DcMotorSimple.Direction.REVERSE

        intake.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        intake.targetPosition = 0

        intake.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        intake.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun update() {
        when {
            gamepad1.right_trigger > 0.0 -> suck()
            gamepad1.left_trigger > 0.0 -> spit()
            else -> off()
        }
    }

    fun verySlowSpit() {
        intake.power = -VERY_SLOW_POWER
    }

    fun fastSuck() {
        intake.power = FAST_POWER
    }

    fun suck() {
        intake.power = POWER
    }

    fun spit() {
        intake.power = -POWER
    }

    fun off() {
        intake.power = 0.0
    }

    fun telemetry() {
        telemetry.addData("intake power", intake.power)
    }
}
