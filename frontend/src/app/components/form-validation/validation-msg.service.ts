import { Injectable } from '@angular/core';

@Injectable()
export class ValidationMsgService {

    private errorMessages = {
        'amount-required-msg': 'Amount is a required field',
        'amount-minlength-msg': 'Amount must have 8 characters',
        'amount-maxlength-msg': 'Amount can have maximum 30 characters',

        'walletLocation-required-msg': 'Wallet location is a required field',
        'walletLocation-minlength-msg': 'Wallet location must have 8 characters',
        'walletLocation-maxlength-msg': 'Wallet location can have maximum 30 characters',

        'comment-required-msg': 'Comment is a required field',


    };

    public getValidationMsg(validationId: string): string {
        return this.errorMessages[validationId];
    }

}
