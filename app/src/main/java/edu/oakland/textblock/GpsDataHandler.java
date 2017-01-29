package edu.oakland.textblock;


import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;



public class GpsDataHandler {
    private boolean activeStatus;
    private long time;
    private long timeStopped;
    //distance in meters
    private double distanceM;
    private double speed;
    private double maxSpeed;


    private gpsUpdateTrigger gpsTrigger;

    //when GPS service updates location, application is updated with latest info
    public interface gpsUpdateTrigger{
         void update();
    }

    public void whenTriggered(gpsUpdateTrigger gpsUpdateTrigger){
        this.gpsTrigger = gpsUpdateTrigger;
    }

    public void update(){
        gpsTrigger.update();
    }

    //defining variables used in calculating and displaying distance and speed
    public GpsDataHandler() {
        activeStatus = false;
        distanceM = 0;
        speed = 0;
        maxSpeed = 0;
        timeStopped = 0;
    }

    //initiates the class
    public GpsDataHandler(gpsUpdateTrigger gpsUpdateTrigger){
        this();
        whenTriggered(gpsUpdateTrigger);
    }

    //fetches distance from GPS.  By default, Location services uses meters.  .
    public void distanceOutputFunction(double distance){
        distanceM = (distanceM + distance);


    }

    //Outputs distance to string so it can be put in UI
    public String outputDistanceFunction(){
        String s;

            s = new String(String.format("%.3f", distanceM) + "miles");

        return s;
    }

    //outputs max speed to string for use in UI
    public String topSpeedFunction() {
        String s;

        s = new String(String.format("%.0f", maxSpeed) + "mph");
        return s;
    }

    //calculates average speed then outputs it to string for UI. Assumes vehicle in motion.
    //multiply by 2.23694 to get mph instead of meters/second
    public String meanSpeedFunction(){
        double averageSpeed = ((distanceM / (time / 1000)) * 2.23694);
        String s;
        if (time > 0){
            s = new String(String.format("%.0f", averageSpeed) + "mph");

        }else{
            s = new String(0 + "mph");
        }
        return s;
    }

    //If a vehicle stops, the calc for average speed must ignore the time the vehicle is stopped.
    //multiply by 2.23694 to get mph instead of meters/second
    public String meanSpeedFunctionStopped(){
        double motionTime = time - timeStopped;
        String s;
        if (motionTime < 0){
            s = new String(0 + "mph");
        }else{
            double average = ((distanceM / ( (time - timeStopped) / 1000)) * 2.23694) ;
            s = new String(String.format("%.0f", average) + "mph");
        }
        return s;
    }

    //Determines max speed by an operation of the current speed vs the stored max speed
    //speed here is input from GPSServices from onLocationChanged()
    //multiply by 2.23694 to get mph instead of meters/second
    public void currentSpeed(double speed) {

        this.speed = speed * 2.23694;
        if (speed > maxSpeed){
            maxSpeed = speed;
        }
    }


    //keeps tabs on how long vehicle has been stopped
    public void setTimeStopped(long timeStopped) {
        this.timeStopped += timeStopped;
    }

    //used to deliver the speed 
    public double getSpeed() {
        return speed;
    }

    //timer
    public long getTime() {
        return time;
    }

    //updates timer
    public void setTime(long time) {
        this.time = time;
    }
}

