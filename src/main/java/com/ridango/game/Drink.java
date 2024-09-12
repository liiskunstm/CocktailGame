package com.ridango.game;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Drink {

    public String idDrink;
    public String strDrink;
    public String strInstructions;
    public String strCategory;
    public String strGlass;
    public String strDrinkThumb;
    public String strIngredient1;
    public String strIngredient2;
    public String strIngredient3;


    public String getIngredients() {
        return strIngredient1 + ", " + strIngredient2 + ", " + strIngredient3;
    }

}
