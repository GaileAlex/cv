import { Directive, HostListener, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Directive({
    selector: '[appFormSubmitValidationMsg]'
})
export class FormSubmitValidationMsgDirective {

    @Input() validationControl: FormGroup;

    @HostListener('click', ['$event'])
    handleClickEvent(event) {
        this.markAsTouched(this.validationControl);
    }

    private markAsTouched(formGroup: FormGroup): void {
        formGroup.markAsTouched();
        formGroup.updateValueAndValidity();
        (Object as any).values(formGroup.controls).forEach(
            control => {
                control.markAsTouched();
                control.updateValueAndValidity({onlySelf: false, emitEvent: true});
                if (control.controls) {
                    this.markAsTouched(control);
                }
            });
    }

}
