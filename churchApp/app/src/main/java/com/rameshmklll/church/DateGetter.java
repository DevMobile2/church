package com.rameshmklll.church;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by MRamesh on 28-12-2017.
 */

public class DateGetter {

    static String getDate(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;

    }

}
