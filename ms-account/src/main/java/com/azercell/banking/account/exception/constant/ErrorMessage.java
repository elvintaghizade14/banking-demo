
package com.azercell.banking.account.exception.constant;

import com.azercell.banking.commonlib.exception.constant.CommonErrorMessage;

public final class ErrorMessage extends CommonErrorMessage {

    public static final String ACCOUNT_ALREADY_EXIST_MESSAGE
            = "Active account with userId [{0}] and in currency [{1}] already exists!";
    public static final String ACCOUNT_NOT_FOUND_MESSAGE
            = "Active account with userId [{0}], iban [{1}] and currency [{2}] not found!";
    public static final String PURCHASE_NOT_FOUND_MESSAGE = "Purchase not find with id [{0}]";
}
