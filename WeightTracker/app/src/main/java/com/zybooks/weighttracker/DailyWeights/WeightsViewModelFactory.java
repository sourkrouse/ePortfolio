package com.zybooks.weighttracker.DailyWeights;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.zybooks.weighttracker.DailyWeights.WeightsDataSource;
import com.zybooks.weighttracker.DailyWeights.WeightsRepository;
import com.zybooks.weighttracker.ui.login.LoginViewModel;

/*
Last Updated 10/6/2024, Laura Brooks
This file is a factory file that works with the Weights View model to run the file in the background
with the main activity.


 */
public class WeightsViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WeightsViewModel.class)) {
            return (T) new WeightsViewModel(WeightsRepository.getInstance(new WeightsDataSource()));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
