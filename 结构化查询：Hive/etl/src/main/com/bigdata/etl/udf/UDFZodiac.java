package com.bigdata.etl.udf;

import org.apache.hadoop.hive.ql.exec.UDF;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class UDFZodiac extends UDF {

    private SimpleDateFormat df;

    public UDFZodiac() {
        df = new SimpleDateFormat("yyyy-MM-dd");

    }

    public String evaluate(Calendar bday) {
        return this.evaluate(bday.get(Calendar.MONTH + 1), bday.get(Calendar.DAY_OF_MONTH));
    }

    public String evaluate(String bday) {
        Date date = null;
        try {
            date = df.parse(bday);
        } catch (Exception e) {
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return this.evaluate(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public String evaluate(Integer month, Integer day) {
        if (month == 1) {
            if (day < 20) {
                return "摩羯座";
            } else {
                return "水瓶座";
            }
        }

        if (month == 2) {
            if (day < 19) {
                return "水瓶座";
            } else {
                return "双鱼座";
            }
        }

        if (month == 3) {
            if (day < 21) {
                return "双鱼座";
            } else {
                return "白羊座";
            }
        }

        if (month == 4) {
            if (day < 20) {
                return "白羊座";
            } else {
                return "金牛座";
            }
        }

        if (month == 5) {
            if (day < 21) {
                return "金牛座";
            } else {
                return "双子座";
            }
        }

        if (month == 6) {
            if (day < 21) {
                return "双子座";
            } else {
                return "巨蟹座";
            }
        }

        if (month == 7) {
            if (day < 23) {
                return "巨蟹座";
            } else {
                return "狮子座";
            }
        }

        if (month == 8) {
            if (day < 23) {
                return "狮子座";
            } else {
                return "处女座";
            }
        }

        if (month == 9) {
            if (day < 23) {
                return "处女座";
            } else {
                return "天秤座";
            }
        }

        if (month == 10) {
            if (day < 23) {
                return "天秤座";
            } else {
                return "天蝎座";
            }
        }

        if (month == 11) {
            if (day < 23) {
                return "天蝎座";
            } else {
                return "射手座";
            }
        }

        if (month == 12) {
            if (day < 23) {
                return "射手座";
            } else {
                return "摩羯座";
            }
        }

        return null;
    }

}
