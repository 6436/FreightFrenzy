package org.firstinspires.ftc.teamcode


import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

lateinit var gamepad1: Gamepad
lateinit var gamepad2: Gamepad

lateinit var telemetry: Telemetry

lateinit var hardwareMap: HardwareMap

lateinit var isStopRequested: () -> Boolean
