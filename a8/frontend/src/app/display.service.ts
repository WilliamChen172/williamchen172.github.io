import { Injectable } from '@angular/core';
import { EventEmitter } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DisplayService {
  public hide: EventEmitter<boolean> = new EventEmitter<boolean>();

  public hideDisplay() {
    this.hide.emit(true)
  }

  constructor() { }

}
