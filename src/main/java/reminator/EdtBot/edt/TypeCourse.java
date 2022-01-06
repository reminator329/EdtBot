package reminator.EdtBot.edt;

import java.util.Objects;

public class TypeCourse {

    private String modality;
    private String lien;

    public TypeCourse(String modality, String lien) {
        this.modality = modality;
        this.lien = lien;
    }

    public TypeCourse() {
        this(null, null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeCourse that = (TypeCourse) o;
        return Objects.equals(modality, that.modality) && Objects.equals(lien, that.lien);
    }
}
