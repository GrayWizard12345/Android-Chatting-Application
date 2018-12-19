package com.example.hamlet.mobileprogrammingclass_chat_project.classes;

public class Contact {

    private String contactImage;
    private String contactName;
    private String contactIdentifier;

    public Contact() {
    }

    public Contact(String contactImage, String contactName, String contactIdentifier) {
        this.contactImage = contactImage;
        this.contactName = contactName;
        this.contactIdentifier = contactIdentifier;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactIdentifier() {
        return contactIdentifier;
    }

    public void setContactIdentifier(String contactIdentifier) {
        this.contactIdentifier = contactIdentifier;
    }
}
