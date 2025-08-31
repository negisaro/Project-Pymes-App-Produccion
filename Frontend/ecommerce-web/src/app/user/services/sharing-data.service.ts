import { EventEmitter, Injectable } from '@angular/core';
import { User } from '../interfaces/user.interface';



@Injectable({
  providedIn: 'root'
})
export class SharingDataService {

  private _newUserEventEmitter: EventEmitter<User> = new EventEmitter();

  private _idUserEventEmitter = new EventEmitter();

  private _findUserByIdEventEmitter = new EventEmitter();

  private _selectUserEventEmitter = new EventEmitter();

  private _errorsUserFormEventEmitter = new EventEmitter();

  private _pageUsersEventEmitter = new EventEmitter();

  private _handlerLoginEventEmitter = new EventEmitter();

  constructor() { }

  get handlerLoginEventEmitter() {
    return this._handlerLoginEventEmitter;
  }
  get pageUsersEventEmitter() {
    return this._pageUsersEventEmitter;
  }
  get errorsUserFormEventEmitter() {
    return this._errorsUserFormEventEmitter;
  }

  get selectUserEventEmitter() {
    return this._selectUserEventEmitter;
  }

  get findUserByIdEventEmitter() {
    return this._findUserByIdEventEmitter
  }

  get newUserEventEmitter(): EventEmitter<User> {
    return this._newUserEventEmitter;
  }

  get idUserEventEmitter(): EventEmitter<number>{
    return this._idUserEventEmitter;
  }

}
