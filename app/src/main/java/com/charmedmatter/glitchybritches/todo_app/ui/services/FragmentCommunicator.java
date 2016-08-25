package com.charmedmatter.glitchybritches.todo_app.ui.services;


//Interface for passing data between fragments

public interface FragmentCommunicator <T> {
    //Method to send data.  String parameter allows identification of data
    //Generic parameter represents data to be passed
    void sendData(String operation, T data);

    //Method for launching fragment based upon fragment class name
    void launchFragment(String fragmentClassName);
}