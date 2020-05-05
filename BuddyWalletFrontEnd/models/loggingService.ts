// Logger Service Model

export class LoggingService{
    logStatus(message: string){
        let currentDateTime=new Date();
        let currentDateTimeString= currentDateTime.toDateString() + currentDateTime.getTime() ;
        console.log(` ${ (currentDateTimeString) } : `,message);
    }
}