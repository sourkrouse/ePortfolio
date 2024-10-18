package com.zybooks.weighttracker.DailyWeights;

/**
 * NOTE: This page is not currently used in the project, however, it is left as a placeholder for future enhancements.
 * Last updated 10/16/2024, by Laura Brooks.
 * @param <T>
 */
public class ResultList<T> {
    // hide the private constructor to limit subclass types (Success, Error)
    private ResultList() {
    }

    @Override
    public String toString() {
        if (this instanceof ResultList.Success) {
            ResultList.Success success = (ResultList.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof ResultList.Error) {
            ResultList.Error error = (ResultList.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends ResultList {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    // Error sub-class
    public final static class Error extends ResultList {
        private Exception error;

        public Error(Exception error) {
            this.error = error;
        }

        public Exception getError() {
            return this.error;
        }
    }
}
