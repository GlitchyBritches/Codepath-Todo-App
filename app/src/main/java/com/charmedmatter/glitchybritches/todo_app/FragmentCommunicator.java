package com.charmedmatter.glitchybritches.todo_app;


//Interface for passing data between fragments
//Has a generic parameter to allow objects of any type to be passed
public interface FragmentCommunicator <T> {
    void sendData(String operation, T data);
    void launchFragment(String fragmentClassName);
}