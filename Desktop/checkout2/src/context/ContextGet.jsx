import React, { createContext, useContext, useState, useEffect } from 'react';
import { getFirestore, doc, getDoc } from 'firebase/firestore'
import { ContextInput } from "@/context/ContextInput";

// Crear el contexto
const DataContext = createContext();

// Crear un proveedor del contexto
export const DataProvider = ({ children }) => {
  const [data, setData] = useState(null); // Aquí almacenarás los datos de Firebase
const comprasId =  "HLlpuVddYG0jJadskYq6";
if(data){
    console.log(data)
}
  // useEffect(() => {
  //   const fetchData = async () => {
  //     try {
  //       const db = getFirestore();
        
  //       // Especificar la referencia del documento usando la colección y el ID del documento
  //       const docRef = doc(db, 'compras',  comprasId );
  //       const docSnap = await getDoc(docRef);

  //       if (docSnap.exists()) {
  //         setData({ id: docSnap.id, ...docSnap.data() });
  //       } else {
  //         console.error("No se encontró el documento.");
  //       }
  //     } catch (error) {
  //       console.error('Error al obtener el documento:', error);
  //     }
  //   };

  //   if (comprasId) {
  //     fetchData();
  //   }
  // }, [comprasId]); // El useEffect se ejecuta cada vez que documentId cambie

  return (
    <DataContext.Provider value={{ data }}>
      {children}
    </DataContext.Provider>
  );
};

// Hook personalizado para acceder al contexto
export const useDataContext = () => useContext(DataContext);
