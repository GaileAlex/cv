import { Component, OnInit } from '@angular/core';
import { ChatService } from "../../service/chat.service";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { marked } from "marked";
import {SplitButtonModule} from 'primeng/splitbutton';

interface ChatEntry {
    role: 'user' | 'bot';
    text: string;
}

@Component({
    selector: 'app-chat',
    templateUrl: './chat.component.html',
    styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {
    promptText: string = '';
    chatHistory: ChatEntry[] = [];
    selectedLang: string = 'en-US';
    voices: SpeechSynthesisVoice[] = [];
    selectedVoiceIndex: number = 0;
    synth = window.speechSynthesis;
    recognition: any = null;
    isRecognizing = false;
    restartTimer: any = null;
    languageOptions = [
        { name: 'English (US)', code: 'en-US' },
        { name: 'English (UK)', code: 'en-GB' },
        { name: 'Русский', code: 'ru-RU' },
        { name: 'Français', code: 'fr-FR' },
        { name: 'Deutsch', code: 'de-DE' }
    ];
    voiceOptions: { label: string, value: number }[] = [];


    constructor(private chatService: ChatService, private sanitizer: DomSanitizer) {
    }

    ngOnInit(): void {
        this.updateVoices();

        if ('speechSynthesis' in window) {
            window.speechSynthesis.onvoiceschanged = () => this.updateVoices();
        }

        const SR = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
        if (SR) {
            this.recognition = new SR();
            this.recognition.interimResults = false;
            this.recognition.continuous = true;
            this.recognition.maxAlternatives = 1;

            this.recognition.onresult = (event: any) => {
                let finalTranscript = '';
                for (let i = event.resultIndex; i < event.results.length; ++i) {
                    if (event.results[i].isFinal) {
                        finalTranscript += event.results[i][0].transcript;
                    }
                }
                if (finalTranscript) {
                    this.promptText = finalTranscript;
                    this.stopVoiceInput();
                    this.sendPrompt();
                }
            };

            this.recognition.onerror = (event: any) => {
                console.error('Speech recognition error:', event.error);
            };

            this.recognition.onspeechstart = () => {
                if (this.restartTimer) {
                    clearTimeout(this.restartTimer);
                    this.restartTimer = null;
                }
            };

            this.recognition.onend = () => {
                if (this.isRecognizing && !this.restartTimer) {
                    this.restartTimer = setTimeout(() => {
                        this.restartTimer = null;
                        try {
                            this.recognition.start();
                        } catch {
                        }
                    }, 500);
                }
            };
        }
    }

    updateLangAndVoices(event: any) {
        this.selectedLang = event.value.code;
        this.updateVoices();
    }

    updateVoices(): void {
        const allVoices = this.synth.getVoices();

        if (!allVoices.length) {
            setTimeout(() => this.updateVoices(), 100);
            return;
        }

        this.voices = allVoices.filter(v => v.lang === this.selectedLang);

        // создаём массив для p-dropdown
        this.voiceOptions = this.voices.map((v, i) => ({
            label: v.name + (v.default ? ' (по умолчанию)' : ''),
            value: i
        }));

        if (this.voices.length > 0) {
            this.selectedVoiceIndex = this.voices.length - 1;
        }
    }

    sendPrompt(): void {
        if (!this.promptText.trim()) return;

        const userMessage = this.promptText; // сохраняем чистый текст
        this.promptText = '';

        // добавляем сообщение пользователя в историю
        this.appendToHistory('user', userMessage);

        const body = {
            model: 'openchat',
            prompt: this.buildConversationPrompt(userMessage),
            stream: false
        };

        this.chatService.generate(body).subscribe({
            next: (data: any) => {
                const reply = data.response.trim();
                this.appendToHistory('bot', reply);

                // читаем вслух **исходный текст**, без разметки
                this.speakText(reply);
            },
            error: (err) => {
                console.error(err);
                this.appendToHistory('bot', '[Ошибка сервера]');
            }
        });
    }


    appendToHistory(role: 'user' | 'bot', text: string): void {
        this.chatHistory.push({role, text});
        setTimeout(() => {
            const historyEl = document.getElementById('chatHistory');
            if (historyEl) {
                historyEl.scrollTop = historyEl.scrollHeight;
            }
        });
    }

    buildConversationPrompt(newPrompt: string): string {
        const historyText = this.chatHistory
            .map(entry => (entry.role === 'user' ? 'User: ' : 'Assistant: ') + entry.text)
            .join('\n');
        return historyText ? historyText + '\nUser: ' + newPrompt + '\nAssistant:'
            : 'User: ' + newPrompt + '\nAssistant:';
    }

    speakText(text: string): void {
        if (!this.voices.length) this.updateVoices();
        const utterance = new SpeechSynthesisUtterance(text);
        utterance.lang = this.selectedLang;
        if (this.voices[this.selectedVoiceIndex]) {
            utterance.voice = this.voices[this.selectedVoiceIndex];
        }

        // Обработка ошибок
        utterance.onerror = (event) => {
            console.error('SpeechSynthesisUtterance error:', event.error);
        };

        this.synth.speak(utterance);
    }

    startVoice(): void {
        if (!this.recognition) return alert('Распознавание речи не поддерживается.');
        if (this.isRecognizing) return;
        this.recognition.lang = this.selectedLang;
        this.recognition.start();
        this.isRecognizing = true;
    }

    stopVoiceInput(): void {
        if (!this.recognition || !this.isRecognizing) return;
        this.isRecognizing = false;
        if (this.restartTimer) {
            clearTimeout(this.restartTimer);
            this.restartTimer = null;
        }
        this.recognition.stop();
    }

    stopSpeaking(): void {
        if (!this.synth) return;
        // Прерываем текущие озвучки
        this.synth.cancel();

        // Немного ждем, чтобы движок очистил очередь
        setTimeout(() => {
            // Проверка, что синтезатор готов к новым озвучкам
            if (!this.synth.speaking && !this.synth.pending) {
                console.log('Synth ready for next utterance');
            }
        }, 50);
    }

    clearChat(): void {
        this.chatHistory = [];
    }

    formatText(text: string): SafeHtml {
        const result = marked.parse(text) as string;
        return this.sanitizer.bypassSecurityTrustHtml(result);
    }
}
