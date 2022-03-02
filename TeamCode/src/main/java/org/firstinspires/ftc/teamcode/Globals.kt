package org.firstinspires.ftc.teamcode

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.Telemetry

enum class Alliance(val value: Double) {
    RED(1.0), BLUE(-1.0)
}

lateinit var gamepad1: Gamepad
lateinit var gamepad2: Gamepad

lateinit var telemetry: Telemetry
const val TELEMETRY_FORMAT = "%.2f"
fun Telemetry.addFormattedData(caption: String, value: Any) =
    this.addData(caption, TELEMETRY_FORMAT, value)!!

fun Telemetry.Line.addFormattedData(caption: String, value: Any) =
    this.addData(caption, TELEMETRY_FORMAT, value)!!

fun Telemetry.Item.addFormattedData(caption: String, value: Any) =
    this.addData(caption, TELEMETRY_FORMAT, value)!!

lateinit var hardwareMap: HardwareMap

lateinit var isStopRequested: () -> Boolean
