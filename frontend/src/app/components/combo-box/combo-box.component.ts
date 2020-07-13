import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { SelectItem } from 'primeng/api';

@Component({
    selector: 'app-combo-box',
    templateUrl: './combo-box.component.html',
    styleUrls: ['./combo-box.component.css']
})
export class ComboboxComponent implements OnInit {

    chooseValue = 'Author';
    textConditionOptions: SelectItem [];
    textConditionOption = 'Contains';
    dateConditionOptions: SelectItem [];
    dateConditionOption = 'Equal to';

    constructor(private formBuilder: FormBuilder) {
        this.filterForm = this.formBuilder.group({
            selectedFilter: new FormArray([])
        });

        this.textConditionOptions = [
            {label: 'Contains', value: 'Contains'},
            {label: 'Begin with', value: 'Begin with'}
        ];
        this.dateConditionOptions = [
            {label: 'Equal to', value: 'Equal to'},
            {label: 'Begin with', value: 'Begin with'}
        ];
    }

    get filterForms() {
        return this.filterForm.get('selectedFilter') as FormArray;
    }

    filterForm: FormGroup;
    @Output() formReady = new EventEmitter<FormGroup>();
    chooseValues = [
        {label: 'Author', value: 'Author'},
        {label: 'Title', value: 'Title'},
        {label: 'Date', value: 'Date'}
    ];
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
            conditionOption: 'Contains',
            textRequest: '',
            day: '1',
            month: '1',
            year: this.currentYear,

        });
        this.filterForms.push(filter);
        const f2=null;
    }

    removeFilter(i) {
        this.filterForms.removeAt(i);
    }
}
