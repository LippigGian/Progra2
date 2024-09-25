import axios from 'axios';

// Crear una instancia de axios
const axiosInstance = axios.create({
  baseURL: '', // Cambia esto a la URL base de tu API si es necesario
  timeout: 1000,
  headers: { 'Content-Type': 'application/json' },
});

export default axiosInstance;