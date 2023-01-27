package com.example.madgroupassignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import com.example.madgroupassignment.databinding.ActivityAboutPageBinding;
import com.example.madgroupassignment.databinding.ActivityLobbyBinding;

public class AboutPage extends DrawerBaseAcitivity {
    ActivityAboutPageBinding activityAboutPageBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAboutPageBinding = ActivityAboutPageBinding.inflate(getLayoutInflater());
        allocateActivityTitle("About");
        setContentView(activityAboutPageBinding.getRoot());
    }
}