import { createContext, useState } from "react";



export const ContextInput = createContext();

const ContextInputProvider = ({ children }) => {
    const [inputAmount, setInputAmount] = useState({amount: 0});
    const [inputDescription, setInputDescription] = useState("");

    const amount = 1500;
    return(
        <ContextInput.Provider value={{inputAmount, setInputAmount, inputDescription, setInputDescription}}>
            {children}
        </ContextInput.Provider>
    )

}
export default ContextInputProvider;


