import { Time } from '@angular/common';


// Mails entity model

export class Mails{
    id:number;
    name:string;
    category:string;
    subject:string;
    message:string;
    mailDate:Date;
    mailTime:Time;
    seen:number;
}
