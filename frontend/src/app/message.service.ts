import {Injectable} from '@angular/core';
import {ErrorItem} from './ErrorItem';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  messages: string[] = [];

  add(message: string) {
    this.messages.push(message);
  }

  addErrors(items: ErrorItem[]) {
    const strings: string[] = items.map(item => `${item.code}: ${item.message}`);
    this.messages = this.messages.concat(strings);

  }

  clear() {
    this.messages = [];
  }
}
