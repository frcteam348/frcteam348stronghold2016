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
 * joystickL,joystickR > drive joysticks
 * box > breakout box
 * dashboard > smart dashboard
 * shiftSol,flailSol > double solenoids
 * shootSol > single solenoid
 *//test




import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

public class Robot extends IterativeRobot {
    CANTalon driveCimLF,driveCimLB,driveCimRF,driveCimRB;
    Joystick joystickL,joystickR,box;
    SmartDashboard dashboard;
    DoubleSolenoid shiftSol,flailSol;
    Solenoid shootSol;
    
    public void robotInit() {
    	joystickL= new Joystick(2);
    	joystickR= new Joystick(1);
        driveCimLF= new CANTalon(2);
        driveCimLB= new CANTalon(4);
        driveCimRF= new CANTalon(3);
        driveCimRB= new CANTalon(1);
        dashboard= new SmartDashboard();
        shiftSol= new DoubleSolenoid(0,1);
        flailSol= new DoubleSolenoid(2,3);
        shootSol= new Solenoid(4);
        box= new Joystick(0);
    }
 
    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
    }

    public void teleopPeriodic(){
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
    }

    public void testPeriodic() {
    
    }
    
}
