// firebaseConfig.js
import { initializeApp } from "firebase/app";
import { getDatabase, ref, set } from "firebase/database";

// Tu configuración de Firebase

// Your web app's Firebase configuration
const firebaseConfig = {
    apiKey: "AIzaSyAPOjizDWK73B4O7IVWcr8blXCsrtA6uM8",
    authDomain: "checkout-2404.firebaseapp.com",
    projectId: "checkout-2404",
    storageBucket: "checkout-2404.appspot.com",
    messagingSenderId: "239629911297",
    appId: "1:239629911297:web:a244d9c065c96a2a844700"
  };

// Inicializa Firebase
const app = initializeApp(firebaseConfig);

// Obtén una instancia de la base de datos
const database = getDatabase(app);

export { database, ref, set };


