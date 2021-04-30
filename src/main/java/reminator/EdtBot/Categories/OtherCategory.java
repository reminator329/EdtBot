package reminator.EdtBot.Categories;

public class OtherCategory implements Category {

    @Override
    public String getName() {
        return "Autres";
    }

    @Override
    public String getDescription() {
        return "Les commandes sans catégorie particulière.";
    }
}
