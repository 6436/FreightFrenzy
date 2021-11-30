package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
//import com.qualcomm.hardware.lynx.LynxModule
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
//    private lateinit var hubs: List<LynxModule>

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

//        hubs = org.firstinspires.ftc.teamcode.hardwareMap.getAll(LynxModule::class.java)
//        for (i in hubs) i.bulkCachingMode = LynxModule.BulkCachingMode.MANUAL

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
//        for (i in hubs) i.clearBulkCache()

        drivetrain.update()

        intakeMotor.power = if (gamepad1.left_trigger > 0.0) 0.55 else if (gamepad1.right_bumper) -0.55 else if (gamepad1.right_trigger > 0.0) -0.95 else 0.0


        when {
            gamepad2.right_bumper -> {
                liftMotor.targetPosition = -2800
            }
            gamepad2.y -> {
                liftMotor.targetPosition = -1200
//                liftMotor.targetPosition+= 1
            }
            gamepad2.x -> {
                liftMotor.targetPosition = 0
//                liftMotor.targetPosition -= 1
            }
        }
        liftMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        liftMotor.power = 0.5

        when {
            gamepad2.a -> {
                liftServo.position = 0.358
            }
            gamepad2.b -> {
                liftServo.position = 0.8
            }
            gamepad2.left_trigger > 0.0 -> {
                liftMotor.targetPosition = 500
                                liftServo.position =0.2
            }
            gamepad2.right_trigger > 0.0 -> {
                                liftServo.position -= 0.001
            }
            gamepad2.left_bumper -> {
                liftServo.position += 0.001
            }
        }


        drivetrain.telemetry()
        telemetry.addData("liftServo", liftServo.position)
        telemetry.addData("liftMotor", liftMotor.currentPosition)
    }
}
