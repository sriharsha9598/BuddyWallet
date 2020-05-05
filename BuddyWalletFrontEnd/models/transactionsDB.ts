import { Time } from '@angular/common';

// Transactions entity model

export class eStatement{
    id:number;
    transaction:string;
    amount:number;
    transDate:Date;
    transTime:Time;
    balance:number;
}