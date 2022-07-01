package testModel.Order;

import lombok.Data;
import java.util.ArrayList;

@Data
public class OrderModel {

    private ArrayList<String> ingredients;

    public OrderModel(ArrayList<String> ingredients){
        this.ingredients = ingredients;
    }

    public static OrderModel getDefaultOrder (){

        ArrayList<String> ingredients = new ArrayList<>();

        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        ingredients.add("61c0c5a71d1f82001bdaaa72");

        return new OrderModel(ingredients);
    }


    public static OrderModel getWrongHash (){

        ArrayList<String> ingredients = new ArrayList<>();

        ingredients.add("wrongIngredientsHash");
        ingredients.add("61c0c5a71d1f82001bdaaa6f");
        ingredients.add("61c0c5a71d1f82001bdaaa72");

        return new OrderModel(ingredients);
    }
}
