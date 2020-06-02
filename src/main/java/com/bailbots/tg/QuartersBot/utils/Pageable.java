package com.bailbots.tg.QuartersBot.utils;


import java.util.Calendar;

public class Pageable {

    public static int getPreviousPage(int maxPage, Integer page) {
        page = page - 1;

        if (page == -1) {
            page = maxPage - 1;
        }
        return page;
    }

    public static int getNextPage(int maxPage, Integer page) {
        page = page + 1;
        if (page == maxPage) {
            page = 0;
        }
        return page;
    }

}
