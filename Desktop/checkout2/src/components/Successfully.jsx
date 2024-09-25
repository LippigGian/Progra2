import React from "react";
import { useLocation } from "react-router-dom"; // Importar useLocation
import Cards from "./Cards"
function Successfully() {
  const location = useLocation();
  const { id } = location.state || {}; // Acceder al ID pasado como estado

  return (
    <div className='flex justify-center gap-4 w-full '>
      <div className='flex justify-center w-full max-w-[500px] m-5'>
        <Cards className='flex flex-col items-center text-center'> {/* Añadir text-center aquí */}
          <img className="w-[90px] mx-auto " src="https://cdn.prod.website-files.com/66182d420e634ac8b9279dd6/66182d420e634ac8b9279ea7_Capa%201.svg" alt="Compra exitosa" />
          <h1 className="font-semibold self-center text-[20px]">¡Listo! El pago se encuentra aprobado</h1>
          {id && <p className="font-regular self-center text-center text-[18px]">Tu código de autorización es: {id}</p>}
          <p className="font-regular self-center text-[18px]">Coordina con el comercio la entrega de tu producto</p>
        </Cards>
      </div>
    </div>
  );
}

export default Successfully;