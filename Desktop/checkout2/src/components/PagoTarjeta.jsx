import React, { useEffect } from 'react';
import Cards from './Cards';
import CheckoutForm from './CheckoutForm';
import { useContext } from 'react';
import { ContextInput } from "@/context/ContextInput";
import { useParams } from 'react-router-dom';
import axios from "../../axiosConfig"
import { useState } from 'react';
function PagoTarjeta() {
  const {inputAmount, inputDescription} = useContext(ContextInput);
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
    <>
    {loading && <p>Cargando...</p>}
    {!loading &&
    <div className="flex flex-col lg:flex-row justify-center items-center lg:items-start gap-4  p-5">
            <p>{order.name}</p>
      <div className='flex flex-1 justify-center w-full max-w-[500px]'>
        <Cards className='flex-1 '>
          <div className='p-4'>
            <p className='mb-[32px] font-600 text-[20px]'>Ingresá los datos de la tarjeta</p>
            <CheckoutForm  />
          </div>
        </Cards>
      </div>
      <div className='flex flex-1 justify-center w-full max-w-[500px]'>
        <Cards className='flex-1 '>
          <div className='p-4 flex flex-col gap-4'>
            <p className='mb-4 font-600 text-[20px] pb-[38px]'>Detalle de la compra</p>
            <div className='flex flex-row justify-between pb-[38px]'>
              <p className='font-400 text-[20px]'>Total a pagar</p>
              <p className='font-400 text-[20px]'>${order.amount}</p>
            </div>
            <div className='flex flex-row justify-between pb-[38px]'>
              <p className='font-400 text-[20px]'>Descripción</p>
              <p className='font-400 text-[20px]'>{order.description}</p>
            </div>
          </div>
        </Cards>
      </div>
    </div>}
    </>
  );
}

export default PagoTarjeta;


