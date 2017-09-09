package com.trading_platform.rahulfakir.tradingplatform.DataValidation;

import android.text.TextUtils;

/**
 * Created by rahulfakir on 7/15/17.
 */

public class DataValidation {

    public DataValidation() {}

    //  Email address validation
    public ValidationResult validateEmailAddress(String emailAddress) {
        if (TextUtils.isEmpty(emailAddress)) {
            return new ValidationResult(1, "Email address cannot be empty");
        } else {

            return android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches() ?
                    new ValidationResult(0, "Email Address passed validation") :
                    new ValidationResult(2, "Invalid email address");
        }
    }

    //  Password validation
    public ValidationResult validatePassword(String password) {
        // TODO: Add password validation
        return !(password.length() < 5) ?
                new ValidationResult(0, "Password passed validation") :
                new ValidationResult(1, "Invalid password");
    }

    //  Password validation
    public ValidationResult validateName(String name, String nameType) {
        // TODO: Add name validation
        return !(name.length() < 3) ?
                new ValidationResult(0, nameType + " passed validation") :
                new ValidationResult(1, "Invalid " + nameType);
    }



    public class ValidationResult {
        private Integer validationCode;
        private String validationMessage;

        ValidationResult(Integer errorCode, String errorMessage) {
            this.validationCode = errorCode;
            this.validationMessage = errorMessage;
        };

        public String getValidationMessage() {
            return validationMessage;
        }

        public Integer getValidationCode() {
            return validationCode;
        }
    }

}
