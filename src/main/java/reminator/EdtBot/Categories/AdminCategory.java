package reminator.EdtBot.Categories;

public class AdminCategory implements Category {

    @Override
    public String getName() {
        return "Admin";
    }

    @Override
    public String getDescription() {
        return "Les commandes pour les admins";
    }
}
