package agh.ics.oop;

import agh.ics.oop.model.info.Constants;
import agh.ics.oop.model.info.ConstantsList;
import agh.ics.oop.model.maps.WorldMap;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        System.out.print("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }

//        Constants constants = new Constants(0, 0, 0, 6, 4, true, false, 0, 5, 0, 0, 0, 3, 0, 0);
//        ConstantsList.addToConstantsList(0, constants);
//        WorldMap map = new WorldMap(0);
//        System.out.println(map);
//        map.growGrass();
//        System.out.println(map);
    }

}