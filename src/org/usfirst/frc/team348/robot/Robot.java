package org.usfirst.frc.team348.robot;

//to do
// 1. 
// 2.
// 3. 
// 4. 


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;

public class Robot extends IterativeRobot {
    CANTalon leftA,leftB,rightA,rightB;
    Joystick leftJ,rightJ,box;
    SmartDashboard dash;
    DoubleSolenoid shift,flail;
    Solenoid shoot;
    
    public void robotInit() {
        leftJ= new Joystick(2);
        rightJ= new Joystick(1);
        leftA= new CANTalon(2);
        leftB= new CANTalon(4);
        rightA= new CANTalon(3);
        rightB= new CANTalon(1);
        dash= new SmartDashboard();
        shift= new DoubleSolenoid(0,1);
        flail= new DoubleSolenoid(2,3);
        shoot= new Solenoid(4);
        box= new Joystick(0);
    }
 
    public void autonomousInit() {
    }

    public void autonomousPeriodic() {
    }

    public void teleopPeriodic(){
        leftA.set(leftJ.getY());
        leftB.set(leftJ.getY());
        rightA.set(rightJ.getY());
        rightB.set(rightJ.getY());
        if(box.getRawButton(5)){
        	shift.set(Value.kForward);
        }
        if(!box.getRawButton(5)){
        	shift.set(Value.kReverse);
        }
        if(box.getRawButton(7)){
        	flail.set(Value.kForward);
        }
        if(!box.getRawButton(7)){
        	flail.set(Value.kReverse);
        }
        if(box.getRawButton(8)){
        	shoot.set(true);
        }
        if(!box.getRawButton(8)){
        	shoot.set(false);
        }
    }

    public void testPeriodic() {
    
    }
    
}
