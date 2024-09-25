import React from 'react';

import { useDataContext } from "../context/ContextGet";

const AmountRecieve = () => 
{
    const { data } = useDataContext();

  if (!data) {
    return <p>Cargando...</p>;
  }
      
    return (
<div>
      <h1>Datos de Firebase:</h1>
      <div>
      <h1>Datos del documento:</h1>
      <p>ID: {data.id}</p>
      <p>Precio: {data.amount}</p> {/* Ajusta los campos según tu estructura */}
    </div>
    </div>
    );
};

export default AmountRecieve;



