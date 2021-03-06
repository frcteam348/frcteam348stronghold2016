package org.usfirst.frc.team348.robot;

//***************************************************************************************************//
//to do
// 1. update the how to document
// 2. add the variable map to this document at the top with remark statements
// 3. talonSR
// 4. 

//***************************************************************************************************//
//variable database
/*

 * driveCimLF,driveCimLB,driveCimRF,driveCimRB > talonsrx > PID > drive motors
 * flailBag > talonSR > PWM > flail motor
 * shootCim > talonsrx > shooting motor talon
 * joystickL,joystickR > drive joysticks
 * box > breakout box
 * dashboard > smart dashboard
 * shiftSol,flailSol > double solenoids
 * shootSol > single solenoid
 * 
 * meth causes mass genocide
 * Ward is savage
 * Magno did nothing
 */




import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;


public class Robot extends IterativeRobot {
    CANTalon driveCimLF,driveCimLB,driveCimRF,driveCimRB,shootCim,aimCim;
    Talon flailBag;
    Joystick joystickL,joystickR,box;
    SmartDashboard dashboard;
    DoubleSolenoid shiftSol,flailSol;
    Solenoid shootSol;
    Preferences prefs;
    Timer spoolTime;
    int spoolStage;
 
    
    public void robotInit() {
    	joystickL = new Joystick(2);
    	joystickR = new Joystick(1);
        driveCimLF= new CANTalon(5);
        driveCimLB= new CANTalon(3);
        driveCimRF= new CANTalon(2);
        driveCimRB= new CANTalon(4);
        aimCim    = new CANTalon(1);
        shootCim  = new CANTalon(6);
        flailBag  = new Talon(0);
        dashboard = new SmartDashboard();
        shiftSol  = new DoubleSolenoid(0,1);
        flailSol  = new DoubleSolenoid(3,2);
        shootSol  = new Solenoid(4);
        box       = new Joystick(0);
        prefs     = Preferences.getInstance();
        spoolTime = new Timer();
    }
 
    public void autonomousInit() {
    }
    public void teleopInit() {
    	shootCim.changeControlMode(TalonControlMode.Voltage);
    	spoolStage = 0;
    	//int absolutePosition = aimCim.getPulseWidthPosition() & 0xFFF;
    	//aimCim.setEncPosition(absolutePosition);
    	//aimCim.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
    	aimCim.setFeedbackDevice(FeedbackDevice.AnalogPot);
    	//aimCim.changeControlMode(TalonControlMode.PercentVbus);
    	aimCim.changeControlMode(TalonControlMode.Position);
    	aimCim.reverseSensor(false);
    	aimCim.setAllowableClosedLoopErr(0);
    	aimCim.setProfile(0);
    	//aimCim.setP(1.5);
    	//aimCim.setI(0);
    	//aimCim.setD(0.0);
    }
    
    
    
    public void autonomousPeriodic() {
    }

    
    
    
    
    public void teleopPeriodic(){
    	
    	aimCim.setP(prefs.getDouble("AimP", 1.5));
    	aimCim.setI(prefs.getDouble("AimI", 0));
    	
    	double shoot = ((box.getZ()+1)/2)*12.0;
    	double shoot2 = -(box.getZ());
    	double leftCmd = joystickL.getY();
    	double rightCmd = joystickR.getY();
    	
    	
    	
    	
    	// this measure is 0 when we are fully symmetric and 1 when we are fully antisymmetric
    	double antisymmetryMeasure = Math.abs(leftCmd - rightCmd) / 2;
    	
    	double antisymmetryLimit = 0.7;
    	double commandLimitDuringAntisymmetry = 0.6; // this is the fastest you can tell the motors to run while we judge that commands are antisymmetric
    	if(antisymmetryMeasure > antisymmetryLimit) {
    		leftCmd = Math.signum(leftCmd) * Math.min(commandLimitDuringAntisymmetry, Math.abs(leftCmd));
    		rightCmd = Math.signum(rightCmd) * Math.min(commandLimitDuringAntisymmetry, Math.abs(rightCmd));
    	}
    	
    	
    	
    	// the right motors are reversed
    	rightCmd *= -1;
    	
    	double aimPos_Down  = prefs.getDouble("AimPos_Down",  445);
    	double aimPos_Drive = prefs.getDouble("AimPos_Drive", 425);
    	double aimPos_Shoot = prefs.getDouble("AimPos_Shoot", 308);
    	double aimPos_Start = prefs.getDouble("AimPos_Start", 298);
    	
    	double aimPoint = aimPos_Drive;
    	
        if(box.getRawButton(5)){
        	shiftSol.set(Value.kForward);
        	driveCimLF.set(leftCmd);
        	driveCimLB.set(leftCmd);
        	driveCimRF.set(rightCmd);
        	driveCimRB.set(rightCmd);
        }
        if(!box.getRawButton(5)){
        		shiftSol.set(Value.kReverse);
        		driveCimLF.set(leftCmd);
            	driveCimLB.set(leftCmd);
            	driveCimRF.set(rightCmd);
            	driveCimRB.set(rightCmd);	
        }
     
        if(joystickR.getRawButton(2)&&!joystickR.getRawButton(1)&&spoolStage!=3){
        	// commanded to pickup balls
        	flailSol.set(Value.kReverse);
        	flailBag.set(-.5);
        	shootCim.set(5.5);
        	shootSol.set(false);
        	aimPoint = aimPos_Down;
        }
        if(joystickR.getRawButton(1)&&!joystickR.getRawButton(2)&&spoolStage==0&&spoolStage!=3){
        	// commanded to shoot, spool up for shooting
        	spoolTime.stop();
        	spoolTime.reset();
        	spoolTime.start();
        	spoolStage=1;
        	aimPoint = aimPos_Shoot;
        }
        if(joystickR.getRawButton(1)&&!joystickR.getRawButton(2)&&spoolStage==1&&spoolStage!=3){
        	// commanded to shoot, waiting for spoolup to complete
        	flailSol.set(Value.kForward);
        	flailBag.set(0);
        	shootCim.set(-12);
        	aimPoint = aimPos_Shoot;
        	if(spoolTime.get()>=prefs.getDouble("Delay_ShootWarmup", 0.5)){
        		// commanded to shoot, time to actually shoot
        		shootSol.set(true);
        		spoolTime.reset();
        		spoolStage=3;
        	}
        }
        if(spoolStage==3&&spoolTime.get()>=prefs.getDouble("Delay_Shoot", 0.75)){
        	// shooting has finished
        	shootSol.set(false);
        	spoolStage=0;
        	aimPoint = aimPos_Drive;
        }
        if(!joystickR.getRawButton(2)&&!joystickR.getRawButton(1)&&spoolStage!=3){
        	// commanded to just drive around
			flailSol.set(Value.kReverse);
        	flailBag.set(0);
        	shootCim.set(0);
        	shootSol.set(false);
        	spoolStage=0;
        	aimPoint = aimPos_Drive;
        }
        
       /* if(box.getRawButton(7)){
        	flailSol.set(Value.kForward);
        }
        if(!box.getRawButton(7)){
        	flailSol.set(Value.kReverse);
        }
       */ 
        double down = prefs.getDouble("AimPos_Down", -1.345);
        double start = prefs.getDouble("AimPos_Start", -1.098);
        
        double point = down + ((shoot2+1)/2) * (start - down);
        
        
        //shootCim.set(0);
        //aimCim.set(shoot2 * 0.7);
        aimCim.set(aimPoint);
        dashboard.putNumber("shoot speed", shoot);
        dashboard.putNumber("aim P", aimCim.getP());
        dashboard.putNumber("aim I", aimCim.getI());
        dashboard.putNumber("aim D", aimCim.getD());
        dashboard.putNumber("aim error", aimCim.getError());
        dashboard.putNumber("aim position", aimCim.getPosition()); 
        dashboard.putNumber("aim target point", aimCim.getSetpoint());
        dashboard.putNumber("shooty", shootCim.get());
        dashboard.putNumber("left joystick y", leftCmd);
        dashboard.putNumber("right joystick y", rightCmd);
        dashboard.putNumber("antisymmetryMeasure", antisymmetryMeasure);
        

    }
 
    public void testPeriodic() {
    
    }
    
} 
 