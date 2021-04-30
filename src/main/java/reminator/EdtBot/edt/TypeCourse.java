package reminator.EdtBot.edt;

public class TypeCourse {

    private String modality;
    private String lien;

    public TypeCourse(String modality, String lien) {
        this.modality = modality;
        this.lien = lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }

    public String getLien() {
        return lien;
    }

    public String getModality() {
        return modality;
    }
}
