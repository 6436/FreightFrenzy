package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import kotlin.math.roundToInt
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap

@Autonomous(preselectTeleOp = "TeleOp")
class MoveForwardAutonomous : LinearOpMode() {
    private lateinit var fl: DcMotorEx
    private lateinit var fr: DcMotorEx
    private lateinit var bl: DcMotorEx
    private lateinit var br: DcMotorEx
    lateinit var motors: Array<DcMotorEx>

    override fun runOpMode() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalHardwareMap = hardwareMap

        fl = hardwareMap.get(DcMotorEx::class.java, ::fl.name)
        fr = hardwareMap.get(DcMotorEx::class.java, ::fr.name)
        bl = hardwareMap.get(DcMotorEx::class.java, ::bl.name)
        br = hardwareMap.get(DcMotorEx::class.java, ::br.name)
        motors = arrayOf(fl, fr, bl, br)

        for (i in motors.indices) motors[i].direction =
            if (i % 2 == 0) DcMotorSimple.Direction.REVERSE else DcMotorSimple.Direction.FORWARD

        for (motor in motors) {
            motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

            motor.targetPosition = 0

            motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

            motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        }

        waitForStart()

        for (motor in motors) {
            motor.targetPosition = (10 * 43.4653422824).roundToInt()
            motor.mode = DcMotor.RunMode.RUN_TO_POSITION
            motor.power = 0.25
        }
        while (fl.isBusy) {
        }
        for (motor in motors) {
            motor.power = 0.0
        }
    }
}
