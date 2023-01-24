package org.firstinspires.ftc.teamcode;
import java.util.List;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;


import java.util.ArrayList;

@Autonomous(name = "Terminal right")
public class move_to_terminal extends LinearOpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotor ClawHeight = null;
    private Servo Claw = null;
    public static final int DC_MOTOR_COUNTS_PER_REV = 28;
    public static final int DC_MOTOR_GEAR_RATIO = 2400;
    public static final int DC_MOTOR_COUNTS = (int)((DC_MOTOR_COUNTS_PER_REV * DC_MOTOR_GEAR_RATIO) / Math.PI);
    public static final int DRIVETRAIN_WHEEL_DIAMETER = 4;
    public static final int DRIVETRAIN_COUNTS_PER_INCH = DC_MOTOR_COUNTS / DRIVETRAIN_WHEEL_DIAMETER;

    
         public void drive(double speed, int inches, double power) {

        int target = inches * DRIVETRAIN_COUNTS_PER_INCH;
        rightFrontDrive.setTargetPosition(target);
        leftFrontDrive.setTargetPosition(target);
        rightBackDrive.setTargetPosition(target);
        leftBackDrive.setTargetPosition(target);


        }        
        public void turnleft(double radians) {
            double distance = 2 * Math.PI * radius * (radians/(2*Math.PI));
            int target = (int) (distance * DRIVETRAIN_COUNTS_PER_INCH);
            rightFrontDrive.setTargetPosition(-target);
            leftFrontDrive.setTargetPosition(target);
            rightBackDrive.setTargetPosition(-target);
            leftBackDrive.setTargetPosition(target);
        }                
        public void turnright(double radians) {
            double distance = 2 * Math.PI * radius * (radians/(2*Math.PI));
            int target = (int) (distance * DRIVETRAIN_COUNTS_PER_INCH);
            rightFrontDrive.setTargetPosition(target);
            leftFrontDrive.setTargetPosition(-target);
            rightBackDrive.setTargetPosition(target);
            leftBackDrive.setTargetPosition(-target);
        }
    @Override
    public void runOpMode()
    {
        leftFrontDrive  = hardwareMap.get(DcMotor.class, "left_front_drive");
        leftBackDrive  = hardwareMap.get(DcMotor.class, "left_back_drive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "right_front_drive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "right_back_drive");
        ClawHeight = hardwareMap.get(DcMotor.class, "Claw_height");
        Claw = hardwareMap.get(Servo.class, "Claw");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        ClawHeight.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ClawHeight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        while (opModeIsActive()) {
            try {
            turnleft(Math.PI/2);
            drive(1000, 48, 0.7);
            telemetry.addLine("Done");
            }
            catch (Exception e) {
                telemetry.addLine("Error", + e.getmessage());
            }
        }
    }
}