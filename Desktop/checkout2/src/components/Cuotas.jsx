import React from 'react';

const Cuotas = ({children}) => {
    // Aquí puedes agregar la lógica y el contenido del componente Cuotas
    return (
        <div className='flex rounded-[16px] bg-[#F5F5F5]  m-2'>
            {children}
        </div>
    );
};

export default Cuotas;