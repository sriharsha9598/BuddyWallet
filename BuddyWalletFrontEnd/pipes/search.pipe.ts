import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'search',
  pure:false
})
export class SearchPipe implements PipeTransform {

  // This pipe is used to filter the users in admin page by account number, name, mobile number, email id
  transform(users: any, searchText: any[]): any 
  {
    if(searchText[0])
    {
      users=users.filter(e=>(e["accNum"].toString().startsWith(searchText[0].toString())));
    }
    if(searchText[1])
    {
      users=users.filter(e=>(e["firstName"].toLowerCase().includes(searchText[1].toLowerCase())));
    }
    if(searchText[2])
    {
      users=users.filter(e=>(e["lastName"].toLowerCase().includes(searchText[2].toLowerCase())));
    }
    if(searchText[3])
    {
      users=users.filter(e=>(e["phoneNum"].toString().startsWith(searchText[3].toString())));
    }
    if(searchText[4])
    {
      users=users.filter(e=>(e["emailId"].toLowerCase().includes(searchText[4].toLowerCase())));
    }
    
    return users;
  }

}
