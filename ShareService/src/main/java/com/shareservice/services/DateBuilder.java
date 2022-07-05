package com.shareservice.services;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateBuilder {

    public LocalDate build(String strDate) {
        String[] dateArray = new String[3];
        dateArray = strDate.split("-");
        for (int i = 1; i < 3; i++) {
            char[] chars = dateArray[i].toCharArray();
            if (chars[0] == '0') {
                dateArray[i] = "" + chars[1];
            } else {
                dateArray[i] = "" + chars[0] + chars[1];
            }
        }

        return LocalDate.of(Integer.parseInt(dateArray[0]),
                Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2]));
    }
}
