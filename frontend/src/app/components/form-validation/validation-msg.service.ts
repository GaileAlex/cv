import { Injectable } from '@angular/core';

@Injectable()
export class ValidationMsgService {

    private errorMessages = {
        'amount-required-msg': 'Amount is a required field',

        'walletLocation-required-msg': 'Wallet location is a required field',

        'comment-required-msg': 'Comment is a required field',

        'username-required-msg': 'Username is required',
        'username-minlength-msg': 'Username must have 3 characters',
        'username-maxlength-msg': 'Username can have maximum 20 characters',

        'password-required-msg': 'Password is required',
        'password-minlength-msg': 'Password must be at least 6 characters',

        'email-required-msg': 'Email is required',
        'email-valid-msg': 'Email must be a valid email address',


    };

    public getValidationMsg(validationId: string): string {
        return this.errorMessages[validationId];
    }

}
