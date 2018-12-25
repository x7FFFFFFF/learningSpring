import {Role} from './Role';

export class Result {


  constructor(public number: number, public totalElements: number, public totalPages: number, public content: Role[]) {
  }
}



