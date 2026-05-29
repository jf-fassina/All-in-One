package com.myapp.beans;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named           // makes this available in .xhtml as #{helloBean}
@ViewScoped      // keeps this alive while you're on the same page
public class HelloBean implements Serializable {

    private String name;
    private String message;

    // Called when you click the button
    public void greet() {
        this.message = "Hello, " + name + "!";
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMessage() { return message; }
}
