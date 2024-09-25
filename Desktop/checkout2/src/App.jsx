import "./App.css";
import Checkout from "./components/Checkout";
import PagoTarjeta from "./components/PagoTarjeta";
import Navbar from './components/Navbar.jsx'
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import SimpleForm from "./components/SimpleForm";
import Successfully from "./components/Successfully";
import  ContextInputProvider  from "./context/ContextInput"
import CheckoutForm2 from "./components/CheckoutForm2";

import { DataProvider } from "./context/ContextGet";
import AmountRecieve from "./components/AmountRecieve";

import PaymentLinkGenerator from "./components/PaymentLinkGenerator";

import "../mocks/axiosMock"
function App() {
  return (
    <Router>
      <DataProvider>
      <ContextInputProvider>
        <Navbar></Navbar>
        {/* <AmountRecieve></AmountRecieve> */}
        {/* <CheckoutForm2></CheckoutForm2> */}
    <Routes>

    
    <Route path="/link" element={<PaymentLinkGenerator />} />
    {/* <Route path="/checkout" element={<Checkout />} /> */}
    <Route path="/checkout/orders/:orderId" element={<Checkout />} />
    {/* <Route path="/pago-tarjeta" element={<PagoTarjeta />} /> */}
    <Route path="/pago-tarjeta/orders/:orderId" element={<PagoTarjeta />} />
    <Route path="/successfully" element={<Successfully />} />
   </Routes>
   </ContextInputProvider>
   </DataProvider>
    </Router>
  );
}

export default App;
