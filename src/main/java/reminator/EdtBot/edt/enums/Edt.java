package reminator.EdtBot.edt.enums;

import reminator.EdtBot.utils.HTTPRequest;

public enum Edt {
    // 1A
    EDT01("jjr0au21evqc6guauvan3034ug@group.calendar.google.com"),
    EDT02("8nam511995lbsisujjcq80h964@group.calendar.google.com"),
    EDT1("4jpbp5hcdimlmov6kscioe4am8@group.calendar.google.com"),
    EDT2("e44ep4hdrj5b2defqf9mmcpd2k@group.calendar.google.com"),
    EDT3("suv86i7e8723b3o0utsq1ackes@group.calendar.google.com"),

    //2A
    EDT_2A("b99stiti1jc6in7n2cf9buoquk@group.calendar.google.com"),
    EDT_2A_TP("db3arpv5ok1h4sd8rumvgd3cgc@group.calendar.google.com"),

    //3A
    EDT_3A_C("master.sir.ups@gmail.com"),
    EDT_3A_G1("lg0vrqu5jj8autgtnf6nep2cgo@group.calendar.google.com"),
    EDT_3A_G2("gdlsf0bsq0jgg6alasc48epf14@group.calendar.google.com")
    ;

    private final String id;

    Edt(String id) {
        this.id = id;
    }

    private String getUrl() {
        return "https://clients6.google.com/calendar/v3/calendars/" + id + "/events?calendarId=" + id + "&singleEvents=true&timeZone=Europe/Paris&maxAttendees=1&maxResults=250&sanitizeHtml=true";
    }

    private String getUrl(String timeMin, String timeMax) {
        return this.getUrl() +
                "&timeMin=" + timeMin +
                "&timeMax=" + timeMax +
                "&key=AIzaSyBNlYH01_9Hc5S1J9vuFmu2nUqBZJNAXxs";
    }

    public HTTPRequest getHTTPRequest(String timeMin, String timeMax) {
        System.out.println(getUrl(timeMin, timeMax));
        return new HTTPRequest(getUrl(timeMin, timeMax));
        /*
        .withHeader("Accept", "*//** ")
        .withHeader("Accept-Encoding", "gzip, deflate, br")
        .withHeader("Accept-Language", "fr,fr-FR;q=0.8,en-US;q=0.5,en;q=0.3")
                .withHeader("Alt-Used", "clients6.google.com")
                .withHeader("Authorization", "SAPISIDHASH 1623620464_45227efa839cdbf6d303f0f297cb46331c46af2f")
                .withHeader("Connection", "keep-alive")
                .withHeader("Cookie", "NID=216=C0pWkbCu6m4OwTbFB1n_cf1oVzW98jlWVT66AxGGzzDzMrVg4FsnAcMi0JxZeLXVO0JsEE2Msf3e3lQcQSYcsehSG07F6uTpIdYVBG3WXYSVjnvzcnsmR0EI8iRx-s-jaJ-PX4vJ9Yd1a4IdgeoyJ5-pft-x5ruTdn28h7wXN-ql4H5HI2yOnGMCfnO5INtrOPaT_1010lBMRxqvl88Jyz8Ag0YYnGeZP5rtXfjM6TyaK-9_ldIoGfwZEYch_r64diPhy47kv_8MjKmT4LKsg1jHpajaMk8W66SkOss6T93loHTyrDG74gphFFWWUJfaPCu02dwgwqwoPWkbfcg; CONSENT=YES+FR.fr+V10+BX+950; 1P_JAR=2021-06-13-20; ANID=AHWqTUm8h3a2h7s9D9DWhLlHtm1PrWSHtuQmIVSsJfMWhCghzH9sZUgvIv7hveld; OGPC=19024399-1:; OGP=-19024399:; SID=9w…xpWHHP5HRFnS2rv_76tEPrl6L28mNhq8RoZ0lLLINl6oxaF2nXh5rwUEQ.; __Secure-3PSID=9weAyksyGBCOXxpWHHP5HRFnS2rv_76tEPrl6L28mNhq8RoZGftGFo3w5TxXgm-TjiKoJg.; HSID=AYJlHFUSKV8j2FSsE; SSID=A57om-UxrLOU1QeIS; APISID=7CrbPI91M0f4Gyo3/Ak82x3y2ajbR-7kai; SAPISID=ebY8-c2N9dhr3mL0/AHn-yjoCIgUIODat8; __Secure-3PAPISID=ebY8-c2N9dhr3mL0/AHn-yjoCIgUIODat8; SIDCC=AJi4QfEaT-6mE-I2UCEq9TraB0BXu5j2IcbOMLwwxOaq6_A6qAiphZGL9ARlYsuXES9Hm-t4jF4; __Secure-3PSIDCC=AJi4QfGKVsKA1xnknAgPnaPBxsz2M07xmZlpkeE4dNTzwwSi5E1_uZ25hgBvm0vB4glgNE8MSw")
                .withHeader("Host", "clients6.google.com")
                .withHeader("Referer", "https://clients6.google.com/static/proxy.html?usegapi=1&jsh=m%3B%2F_%2Fscs%2Fapps-static%2F_%2Fjs%2Fk%3Doz.gapi.fr.ociYurqUxMU.O%2Fam%3DAQ%2Fd%3D1%2Frs%3DAGLTcCOIwDSigzXEc04K81EinhGJsWNk4A%2Fm%3D__features__")
                .withHeader("TE", "Trailers")
                .withHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0")
                .withHeader("X-ClientDetails", "appVersion=5.0%20(Windows)&platform=Win32&userAgent=Mozilla%2F5.0%20(Windows%20NT%2010.0%3B%20Win64%3B%20x64%3B%20rv%3A89.0)%20Gecko%2F20100101%20Firefox%2F89.0")
                .withHeader("X-Goog-AuthUser", "0")
                .withHeader("X-Goog-Encode-Response-If-Executable", "base64")
                .withHeader("X-JavaScript-User-Agent", "google-api-javascript-client/1.1.0")
                .withHeader("X-Origin", "https://calendar.google.com")
                .withHeader("X-Referer", "https://calendar.google.com")
                .withHeader("X-Requested-With", "XMLHttpRequest");*/
    }
}
