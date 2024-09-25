import React from "react";
import Cards from "./Cards";
import { CreditCard, ChevronRight } from "lucide-react";
import { Button } from "./ui/button";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";

import modo from "../assets/modo.svg";
import { set } from "firebase/database";
import { ContextInput } from "../context/ContextInput";
import { useEffect } from "react";
import axios from "../../axiosConfig";
import { useState } from "react";
import { useParams } from "react-router-dom";

function Checkout() {
  const navigate = useNavigate();
  const { inputAmount, setInputAmount } = useContext(ContextInput);
  const {orderId} = useParams();
  const [order, setOrder]= useState("")
  const [loading, setLoading] = useState(true);


  useEffect(()=>{
    const fetchData = async () => {
    try{
        const response = await axios.get(`pago-tarjeta/orders/${orderId}`)
        console.log("el order ID es: ", orderId)
        console.log("la respuesta es: ", response.data)
        setOrder(response.data)
    }catch{
        console.log("Error al obtener el detalle de la orden");
    }finally{
        setLoading(false);
    }
    }
    fetchData()
  },[orderId])


  return (
    <div className="flex justify-center p-5 ">
      {loading && <p>Cargando...</p>}
      {!loading &&
      
      <div className="flex flex-col  gap-[22px] w-full max-w-[500px]">
        <div className=" flex-col min-h-[96px] rounded-[16px] bg-primaryViolet text-white flex items-center gap-2 text-center justify-center">
          <p className="font-600 text-[18px]">Total a pagar</p>
          <p className="text-[34px]"> ${order.amount}</p>
        </div>
        <div className="flex min-h-[300px] justify-center align-middle ">
          <Cards className="p-5 m-5 ">
            <Button
              className="bg-white border text-dark shadow-md hover:bg-transparent w-auto h-auto p-5"
              onClick={() => navigate("/pago-tarjeta")}
            >
              <div className="flex items-center justify-between w-full gap-2">
                <div className="flex items-center gap-5">
                  <CreditCard className="w-5 h-5" />
                  <div className="flex flex-col  text-left ">
                    <p className="primary font-600 text-[16px]">
                      Pagar con tarjeta
                    </p>
                    <p className=" text-leyenda text-[14px]">
                      Crédito, débito y prepagas
                    </p>
                  </div>
                </div>

                <ChevronRight className="w-5 h-5 text-primaryViolet" />
              </div>
            </Button>

            <Button className="bg-white border text-dark shadow-md hover:bg-transparent w-auto h-auto p-5">
              <div className="flex items-center justify-between w-full gap-2">
                <div className="flex items-center gap-5">
                  <img src={modo} alt="Credit Card" className="w-[24.67] " />{" "}
                  <div className="flex flex-col  text-left ">
                    <p className="primary font-600 text-[16px]">
                      Pagar con MODO
                    </p>
                    <p className=" text-leyenda text-[14px]">
                      Con app Modo o app bancarias
                    </p>
                  </div>
                </div>

                <ChevronRight className="w-5 h-5 text-primaryViolet" />
              </div>
            </Button>
          </Cards>
        </div>
      </div>
      }
    </div>
  );
}

export default Checkout;
