package reminator.EdtBot.edt.enums;

import reminator.EdtBot.utils.HTTPRequest;

public enum Edt {
    EDT01("jjr0au21evqc6guauvan3034ug"),
    EDT02("8nam511995lbsisujjcq80h964"),
    EDT1("4jpbp5hcdimlmov6kscioe4am8"),
    EDT2("e44ep4hdrj5b2defqf9mmcpd2k");

    private final String id;

    Edt(String id) {
        this.id = id;
    }

    private String getUrl() {
        return "https://clients6.google.com/calendar/v3/calendars/8nam511995lbsisujjcq80h964@group.calendar.google.com/events?calendarId=" + id + "@group.calendar.google.com&singleEvents=true&timeZone=Europe/Paris&maxAttendees=1&maxResults=250&sanitizeHtml=true";
    }

    private String getUrl(String timeMin, String timeMax) {
        return this.getUrl() +
                "&timeMin=" + timeMin +
                "&timeMax=" + timeMax +
                "&key=AIzaSyBNlYH01_9Hc5S1J9vuFmu2nUqBZJNAXxs";
    }

    public HTTPRequest getHTTPRequest(String timeMin, String timeMax) {
        return new HTTPRequest(getUrl(timeMin, timeMax));
    }
}
