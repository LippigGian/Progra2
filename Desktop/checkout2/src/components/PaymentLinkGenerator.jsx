// import React, { useState, useContext } from "react";

import React from "react";

import { useState } from "react";
import { useContext } from "react";

// Importar la instancia de axios configurada
import axios from "../../axiosConfig";

import { useNavigate } from "react-router-dom";

//Context
import {ContextInput} from "../context/ContextInput";


const PaymentLinkGenerator = () => {
  const navigate = useNavigate();
  const [amount, setAmount] = useState("");
  const [description, setDescription] = useState("");
  const [paymentLink, setPaymentLink] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const {inputAmount, setInputAmount} = useContext(ContextInput);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    console.log(`Tipo de amount: ${typeof amount}`);
        try {
      const response = await axios.post("/api/link", {
        amount,
        description,
        currencyCode: "038",
        userId: 1818,
      });
      setInputAmount({amount: amount});
      console.log(response.data);
      setPaymentLink(response.data.link);
      console.log(response.data.message);
    } catch (error) {
      console.error("Error al generar el link de pago :", error);
    } finally {
      setLoading(false);
    }
  };

  const handleClick = () => {
    console.log(inputAmount);
    navigate("/checkout");
  };

  return (
    <div className="payment-link-generator flex flex-col items-center justify-center min-h-screen bg-gray-100 p-4">
      <form
        onSubmit={handleSubmit}
        className="bg-white p-6 rounded-lg shadow-md w-full max-w-md"
      >
        <div className="mb-4">
          <label
            htmlFor="amount"
            className="block text-gray-700 font-bold mb-2"
          >
            Monto:
          </label>
          <input
            type="number"
            id="amount"
            value={amount}
            onChange={(e) => setAmount(parseFloat(e.target.value))}
            required
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
          />
        </div>
        <div className="mb-4">
          <label
            htmlFor="description"
            className="block text-gray-700 font-bold mb-2"
          >
            Descripción:
          </label>
          <input
            type="text"
            id="description"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            required
            className="w-full px-3 py-2 border border-gray-300 rounded-md"
          />
        </div>
        <button
          type="submit"
          disabled={loading}
          className={`w-full px-4 py-2 text-white rounded-md ${
            loading
              ? "bg-gray-400 cursor-not-allowed"
              : "bg-blue-500 hover:bg-blue-700"
          }`}
        >
          {loading ? "Generando..." : "Crear Link de Pago"}
        </button>
      </form>

      {paymentLink && (
        <div className="mt-6 text-center">
          <p className="text-gray-700 font-bold">Link de pago generado:</p>
          <a
            href={paymentLink}
            target="_blank"
            rel="noopener noreferrer"
            className="text-blue-500 underline"
          >
            {paymentLink}
          </a>
          <div>
            <button onClick={() => handleClick()}>¡Ir a pagar!</button>
          </div>
        </div>
      )}
      {error && <p className="mt-6 text-red-500">{error}</p>}
    </div>
  );
};

export default PaymentLinkGenerator;
