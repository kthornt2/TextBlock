package edu.oakland.textblock;

/**
 * Created by Wind on 04/24/2017.
 */

public class UnlockAssistant {
    public static void stopListening() {
        GpsServices.lockIsListening = false;
        GpsServices.showGPSDialogue = false;
    }

    public static void resumeListening() {
        GpsServices.lockIsListening = true;
        GpsServices.showGPSDialogue = true;
    }
}
