import React, { useState, useContext } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import { Button } from "./ui/button";
import { Label } from "./ui/label";
import { Input } from "./ui/input";
import { Check } from "lucide-react";
import { CircleAlert } from "lucide-react"; // Importar el icono

import { Navigate, useNavigate } from "react-router-dom";

import "../components/spinner.css";
import { writeToFirestore } from "../utils/ventas";

import { database, ref, set } from "../utils/firebaseConfig"; // Importar la instancia de la base de datos de Firebase

import axios from "../../axiosConfig";

//Context
import {ContextInput} from "../context/ContextInput";

import Cuotas from "./Cuotas";

// Definir el esquema de validación con Zod
const schema = z.object({
  cardNumber: z
    .string()
    .min(16, "El número de tarjeta debe tener al menos 16 dígitos")
    .max(19, "El número de tarjeta no puede tener más de 19 dígitos")
    .regex(/^\d+$/, "El número de tarjeta solo puede contener dígitos")
    .transform((val) => val.replace(/\s+/g, "")), // Eliminar espacios para la validación

  expiryMonth: z
    .string()
    .min(1, "El mes debe tener al menos 1 dígitos")
    .max(2, "El mes no puede tener más de 2 dígitos")
    .regex(/^([1-9]|1[0-2])$/, "El mes debe estar entre 01 y 12"),

  expiryYear: z
    .string()
    .min(4, "El año debe tener al menos 4 dígitos")
    .max(4, "El año no puede tener más de 4 dígitos")
    .regex(/^\d{4}$/, "El año debe ser un número de 4 dígitos")
    .transform((val) => parseInt(val, 10)) // Convertir a número entero para la validación

    // Validar que el año no esté en el pasado (año actual o futuro)
    .refine((val) => val >= new Date().getFullYear(), {
      message: "El año de expiración debe ser el año actual o futuro",
    }),

  cvv: z
    .string()
    .min(3, "El CVV debe tener al menos 3 dígitos")
    .max(4, "El CVV no puede tener más de 4 dígitos")
    .regex(/^\d+$/, "El CVV solo puede contener dígitos"),

  cardHolderName: z
    .string()
    .nonempty("El nombre del titular es requerido")
    .regex(
      /^[a-zA-Z\s]+$/,
      "El nombre del titular solo puede contener letras y espacios"
    ),

  dni: z
    .string()
    .min(7, "El DNI debe tener al menos 7 dígitos")
    .max(10, "El DNI no puede tener más de 8 dígitos")
    .regex(/^\d{2}\.\d{3}\.\d{3}$/, "El DNI debe tener el formato 99.999.999"),
});

function CheckoutForm() {
  const [id, setId] = useState(undefined);
  const [loading, setLoading] = useState(false); // Estado para controlar la carga
  const [tipoTarjeta, setTipoTarjeta] = useState(null);
  const [tarjetaCredito, setTarjetaCredito] = useState(null);
  const [installment, setInstallment] = useState(null);

  const {inputAmount, setInputAmount} = useContext(ContextInput);

  //Navegar a la pagina de exito
  const navigate = useNavigate();

  const enviarCompra = async (order) => {
    setLoading(true); // Iniciar el modo de carga
    const response = await writeToFirestore(order);
    setLoading(false); // Terminar el modo de carga
    if (response.success) {
      setId(response.id);
      navigate("/successfully", { state: { id: response.id } });
    } else {
      console.log("no se guardo correctamente en la base de datos.");
    }
  };

  // const consultarTarjeta = async (bin) => {
  //   try {
  //   const response = await fetch(`https://lookup.binlist.net/${bin}`);
  //   const data = await response.json();
  //   return data;
  //   } catch (error) {
  //     console.error("Error al consultar la tarjeta:", response);
  //     return null;
  //   }
  // }
  // Formateo del dni con puntos
  const formatDni = (value) => {
    console.log("el input es"+inputAmount.amount)
    return value
      .replace(/\D/g, "") // Eliminar caracteres no numéricos
      .replace(/(\d{2})(\d{0,3})(\d{0,3})/, (match, p1, p2, p3) => {
        let formatted = p1;
        if (p2) formatted += "." + p2;
        if (p3) formatted += "." + p3;
        return formatted;
      }) // Insertar puntos en las posiciones correctas
      .slice(0, 10); // Limitar a 10 caracteres (incluyendo los puntos)
      
  };

  // Configurar react-hook-form con Zod
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(schema), // Conectar Zod con react-hook-form
  });

  const consultarTarjeta = async (cardNumber) => {
    try {
      const response = await axios.post(`/api/numerotarjeta`, {numeroTarjeta: cardNumber});
      console.log(response.data);
      // console.log(response.data.data[0].type);
      setTarjetaCredito(response.data);
      setInstallment(response.data.data[0].installments);
      //Esto es lo que tengo qe mapear:
      console.log(response.data.data[0].installments);
      return response.data.data[0].type;
    } catch (error) {
      console.error("Error al consultar la tarjeta:", error);
      return null;
    }
  };


  const handleCardNumberChange = async (e) => {
    console.log(e.target.value);
    const cardNumber = e.target.value;
    if (cardNumber.length == 5) {
      const tipo = await consultarTarjeta(cardNumber);
    setTipoTarjeta(tipo);
    console.log(tipoTarjeta);
    }
  
  };
  // Función para guardar la transacción en Firebase
  const saveTransaction = (data) => {
    const transactionRef = ref(
      database,
      "transactions/" + new Date().getTime()
    ); // Usa una marca de tiempo como ID único
    set(transactionRef, data)
      .then(() => {
        console.log("Transacción guardada con éxito");
      })
      .catch((error) => {
        console.error("Error al guardar la transacción:", error);
      });
  };

  // Función para manejar el envío del formulario
  const onSubmit = (data) => {
    console.log("Datos del formulario:", data);
    enviarCompra(data);
    saveTransaction(data); // Guarda la transacción en Firebase
  };

  const handleClickBoton = () => {
    console.log(tarjetaCredito.data[0].installments[0].name);
  }
  return (
    <div className="max-w-[500px]">
      {loading && <div className="loading-spinner"></div>}{" "}
      {/* Indicador de carga */}
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          <Label className=" font-400 text-[20px] ">
            Número de tarjeta
            <Input
              className="text-[14px] mt-[10px]"
              type="text"
              {...register("cardNumber", { required: true })}
              placeholder="Ingresá los números de la tarjeta"
              // autoComplete="off"
              onKeyPress={(e) => {
                if (!/[0-9]/.test(e.key)) {
                  e.preventDefault(); // Previene la entrada de caracteres no numéricos
                }
              }}
              onChange={(e) => {
                handleCardNumberChange(e); // Llama a tu manejador personalizado
                register("cardNumber").onChange(e); // Llama al manejador de react-hook-form
              }}
            />
            {errors.cardNumber && (
              <p className="mt-3 text-error text-[13px] flex  items-center ">
                <CircleAlert className="w-[18px] mr-2" />
                {errors.cardNumber.message}
              </p>
            )}
            {/* {errors.cardNumber && (
              <p className="mt-3 text-error text-[13px]">
                {errors.cardNumber.message}
              </p>
            )} */}
          </Label>
        </div>
        <div className="flex mt-[15px]  gap-10">
          <Label className=" font-400 text-[20px]">
            Expiración
            <Input
              className="text-[14px] mt-[10px]"
              type="number"
              {...register("expiryMonth")}
              placeholder="Mes"
              min="1"
              max="12"
              inputMode="numeric" // Asegura que el teclado numérico se muestre en dispositivos móviles
              pattern="[0-9]*" // Restringe la entrada a solo números
              onKeyPress={(e) => {
                if (!/[0-9]/.test(e.key)) {
                  e.preventDefault(); // Previene la entrada de caracteres no numéricos
                }
              }}
              onInput={(e) => {
                if (e.target.value.length > 2) {
                  e.target.value = e.target.value.slice(0, 2); // Limita la entrada a 2 caracteres
                }
              }}
            />
            {/* {errors.expiryMonth && <p className="mt-3 text-error text-[13px]">{errors.expiryMonth.message}</p>} */}
            {errors.expiryMonth && (
              <p className="mt-3 text-error text-[13px] flex ">
                <CircleAlert className="w-[40px] mr-2" />
                {errors.expiryMonth.message}
              </p>
            )}
          </Label>

          <Label className=" font-400 text-[20px]">
            Año
            <Input
              type="number"
              className="text-[14px] mt-[10px]"
              {...register("expiryYear")}
              placeholder="Año"
              min="2024"
              onInput={(e) => {
                if (e.target.value.length > 2) {
                  e.target.value = e.target.value.slice(0, 4);
                }
              }}
              onKeyPress={(e) => {
                if (!/[0-9]/.test(e.key)) {
                  e.preventDefault(); // Previene la entrada de caracteres no numéricos
                }
              }}
            />
            {errors.expiryYear && (
              <p className="mt-3 text-error text-[13px] flex ">
                <CircleAlert className="w-[40px] mr-2" />
                {errors.expiryYear.message}
              </p>
            )}
            {/* {errors.expiryYear && (
              <p className="mt-3 text-error text-[13px]">
                {errors.expiryYear.message}
              </p>
            )} */}
          </Label>

          <Label className=" font-400 text-[20px]">
            CVV
            <Input
              type="text"
              className="text-[14px] mt-[10px]"
              {...register("cvv")}
              placeholder="CVV"
              onInput={(e) => {
                if (e.target.value.length > 2) {
                  e.target.value = e.target.value.slice(0, 4); // Limita la entrada a 2 caracteres
                }
              }}
              onKeyPress={(e) => {
                if (!/[0-9]/.test(e.key)) {
                  e.preventDefault(); // Previene la entrada de caracteres no numéricos
                }
              }}
            />
            {errors.cvv && (
              <p className="mt-3 text-error text-[13px] flex ">
                <CircleAlert className="w-[40px] mr-2" />
                {errors.cvv.message}
              </p>
            )}
            {/* {errors.cvv && (
              <p className="mt-3 text-error text-[13px]">
                {errors.cvv.message}
              </p>
            )} */}
          </Label>
        </div>

        <div className="mt-[15px] mb-[15px]">
          <Label className=" font-400 text-[20px]">
            Nombre
            <Input
              className="text-[14px] mt-[10px]"
              type="text"
              {...register("cardHolderName")}
              placeholder="Ingresá el nombre y apellido"
            />
            {errors.cardHolderName && (
              <p className="mt-3 text-error text-[13px] flex flex  items-center">
                <CircleAlert className="w-[18px] mr-2" />
                {errors.cardHolderName.message}
              </p>
            )}
          </Label>
        </div>
        <div className="mt-[15px] mb-[15px]">
          <Label className=" font-400 text-[20px]">
            DNI
            <Input
              className="text-[14px] mt-[10px]"
              type="text"
              {...register("dni")}
              placeholder="Ingresá el DNI"
              maxLength="10"
              onKeyPress={(e) => {
                if (!/[0-9]/.test(e.key)) {
                  e.preventDefault(); // Previene la entrada de caracteres no numéricos
                }
              }}
              onInput={(e) => {
                e.target.value = formatDni(e.target.value);
              }}
            />
            {errors.dni && (
              <p className="mt-3 text-error text-[13px] flex flex  items-center">
                <CircleAlert className="w-[18px] mr-2" />
                {errors.dni.message}
              </p>
            )}
          </Label>


            {
              tipoTarjeta === "CREDIT" && (
               <div className="flex flex-col">
              
                {/* <button onClick={handleClickBoton}>presioname</button> */}
                {
                  installment.map((installment)  => {
                    return (
                      <Cuotas>
                   <div className="flex gap-5 p-2 w-full justify-between" key={installment.installmentId}>
                    <div className="flex flex-col">
                    <input
                        type="radio"
                        id={installment.installmentId}
                        name="installment"
                        value={installment.installmentId}
                        {...register("installment")}
                      />

                      <h2>{installment.name}</h2>
                      <p>CF: {installment.financialRate}%</p>

                      </div>
                      <div>
                        <p>{installment.total}</p>
                      </div>
                      {/* <label htmlFor={installment.installmentId}>
                        {installment.name} - {installment.quantity} cuotas de ${installment.total}
                      </label> */}
                      
                    </div>
                    </Cuotas>
                    
                    )
                  })
                }
                
                </div>
              )

            }


        </div>

        <div className="flex w-auto pt-[64px]">
          <Button
            type="submit"
            className="bg-primaryViolet text-white rounded-[50px] h-[56px] w-full text-[16px] "
          >
            Continuar
          </Button>
        </div>
      </form>
    </div>
  );
}

export default CheckoutForm;
