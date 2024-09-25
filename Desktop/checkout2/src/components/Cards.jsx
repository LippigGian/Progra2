
import "./cards.css";

import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "./ui/card";

function Cards({children, title, description}) {
  return (


    <Card className="card min-w-full  rounded-[16px] ">

      
        <CardContent className="flex flex-col gap-[29px]  p-6 ">
          {children}
          
        </CardContent>
   
    </Card>
      //   <Card className="card min-w-full  md:min-w-[500px] lg:min-w-[600px] rounded-[16px]">
      //   <CardContent className="flex flex-col gap-4 sm:gap-6 md:gap-8 lg:gap-10 p-4 sm:p-6 md:p-8 lg:p-10">
      //     {children}
      //   </CardContent>
      // </Card>
  );
}

export default Cards;
