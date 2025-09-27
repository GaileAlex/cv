import { Component, OnInit } from '@angular/core';
import { ChatService } from "../../service/chat.service";
import { DomSanitizer, SafeHtml } from "@angular/platform-browser";
import { marked } from "marked";
import { StatisticsService } from "../../service/statistics.service";
import { UserDataService } from "../../service/user-data.service";
import { ChatMessage } from '../../models/chatMessage';

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

    userName = '';
    sessions: { id: string; description: string }[] = [];
    selectedSessionId: string | null = null;

    promptText = '';
    chatHistory: ChatEntry[] = [];

    selectedLang = 'en-US';
    voices: SpeechSynthesisVoice[] = [];
    selectedVoiceIndex = 0;
    synth = window.speechSynthesis;

    recognition: any = null;
    isRecognizing = false;
    collectedTranscript = '';

    languageOptions = [
        {name: 'Русский', code: 'ru-RU'},
        {name: 'English (US)', code: 'en-US'}
    ];

    voiceOptions: { label: string, value: number }[] = [];

    constructor(
        private chatService: ChatService,
        private sanitizer: DomSanitizer,
        private statisticsService: StatisticsService,
        private userDataService: UserDataService
    ) {}

    ngOnInit(): void {
        this.userName = this.userDataService.getUserName();
        this.loadSessions();
        this.initVoices();
        this.initSpeechRecognition();
    }

    loadSessions(): void {
        this.chatService.getSessionsHistory(this.userName).subscribe({
            next: sessionsMap => {
                this.sessions = Object.entries(sessionsMap).map(([id, description]) => ({id, description}));
            },
            error: err => console.error('Failed to load sessions', err)
        });
    }

    onSelectSession(session: { id: string, description: string }): void {
        if (session.id === 'null') {
            this.selectedSessionId = null;
            this.chatHistory = [{role: 'bot', text: session.description}];
        } else {
            this.selectedSessionId = session.id;
            this.loadHistory();
        }
    }

    loadHistory(): void {
        if (!this.selectedSessionId) return;
        this.chatService.getHistory(this.selectedSessionId).subscribe({
            next: history => {
                this.chatHistory = history.map(m => ({
                    role: m.role === 'user' ? 'user' : 'bot',
                    text: m.content
                }));
            },
            error: err => console.error('Failed to load chat history', err)
        });
    }

    initVoices(): void {
        this.updateVoices();
        if ('speechSynthesis' in window) {
            window.speechSynthesis.onvoiceschanged = () => this.updateVoices();
        }
    }

    updateLangAndVoices(event: any): void {
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
        this.voiceOptions = this.voices.map((v, i) => ({
            label: v.name + (v.default ? ' (по умолчанию)' : ''),
            value: i
        }));
        if (this.voices.length) this.selectedVoiceIndex = this.voices.length - 1;
    }

    sendPrompt(): void {
        if (!this.promptText.trim()) return;
        const userMessage = this.promptText;
        this.promptText = '';
        this.appendToHistory('user', userMessage);

        this.chatService.sendMessage(this.userName, this.selectedSessionId, userMessage)
            .subscribe({
                next: (reply: ChatMessage) => {
                    this.appendToHistory('assistant', reply.message);
                    this.speakText(reply.message);
                    this.selectedSessionId = reply.sessionId;
                },
                error: err => {
                    console.error(err);
                    this.appendToHistory('assistant', '[Ошибка сервера]');
                }
            });
    }

    appendToHistory(role: 'user' | 'assistant', text: string): void {
        const chatRole: 'user' | 'bot' = role === 'assistant' ? 'bot' : 'user';
        this.chatHistory.push({role: chatRole, text});
        setTimeout(() => {
            const el = document.getElementById('chatHistory');
            if (el) el.scrollTop = el.scrollHeight;
        }, 0);
    }

    speakText(text: string): void {
        text = this.stripMarkdown(text);
        if (!this.voices.length) this.updateVoices();
        const utterance = new SpeechSynthesisUtterance(text);
        utterance.lang = this.selectedLang;
        if (this.voices[this.selectedVoiceIndex]) utterance.voice = this.voices[this.selectedVoiceIndex];
        utterance.onerror = (e) => console.log('SpeechSynthesisUtterance error', e);
        this.synth.speak(utterance);
    }

    private stripMarkdown(md: string): string {
        return md
            .replace(/[*_`]/g, '')
            .replace(/<[^>]+>/g, '')
            .replace(/\[(.*?)\]\(.*?\)/g, '$1')
            .trim();
    }

    stopSpeaking(): void {
        if (!this.synth) return;
        this.synth.cancel();
    }

    clearChat(): void {
        this.chatHistory = [];
        this.selectedSessionId = null;
    }

    formatText(text: string): SafeHtml {
        const result = marked.parse(text) as string;
        return this.sanitizer.bypassSecurityTrustHtml(result);
    }

    /** --- Исправленный SpeechRecognition --- */
    initSpeechRecognition(): void {
        const SR = (window as any).SpeechRecognition || (window as any).webkitSpeechRecognition;
        if (!SR) return;

        this.recognition = new SR();
        this.recognition.continuous = true;
        this.recognition.interimResults = true; // теперь собираем промежуточные результаты
        this.recognition.maxAlternatives = 1;
        this.recognition.lang = this.selectedLang;

        this.recognition.onresult = (event: any) => {
            let interimTranscript = '';
            for (let i = event.resultIndex; i < event.results.length; i++) {
                if (event.results[i].isFinal) {
                    this.collectedTranscript += event.results[i][0].transcript + ' ';
                } else {
                    interimTranscript += event.results[i][0].transcript;
                }
            }
        };

        this.recognition.onerror = (event: any) => {
            console.error('Speech recognition error', event.error);
        };

        this.recognition.onend = () => {
            if (this.isRecognizing) {
                // автоматический рестарт, если нужно
                try { this.recognition.start(); } catch {}
            }
        };
    }

    startVoice(): void {
        if (!this.recognition || this.isRecognizing) return;
        this.collectedTranscript = '';
        try {
            this.recognition.start();
            this.isRecognizing = true;
        } catch (e) {
            console.error('Failed to start recognition:', e);
        }
    }

    stopVoiceInput(): void {
        if (!this.recognition || !this.isRecognizing) return;
        this.isRecognizing = false;
        this.recognition.stop();
        // небольшая задержка, чтобы onresult успел собрать финальный текст
        setTimeout(() => this.sendCollectedTranscript(), 150);
    }

    private sendCollectedTranscript(): void {
        const text = this.collectedTranscript.trim();
        console.log('Collected text:', text);
        if (!text) return;

        // Очищаем для следующей записи
        this.collectedTranscript = '';
        this.promptText = ''; // это поле просто для отображения, не для API

        // Отправляем на API
        this.appendToHistory('user', text);
        this.chatService.sendMessage(this.userName, this.selectedSessionId, text)
            .subscribe({
                next: (reply: ChatMessage) => {
                    this.appendToHistory('assistant', reply.message);
                    this.speakText(reply.message);
                    this.selectedSessionId = reply.sessionId;
                },
                error: err => {
                    console.error(err);
                    this.appendToHistory('assistant', '[Ошибка сервера]');
                }
            });
    }
}
