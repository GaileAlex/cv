import { Component, ElementRef, Renderer2, ViewChild } from '@angular/core';
import { EnglishService } from "../../service/english.service";
import { EnglishSentence } from "../../models/englishSentence";

@Component({
    selector: 'app-study-english',
    templateUrl: './study-english.component.html',
    styleUrls: ['./study-english.component.css']
})
export class StudyEnglishComponent {
    @ViewChild('sentenceTranslate') sentenceTranslate: ElementRef;
    text: string;
    translate: string;
    padding = '100px';
    isBlur = true;
    englishSentence: EnglishSentence

    constructor(private englishService: EnglishService, private renderer: Renderer2) {
        window.scrollTo(0, 0);
        this.getData();
    }

    onBlur(menuIcon: HTMLElement) {
        if (this.isBlur) {
            menuIcon.classList.remove('backdrop-filter');
            this.isBlur = false;
        } else {
            this.addBlur();
        }
    }

    nextSentence() {
        this.getData();
        this.addBlur();
        return this.englishSentence;
    }

    getData() {
        this.englishService.findOne().subscribe(data => {
            this.englishSentence = data;
        })
    }

    addBlur() {
        this.renderer.addClass(this.sentenceTranslate.nativeElement, 'backdrop-filter');
        this.isBlur = true;
    }

}
