package utils;

public class Logger {
    public static boolean DEBUG_LOG_ENABLED = false;

    public static void log(String s)
    {
        if(DEBUG_LOG_ENABLED)
        {
            System.out.println(s);
        }
    }
    
}
