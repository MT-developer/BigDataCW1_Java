package utils;

import objects.item;

import java.util.ArrayList;

public class vals {
    public static ArrayList<item> arr_itemList = new ArrayList<objects.item>();


    public static boolean bool_HomeHardwareDirect = false;
    public static boolean bool_RgcBuildingSupplies = false;
    public static boolean bool_ScrewFix = false;
    public static boolean bool_Toolstation = false;
    public static boolean bool_Wickes = false;

    public static void set_bool_HomeHardwareDirect_true() {
        bool_HomeHardwareDirect = true;
    }
    public static void set_bool_RgcBuildingSupplies_true() {
        bool_RgcBuildingSupplies = true;
    }
    public static void set_bool_ScrewFix_true() {
        bool_ScrewFix = true;
    }
    public static void set_bool_Toolstation_true() {
        bool_Toolstation = true;
    }
    public static void set_bool_Wickes_true() {
        bool_Wickes = true;
    }
}