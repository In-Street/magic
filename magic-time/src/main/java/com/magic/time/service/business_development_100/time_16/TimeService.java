package com.magic.time.service.business_development_100.time_16;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.*;
import java.util.Date;
import java.util.TimeZone;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;

/**
 *
 * @author Cheng Yufei
 * @create 2022-03-20 4:48 PM
 **/
public class TimeService {

    public static void t1() {
        Date date = new Date(0);
        //Thu Jan 01 08:00:00 CST 1970
        System.out.println(date);

        //Asia/Shanghai
        System.out.println(TimeZone.getDefault().getID());
        System.out.println(ZoneId.systemDefault().getId());
    }

    public static void zone() {
        String stringDate = "2020-01-02 22:00:00";
        ZoneId sh = ZoneId.of("Asia/Shanghai");
        ZoneId ny = ZoneId.of("America/New_York");
        ZoneOffset tokyo = ZoneOffset.ofHours(9);

        /*DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(tokyo);
        ZonedDateTime tokyoTime = ZonedDateTime.parse(stringDate, timeFormatter);*/

        ZonedDateTime tokyoTime = ZonedDateTime.of(LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), tokyo);


        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
        System.out.println(pattern.withZone(tokyo).format(tokyoTime));
        System.out.println(pattern.withZone(sh).format(tokyoTime));
        System.out.println(pattern.withZone(ny).format(tokyoTime));

    }

    private static ThreadLocal<SimpleDateFormat> threadSafeSimpleDateFormat = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    public static void simpledate() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        System.out.println(simpleDateFormat.parse("20220101"));
    }

    public static void datetime() {
        System.out.println(ChronoField.YEAR);
        System.out.println(MONTH_OF_YEAR);
        System.out.println(DAY_OF_MONTH);

        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder().appendValue(ChronoField.YEAR).appendLiteral("-")
                .appendValue(MONTH_OF_YEAR).appendLiteral("-")
                .appendValue(DAY_OF_MONTH)
                .toFormatter();

        System.out.println(dateTimeFormatter.parse("2022-02-02"));
    }

    public static void cal() {
        LocalDateTime now = LocalDateTime.now();
        //周日：2022-03-20T18:43:52.267
        System.out.println(now);

        System.out.println(now.with(TemporalAdjusters.firstDayOfYear()));

        //上周日 2022-03-13T18:43:52.267
        System.out.println(now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)));

        // 相当于一天前：2022-03-19T18:45:31.889
        System.out.println(now.with(TemporalAdjusters.previous(DayOfWeek.SATURDAY)));

        //当天：2022-03-20T18:46:50.189
        System.out.println(now.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)));

        //2022-03-25T18:48:15.528
        System.out.println(now.with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY)));

        //和now同月的第二天
        System.out.println(now.withDayOfMonth(2));

        System.out.println(now.with(t -> t.plus(2, ChronoUnit.DAYS)));

        //检查日期是否满足条件
        System.out.println(now.query(TimeService::isFamilyBirthday));

        Period between = Period.between(LocalDate.of(2022, 2, 1), LocalDate.of(2022, 3, 20));
        //19: 是零几天，而不是总的间隔天数
        System.out.println(between.getDays());
    }


    public static Boolean isFamilyBirthday(TemporalAccessor date) {
        int month = date.get(MONTH_OF_YEAR);
        int day = date.get(DAY_OF_MONTH);
        if (month == Month.FEBRUARY.getValue() && day == 17)
            return Boolean.TRUE;
        if (month == Month.SEPTEMBER.getValue() && day == 21)
            return Boolean.TRUE;
        if (month == Month.MARCH.getValue() && day == 20)
            return Boolean.TRUE;
        return Boolean.FALSE;
    }


    public static void main(String[] args) throws ParseException {
        //t1();
        //zone();
        //simpledate();
        //datetime();
        cal();
    }
}
