package reminator.EdtBot.edt.enums;

public enum Edt {
    EDT01("jjr0au21evqc6guauvan3034ug"),
    EDT02("8nam511995lbsisujjcq80h964"),
    EDT1("4jpbp5hcdimlmov6kscioe4am8"),
    EDT2("e44ep4hdrj5b2defqf9mmcpd2k");

    private String id;

    Edt(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
