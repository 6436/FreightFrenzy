package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.hardware.lynx.LynxModule
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo
import org.firstinspires.ftc.teamcode.hardwareMap as globalHardwareMap
import org.firstinspires.ftc.teamcode.telemetry as globalTelemetry
import org.firstinspires.ftc.teamcode.gamepad1 as globalGamepad1
import org.firstinspires.ftc.teamcode.gamepad2 as globalGamepad2

@TeleOp
class TeleOp : OpMode() {
    private lateinit var hubs: List<LynxModule>

    private val drivetrain = Mecanum()
    private lateinit var liftMotor: DcMotorEx
    private lateinit var intakeMotor: DcMotorEx
    private lateinit var intakeServo: Servo
    private lateinit var liftServo: Servo

    override fun init() {
        telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)

        globalHardwareMap = hardwareMap
        globalTelemetry = telemetry as MultipleTelemetry
        globalGamepad1 = gamepad1
        globalGamepad2 = gamepad2

        hubs = org.firstinspires.ftc.teamcode.hardwareMap.getAll(LynxModule::class.java)
        for (i in hubs) i.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

        drivetrain.initialize()

        liftMotor = hardwareMap.get(DcMotorEx::class.java, ::liftMotor.name)
        liftServo = hardwareMap.get(Servo::class.java, ::liftServo.name)
        intakeMotor = hardwareMap.get(DcMotorEx::class.java, ::intakeMotor.name)
        intakeServo = hardwareMap.get(Servo::class.java, ::intakeServo.name)

        liftMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        liftMotor.targetPosition = 0

        liftMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        liftMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        intakeMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        intakeMotor.targetPosition = 0

        intakeMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        intakeMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun loop() {
        for (i in hubs) i.clearBulkCache()

        drivetrain.update()

        intakeMotor.power = if (gamepad1.left_bumper) -0.5 else 0.0

        when {
            gamepad1.dpad_up -> {
                intakeServo.position = 0.7
            }
            gamepad1.dpad_down -> {
                intakeServo.position = 0.0
            }
            gamepad1.dpad_right -> {
                intakeServo.position += 0.01
            }
            gamepad1.dpad_left -> {
                intakeServo.position -= 0.01
            }
        }

        liftMotor.power =
            when {
                gamepad1.x -> {
                    0.5
                }
                gamepad1.y -> {
                    -0.5
                }
                else -> {
                    0.0
                }
            }

        if (gamepad1.a ) {
            liftServo.position += 0.01
        } else if (gamepad1.b) {
            liftServo.position -= 0.01
        }


        drivetrain.telemetry()
    }
}
