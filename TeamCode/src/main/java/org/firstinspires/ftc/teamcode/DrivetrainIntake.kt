package org.firstinspires.ftc.teamcode


import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.Servo

@TeleOp
class DrivetrainIntake : CarouselTest() {
    private val drivetrain = Mecanum()
    private lateinit var intake: DcMotorEx
    private lateinit var lift: DcMotorEx
    private lateinit var scoring: Servo

    override fun init() {
        super.init()

        org.firstinspires.ftc.teamcode.hardwareMap = hardwareMap
        org.firstinspires.ftc.teamcode.telemetry = telemetry
        org.firstinspires.ftc.teamcode.gamepad1 = gamepad1
        org.firstinspires.ftc.teamcode.gamepad2 = gamepad2
        drivetrain.initialize()

        intake = hardwareMap.get(DcMotorEx::class.java, ::intake.name)

        intake.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        intake.targetPosition = 0

        intake.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        intake.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER

        lift = hardwareMap.get(DcMotorEx::class.java, ::lift.name)

        lift.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE

        lift.targetPosition = 0

        lift.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER

        lift.mode = DcMotor.RunMode.RUN_TO_POSITION

        lift.power = 0.8

        scoring = hardwareMap.get(Servo::class.java, ::scoring.name)

    }

    override fun loop() {
        super.loop()
        drivetrain.drive()

        intake.power = if (gamepad1.right_trigger > 0.0) -0.9 else if (gamepad1.left_trigger > 0.0) 0.9 else 0.0
        telemetry.addData("intakeVelocity", intake.power)

//        lift.power = if (gamepad1.left_bumper) -0.5 else if (gamepad1.right_bumper) 0.5 else 0.0
        lift.targetPosition = if (gamepad1.x) 0 else if (gamepad1.y) -812 else if (gamepad1.right_bumper) -2050 else lift.targetPosition
        telemetry.addData("lift targetPosition", lift.targetPosition)

        scoring.position = if (gamepad1.dpad_up || gamepad1.x) 0.56 else if (gamepad1.a) 0.98 else if (gamepad1.b) 0.1 else scoring.position
        telemetry.addData("scoring position", scoring.position)
    }
}
