package vitals;
 
public abstract class VitalsChecker {
    static final float MAX_TEMPERATURE = 102;
    static final float MIN_TEMPERATURE = 95;
    static final float MAX_PULSE_RATE = 100;
    static final float MIN_PULSE_RATE = 60;
    static final float MIN_SPO2 = 90;
 
    static boolean vitalsOk(float temperature, float pulseRate, float spo2) throws InterruptedException {
        boolean isOk = true;
 
        isOk &= checkAndDisplayMessage(temperature, MIN_TEMPERATURE, MAX_TEMPERATURE, "Temperature is critical!");
        isOk &= checkAndDisplayMessage(pulseRate, MIN_PULSE_RATE, MAX_PULSE_RATE, "Pulse Rate is out of range!");
        isOk &= checkAndDisplayMessage(spo2, MIN_SPO2, Float.MAX_VALUE, "Oxygen Saturation out of range!");
 
        return isOk;
    }
 
    private static boolean checkAndDisplayMessage(float value, float min, float max, String message) throws InterruptedException {
        if (!checkValue(value, min, max)) {
            displayCriticalMessage(message);
            return false;
        }
        return true;
    }
 
    private static boolean checkValue(float value, float min, float max) {
        return value >= min && value <= max;
    }
 
    private static void displayCriticalMessage(String message) throws InterruptedException {
        System.out.println(message);
        animateWarning();
    }
 
    private static void animateWarning() throws InterruptedException {
        for (int i = 0; i < 6; i++) {
            System.out.print("\r* ");
            Thread.sleep(1000);
            System.out.print("\r *");
            Thread.sleep(1000);
        }
    }
}
