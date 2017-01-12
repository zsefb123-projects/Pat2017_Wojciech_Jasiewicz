package com.example.wojciech.jasiewiczwojciech;

import android.util.Log;

public class MyLogger {

    public static String[] loginInvalidInfo =  {
            "Email field is empty",
            "Only one @ sign is allowed in email",
            "Empty local or domain part of email",
            "Empty domain part of email",
            "Two dots near in local part",
            "Two dots near in domain part",
            "Special signs is outside quotation mark",
            "Special sign in domain part",
            "Too long domain",
            "Too short domain"
    };

    public static String[] passwordInvalidInfo = {
            "Too short password",
            "Password don't contain at least one digit, uppercase, lowercase"
    };

    public static char[] notAllowedCharacters = new char[]{'$', '`', '~', '&', ',', ':', ';',
            '=', '?', '#', '|', '<', '>', '^', '*', '(', ')', '[', ']', '%', '!', ' '};

    public static String validatePassword(String password){

        if(password.length() < 8) return passwordInvalidInfo[0];
        if(!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")){
            return passwordInvalidInfo[1];
        }
        return "";
    }


    public static String validateEmail(String email){

        // Empty email
        if(email.isEmpty()) return loginInvalidInfo[0];

        // Zero or more than one "@" character
        String[] emailParts = email.split("@");
        if(emailParts.length != 2) return loginInvalidInfo[1];

        // Empty local or domain part of email
        if ((emailParts[0].isEmpty()) || (emailParts[1].isEmpty())) return loginInvalidInfo[2];

        // Validate local part
        String user = emailParts[0];
        if(user.charAt(user.length() - 1) == '.') return "Dot near to @";
        if(user.charAt(0) == '.') return "Dot as first character";

        String userParts[] = user.split("\\.");
        Log.v("user lenght", "" + userParts.length );
        if(userParts.length == 0){
            String error = MyLogger.specialCharactersInUser(user);
            if(error != "" ) return error;
        }else{
            Log.v("gg","user: " + user);
            for(String s :userParts){
                Log.v("xx", "**" + s );
                String error = MyLogger.specialCharactersInUser(s);
                if(error != "" ) return error;
            }
        }

        //Validate domain part
        String domain = emailParts[1];
        String[] domainParts = domain.split("\\.");
        if(domain.charAt(domain.length() - 1) == '.') return "Dot as end character";
        if(domainParts.length < 2) return loginInvalidInfo[9];
        else if(domainParts.length > 3) return loginInvalidInfo[8];
        else{
            Log.v("ll","domain: " + domain);
            for (String s3 :domainParts) {
                Log.v("yy", s3);
                if (specialCharactersInDomain(s3) != "") return loginInvalidInfo[7];
            }
        }
        return "";
    }

    public static String specialCharactersInUser(String string){
        if(string.length() == 0) return loginInvalidInfo[4];
        int[] index = findQuotationMark(string);
        Log.v("index:", ""+ index[0] + " " + index[1]);
        char[] array = notAllowedCharacters;
        if((index[0] > -1)&&(index[1] > -1)&&(index[0] < index[1])){

            for(int i = 0; i < index[0]; i++){
                if(string.contains(""+ array[i])){
                    Log.v("i:", ""+i);
                    return loginInvalidInfo[6];
                }
            }
            for(int j = index[1] + 1; j < array.length; j++){
                if(string.contains(""+ array[j])){
                    Log.v("j:", ""+ j);
                    return loginInvalidInfo[6];
                }
            }

        }else{
            for (char anArray : array) {
                if (string.contains("" + anArray)) return loginInvalidInfo[6];
            }
        }
        return "";
    }

    public static String specialCharactersInDomain(String string){
        if(string.length() == 0) return loginInvalidInfo[5];
        char[] array = notAllowedCharacters;
        for (char anArray : array) {
            if (string.contains("" + anArray)) return loginInvalidInfo[6];
        }
        return "";
    }

    public static int[] findQuotationMark(String s){
        int[] index = new int[]{-1, -1};
        char[] array = s.toCharArray();
        for(int i = 0; i < array.length; i++ ){
            if(array[i] == '"'){
               index[0] = i;
                break;
            }
        }
        for(int j = array.length - 1; j >= 0; j-- ){
            if(array[j] == '"'){
                index[1] = j;
                break;
            }
        }
        return index;
    }



}

