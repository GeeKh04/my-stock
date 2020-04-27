package com.geekh.mystock.Model.BO;

/**
 * The Class class (Category, Family, Subfamily)
 */
public class Class {
    protected String code;
    protected String label;
    public static String idC = "";// category id
    public static String idF = "";// family id
    public static String idSF = "";// id under Family

    // Return the code
    public String getCode() {
        return code;
    }
    // Return the name
    public String getLabel() {
        return label;
    }
}
