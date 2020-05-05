import { Pipe, PipeTransform } from '@angular/core';
import { Mails } from '../models/MailDB';

@Pipe({
  name: 'filter'
})
export class FilterPipe implements PipeTransform {

  // This is used to filter mails into feedbacks, complaints and help mails using the category
    
  transform(mails: Mails[], category: string): any 
    {
      if(category)
      {
        mails=mails.filter(e=>(e["category"]==(category)));
     }
      return mails;
  }

}
