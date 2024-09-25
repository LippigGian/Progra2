import React from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import { Button } from "./ui/button";
import { Label } from "./ui/label";
import { Input } from "./ui/input";
// Definir el esquema de validación con Zod
const schema = z.object({
  cardNumber: z
    .string()
    .min(16, "El número de tarjeta debe tener al menos 16 dígitos")
    .max(19, "El número de tarjeta no puede tener más de 19 dígitos")
    .regex(/^\d+$/, "El número de tarjeta solo puede contener dígitos")
    .transform((val) => val.replace(/\s+/g, "")), // Eliminar espacios para la validación

  name: z.string().nonempty("El nombre del titular es obligatorio"),

  expiryMonth: z
    .string()
    .min(2, "El mes debe tener al menos 2 dígitos")
    .max(2, "El mes no puede tener más de 2 dígitos")
    .regex(/^(0[1-9]|1[0-2])$/, "El mes debe estar entre 01 y 12"),

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
    .length(3, "El CVV debe tener 3 dígitos")
    .regex(/^\d+$/, "El CVV solo puede contener dígitos"),

  cardHolderName: z.string().nonempty("Nombre del titular es requerido"),
});

function SimpleForm() {
  // Configurar react-hook-form con Zod
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(schema), // Conectar Zod con react-hook-form
  });

  // Manejar el envío del formulario
  const onSubmit = (data) => {
    console.log("Datos del formulario:", data);
  };

  return (
    <div className="max-w-[500px]">
      <form onSubmit={handleSubmit(onSubmit)}>
        <div>
          <Label>
            Número de tarjeta:
            <Input
              type="text"
              {...register("cardNumber")}
              placeholder="Ingresá los números de la tarjeta"
            />
          </Label>
          {errors.cardNumber && <p>{errors.cardNumber.message}</p>}
        </div>
        <div className="flex">
          <Label>
            Expiración:
            <Input
              type="number"
              {...register("expiryMonth")}
              placeholder="Mes"
            />
          </Label>
          {errors.expiryMonth && <p>{errors.expiryMonth.message}</p>}

          <Label>
            Año:
            <Input type="text" {...register("expiryYear")} placeholder="Año" />
          </Label>
          {errors.expiryYear && <p>{errors.expiryYear.message}</p>}

          <Label>
            CVV:
            <Input type="text" {...register("cvv")} placeholder="CVV" />
          </Label>
          {errors.cvv && <p>{errors.cvv.message}</p>}
        </div>
        <div>
          <Label>
            Nombre del titular:
            <Input
              type="text"
              {...register("cardHolderName")}
              placeholder="Ingresá el nombre y apellido"
            />
          </Label>
          {errors.cardHolderName && <p>{errors.cardHolderName.message}</p>}
        </div>
        <Button type="submit" >Enviar</Button>
      </form>
    </div>
  );
}

export default SimpleForm;
