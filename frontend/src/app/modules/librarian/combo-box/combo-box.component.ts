import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';

@Component({
    selector: 'app-combo-box',
    templateUrl: './combo-box.component.html',
    styleUrls: ['./combo-box.component.css']
})
export class ComboboxComponent implements OnInit {

    constructor(private formBuilder: FormBuilder) {
        this.filterForm = this.formBuilder.group({
            selectedFilter: new FormArray([])
        });
    }

    get filterForms() {
        return this.filterForm.get('selectedFilter') as FormArray;
    }

    filterForm: FormGroup;
    @Output() formReady = new EventEmitter<FormGroup>();
    values = ['Author', 'Title', 'Date'];
    days = [];
    months = [];
    years = [];
    currentYear = new Date().getFullYear();

    static createDateArray(list, from, to) {
        for (let i = from; i <= to; i++) {
            list.push(i);
        }
        return list;
    }

    ngOnInit(): void {
        this.formReady.emit(this.filterForm);
        this.addFilter();
        ComboboxComponent.createDateArray(this.days, 1, 31);
        ComboboxComponent.createDateArray(this.months, 1, 12);
        ComboboxComponent.createDateArray(this.years, 2000, this.currentYear);
    }

    addFilter() {
        const filter = this.formBuilder.group({
            searchArea: 'Author',
            conditionOption: 'Begin with',
            textRequest: '',
            day: '1',
            month: '1',
            year: this.currentYear,

        });
        this.filterForms.push(filter);
    }

    removeFilter(i) {
        this.filterForms.removeAt(i);
    }

}
