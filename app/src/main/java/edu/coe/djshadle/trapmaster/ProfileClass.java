package edu.coe.djshadle.trapmaster;

public class ProfileClass {

    //************************************* Private Variables **************************************
    private String profileID_Str;
    private String profileEmail_Str;  //Key
    private String profilePassword_Str;

    //************************************* Public Functions ***************************************
    // Constructors
    public ProfileClass() {
        this.profileID_Str = "tempID";
        this.profileEmail_Str = "tempEmail";
        this.profilePassword_Str = "tempPass";
    }

    public ProfileClass(String profileID_Str, String profileEmail_Str, String profilePassword_Str) {
        this.profileID_Str = profileID_Str;
        this.profileEmail_Str = profileEmail_Str;
        this.profilePassword_Str = profilePassword_Str;
    }

    public ProfileClass(String profileEmail_Str, String profilePassword_Str) {
        this.profileID_Str = "tempID";
        this.profileEmail_Str = profileEmail_Str;
        this.profilePassword_Str = profilePassword_Str;
    }

    // Getters and Setters
    public String getProfileID_Str() {
        return profileID_Str;
    }

    public void setProfileID_Str(String profileID_Str) {
        this.profileID_Str = profileID_Str;
    }

    public String getProfileEmail_Str() {
        return profileEmail_Str;
    }

    public void setProfileEmail_Str(String profileEmail_Str) {
        this.profileEmail_Str = profileEmail_Str;
    }

    public String getProfilePassword_Str() {
        return profilePassword_Str;
    }

    public void setProfilePassword_Str(String profilePassword_Str) {
        this.profilePassword_Str = profilePassword_Str;
    }
}
