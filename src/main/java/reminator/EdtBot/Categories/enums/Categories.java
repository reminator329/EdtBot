package reminator.EdtBot.Categories.enums;

import reminator.EdtBot.Categories.OtherCategory;
import reminator.EdtBot.Categories.Category;
import reminator.EdtBot.Categories.EdtCategory;

public enum Categories {

    EDT(new EdtCategory()),
    OTHER(new OtherCategory());

    Category category;

    Categories (Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }
}
