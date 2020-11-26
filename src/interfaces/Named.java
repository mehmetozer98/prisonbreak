/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.ArrayList;
import worldofzuul.persistence.FileHandler;


public interface Named {

    static String giveName() {

        //if something goes wrong the npcs will stil have a name
        String firstName = "DefaultFirstName";
        String lastName = "DefaultLastName";
        
        ArrayList<String> arrFirst = new FileHandler("src/worldofzuul/Names/FirstName.csv").getCsvList();
        ArrayList<String> arrLast = new FileHandler("src/worldofzuul/Names/LastName.csv").getCsvList();

        int RandomNumber = (int) (Math.random() * arrFirst.size());
        firstName = arrFirst.get(RandomNumber);

        RandomNumber = (int) (Math.random() * arrLast.size());
        lastName = arrLast.get(RandomNumber);

        return firstName + " " + lastName;
    }

}
