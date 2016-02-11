package org.usfirst.frc.team348.robot;

//***************************************************************************************************//
//to do
// 1. update the how to document
// 2. add the variable map to this document at the top with remark statements
// 3. 
// 4. 

//***************************************************************************************************//
//variable database
/*

 * driveCimLF,driveCimLB,driveCimRF,driveCimRB > talonsrx > PID > drive motors
 * shootCim > talonsrx > shooting motor talon
 * joystickL,joystickR > drive joysticks
 * box > breakout box
 * dashboard > smart dashboard
 * shiftSol,flailSol > double solenoids
 * shootSol > single solenoid
 */




import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

public class Robot extends IterativeRobot {
    CANTalon driveCimLF,driveCimLB,driveCimRF,driveCimRB,shootCim;
    Joystick joystickL,joystickR,box;
    SmartDashboard dashboard;
    DoubleSolenoid shiftSol,flailSol;
    Solenoid shootSol;
    
    public void robotInit() {
    	joystickL= new Joystick(2);
    	joystickR= new Joystick(1);
        driveCimLF= new CANTalon(1);
        driveCimLB= new CANTalon(3);
        driveCimRF= new CANTalon(2);
        driveCimRB= new CANTalon(4);
        shootCim= new CANTalon(6);
        dashboard= new SmartDashboard();
        shiftSol= new DoubleSolenoid(0,1);
        flailSol= new DoubleSolenoid(2,3);
        shootSol= new Solenoid(4);
        box= new Joystick(0);
    }
 
    public void autonomousInit() {
    	driveCimLF.changeControlMode(TalonControlMode.Speed);
    	driveCimLB.changeControlMode(TalonControlMode.Follower);
    	driveCimRF.changeControlMode(TalonControlMode.Speed);
    	driveCimRB.changeControlMode(TalonControlMode.Follower);
    	driveCimLF.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	driveCimLF.reverseSensor(false);
    	driveCimLF.setProfile(0);
    	driveCimLF.setF(0.1097);
    	driveCimLF.setP(0.22);
    	driveCimLF.setI(0);
    	driveCimLF.setD(0);
    	driveCimRF.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	driveCimRF.reverseSensor(false);
    	driveCimRF.setProfile(0);
    	driveCimRF.setF(0.1097);
    	driveCimRF.setP(0.22);
    	driveCimRF.setI(0);
    	driveCimRF.setD(0);
    	driveCimRB.set(3);
    	driveCimLB.set(2);
    }
    public void teleopInit() {
    	driveCimLF.changeControlMode(TalonControlMode.PercentVbus);
    	driveCimLB.changeControlMode(TalonControlMode.PercentVbus);
    	driveCimRF.changeControlMode(TalonControlMode.PercentVbus);
    	driveCimRB.changeControlMode(TalonControlMode.PercentVbus);
    	shootCim.changeControlMode(TalonControlMode.Speed);
    	shootCim.setFeedbackDevice(FeedbackDevice.EncRising);
    	shootCim.reverseSensor(false);
    	shootCim.setProfile(0);
    	shootCim.setF(0.1097);
    	shootCim.setP(0.22);
    	shootCim.setI(0);
    	shootCim.setD(0);
    }
    public void autonomousPeriodic() {
    	dashboard.putNumber("left motor error", driveCimLF.getError());
    	dashboard.putNumber("right motor error", driveCimRF.getError());
    }

    public void teleopPeriodic(){
    	double shoot = ((box.getZ()+1)/2)*16.7;
    	driveCimLF.set(joystickL.getY());
    	driveCimLB.set(joystickL.getY());
    	driveCimRF.set(joystickR.getY());
    	driveCimRB.set(joystickR.getY());
        if(box.getRawButton(5)){
        	shiftSol.set(Value.kForward);
        }
        if(!box.getRawButton(5)){
        	shiftSol.set(Value.kReverse);
        }
        if(box.getRawButton(7)){
        	flailSol.set(Value.kForward);
        }
        if(!box.getRawButton(7)){
        	flailSol.set(Value.kReverse);
        }
        if(box.getRawButton(8)){
        	shootSol.set(true);
        }
        if(!box.getRawButton(8)){
        	shootSol.set(false);
        }
        shootCim.set(shoot);
        dashboard.putNumber("shoot speed", shoot);
        dashboard.putNumber("shoot speed error", shootCim.getError());
    }

    public void testPeriodic() {
    
    }
    
}
 