import React from "react";
import { useForm } from "react-hook-form";

const CheckoutForm2 = () => {
  // Your component logic goes here
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const onSubmit = (data) => {
    console.log(data);
  };
  return (
    <div>
      <h2>Formulario</h2>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          <label>Numero de tarjeta</label>
          <input
            type="number"
            {...register("cardNumber", {
              required: true,
              maxLength: 16,
              minLength: 14,
            })}
          ></input>
          {errors.cardNumber?.type === "required" && (
            <p>Este campo es requerido</p>
          )}
            {errors.cardNumber?.type === "minLength" && ("El numero de tarjeta debe tener 16 digitos") }
          {errors.cardNumber?.type === "maxLength" &&
            "El número de tarjeta debe tener 16 dígitos"}
        </div>
        <div>
          <label>Mes</label>
          <input
            type="number"
            {...register("month", {
              required: true,
              maxLength: 2,
              minLength: 2,
              max: 12,
              min: 1,
            })}
          ></input>
          {errors.month?.type === "maxLength" && ("El mes debe tener 2 digitos")}
          {errors.month?.type === "required" && <p>Este campo es requerido</p>}
        </div>
        <div>
          <label>Año</label>
          <input type="number" {...register("year")}></input>
        </div>
        <div>
          <label>CVV</label>
          <input type="number" {...register("cvv")}></input>
        </div>
        <div>
          <label>Nombre completo</label>
          <input type="text" {...register("name")}></input>
        </div>
        <button type="submit">Enviar</button>
      </form>
    </div>
  );
};

export default CheckoutForm2;
