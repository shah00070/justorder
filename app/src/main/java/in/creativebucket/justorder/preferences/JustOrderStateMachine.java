package in.creativebucket.justorder.preferences;

import android.content.Context;

public class JustOrderStateMachine {
    public final static String USER_CURRENT_ADDRESS = "curr_mode";
    public final static String IS_FIRST_LAUNCH = "first_launch";


    public static void setUserAddress(Context context, String userAddress) {
        RawStorageProvider.getInstance(context).dumpDataToStorage(USER_CURRENT_ADDRESS, userAddress);
    }

    public static String getUserAddress(Context context) {
        return RawStorageProvider.getInstance(context).getDataFromStorage(
                USER_CURRENT_ADDRESS);
    }

    public static void setIsFirstLaunch(Context context, boolean isFirstLaunch) {
        RawStorageProvider.getInstance(context).dumpDataToStorage(IS_FIRST_LAUNCH, isFirstLaunch);
    }

    public static boolean isFirstLaunch(Context context) {
        return RawStorageProvider.getInstance(context).isThisValueSet(
                IS_FIRST_LAUNCH, true);
    }

}
