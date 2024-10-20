package com.zybooks.weighttracker.ui.login;

import androidx.annotation.Nullable;

/**
 * *** NOT USED *** no need to save a logged in status
 * Authentication result : success (user details) or error message.
 */
class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private Integer error;

    LoginResult(@Nullable Integer error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }


    int setValue(@Nullable LoginResult error){ return 0;}

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}