package HWm4t2n6;

public class Url {
    public String buildTodayUrl()   {
        return "https://cbr.ru/scripts/XML_daily.asp?";
    }

    public String buildUrlWithDate(String date)  {
        return "https://cbr.ru/scripts/XML_daily.asp?date_req=" + date;
    }

    public String buildUrlWithRange(String dateStart, String dateEnd, String valuteId)    {
        return "https://cbr.ru/scripts/XML_dynamic.asp?date_req1=" + dateStart + "&date_req2=" + dateEnd + "&VAL_NM_RQ=" + valuteId;
    }
}
